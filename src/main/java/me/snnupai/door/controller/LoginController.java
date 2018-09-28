package me.snnupai.door.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.snnupai.door.async.EventProducer;
import me.snnupai.door.pojo.User;
import me.snnupai.door.service.UserService;
import me.snnupai.door.util.constants.Constants;
import org.apache.lucene.index.DocIDMerger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    EventProducer eventProducer;

    @Autowired
    UserService userService;


    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String"),
            @ApiImplicitParam(name = "rememberme", value = "是否记住我,记住时间为一周", dataType = "bool"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "confirmPassword", value = "验证密码", dataType = "String")
    })
    @RequestMapping(path = {"/reg"}, method = {RequestMethod.POST})
    public JSONObject reg(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                          @RequestParam("email") String email,
                          @RequestParam("phone") String phone,
                          @RequestParam("confirmPassword") String confimPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (!password.equals(confimPassword)) {
                jsonObject.put("msg", "你输入的两次密码不一致");
                return jsonObject;
            }
            jsonObject = userService.register(username, password, email, phone);
            if (jsonObject.get("msg").equals("ok")) {
                logger.info("注册成功！");
                try {
                    userService.login(phone, password, rememberme);
                }catch (Exception e){
                    logger.error("登录失败...");
                    e.printStackTrace();
                }
            } else {
                logger.info("注册失败！");
            }
            return jsonObject;
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            jsonObject.put("msg", "注册异常");
            return jsonObject;
        }
    }

    @ApiOperation(value = "用户登录仅仅支持邮箱和手机号登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userstr", value = "手机号或者邮箱", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String"),
            @ApiImplicitParam(name = "rememberme", value = "是否记住我，时间为一周", dataType = "bool")
    })
    @RequestMapping(path = {"/login"}, method = {RequestMethod.POST})
    public JSONObject login(Model model, @RequestParam("userstr") String userstr,
                            @RequestParam("password") String password,
                            @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme) {
        JSONObject jsonObject = new JSONObject();
        logger.info("/login: " + jsonObject.toJSONString());
        try {
            jsonObject = userService.login(userstr, password, rememberme);
            Subject subject = SecurityUtils.getSubject();
            User user = userService.selectByEmailOrPhone(userstr);
            subject.getSession(true).setAttribute(Constants.SESSION_USER_INFO, user);
        } catch (IncorrectCredentialsException e) {
            jsonObject.put("msg", "你输入密码不正确");
        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            jsonObject.put("msg", "登录失败");
        } finally {
            return jsonObject;
        }
    }


    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getUserInfo")
    public JSONObject getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        JSONObject result = new JSONObject();
        User user = null;
        try {
            if(subject.isAuthenticated()){
                user = (User) subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
            }else{
                if(subject.isRemembered()){
                    String str = (String)subject.getPrincipal();
                    user = userService.selectByEmailOrPhone(str);
                }else{
                    logger.info("no login!");
                }
            }

            if(user != null) {
                result.put("username", user.getNickName());
                result.put("userId", user.getId());
                result.put("msg", "ok");
                logger.info("获取用户信息成功");
            }else{
                result.put("msg", "fail");
            }
        } catch (Exception e) {
            logger.info("获取用户信息失败");
            e.printStackTrace();
            result.put("msg", "fail");
        }
        return result;
    }

    @ApiOperation(value = "退出登录状态")
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return "ok";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "fail";
        }
    }
}
