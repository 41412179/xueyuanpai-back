package me.snnupai.door.shiro;



import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import me.snnupai.door.pojo.User;
import me.snnupai.door.service.UserService;
import me.snnupai.door.util.constants.Constants;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.yqr.jxc.service.global.GlobalUserService;
@Component
public class RememberAuthorizationFilter extends FormAuthenticationFilter {

//    @Resource(name="globalUserService")
//    private GlobalUserService globalUserService;
    Logger logger = LoggerFactory.getLogger(RememberAuthorizationFilter.class);
    @Resource(name = "userService")
    private UserService userService;

    /**
     * 这个方法决定了是否能让用户登录
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        boolean isLogined = subject.isAuthenticated() || subject.isRemembered();

        Session session = subject.getSession();
        Object object = session.getAttribute(Constants.SESSION_USER_INFO);
//        if(isLogined) {
//            if (object == null) {
//                String emailOrPhone = subject.getPrincipal().toString();
//                if(emailOrPhone == null){
//                    return false;
//                }
////                System.out.println(emailOrPhone);
//                logger.info("emailOrPhone = " + emailOrPhone);
//                User emailUser = null, phoneUser = null;
//                if(isEmail(emailOrPhone)) {
//                    emailUser = userService.selectByEmail(emailOrPhone);
//                }
//                if(isPhone(emailOrPhone)) {
//                    phoneUser = userService.selectByPhone(emailOrPhone);
//                }
//                if(emailUser != null){
//                    session.setAttribute(Constants.SESSION_USER_INFO, emailUser);
//                }else {
//                    if (phoneUser != null) {
//                        session.setAttribute(Constants.SESSION_USER_INFO, phoneUser);
//                    }else{
//                        logger.error("无法找该用户信息！");
//                        isLogined = false;
//                    }
//                }
//            }
//        }
        return isLogined;
    }

    private boolean isPhone(String string) {
        if(string == null){
            return false;
        }
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    private boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

    @Bean
    public FilterRegistrationBean registration(RememberAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(true);
        return registration;
    }

    public static RememberAuthorizationFilter rememberAuthorizationFilter;

    @PostConstruct
    public void init(){
        rememberAuthorizationFilter = this;
        rememberAuthorizationFilter.userService = this.userService;
        logger.info("rem = " + rememberAuthorizationFilter.userService);
    }
}
