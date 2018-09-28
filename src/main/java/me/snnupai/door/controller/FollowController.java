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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class FollowController {

    @Autowired
    JedisAdapter jedisAdapter;

    @ApiOperation(value = "关注别人", notes = "关注其他用户的接口")
    @ApiImplicitParam(name = "followeeid", value = "我准备关注的人的id")
    @RequestMapping(value = "/follow/user", method = RequestMethod.POST)
    public JSONObject follow(@RequestParam("followeeid") long followeeid) {
        String localUserId = String.valueOf(UserContext.getCurrentUser().getId());
        //to do list
        return null;
    }


    /**
     *
     * @param loveId
     * @return
     */
    @ApiOperation(value = "关注一条表白信息")
    @ApiImplicitParam(name = "loveId", value = "关注的表白信息id")
    @PostMapping(value = "/follow/loveWall")
    public JSONObject followLoveWall(@RequestParam("loveId") Long loveId) {
        User user = UserContext.getCurrentUser();
        String userId = String.valueOf(user.getId());
        int eventType = EventType.FOLLOW.getValue();
        int entityType = EntityType.ENTITY_LOVE;
        String entityId = String.valueOf(loveId);
        JSONObject result = new JSONObject();
        try {
            jedisAdapter.sadd(eventType, entityType, entityId, EntityType.ENTITY_USER, userId);
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }

    @ApiOperation(value = "关注商品")
    @ApiImplicitParam(name = "tradeId", value = "商品id")
    @PostMapping(value = "/follow/trade")
    public JSONObject followTrade(@RequestParam("tradeId") String tradeId){
        User user = UserContext.getCurrentUser();
        String userId = String.valueOf(user.getId());
        int eventType = EventType.FOLLOW.getValue();
        int entityType = EntityType.ENTITY_TRADE;
        String entityId = tradeId;
        JSONObject result = new JSONObject();
        try {
            jedisAdapter.sadd(eventType, entityType, entityId, EntityType.ENTITY_USER, userId);
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }
}
