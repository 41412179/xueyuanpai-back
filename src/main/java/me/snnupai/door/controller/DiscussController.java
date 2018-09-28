package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscussController {
    Logger logger = LoggerFactory.getLogger(DiscussController.class);

    @RequestMapping(path = "/submit")
    public String submit(@RequestParam("title") String title,
                         @RequestParam("content") String content){
        logger.info("title=" + title);
        logger.info("content= " + content);
        return "ok";
    }
}
