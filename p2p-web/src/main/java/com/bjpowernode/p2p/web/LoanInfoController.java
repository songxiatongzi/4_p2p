package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/15
 * @description 描述信息
 */
@Controller
public class LoanInfoController {

    /**
     * loanInfoService 投标产品信息业务
     */
    @Autowired
    private LoanInfoService loanInfoService;

    /**
     * bidInfoService 用户投标信息业务
     */
    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(value = "ptype", required = false) Integer pType,
                       @RequestParam(value = "currentPage", required = false) Integer currentPage
    ) { //required = false 允许有空值  = true 不允许有空值

        //判断当前页是不是为空
        if (currentPage == null) {
            currentPage = 1;
        }
        //准备参数
        Map<String, Object> paramMap = new HashMap<>();
        //判断类型是不是为空  证明已经点击了查询更多的按钮
        if (pType != null) {
            paramMap.put("pType", pType);
        }

        //计算页码
        int pageSize = 9;
        //根据当前页码计算掠过的记录数
        paramMap.put("currentPage", (currentPage - 1) * pageSize);
        //当前页
        paramMap.put("pageSize", pageSize);

        //分页查询产品信息列表
        //产品类型  页码  每页显示的条数 ，封转一个分页查询对象
        PaginationVo<LoanInfo> paginationVo = loanInfoService.queryLoanInfoListByPage(paramMap);

        //计算总页数(前端需要总页数和总条数)
        int totalPage = paginationVo.getTotal().intValue() / pageSize;
        int mod = paginationVo.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        //将前端需要的总页数，总条数，列表返回到前端,当前页数
        model.addAttribute("loanInfoList", paginationVo.getDataList());
        model.addAttribute("totalRows", paginationVo.getTotal());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);

        if (pType != null) {
            model.addAttribute("pType", pType);
        }

        //TODO
        //查询平台投资排行榜


        return "loan";

    }

    /**
     * 首页中点击我要投资，进入到我要投资界面
     *
     * @param request 请求参数
     * @param model   视图对象
     * @param id      产品类型
     * @return 返回一个视图页面
     */
    @RequestMapping("/loan/loanInfo")
    public String loanInfo(HttpServletRequest request, Model model,
                           @RequestParam(value = "id", required = true) Integer id) {
        //注意这里不同的产品的id是不同的
        //根据产品标示获取产品详情（投标产品信息表）
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);
        model.addAttribute("loanInfo", loanInfo);

        //查询出来， 并进行分页查询
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        //掠过的记录数
        paramMap.put("currentPage", 0);
        //每页展示的条数
        paramMap.put("pageSize", 10);
        //产品信息的id
        paramMap.put("loanId", id);

        //根据产品标示获取最近的10条记录
        //通过产品信息的id来查询该产品下用户投资列表，并按投资时间进行倒序排序
        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoListByLoanId(paramMap);
        model.addAttribute("bidInfoList", bidInfoList);
        //根据用户标示获取当前用户的额可用余额


        return "loanInfo";
    }
}
