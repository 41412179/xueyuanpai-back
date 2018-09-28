package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.async.EventType;
import me.snnupai.door.model.BanKuaiType;
import me.snnupai.door.model.EntityType;
import me.snnupai.door.pojo.Love;
import me.snnupai.door.pojo.User;
import me.snnupai.door.service.CommentService;
import me.snnupai.door.service.LoveService;
import me.snnupai.door.service.SensitiveService;
import me.snnupai.door.service.UserService;
import me.snnupai.door.status.AnonymousStatus;
import me.snnupai.door.status.LoveStatus;
import me.snnupai.door.util.AnonymousUtils;
import me.snnupai.door.util.DateUtils;
import me.snnupai.door.util.JedisAdapter;
import me.snnupai.door.util.RedisKeyUtil;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class LoverController {
    @Autowired
    LoveService loveService;

    @Autowired
    UserService userService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    CommentService commentService;

    @ApiOperation(value = "获取表白墙列表", notes = "用户未登录时可以查看列表，但是无法评论和点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", value = "第几页，从第一页开始"),
            @ApiImplicitParam(name = "limit", value = "每页多少条信息")
    })
    @RequestMapping(path = "/loveWall/list", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryLoveInfos(@RequestParam("offset") int offset,
                                     @RequestParam("limit") int limit) {
        JSONObject result = getLoveInfos(offset, limit);
        return result;
    }

    public JSONObject getLoveInfos(int offset, int limit) {
        List<Love> loves = loveService.getLoveList(offset, limit);
        List<Map<String, Object>> loveInfos = new ArrayList<>();
        for (Love love :
                loves) {
            Long loveId = love.getId();
            Map<String, Object> info = new LinkedHashMap<String, Object>();
            info.put("content", love.getContent());
            info.put("time", DateUtils.getDateString(love.getCreatedDate()));
            info.put("loveId", loveId);
            Subject subject = SecurityUtils.getSubject();
            boolean isAuth = subject.isAuthenticated();
            boolean isFollow = false;
            boolean isLike = false;
            String followKey = RedisKeyUtil.getKey(EventType.FOLLOW.getValue(), EntityType.ENTITY_LOVE, String.valueOf(loveId));
            String likeKey = RedisKeyUtil.getKey(EventType.LIKE.getValue(), EntityType.ENTITY_LOVE, String.valueOf(loveId));
            if (isAuth) {
                User user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
                String userId = String.valueOf(user.getId());
                String followVal = RedisKeyUtil.getVal(EntityType.ENTITY_USER, userId);
                isFollow = jedisAdapter.sismember(followKey, followVal);
                String likeVal = RedisKeyUtil.getVal(EntityType.ENTITY_USER, userId);
                isLike = jedisAdapter.sismember(likeKey, likeVal);
            }
            info.put("isFollow", isFollow);
            info.put("isLike", isLike);
            Long likeNum = jedisAdapter.scard(likeKey);
            info.put("likeNum", likeNum);
            info.put("commentNum", commentService.queryCommentNum(BanKuaiType.love, EntityType.ENTITY_LOVE, String.valueOf(loveId)));

            JSONObject commentListInfo;
            commentListInfo = commentService.queryCommentsPage(BanKuaiType.love, EntityType.ENTITY_LOVE, String.valueOf(loveId), offset, limit);
            info.put("commentListInfo", commentListInfo);
            int anonymous = love.getAnonymous();
            if (anonymous == AnonymousStatus.annoy) {
                info.put("anonymous", "匿名发布");
                info.put("nickname", love.getAnonymousNickName());
                info.put("headUrl", love.getAnonymousHeadUrl());
            } else {
                Long userId = love.getUserId();
                User user = userService.getUserById(userId);
                info.put("anonymous", "实名发布");
                info.put("nickname", user.getNickName());
                info.put("headUrl", user.getHeadUrl());
            }
            loveInfos.add(info);
        }
        JSONObject result = new JSONObject();
        result.put("loveInfos", loveInfos);
        boolean noMore;
        Long loveInfoCounts = loveService.queryLoveCounts();
        if (offset * limit >= loveInfoCounts) {
            noMore = true;
        } else {
            noMore = false;
        }
        result.put("noMore", noMore);
        return result;
    }


    @Autowired
    SensitiveService sensitiveService;

    static Random random = new Random();

    @ApiOperation(value = "发布表白信息", notes = "发布表白信息接口，必须登录才能发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "anonymous", value = "是否匿名发布，1表示匿名，0表示不匿名"),
            @ApiImplicitParam(name = "content", value = "表白内容")
    })
    @RequestMapping(path = "/love/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addLove(
            @RequestParam("anonymous") int anonymous,
            @RequestParam("content") String content) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getSession().getAttribute(Constants.SESSION_USER_INFO);

        Love love = new Love();
        love.setCreatedDate(new Date());
        love.setUpdatedDate(new Date());
        love.setStatus(LoveStatus.normal);
        love.setUserId(user.getId());
        love.setContent(sensitiveService.filter(content));
        love.setAnonymous(anonymous);
        if(anonymous == AnonymousStatus.annoy){
            love.setAnonymousNickName(AnonymousUtils.getAnonymousNickName());
            love.setAnonymousHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
        }
        JSONObject result = new JSONObject();
        try {
            loveService.addLove(love);
            result.put("msg", "ok");
        } catch (Exception e) {
            result.put("msg", "fail");
        }
        return result;
    }
}
