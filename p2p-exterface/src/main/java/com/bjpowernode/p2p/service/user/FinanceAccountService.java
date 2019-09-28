package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/17
 * @description 描述信息
 */
public interface FinanceAccountService {


    /**
     *  通过用户的id值来查询用户的财务资金
     * @param id 用户的id
     * @return FinanceAccount 用户的财务资金
     */
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
