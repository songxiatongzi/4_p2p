package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.User;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    User selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_user
     *
     * @mbggenerated Mon Aug 12 20:38:46 CST 2019
     */
    int updateByPrimaryKey(User record);

    /**
     * @author Administrator
     * @description
     * @date 2019/8/14
     * @param
     * @method selectAllUserCount  查询平台注册总人数
     * @return java.lang.Long  返回为long类型
     */
    Long selectAllUserCount();

    /**
     *
     * @param phone 电话号码
     * @return User
     */
    User selectUserByPhone(String phone);

    /**
     * 根据电话号码和密码查询用户
     * @param phone 电话号码
     * @param loginPassword 密码
     * @return user
     */
    User selectUserByPhoneAndLoginPassword(String phone, String loginPassword);
}