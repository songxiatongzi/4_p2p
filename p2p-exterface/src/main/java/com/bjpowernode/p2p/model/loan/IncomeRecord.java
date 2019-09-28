package com.bjpowernode.p2p.model.loan;

import java.io.Serializable;
import java.util.Date;

public class IncomeRecord implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.uid
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Integer uid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.loan_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Integer loanId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.bid_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Integer bidId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.bid_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Double bidMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.income_date
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Date incomeDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.income_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Double incomeMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_income_record.income_status
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    private Integer incomeStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.id
     *
     * @return the value of b_income_record.id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.id
     *
     * @param id the value for b_income_record.id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.uid
     *
     * @return the value of b_income_record.uid
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.uid
     *
     * @param uid the value for b_income_record.uid
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.loan_id
     *
     * @return the value of b_income_record.loan_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Integer getLoanId() {
        return loanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.loan_id
     *
     * @param loanId the value for b_income_record.loan_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.bid_id
     *
     * @return the value of b_income_record.bid_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Integer getBidId() {
        return bidId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.bid_id
     *
     * @param bidId the value for b_income_record.bid_id
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.bid_money
     *
     * @return the value of b_income_record.bid_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Double getBidMoney() {
        return bidMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.bid_money
     *
     * @param bidMoney the value for b_income_record.bid_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.income_date
     *
     * @return the value of b_income_record.income_date
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Date getIncomeDate() {
        return incomeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.income_date
     *
     * @param incomeDate the value for b_income_record.income_date
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.income_money
     *
     * @return the value of b_income_record.income_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Double getIncomeMoney() {
        return incomeMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.income_money
     *
     * @param incomeMoney the value for b_income_record.income_money
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setIncomeMoney(Double incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_income_record.income_status
     *
     * @return the value of b_income_record.income_status
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public Integer getIncomeStatus() {
        return incomeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_income_record.income_status
     *
     * @param incomeStatus the value for b_income_record.income_status
     *
     * @mbggenerated Mon Aug 12 20:38:47 CST 2019
     */
    public void setIncomeStatus(Integer incomeStatus) {
        this.incomeStatus = incomeStatus;
    }
}