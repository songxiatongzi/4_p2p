package com.bjpowernode.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.bjpowernode.constants.Constants;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.config.AlipayConfig;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.util.DateUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/22
 * @description 描述信息
 */
@Controller
public class RechargeRecordController {

    /**
     * redisService redis业务层 用来生成唯一数字
     */
    @Autowired
    private RedisService redisService;

    /**
     * rechargeRecordService 用户充值业务记录
     */
    @Autowired
    private RechargeRecordService rechargeRecordService;


    /**
     * 根据用户选择不同的充值方式进行充值
     * 前端传递到控制层，控制层根据发送来action进行判断是运用了那一种方式进行充值的
     *
     * @param request       请求作用域
     * @param rechargeMoney 用户充值的金额
     * @return
     */
    @RequestMapping("/loan/toAlipayRecharge")
    public String toAlipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        //生成全局唯一充值订单号 = 时间 + redis全局唯一数字
        //将日期转化为字符串，唯一值就是数值的相加
        String rechargeNo = DateUtils.getTimestamp() + redisService.getOnlyNumber();

        //从session 中获取user对象
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //生成充值业务
        String rechargeDesc = "支付宝网页充值";
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeDesc(rechargeDesc);
        int addRechargeRecord = rechargeRecordService.addRechargeRecord(rechargeRecord);

        //对充值的返回值进行判断
        if (addRechargeRecord > 0) {
            //充值成功
            model.addAttribute("out_trade_no", rechargeNo);
            model.addAttribute("total_amount", rechargeMoney);
            model.addAttribute("rechargeDesc", rechargeDesc);

        } else {

            //充值失败 返回到指定的页面，并将充值信息进行返回
            model.addAttribute("trade_msg", "充值失败");
            return "toRechargeBack";

        }
        //去调用自己的支付接口
        return "p2pToPay";
    }

    /**
     * 支付宝服务器同步通知页面
     * back为同步返回
     *
     * @param request      请求参数
     * @param out_trade_no 交易订单号
     * @return 返回视图页面
     */
    @RequestMapping("/loan/alipayback")
    public String alipay(HttpServletRequest request,
                         Model model, @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                         @RequestParam(value = "total_amount", required = true) String total_amount) throws Exception {

        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        //响应的同步返回参数
        Map<String, String[]> requestParams = request.getParameterMap();

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {

            Map paramMap = new HashMap<String, String>();
            paramMap.put("out_trade_no", out_trade_no);
            //调用pay工程的订单查询接口，获取订单的业务处理结果
            String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/alipayQuery", paramMap);

            //将json字符串转换为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);

            //解析json对象
            JSONObject alipayTradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");

            //判断json的返回值
            String code = alipayTradeQueryResponse.getString("code");
            if (StringUtils.equals(code, "10000")) {
                //通信成功
                //获取业务处理结果
                String tradeStatus = alipayTradeQueryResponse.getString("trade_status");

                //对业务的处理状态进行分析
                //TRADE_BUYER_PAY 交易创建
                //TRADE_FINISHED  交易结束
                //给用户充值
                //TRADE_CLOSED    未付款交易超时关闭，更新当前记录的状态为2，充值失败
                if (StringUtils.equals(tradeStatus, "TRADE_CLOSED")) {
                    //交易关闭 Recharge充值
                    RechargeRecord rechargeRecord = new RechargeRecord();
                    rechargeRecord.setRechargeNo(out_trade_no);
                    rechargeRecord.setRechargeStatus("2");
                    int modifyRechargeRecordCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(rechargeRecord);

                    model.addAttribute(Constants.TRADE_MSG, "交易失败");
                    return "toRechargeBack";
                }
                //TRADE_SUCCESS   交易成功，给用户充值1.更新账户可用余额，2.充值记录状态更新为1(用户标示，充值金额，充值订单号)
                if (StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) {
                    //交易成功
                    User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);

                    Map<String, Object> rechargeMap = new HashMap<String, Object>();

                    rechargeMap.put("uid", user.getId());
                    rechargeMap.put("rechargeMoney", total_amount);
                    rechargeMap.put("rechargeNo", out_trade_no);

                    //给用户充值
                    int rechargeCount = rechargeRecordService.recharge(rechargeMap);

                    if (rechargeCount <= 0) {
                        //给用户充值失败
                        model.addAttribute("trade_msg", "验证签名失败");
                        return "toRechargeBack";

                    }

                    return "redirect:/loan/myCenter";

                }
            } else {
                //通信失败
                model.addAttribute(Constants.TRADE_MSG, "通信异常");
                return "toRechargeBack";
            }

        } else {
            model.addAttribute(Constants.TRADE_MSG, "验证签名失败");

            return "toRechargeBack";
        }
        // 重定向到指定页面
        return "redirect:/loan/myCenter";
    }

    @RequestMapping("/loan/toWxPayRecharge")
    public String toWxPayRecharge(HttpServletRequest request, Model model,
                                  @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //生成充值记录
        //生成全局唯一充值订单号 = 时间 + redis全局唯一数字
        //将日期转化为字符串，唯一值就是数值的相加
        String rechargeNo = DateUtils.getTimestamp() + redisService.getOnlyNumber();

        //从session 中获取user对象
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //生成充值业务
        String rechargeDesc = "微信网页充值";
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeTime(new Date());
        // "0" 充值中
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeDesc(rechargeDesc);
        int addRechargeRecord = rechargeRecordService.addRechargeRecord(rechargeRecord);

        if (addRechargeRecord > 0) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            //重值成功
            model.addAttribute("rechargeNo", rechargeNo);
            model.addAttribute("rechargeMoney", rechargeMoney);
            model.addAttribute("rechargeTime", date);
        } else {
            //充值失败
            model.addAttribute(Constants.TRADE_MSG, "充值失败");
            //返回到错误的展示页面
            return "toRechargeBack";
        }

        //去二维码的展示页面，将二维码进行展示
        return "toRechargeWxPayBack";
    }

    /**
     * 调用微信接口生成二维码
     *
     * @param request       请求参数
     * @param rechargeNo    商户订单号
     * @param rechargeMoney 标价金额
     * @throws Exception    doPost抛出异常
     */
    @RequestMapping("/loan/generateQrCode")
    public void generateQrCode(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "rechargeNo", required = true) String rechargeNo,
                               @RequestParam(value = "rechargeMoney", required = true) String rechargeMoney) throws Exception {
        //准备请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("body", "微信充值");
        paramMap.put("out_trade_no", rechargeNo);
        paramMap.put("total_fee", rechargeMoney);

        //只要获取统一下单api接口就可以生成二维码
        //而获取只能从pay接口中获取，pay工程只能从微信接口中获取
        String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/WxPay", paramMap);

        //将json格式字符串转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取通信标示
        String returnCode = jsonObject.getString("return_code");

        if(StringUtils.equals(Constants.SUCCESS_UPPER, returnCode)){
            //返回值成功
            String resultCode = jsonObject.getString("result_code");
            //获取业务处理结果
            if(StringUtils.equals(Constants.SUCCESS_UPPER, resultCode)){
                //获取url
                String url = jsonObject.getString("code_url");
                //生成二维码图片
                //矩阵编码的格式
                Map<EncodeHintType, Object> encodeHintTypeObjectMap = new HashMap<>();
                encodeHintTypeObjectMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                //生成矩阵对象
                BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 100, 100, encodeHintTypeObjectMap);

                OutputStream outputStream = response.getOutputStream();
                //通过流对象响应到前端
                MatrixToImageWriter.writeToStream(bitMatrix, "jpg", outputStream);

                outputStream.flush();
                outputStream.close();
            }else{
                response.sendRedirect(request.getContextPath()+"/toRechargeBack.jsp");

            }

        }else{
            //失败
            response.sendRedirect(request.getContextPath()+"/toRechargeBack.jsp");
        }



    }

}
