package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/21
 * @description 描述信息
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements RedisService {

    /**
     * redisTemplate redis模板对象
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 将电话号码和生成的随机数放到redis数据库中，并设置了失效时间
     *
     * @param phone       电话号码
     * @param messageCode 生成的随机数
     * @param timeout     失效时间
     */
    @Override
    public void put(String phone, String messageCode, int timeout) {

        //将传递的数据存放到redis数据库进行存储
        redisTemplate.opsForValue().set(phone, messageCode, timeout, TimeUnit.SECONDS);

    }

    /**
     * 通过用户名查询redis中存储的随机数
     *
     * @param phone 用户名
     * @return 存放到redis中的随机数
     * <p>
     * redisTemplate 是redis模板对象
     */
    @Override
    public String get(String phone) {

        return (String) redisTemplate.opsForValue().get(phone);

    }

    /**
     * 获取redis中产生的唯一值
     *
     * @return 获取redis中产生的唯一值
     */
    @Override
    public Long getOnlyNumber() {

        //提供的值从1开始向上递增
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER, 1);
    }
}
