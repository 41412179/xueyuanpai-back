package me.snnupai.door.model;

import me.snnupai.door.pojo.User;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserContext {
    public static User getCurrentUser(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getSession().getAttribute(Constants.SESSION_USER_INFO);
        return user;
    }
}
