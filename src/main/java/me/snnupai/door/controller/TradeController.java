package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Zone;
import com.qiniu.storage.model.DefaultPutRet;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.async.EventType;
import me.snnupai.door.mapper.ImageMapper;
import me.snnupai.door.model.EntityType;
import me.snnupai.door.model.UserContext;
import me.snnupai.door.pojo.*;
import me.snnupai.door.service.CommentService;
import me.snnupai.door.service.SensitiveService;
import me.snnupai.door.service.TradeService;
import me.snnupai.door.status.AnonymousStatus;
import me.snnupai.door.status.DelStatus;
import me.snnupai.door.status.TradeStatus;
import me.snnupai.door.status.XiaoquStatus;
import me.snnupai.door.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static me.snnupai.door.util.ImageUtils.fileUpload;

@RestController
@Slf4j
public class TradeController {

    @Autowired
    TradeService tradeService;


    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * @param offset
     * @param limit
     * @return
     */
    @ApiOperation(value = "获取列表页")
    @RequestMapping(path = "/get/trade/list", method = RequestMethod.GET)
    public JSONObject getTradeList(@RequestParam("offset") int offset,
                                   @RequestParam("limit") int limit) {

        User user = UserContext.getCurrentUser();
        JSONObject result = new JSONObject();
        List<Map<String, Object>> tradeInfos = new LinkedList<>();
        List<Trade> trades = tradeService.getTradeList((offset - 1) * limit, limit);
        if (trades != null) {
            for (Trade trade : trades) {
                Map<String, Object> info = new HashMap<>();
                String tradeId = trade.getId();
                info.put("tradeId", tradeId);
                info.put("title", trade.getTitle());
                info.put("content", trade.getContent());
                info.put("createdDate", DateUtils.getDateString(trade.getCreatedDate()));
                info.put("nickname", trade.getAnonymousNickName());
                int xiaoQu = trade.getXiaoqu();
                if (xiaoQu == XiaoquStatus.yanTa) {
                    info.put("xiaoQu", "雁塔校区");
                } else {
                    info.put("xiaoQu", "长安校区");
                }

                if (user == null) {
                    info.put("follow", false);
                } else {
                    String key = RedisKeyUtil.getKey(EventType.FOLLOW.getValue(), EntityType.ENTITY_TRADE, tradeId);
                    String val = RedisKeyUtil.getVal(EntityType.ENTITY_USER, String.valueOf(user.getId()));
                    boolean follow = jedisAdapter.sismember(key, val);
                    if (follow) {
                        info.put("follow", true);
                    } else {
                        info.put("follow", false);
                    }

                }
                Image image = getFirstImage(tradeId);
                info.put("imageUrl", image.getUrl());

                tradeInfos.add(info);
            }
        }
        result.put("tradeInfos", tradeInfos);
        boolean noMore = true;
        Long tradeCounts = tradeService.count();
        if (offset * limit >= tradeCounts) {
            noMore = true;
        } else {
            noMore = false;
        }
        result.put("noMore", noMore);
        return result;
    }

    private Image getFirstImage(String id) {
        ImageExample example = new ImageExample();
        ImageExample.Criteria criteria = example.createCriteria();
        criteria.andEntityIdEqualTo(id);
        criteria.andEntityTypeEqualTo(EntityType.ENTITY_TRADE)
                .andDelStatusEqualTo(DelStatus.non_del);

        List<Image> images = imageMapper.selectByExample(example);
        if (images != null && images.size() != 0) {
            return images.get(0);
        } else {
            Image defaultImage = new Image();
            defaultImage.setUrl("http://p7bv5h9bw.bkt.clouddn.com/Fp_Fu4IdCDrGd-5PtB5wpzBKdWfg");
            return defaultImage;
        }
    }


    private List<Image> getImageList(String id) {
        ImageExample example = new ImageExample();
        ImageExample.Criteria criteria = example.createCriteria();
        criteria.andEntityIdEqualTo(id);
        criteria.andEntityTypeEqualTo(EntityType.ENTITY_TRADE);

        List<Image> images = imageMapper.selectByExample(example);
        return images;
    }


    @Autowired
    CommentService commentService;

    @ApiOperation(value = "商品详情页")
    @ApiImplicitParam(name = "tradeId", value = "商品id")
    @RequestMapping(path = "/get/trade/detail", method = RequestMethod.GET)
    public JSONObject queryTrade(@RequestParam("tradeId") String tradeId) {
        JSONObject result = new JSONObject();
        User user = UserContext.getCurrentUser();
        Trade trade = tradeService.getTradeById(tradeId);
        List<Image> images = getImageList(tradeId);
        List<String> imageUrls = new LinkedList<>();
        for (Image image :
                images) {
            imageUrls.add(image.getUrl());
        }

        result.put("images", imageUrls);
        result.put("nickname", trade.getAnonymousNickName());
        result.put("title", trade.getTitle());
        result.put("describe", trade.getContent());
        result.put("time", DateUtils.getDateString(trade.getUpdatedDate()));
        result.put("qq", trade.getQq());
        result.put("weixin", trade.getWeixin());
        result.put("price", trade.getPrice());
        result.put("headUrl", trade.getAnonymousHeadUrl());
        if(user != null) {
            String userId = String.valueOf(user.getId());
            String key = RedisKeyUtil.getKey(EventType.FOLLOW.getValue(), EntityType.ENTITY_TRADE, tradeId);
            String val = RedisKeyUtil.getVal(EntityType.ENTITY_USER, userId);
            boolean follow = jedisAdapter.sismember(key, val);
            result.put("follow", follow);
            if (user.getId() == trade.getOwnerId()) {
                int status = trade.getStatus();
                switch (status) {
                    case 0:
                        result.put("sellStatus", "未出售");
                    case 1:
                        result.put("sellStatus", "已过期");
                    case 2:
                        result.put("sellStatus", "已出售");
                }
            }
        }else {
            result.put("follow", false);
        }

//        Map<String, Object> comments = new HashMap<>();
//        List<Comment> comments1 = commentService.queryAllCommentsByEntityId(EntityType.ENTITY_TRADE, trade.getId());
//        model.addAttribute("comments", comments);
        return result;
    }

    @Value("${file.path}")
    String filePath;

    @RequestMapping(path = "/trade/uploadHeadImage", method = RequestMethod.POST)
    @ResponseBody
    public String uploadHeadImage(MultipartFile file) {
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        String fileName = UUID.randomUUID().toString() + extName;
        try {
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(filePath + fileName)));
            return "ok";
        } catch (Exception e) {
            return "fail";
        }
    }


    @Autowired
    ImageMapper imageMapper;

    @Value("${qiniu.domain}")
    String qiNiuDomain;

    @Autowired
    SensitiveService sensitiveService;

    @ApiOperation(value = "发布二手物品信息", notes = "跳蚤市场板块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "发布物品标题"),
            @ApiImplicitParam(name = "describe", value = "发布物品描述"),
            @ApiImplicitParam(name = "qq", value = "qq号"),
            @ApiImplicitParam(name = "weixin", value = "微信号"),
            @ApiImplicitParam(name = "price", value = "价格，单位元"),
            @ApiImplicitParam(name = "xiaoQu", value = "0代表长安校区， 1代表雁塔校区"),
            @ApiImplicitParam(name = "pics", value = "以文件的形式上传多张图片")
    })
    @RequestMapping(path = "/add/trade", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addTrade(@RequestParam("title") String title,
                               @RequestParam("describe") String content,
                               @RequestParam(value = "qq", required = false) String qq,
                               @RequestParam(value = "weixin", required = false) String weixin,
                               @RequestParam(value = "price", required = true) int price,
                               @RequestParam(value = "xiaoQu", required = true) int xiaoQu,
                               @RequestParam(value = "pics") MultipartFile[] files) {
        JSONObject result = new JSONObject();
        if(files == null || files.length == 0) {
            log.error("图片上传失败");
            result.put("msg", "fail");
            return result;
        }


        if (!StringUtils.isEmpty(qq)) {
            boolean correct = QQCheckUtils.checkQQ(qq);
            if (!correct) {
                result.put("msg", "fail");
                result.put("detail", "qq不合法");
                return result;
            }
        }
        if (!StringUtils.isEmpty(weixin)) {
            boolean correct = WeixinCheckUtil.checkWeixin(weixin);
            if (!correct) {
                result.put("msg", "fail");
                result.put("detail", "微信号不合法");
                return result;
            }
        }

        User user = UserContext.getCurrentUser();
        //分布式id
        String tradeId = UUID.randomUUID().toString().replaceAll("-", "");

        Trade trade = new Trade();
        trade.setId(tradeId);
        trade.setTitle(title);
        if (xiaoQu == XiaoquStatus.changAn) {
            trade.setXiaoqu(xiaoQu);
        } else if (xiaoQu == XiaoquStatus.yanTa) {
            trade.setXiaoqu(xiaoQu);
        } else {
            result.put("msg", "fail");
            result.put("detail", "校区不正确");
            return result;
        }
        int anonymous = AnonymousStatus.non_annoy;
        trade.setAnonymous(anonymous);
        if (anonymous == AnonymousStatus.annoy) {
            trade.setAnonymousNickName(AnonymousUtils.getAnonymousNickName());
            trade.setAnonymousHeadUrl(AnonymousUtils.getAnonymousHeadUrl());
        } else {
            trade.setAnonymousNickName(user.getNickName());
            trade.setAnonymousHeadUrl(user.getHeadUrl());
        }
        content = sensitiveService.filter(content);
        trade.setContent(content);
        trade.setCreatedDate(new Date());
        trade.setUpdatedDate(new Date());
        trade.setOwnerId(user.getId());
        if (qq != null) {
            trade.setQq(qq);
        } else {
            trade.setQq("");
        }
        if (weixin != null) {
            trade.setWeixin(weixin);
        } else {
            trade.setWeixin("");
        }

        trade.setPrice(price);
        trade.setDelFlag(DelStatus.non_del);
        trade.setStatus(TradeStatus.willSell);
        try {

            tradeService.addTrade(trade);

            if (files != null && files.length >= 1) {
                for (MultipartFile file : files) {
                    BufferedOutputStream bw = null;
                    try {
                        String fileName = file.getOriginalFilename();
                        //判断是否有文件且是否为图片文件
                        if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && isImageFile(fileName)) {

                            String fileUrl = filePath + "/" + UUID.randomUUID().toString() + getFileType(fileName);
                            //创建输出文件对象
                            File outFile = new File(fileUrl);
                            //拷贝文件到输出文件对象
                            FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);

                            DefaultPutRet ret = fileUpload(Zone.zone0(), ImageUtils.getUploadCredential(), fileUrl);

                            String imageUrl = qiNiuDomain + "/" + ret.hash;
                            log.info("upload imageUrl = " + imageUrl);

                            Image image = new Image();
                            image.setEntityType(EntityType.ENTITY_TRADE);
                            image.setEntityId(tradeId);
                            image.setUrl(imageUrl);
                            image.setCreatedDate(new Date());
                            image.setDelStatus(DelStatus.non_del);
                            image.setCreatedBy(user.getId());
                            image.setUpdatedBy(user.getId());
                            image.setUpdatedDate(new Date());
                            imageMapper.insert(image);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bw != null) {
                                bw.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            result.put("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "fail");
            result.put("detail", "上传失败");
        }
        return result;
    }


    /**
     * 判断文件是否为图片文件
     *
     * @param fileName
     * @return
     */
    private Boolean isImageFile(String fileName) {
        String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (fileName == null) {
            return false;
        }
        fileName = fileName.toLowerCase();
        for (String type : img_type) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

//    private Boolean isImageFile2(String filename){
//
//    }



    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    private String getFileType(String fileName) {
        if (fileName != null && fileName.indexOf(".") >= 0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }

    @ApiOperation(value = "确认商品出售")
    @PostMapping("/confirm/trade")
    public JSONObject confirmTrade(@RequestParam("tradeId") String tradeId) {
        JSONObject result = new JSONObject();
        Trade trade = tradeService.getTradeById(tradeId);
        User user = UserContext.getCurrentUser();

        long ownerId = trade.getOwnerId();
        long userId = user.getId();
        if (ownerId == userId) {
            trade.setStatus(TradeStatus.solded);
            trade.setUpdatedDate(new Date());
            tradeService.updateTrade(trade);
            result.put("msg", "ok");
            return result;
        } else {
            log.info("ownerId = " + ownerId + " " + "userId = " + userId);
            result.put("msg", "fail");
        }
        return result;
    }
}
