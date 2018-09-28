package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import me.snnupai.door.mapper.UserMapper;
import me.snnupai.door.model.BanKuaiType;
import me.snnupai.door.model.EntityType;
import me.snnupai.door.model.HostHolder;
import me.snnupai.door.model.UserContext;
import me.snnupai.door.pojo.Comment;
import me.snnupai.door.pojo.User;
import me.snnupai.door.service.CommentService;
import me.snnupai.door.service.SensitiveService;
import me.snnupai.door.service.TradeService;
import me.snnupai.door.status.AnonymousStatus;
import me.snnupai.door.status.CommentStatus;
import me.snnupai.door.util.AnonymousUtils;
import me.snnupai.door.util.JedisAdapter;
import me.snnupai.door.util.RedisKeyUtil;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Log4j
public class CommentController {

    @Autowired
    CommentService commentService;
    //    @Autowired
//    HostHolder hostHolder;
    @Autowired
    TradeService tradeService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SensitiveService sensitiveService;

    @ApiOperation(value = "查询表白墙某一条帖子的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loveId", value = "表白帖子id"),
            @ApiImplicitParam(name = "offset", value = "第几页"),
            @ApiImplicitParam(name = "limit", value = "每页多少条")
    })
    @PostMapping(path = "/love/comment/list")
    public JSONObject getLoveCommentList(@RequestParam("loveId") Long loveId,
                                         @RequestParam("offset") int offset,
                                         @RequestParam("limit") int limit){
        JSONObject commentListInfo;
        commentListInfo = commentService.queryCommentsPage(BanKuaiType.love, EntityType.ENTITY_LOVE, String.valueOf(loveId), offset, limit);

        return commentListInfo;
    }

    @ApiOperation(value = "查询某一条跳蚤商品的评论列表")
    @PostMapping(path = "/trade/comment/list")
    public JSONObject getTradeCommentList(@RequestParam("tradeId") String tradeId,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit){
        JSONObject commentListInfo;
        commentListInfo = commentService.queryCommentsPage(BanKuaiType.trade, EntityType.ENTITY_TRADE, tradeId, offset, limit);
        return commentListInfo;
    }

/*
    @RequestMapping(path = "/comment/list", method = RequestMethod.GET)
    @ResponseBody
    public String getTradeCommentList(HttpServletRequest request) {
        log.info("comment list page....");
        String postId = request.getParameter("postId");
        int offset = Integer.parseInt(request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int banKuaiType = Integer.parseInt(request.getParameter("banKuaiType"));

        List<Map<String, Object>> commentInfos = new LinkedList<>();
        List<Comment> comments = commentService.queryAllCommentsByEntityId(banKuaiType, postId, offset, limit);

        for (Comment comment :
                comments) {
            int entityType = comment.getEntityType();
            Long ownerId = comment.getCreatedBy();
            String nickname = userMapper.selectByPrimaryKey(ownerId).getNickName();

            String toName = "";
            if (entityType == EntityType.ENTITY_TRADE) {
                toName = "";
            } else if (entityType == EntityType.ENTITY_COMMENT) {
                String commentId = comment.getEntityId();
                Comment toComment = commentService.queryOneById(Long.parseLong(commentId));
                Long toId = toComment.getCreatedBy();
                toName = userMapper.selectByPrimaryKey(toId).getNickName();
            }
            String content = comment.getContent();
            Date time = comment.getCreatedDate();
            Map<String, Object> info = new HashMap<>();
            info.put("nickname", nickname);
            info.put("toName", toName);
            info.put("entityType", entityType);
            info.put("content", content);
            info.put("id", comment.getId());
            info.put("num", jedisAdapter.scard(RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT, comment.getId())));
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:MM");

            info.put("time", bartDateFormat.format(time));
            commentInfos.add(info);
        }
        log.info(JSONObject.toJSONString(commentInfos));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commentInfos", commentInfos);
        return jsonObject.toString();
    }
*/
    @ApiOperation(value = "回复评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "被回复评论id"),
            @ApiImplicitParam(name = "content", value = "评论内容"),
            @ApiImplicitParam(name = "anonymous", value = "是否匿名回复，1表示匿名，0表示不匿名")
    })
    @PostMapping("/reply/comment")
    public JSONObject replyComment(@RequestParam("commentId") Long commentId,
                                   @RequestParam("content") String content,
                                   @RequestParam("anonymous") int anonymous) {
        JSONObject result = new JSONObject();
        User user = UserContext.getCurrentUser();
        Comment parentComment = commentService.queryOneById(commentId);
        int banKuaiType = parentComment.getBanKuaiType();
        int entityType = EntityType.ENTITY_COMMENT;
        String entityId = String.valueOf(commentId);
        String postId = parentComment.getPostId();
        content = sensitiveService.filter(content);
        try {
            addPostComment(banKuaiType, entityType, entityId, postId, anonymous, content, user.getId());
            result.put("msg", "ok");
        } catch (Exception e) {
            log.error(e.getStackTrace());
            result.put("msg", "fail");
        }
        return result;
    }

//    /**
////     * 回复别人的评论
////     *
////     * @param request
////     * @return
////     */
////    @RequestMapping(path = "/trade_comment_reply", method = RequestMethod.POST)
////    @ResponseBody
////    public String replyTradeComment(HttpServletRequest request) {
////
////        User user = UserContext.getCurrentUser();
////        String content = request.getParameter("content");
////        String commentId = request.getParameter("entityId");
////        int anonymous = Integer.parseInt(request.getParameter("anonymous"));
////
////
////        Comment toComment = commentService.queryOneById(Long.parseLong(commentId));
////
////        Comment comment = new Comment();
////        comment.setCreatedBy(user.getId());
////        comment.setUpdateBy(user.getId());
////        comment.setCreatedDate(new Date());
////        comment.setUpdatedDate(new Date());
////        comment.setEntityType(EntityType.ENTITY_COMMENT);
////        comment.setEntityId(commentId);
////        comment.setAnonymous(anonymous);
////        comment.setBanKuaiType(BanKuaiType.trade);
////        comment.setContent(content);
////        comment.setPostId(toComment.getPostId());
////        comment.setStatus(CommentStatus.normal);
////        try {
////            commentService.addComment(comment);
////            return "ok";
////        } catch (Exception e) {
////            log.error(e.getMessage());
////            return "fail";
////        }
////    }

    /**
     * 回复跳蚤市场帖子
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "回复跳蚤市场帖子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "回复内容" ),
            @ApiImplicitParam(name = "tradeId", value = "商品id"),
            @ApiImplicitParam(name = "anonymous", value = "是否匿名")
    })
    @RequestMapping(path = "/add/trade/comment", method = RequestMethod.POST)
    public JSONObject addTradeComment(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        User user = UserContext.getCurrentUser();

        String content = request.getParameter("content");
        int banKuaiType = BanKuaiType.trade;
        int entityType = EntityType.ENTITY_TRADE;
        String entityId = request.getParameter("tradeId");
        int anonymous = Integer.parseInt(request.getParameter("anonymous"));
        String postId = entityId;
        try {
            addPostComment(banKuaiType, entityType, entityId, postId, anonymous, content, user.getId());
            result.put("msg", "ok");
        } catch (Exception e) {
            log.error(e.getStackTrace());
            result.put("msg", "fail");
        }
        return result;
    }

//    @RequestMapping(path = "/forum/post/comment/add", method = RequestMethod.POST)
//    @ResponseBody
//    public String addPostComment(@RequestParam("content") String content,
//                                 @RequestParam("entityid") String entityId) {
//        return addComment(content, entityId, EntityType.ENTITY_FORUM_POST);
//    }
//
//    @RequestMapping(path = "/forum/postcomment/comment/add", method = RequestMethod.POST)
//    @ResponseBody
//    public String addCommentOfPostComment(@RequestParam("content") String content,
//                                          @RequestParam("entityid") String entityId) {
//        return addComment(content, entityId, EntityType.ENTITY_COMMENT);
//    }
//
//
//    private String addComment(String content, String entityId, int entityType) {
//        User user = hostHolder.getUser();
//        Comment comment = new Comment();
//        comment.setCreatedBy(user.getId());
//        comment.setContent(content);
//        comment.setCreatedDate(new Date());
//        comment.setEntityId(entityId);
//        comment.setEntityType(entityType);
//        return commentService.addComment(comment);
//    }

    /**
     * 给表白帖子添加评论
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "给表白信息添加评论", notes = "用户必须要登录状态才能评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entityId", value = "表白信息id"),
            @ApiImplicitParam(name = "content", value = "评论信息内容"),
            @ApiImplicitParam(name = "anonymous", value = "是否匿名发布")
    })
    @PostMapping("/add/love/comment")
    @ResponseBody
    public JSONObject addLoveComment(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String loveId = request.getParameter("entityId");
        String content = request.getParameter("content");
        content = sensitiveService.filter(content);
        int anonymous = Integer.parseInt(request.getParameter("anonymous"));
        int entityType = EntityType.ENTITY_LOVE;
        int banKuaiType = BanKuaiType.love;
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
        try {
            addPostComment(banKuaiType, entityType, loveId, loveId, anonymous, content, user.getId());
            result.put("msg", "ok");
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }

//    /**
//     * 回复别人的评论
//     * @param request
//     * @return
//     */
//    @PostMapping("/add/love/comment/comment")
//    @ResponseBody
//    public ResponseEntity addLoveCommentComment(HttpServletRequest request) {
//        User user = hostHolder.getUser();
//        String content = request.getParameter("content");
//        String commentId = request.getParameter("entityId");
//        int anonymous = Integer.parseInt(request.getParameter("anonymous"));
//        Comment toComment = commentService.queryOneById(Long.parseLong(commentId));
//        int banKuaiType = BanKuaiType.love;
//        int entityType = EntityType.ENTITY_COMMENT;
//        try {
//            addPostComment(banKuaiType, entityType, commentId, toComment.getPostId(), anonymous, content, user.getId());
//            return new ResponseEntity("ok", HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity("fail", HttpStatus.OK);
//        }
//    }


    private void addPostComment(int banKuaiType, int entityType, String entityId, String postId, int anonymous, String content, Long userId) {
        Comment comment = new Comment();
        comment.setBanKuaiType(banKuaiType);
        comment.setEntityType(entityType);
        comment.setEntityId(entityId);
        comment.setPostId(postId);
        comment.setAnonymous(anonymous);
        if(anonymous == AnonymousStatus.annoy){
            comment.setHeadUrl(AnonymousUtils.getAnonymousHeadUrl());
            comment.setNickName(AnonymousUtils.getAnonymousNickName());
        }else{
            User user = UserContext.getCurrentUser();
            comment.setHeadUrl(user.getHeadUrl());
            comment.setNickName(user.getNickName());
        }
        String filterContent = sensitiveService.filter(content);
        comment.setContent(filterContent);
        comment.setCreatedDate(new Date());
        comment.setUpdatedDate(new Date());
        comment.setCreatedBy(userId);
        comment.setUpdateBy(userId);
        comment.setStatus(CommentStatus.normal);
        commentService.addComment(comment);
    }
}
