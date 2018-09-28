package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.model.HostHolder;
import me.snnupai.door.pojo.Post;
import me.snnupai.door.service.ForumService;
import me.snnupai.door.util.StatusType;
import me.snnupai.door.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static me.snnupai.door.util.Utils.fail;
import static me.snnupai.door.util.Utils.ok;
import static me.snnupai.door.util.Utils.limit;

@RestController
@Slf4j
public class ForumController {

    @Autowired
    ForumService forumService;

    @RequestMapping(path = "/forum/post/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addForumPost(@RequestParam("content") String content,
                               @RequestParam("title") String title){

        Post post = new Post();
        post.setUserId(3L);
        post.setContent(content);
        post.setCreatedDate(new Date());

        post.setTitle(title);
        post.setStatus(StatusType.create.ordinal());
        JSONObject result = new JSONObject();
        try {
            forumService.insertForumPost(post);
            result.put("msg", "ok");
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("msg", "fail");
        }
        return result;
    }

    @RequestMapping(path = "/forum/post/list", method = RequestMethod.GET)
    @ResponseBody
    public String queryPostList(@RequestParam("pagenum") int pagenum,
                                Model model){
        int offset = (pagenum - 1) * limit;
        int limit = Utils.limit;
        List<Post> posts = forumService.queryPostList(offset, Utils.limit);
        model.addAttribute("posts", posts);
        return JSONObject.toJSONString(posts);
    }
}
