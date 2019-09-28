package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;

import java.util.List;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/14
 * @description 描述信息
 */
public interface BidInfoService {

    /**
     * @author Administrator
     * @date 2019/8/14
     * @method queryAllBidMoney 获取累计成交金额
     * @return java.lang.Double
     **/
    Double queryAllBidMoney();

    /**
     * 通过用户信息查询投标记录数
     * @param paramMap 用户id 掠过的记录数  每页展示的记录数
     * @return queryRecentlyBidInfoList 用户投标信息列表
     */
    List<BidInfo> queryRecentlyBidInfoList(Map<String, Object> paramMap);

    /**
     * 通过投标产品的id来查询所对应的用户投资信息表
     * @param paramMap 数组 包含掠过的记录数 每页展示的记录数 投标产品的id
     * @return 投标产品列表
     */
    List<BidInfo> queryRecentlyBidInfoListByLoanId(Map<String, Object> paramMap);
}
