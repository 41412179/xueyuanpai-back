package me.snnupai.door.service;


import com.alibaba.fastjson.JSONObject;
import me.snnupai.door.async.EventProducer;
import me.snnupai.door.mapper.FeedMapper;
import me.snnupai.door.mapper.LoginTicketMapper;
import me.snnupai.door.mapper.UserMapper;
import me.snnupai.door.pojo.*;
import me.snnupai.door.status.RegisterStatus;
import me.snnupai.door.status.UserStatus;
import me.snnupai.door.status.VIPStatus;
import me.snnupai.door.util.StringUtils;
import me.snnupai.door.util.Utils;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private EventProducer eventProducer;
    @Autowired
    UserMapper userMapper;

    public User selectByEmail(String email) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andEmailEqualTo(email);

        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public User selectByPhone(String phone) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andPhoneEqualTo(phone);

        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public JSONObject register(String nickname, String password, String email, String phone) {
        JSONObject map = new JSONObject();
        if (StringUtils.isBlank(nickname)) {
            map.put("msg", "昵称不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(phone)) {
            map.put("msg", "手机号不能为空");
            return map;
        }

        if (StringUtils.isBlank(email)) {
            map.put("msg", "邮箱不能为空");
            return map;
        }
        User user = selectByEmail(email);
        if (user != null) {
            map.put("msg", "邮箱已经被注册");
            return map;
        }

        user = selectByPhone(phone);

        if (user != null) {
            map.put("msg", "手机号已经被注册");
            return map;
        }

        if (nickname.contains(" ")) {
            map.put("msg", "昵称不能包含空格");
            return map;
        }

        if (nickname.length() > 16) {
            map.put("msg", "昵称长度不能大于16位");
            return map;
        }

        if (password.length() > 16) {
            map.put("msg", "密码长度不能大于16位");
            return map;
        }
        if (password.length() < 6) {
            map.put("msg", "密码长度不能小于6位");
            return map;
        }

        // 密码强度
        user = new User();
        user.setNickName(nickname);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        SimpleHash hash = new SimpleHash("MD5", password, null, 1);
        user.setPassword(hash.toBase64());
        user.setEmail(email);
        user.setPhone(phone);
        user.setRegisterStatus(RegisterStatus.unConfim);
        user.setStatus(UserStatus.normal);
        user.setVip(VIPStatus.non_member);
        user.setAccPoints(0);
        user.setDescription("");
        user.setCreatedDate(new Date());
        user.setUpdatedDate(new Date());
        user.setRealName("");
        user.setMajor("");
        user.setSex((byte) 1);
        user.setBirthYear(1997);
        user.setEntranceYear(2014);
        try {
            userMapper.insert(user);
            map.put("msg", "ok");
        } catch (Exception e) {
            logger.error(e.getMessage());
            map.put("msg", "fail");
        }
        return map;

        //发送验证邮件
//        eventProducer.fireEvent(new EventModel(EventType.LOGIN)
//                .setExt("nickname", nickname)
//                .setExt("email", email)
//                .setActorId(Utils.SYSTEM_USERID));

    }


    public JSONObject login(String phoneOrEmail, String password, boolean rememberme) {
        JSONObject result = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(phoneOrEmail, password, rememberme);
        logger.info("rememberMe: " + rememberme);
        token.setRememberMe(true);

        try {
            subject.login(token);
            User user = selectByEmailOrPhone(phoneOrEmail);
            subject.getSession(true).setAttribute(Constants.SESSION_USER_INFO, user);
            result.put("msg", "ok");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            result.put("msg", "fail");
        }
        return result;
    }

    @Autowired
    LoginTicketMapper loginTicketMapper;

    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public void updateUser(User user) {
        userMapper.insert(user);
    }

    @Autowired
    FeedMapper feedMapper;

    public List<Feed> queryFeed(long id, int offset, int limit) {
        FeedExample feedExample = new FeedExample();

        FeedExample.Criteria criteria = feedExample.createCriteria();
        criteria.andUserIdEqualTo(id);

        feedExample.setOrderByClause(" `id` asc ");
        feedExample.setOffset(offset);
        feedExample.setLimit(limit);

        List<Feed> feeds = feedMapper.selectByExample(feedExample);
        return feeds;
    }

    public User selectByEmailOrPhone(String emailOrPhone) {
        User emailUser = selectByEmail(emailOrPhone);
        User phoneUser = selectByPhone(emailOrPhone);
        if (emailUser != null) {
            return emailUser;
        } else if (phoneUser != null) {
            return phoneUser;
        } else {
            return null;
        }

    }
}
