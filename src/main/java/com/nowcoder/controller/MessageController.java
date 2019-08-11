package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2019/8/11.
 */
@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @RequestMapping(path="/msg/sendMessage",method = RequestMethod.POST)
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try {
            Message message=new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setConversationId(fromId>toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());
        }catch (Exception e){
            logger.error("发送消息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1, "插入评论失败");
        }
    }
    @RequestMapping(path="/msg/msgDetail",method = RequestMethod.GET)
    public String getMsgDetail(Model model,@Param("conversationId") String conversationId){
        try {
            List<Message> conversationList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<>();
            for(Message message:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                User user=userService.getUser(message.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取消息详情页失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path="/msg/list",method = RequestMethod.GET)
    public String conversationDetail(Model model){
        try {

        }catch (Exception e){
            logger.error("获取站内信失败"+e.getMessage());
        }
        return "letter";
    }

}
