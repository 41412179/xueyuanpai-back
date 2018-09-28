package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import me.snnupai.door.service.GongGaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guohaodong
 * @create 2018-04-11 22:10
 **/
@RestController
public class GongGaoController {

    @Autowired
    GongGaoService gongGaoService;

    @PostMapping(path = "/back/gonggao/add")
    public String addGongGao(@RequestParam("content") String content){
        gongGaoService.addGongGao(content);
        return "redirect:/index";
    }

    @GetMapping(path = "/back/gonggao")
    public String gongGaoBack(){
        return "announcement";
    }


    @GetMapping(path = "/query/gonggao")
    public JSONObject queryGonggao(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", gongGaoService.getContent());
        return jsonObject;
    }

}
