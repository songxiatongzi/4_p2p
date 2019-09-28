package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/22
 * @description 描述信息
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {

    /**
     * rechargeRecordMapper 用户充值
     */
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    /**
     * financeAccount
     */
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 新郑充值记录
     *
     * @param rechargeRecord 充值对象
     * @return >0成功
     */
    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {

        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    /**
     * 更新用户充值记录表的交易状态
     *
     * @param rechargeRecord 用户交易列表
     * @return int
     */
    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    /**
     * 用户充值
     *
     * @param rechargeMap
     * @return
     */
    @Override
    public int recharge(Map<String, Object> rechargeMap) {

        //更新账户可用余额
        int updateAccount = financeAccountMapper.updateFinanceAccountByRecharge(rechargeMap);

        if (updateAccount > 0) {
            //更新充值记录状态
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo((String) rechargeMap.get("rechargeNo"));
            rechargeRecord.setRechargeStatus("1");
            int i = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
            if (i <= 0) {
                return 0;
            }
        } else {
            return 0;
        }

        return 1;
    }
}
