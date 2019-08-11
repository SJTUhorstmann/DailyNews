package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lenovo on 2019/8/11.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path="/like",method = RequestMethod.GET)
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        long likeCount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateCommentCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path="/disLike",method = RequestMethod.GET)
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId){
        long likeCount=likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateCommentCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
