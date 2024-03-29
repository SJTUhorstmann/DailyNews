package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String,Object> register(String name,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(name)){
            map.put("msgname","用户名为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msgpwd","密码为空");
            return map;
        }
        User user=userDAO.selectByName(name);
        if(user!=null){
            map.put("msgname","该用户已经被注册");
            return map;
        }
        user=new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setHeadUrl("");
        userDAO.addUser(user);
        return map;
    }

    public Map<String,Object> login(String name,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(name)){
            map.put("msgname","用户名为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msgpwd","密码为空");
            return map;
        }
        User user=userDAO.selectByName(name);
        if(user==null){
            map.put("msgname","该用户名不存在");
            return map;
        }
        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginTicket.setExpiredTime(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicket.setStatus(0);
        loginTicketDAO.insert(loginTicket);
        return loginTicket.getTicket();
//        User user=userDAO.selectByName(username);
//        loginTicket.setUserId(user.getId());
//        String ticket=UUID.randomUUID().toString().replace("-","");
//        loginTicket.setTicket(ticket);
//        loginTicket.setStatus(0);
//        Date date=new Date();
//        date.setTime(date.getTime()+3600*24);
//        loginTicketDAO.insert(loginTicket);
//        return ticket;
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
