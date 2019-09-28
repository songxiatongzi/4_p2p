package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/22
 * @description 描述信息
 */
public interface RechargeRecordService {


    /**
     * 新郑充值记录
     *
     * @param rechargeRecord 充值对象
     * @return >0成功
     */
    int addRechargeRecord(RechargeRecord rechargeRecord);

    /**
     * 更新用户充值记录表的交易状态
     * @param rechargeRecord 用户交易列表
     * @return int
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);

    /**
     * 用户充值
     * @param rechargeMap
     * @return
     */
    int recharge(Map<String, Object> rechargeMap);
}
