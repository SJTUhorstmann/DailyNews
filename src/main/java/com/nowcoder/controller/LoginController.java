package com.nowcoder.controller;

import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lenovo on 2019/8/10.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;

    @RequestMapping(path="reg",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember",defaultValue = "0") int rem){
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path="login",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember",defaultValue = "0") int rem,
                        HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rem>0){
                    cookie.setMaxAge(24*3600*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"登陆成功");
            }else{
                return ToutiaoUtil.getJSONString(1,"登陆失败");
            }
        }catch (Exception e){
            logger.error("登陆失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆失败");
        }
    }
    @RequestMapping(path="logout",method = {RequestMethod.POST,RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
