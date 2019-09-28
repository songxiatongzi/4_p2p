package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/17
 * @description 描述信息
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     *
     * @param id 用户的id
     * @return FinanceAccount 用户的可用资金
     */
    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer id) {

        return financeAccountMapper.selectFinanceAccountByUid(id);

    }


}
