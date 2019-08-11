package com.nowcoder.service;

import com.nowcoder.util.JedisUtil;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2019/8/11.
 */
@Service
public class LikeService {
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 喜欢返回1，不喜欢返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisUtil.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey= RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisUtil.sismember(disLikeKey,String.valueOf(userId))? -1:0;
    }

    public long like(int userId, int entityType, int entityId) {
        // 在喜欢集合里增加
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisUtil.sadd(likeKey,String.valueOf(userId));
        String disLikeKey= RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisUtil.srem(disLikeKey,String.valueOf(userId));
        return jedisUtil.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId) {
        // 在不喜欢集合里增加
        String disLikeKey= RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisUtil.sadd(disLikeKey,String.valueOf(userId));
        String likeKey=RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisUtil.srem(likeKey,String.valueOf(userId));
        return jedisUtil.scard(likeKey);
    }
}
