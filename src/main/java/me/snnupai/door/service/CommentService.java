package me.snnupai.door.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.async.EventType;
import me.snnupai.door.mapper.CommentMapper;
import me.snnupai.door.model.EntityType;
import me.snnupai.door.model.UserContext;
import me.snnupai.door.pojo.Comment;
import me.snnupai.door.pojo.CommentExample;
import me.snnupai.door.pojo.User;
import me.snnupai.door.status.CommentStatus;
import me.snnupai.door.status.DelStatus;
import me.snnupai.door.util.DateUtils;
import me.snnupai.door.util.JedisAdapter;
import me.snnupai.door.util.RedisKeyUtil;
import me.snnupai.door.util.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Service;

import java.util.*;

import static me.snnupai.door.util.Utils.fail;
import static me.snnupai.door.util.Utils.getJSONString;
import static me.snnupai.door.util.Utils.ok;

@Service
@Slf4j
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    public String addComment(Comment comment) {
        int ret = commentMapper.insert(comment);
        if(ret < 0){
            log.error("评论插入失败！" + comment.toString());
            return fail;
        }else{
            return ok;
        }
    }

    public List<Comment> queryById(int type, String id) {
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andEntityTypeEqualTo(type);
        criteria.andEntityIdEqualTo(id);
        return commentMapper.selectByExample(example);
    }

    public List<Comment> queryAllCommentsByEntityId(int type, String id) {
        return null;
    }

    public List<Comment> queryAllCommentsByEntityId(int banKuaiType, String id, int offset, int limit) {
        CommentExample example = new CommentExample();

        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause(" `created_date` asc ");

        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andBanKuaiTypeEqualTo(banKuaiType)
                .andPostIdEqualTo(id)
                .andStatusEqualTo(CommentStatus.normal);

        return commentMapper.selectByExampleWithBLOBs(example);
    }

    public Comment queryOneById(long id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    public Long queryCommentNum(int banKuaiType, int entityType, String entityId) {
        CommentExample example = new CommentExample();
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andBanKuaiTypeEqualTo(banKuaiType);
        criteria.andEntityTypeEqualTo(entityType);
        criteria.andPostIdEqualTo(entityId);
        criteria.andStatusEqualTo(DelStatus.non_del);

        return commentMapper.countByExample(example);
    }

    @Autowired
    JedisAdapter jedisAdapter;

    public JSONObject queryCommentsPage(int banKuaiType, int entityType, String entityId, int offset, int limit) {
        CommentExample example = new CommentExample();
        offset = (offset - 1) * limit;
        log.info("offset = " + offset + ": limit=" + limit);
        example.setOffset(offset);
        example.setLimit(limit);

        example.setOrderByClause(" created_date desc ");
        CommentExample.Criteria criteria = example.createCriteria();
        criteria.andBanKuaiTypeEqualTo(banKuaiType);
        criteria.andEntityTypeEqualTo(entityType);
        criteria.andPostIdEqualTo(entityId);
        criteria.andStatusEqualTo(DelStatus.non_del);


        JSONObject commentList = new JSONObject();
        List<Comment> comments = commentMapper.selectByExampleWithBLOBs(example);
        List<Map<String, Object>> newComments = new ArrayList<>();

        for (Comment comment:
             comments) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("content", comment.getContent());
            map.put("createdDate", DateUtils.getDateString(comment.getCreatedDate()));
            map.put("headUrl", comment.getHeadUrl());
            map.put("anonymous", comment.getAnonymous());
            map.put("nickname", comment.getNickName());
            String commentId = String.valueOf(comment.getId());
            map.put("id", commentId);

            User user = UserContext.getCurrentUser();
            if(user == null){
                map.put("isLike", false);
                map.put("isDisLike", false);
            }else {
                String userId = String.valueOf(user.getId());
                String likeKey = RedisKeyUtil.getKey(EventType.LIKE.getValue(), EntityType.ENTITY_COMMENT, commentId);
                String disLikeKey = RedisKeyUtil.getKey(EventType.DISLIKE.getValue(), EntityType.ENTITY_COMMENT, commentId);
                String likeVal = RedisKeyUtil.getVal(EntityType.ENTITY_USER, userId);
                String disLikeVal = RedisKeyUtil.getVal(EntityType.ENTITY_USER, userId);
                boolean like = jedisAdapter.sismember(likeKey, likeVal);
                boolean dislike = jedisAdapter.sismember(disLikeKey, disLikeVal);
                map.put("isLike", like);
                map.put("isDisLike", dislike);
            }
            newComments.add(map);
        }
        commentList.put("commentList", newComments);

        boolean noMore = false;
        if(((offset * limit) >= queryCommentNum(banKuaiType, entityType, entityId))){
            noMore = true;
        }else{
            noMore = false;
        }
        commentList.put("noMore", noMore);
        return commentList;
    }
}
