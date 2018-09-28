package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.async.EventType;
import me.snnupai.door.model.EntityType;
import me.snnupai.door.model.UserContext;
import me.snnupai.door.pojo.User;
import me.snnupai.door.util.JedisAdapter;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 点赞
 */
@RestController
@Slf4j
public class LikeController {

    @Autowired
    JedisAdapter jedisAdapter;

//    @RequestMapping(path = "/trade/follow", method = RequestMethod.POST)
//    @ResponseBody
//    public String followTrade(@RequestParam("id") String tradeId){
//
//        User user = UserContext.getCurrentUser();
//        if(user == null ){
//            return "redirect:/reglogin";
//        }else{
//            String key = RedisKeyUtil.getCollectionKey(EntityType.ENTITY_TRADE, user.getId());
//            long ret = -1;
//            if(jedisAdapter.sismember(key, tradeId)){
//                ret = jedisAdapter.srem(key, tradeId);
//            }else{
//                ret = jedisAdapter.sadd(key, tradeId);
//            }
//            boolean status = jedisAdapter.sismember(key, tradeId);
//            return status == true ? "1" : "2";
//        }
//    }


//    @PostMapping(path = "/trade/comment/like/status")
//    @ResponseBody
//    public String getStatus(@RequestParam("commentId") Long commentId){
//        User user = UserContext.getCurrentUser();
//        if(user == null){
//            return "fail";
//        }
//        String key =RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT, commentId);
//        if(jedisAdapter.sismember(key, String.valueOf(user.getId()))){
//            return "true" ;
//        }else {
//            return "false";
//        }
//    }


//    @PostMapping(path = "/trade/comment/like")
//    @ResponseBody
//    public String likeTradeComment(@RequestParam("commentId") Long commentId){
//        User user = UserContext.getCurrentUser();
//        if(user == null){
//            return "fail";
//        }
//        String key =RedisKeyUtil.getLikeKey(EntityType.ENTITY_COMMENT, commentId);
//
//        if (jedisAdapter.sismember(key, String.valueOf(user.getId()))) {
//            jedisAdapter.srem(key, String.valueOf(user.getId()));
//        } else {
//           jedisAdapter.sadd(key, user.getId());
//        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("isLike", jedisAdapter.sismember(key, String.valueOf(user.getId())) ? "true" :"false" );
//        result.put("num", jedisAdapter.scard(key));
//        result.put("status", "ok");
//
//
//        JSONObject jsonObject = new JSONObject();
//        List<Map<String, Object>> info = new ArrayList<>();
//        info.add(result);
//        jsonObject.put("info", info);
//
//        return jsonObject.toString();
//    }

//    @RequestMapping(path = "/trade/like", method = RequestMethod.POST)
//    @ResponseBody
//    public String likeTrade(@RequestParam("tradeid") Long tradeId){
//        User user = UserContext.getCurrentUser();
//        if(user == null){
//            return "fail";
//        }
//
//        String key = RedisKeyUtil.getLikeKey( EntityType.ENTITY_TRADE, tradeId);
//        return String.valueOf(jedisAdapter.sadd(key, user.getId()));
//    }

//    @RequestMapping(path = "/trade/dislike", method = RequestMethod.POST)
//    @ResponseBody
//    public String dislikeTrade(@RequestParam("userid") Long userId,
//                            @RequestParam("tradeid") Long tradeId){
//        String key = RedisKeyUtil.getDisLikeKey( EntityType.ENTITY_TRADE, tradeId);
//        return String.valueOf(jedisAdapter.sadd(key, userId));
//    }

//    @RequestMapping(path = "/trade/comment/like", method = RequestMethod.POST)
//    @ResponseBody
//    public String likeComment(@RequestParam("userid") Long userId,
//                            @RequestParam("commentid") Long commentId){
//        String key = RedisKeyUtil.getLikeKey( EntityType.ENTITY_COMMENT, commentId);
//        return String.valueOf(jedisAdapter.sadd(key, userId));
//    }

//    @RequestMapping(path = "/trade/comment/dislike", method = RequestMethod.POST)
//    @ResponseBody
//    public String dislikeComment(@RequestParam("userid") Long userId,
//                              @RequestParam("commentid") Long commentId){
//        String key = RedisKeyUtil.getDisLikeKey( EntityType.ENTITY_COMMENT, commentId);
//        return String.valueOf(jedisAdapter.sadd(key, userId));
//    }
//
//
//    @RequestMapping(path = "/forum/post/like", method = RequestMethod.POST)
//    @ResponseBody
//    public String likePost(@RequestParam("postid") Long postId){
//        User user = UserContext.getCurrentUser();
//        String key = RedisKeyUtil.getLikeKey(EntityType.ENTITY_FORUM_POST, postId);
//        return String.valueOf(jedisAdapter.sadd(key, user.getId()));
//    }
//
//    @RequestMapping(path = "/forum/post/dislike", method = RequestMethod.POST)
//    @ResponseBody
//    public String disLikePost(@RequestParam("postid") Long postId){
//        User user = UserContext.getCurrentUser();
//        String key = RedisKeyUtil.getDisLikeKey(EntityType.ENTITY_FORUM_POST, postId);
//        return String.valueOf(jedisAdapter.sadd(key, user.getId()));
//    }

    @ApiOperation(value = "对一条表白信息点赞")
    @ApiImplicitParam(name = "loveId", value = "表白信息的id")
    @PostMapping("/loveWallInfoLike")
    public JSONObject likeLove(@RequestParam("loveId") Long loveId){
        JSONObject result = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
            String userId = String.valueOf(user.getId());

            int eventType = EventType.LIKE.getValue();
            int entityType = EntityType.ENTITY_LOVE;
            jedisAdapter.sadd(eventType, entityType, String.valueOf(loveId), EntityType.ENTITY_USER, String.valueOf(userId));
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }

    @ApiOperation(value = "关注一条表白信息")
    @ApiImplicitParam(name = "loveId", value = "表白信息id")
    @PostMapping("/loveWallInfoFollow")
    public JSONObject followLove(@RequestParam("loveId") Long loveId){
        JSONObject result = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
            Long userId = user.getId();
            int eventType = EventType.FOLLOW.getValue();
            int entityType = EntityType.ENTITY_LOVE;
            jedisAdapter.sadd(eventType, entityType, String.valueOf(loveId), EntityType.ENTITY_USER, String.valueOf(userId));

            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }

    @ApiOperation(value = "对一条评论点赞")
    @ApiImplicitParam(name = "commentId", value = "评论的id")
    @PostMapping("/like/comment")
    public JSONObject likeComment(@RequestParam("commentId") String commentId){
        JSONObject result = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
            String userId = String.valueOf(user.getId());
            int eventType = EventType.LIKE.getValue();
            int entityType = EntityType.ENTITY_COMMENT;
            jedisAdapter.sadd(eventType, entityType, commentId, EntityType.ENTITY_USER, userId);
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }
    @ApiOperation(value = "对一条评论踩")
    @ApiImplicitParam(name = "commentId", value = "评论的id")
    @PostMapping("/dislike/comment")
    public JSONObject dislikeComment(@RequestParam("commentId") String commentId){
        JSONObject result = new JSONObject();
        try {
            User user = UserContext.getCurrentUser();
            String userId = String.valueOf(user.getId());
            int eventType = EventType.DISLIKE.getValue();
            int entityType = EntityType.ENTITY_COMMENT;
            jedisAdapter.sadd(eventType, entityType, commentId, EntityType.ENTITY_USER, userId);
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }
}
