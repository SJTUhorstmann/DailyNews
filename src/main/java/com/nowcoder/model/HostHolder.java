package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2019/8/10.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<>();
    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
