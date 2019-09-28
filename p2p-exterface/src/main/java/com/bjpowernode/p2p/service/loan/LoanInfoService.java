package com.bjpowernode.p2p.service.loan;


import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/13
 * @description 描述信息
 */
public interface LoanInfoService {

    /**
     * @author Administrator
     * @description
     * @date 2019/8/13
     * @method queryHistoryAverageRate 获取历史年华收益率
     * @return java.lang.Double
     **/
    Double queryHistoryAverageRate();

    /**
     * @author Administrator
     * @description  通过产品类型查询投标产品信息列表
     * @date 2019/8/15
     * @param  paramMap 参数中包含 （页码  产品类型）
     * @method queryLoadInfoListByProductType
     * @return com.bjpowernode.p2p.model.loan.LoanInfo
     **/
    List<LoanInfo> queryLoadInfoListByProductType(Map<String, Object> paramMap);

    /**
     * @author Administrator
     * @description 分页查询信息列表
     * @date 2019/8/16
     * @param paramMap 产品类型 每页展示的记录数  页码
     * @method queryLoanInfoListByPage
     * @return com.bjpowernode.p2p.model.vo.PaginationVo<com.bjpowernode.p2p.model.loan.LoanInfo>
     **/
    PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);

    /**
     * 通过投标产品的id来查询投标信息列表
     * @param id 投标产品的信息表的id
     * @return 投标信息列表
     */
    LoanInfo queryLoanInfoById(Integer id);
}

