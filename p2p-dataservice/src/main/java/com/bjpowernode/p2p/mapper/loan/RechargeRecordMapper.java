package com.bjpowernode.p2p.mapper.loan;


import com.bjpowernode.p2p.model.loan.RechargeRecord;

public interface RechargeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    int insert(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    int insertSelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    RechargeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    int updateByPrimaryKeySelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    int updateByPrimaryKey(RechargeRecord record);

    /**
     * 更新用户充值记录表的交易状态
     * @param rechargeRecord 用户交易列表
     * @return int
     */
    int updateRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);


}