package com.bjpowernode.p2p.web;

import com.bjpowernode.constants.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/13
 * @description 加载欢迎页面
 */
@Controller
public class IndexController {

    //投标产品信息业务
    //@Autowired
    @Resource //注意使用resource会产生错误
    private LoanInfoService loadInfoService;

    //用户表
    @Resource
    private UserService userService;

    //用户投标信息表
    @Resource
    private BidInfoService bidInfoService;

    /**
     * 设置欢迎页
     *
     * @return 返回视图页面，实际这样使用的比较多(String)
     */
    @RequestMapping("/index")
    public String index(Model model) {

        //获取历史年化收益率
        System.out.println("historyAverageRate");
        Double historyAverageRate = loadInfoService.queryHistoryAverageRate();
        System.out.println(historyAverageRate);
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);

        //获取平台用户
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);

        //获取累计成交金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);
        //获取新手宝(1个)
        //使用了mysql中的limit进行截取下标操作

        //准备参数
        Map<String, Object> paramMap = new HashMap<>();
        /*
         * 页码 这个页码是经过计算之后得出的结果，
         * 比如第二页展示的是 (2-1) * pageSize 这样第一页显示的是掠过）条记录
         */
        paramMap.put("currentPage", 0);
        //每页展示的条数
        paramMap.put("pageSize", 1);
        //前端发送请求应该带的参数 产品类型 ，显示第一页，每页显示几个
        paramMap.put("productType", Constants.PRODUCT_TYPE_XINSHOUBAO);
        //通过产品类型查询(投标产品信息表)
        List<LoanInfo> xLoanInfoList = loadInfoService.queryLoadInfoListByProductType(paramMap);
        System.out.println(xLoanInfoList);
        model.addAttribute("xLoanInfoList", xLoanInfoList);

        //获取优选产品(满月宝 季度宝)4个
        paramMap.put("pageSize", 4);
        paramMap.put("productType", Constants.PRODUCT_TYPE_YOUXUAN);
        List<LoanInfo> yLoanInfoList = loadInfoService.queryLoadInfoListByProductType(paramMap);
        model.addAttribute("yLoanInfoList", yLoanInfoList);

        //获取散标(8个)
        paramMap.put("pageSize", 8);
        paramMap.put("productType", Constants.PRODUCT_TYPE_SANBIAO);
        List<LoanInfo> sLoanInfoList = loadInfoService.queryLoadInfoListByProductType(paramMap);
        model.addAttribute("sLoanInfoList", sLoanInfoList);
        //将查询所有的参数返回到主页面中显示

        return "index";


    }
}
