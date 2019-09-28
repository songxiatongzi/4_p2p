package com.bjpowernode.p2p.service.user;

import com.bjpowernode.constants.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/14
 * @description 描述信息
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    //用户资金账户表
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * @author Administrator
     * @description
     * @date 2019/8/14
     * @param
     * @method queryAllUserCount 查询平台注册总人数
     * @return java.lang.Long
     **/

    @Override
    public Long queryAllUserCount() {

        //opsForValue 模板对象
        Long allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);

        //判断平台用户是不为空
        if(allUserCount == null) {
            synchronized (this) {
                allUserCount = (Long) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);

                if(allUserCount == null){
                    //去数据库中查询
                    allUserCount = userMapper.selectAllUserCount();

                    //将查询结构存放到redis数据库中，在缓存数据库中存储10分钟
                    redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT, allUserCount, 10, TimeUnit.MINUTES);
                }

            }

        }

        return allUserCount;
    }

    /**
     *  queryUserByPhone 根据电话号码查询用户信息
     *  @return user
     *  @param phone 电话号码
     */
    @Override
    public User queryUserByPhone(String phone) {

        return userMapper.selectUserByPhone(phone);
    }

    /**
     *
     * @return
     */
    @Override
    public ResultObject register(String phone,String loginPassword) {

        ResultObject resultObject = new ResultObject();
        //给返回值一个初始值
        resultObject.setErrorCode(Constants.SUCCESS);

        User user = new User();
        //用户id为自增
        //登陆时间和最后登陆时间相同
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        //添加用户
        int insertUserCount = userMapper.insertSelective(user);

        if(insertUserCount > 0 ){
            //添加用户成功，给用户创建一个账户
            User userInfo = userMapper.selectUserByPhone(phone);
            //创建一个账户
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userInfo.getId());
            //注册领红包活动
            financeAccount.setAvailableMoney(888.0);
            //添加账户
            int acount = financeAccountMapper.insertSelective(financeAccount);
            if(acount < 0){
                //添加账户失败，返回fail
                resultObject.setErrorCode(Constants.FAIL);
            }
        }else{
            //添加用户失败
            resultObject.setErrorCode(Constants.FAIL);
        }



        return resultObject;
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 成功或者失败
     */
    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 执行用户登陆业务
     * @param phone 电话号码
     * @param loginPassword 登陆密码
     * @return
     */
    @Override
    public User login(String phone, String loginPassword) {

        //根据手机和密码查询用户信息
        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);

        //查询到结果
        if(user != null){
            //更新最近的登录时间
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);

        }
        //更新最近的登陆时间
        return user;
    }
}
