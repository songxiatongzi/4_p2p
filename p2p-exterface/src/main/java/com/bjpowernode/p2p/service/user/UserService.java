package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/14
 * @description 描述信息
 */
public interface UserService {

    /**
     * queryAllUserCount 查询平台总人数
     * @return
     */
    Long queryAllUserCount();

    /**
     * @param phone 电话号码
     * @method queryUserByPhone 通过电话号码查询用户信息
     * @return User
     */
    User queryUserByPhone(String phone);

    /**
     * register 添加用户和创建用户账户的业务
     * @return 添加用户的结果和创建账户的结果
     */
    ResultObject register(String phone,String loginPassword);

    /**
     * 更新用户的姓名和身份证号码
     * @param user 用户信息
     * @return 1 更新成功  0失败
     */
    int modifyUserById(User user);

    /**
     * 登陆操作
     * @param phone 电话号码
     * @param loginPassword 登陆密码
     * @return
     */
    User login(String phone, String loginPassword);
}
