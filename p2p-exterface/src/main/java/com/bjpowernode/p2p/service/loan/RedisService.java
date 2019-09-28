package com.bjpowernode.p2p.service.loan;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/21
 * @description 描述信息
 */
public interface RedisService {

    /**
     * 将电话号码和生成的随机数放到redis数据库中，并设置了失效时间
     * @param phone 用户名
     * @param messageCode 生成的随机数
     * @param timeout 在redis中的失效时间
     */
    void put(String phone, String messageCode, int timeout);

    /**
     * 通过用户名查询redis中存储的随机数
     * @param phone 用户名
     * @return 存放到redis中的随机数
     */
    String get(String phone);

    /**
     * 从redis中获取唯一值
     * @return 返回redis中产生的唯一值
     */
    Long getOnlyNumber();

}
