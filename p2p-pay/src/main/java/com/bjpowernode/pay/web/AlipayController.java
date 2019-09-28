package com.bjpowernode.pay.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.bjpowernode.pay.config.AlipayConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/23
 * @description 支付宝支付控制层 用来调用支付宝的接口
 */
@Controller
public class AlipayController {


    /**
     * @param request      请求体
     * @param model        数据对象
     * @param out_trade_no 商户订单号
     * @param total_amount 订单金额
     * @param subject      订单名称
     * @return
     * @throws AlipayApiException 阿里支付抛出异常
     */
    @RequestMapping("/api/alipay")
    public String alipay(HttpServletRequest request, Model model,
                         @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                         @RequestParam(value = "total_amount", required = true) Double total_amount,
                         @RequestParam(value = "subject", required = true) String subject) throws AlipayApiException {

        //用户点击付款之后展示的页面
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // 页面跳转同步通知页面路径 需http://格式的完整路径
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        // 服务器异步通知页面路径  需http://格式的完整路径
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //将返回的值打印成为一个页面
        //将这个页面转换到支付宝支付的页面
        model.addAttribute("result", result);

        return "payToAlipay";

    }


    /**
     * 调用支付宝订单查询接口，返回订单详情，格式为Json
     * 如果前端定义的是json 那从支付宝接口返回的是xml文件也需要转换成为json格式
     *
     * @param request      请求参数
     * @param out_trade_no 用户订单号
     * @return 测试返回值类型
     *          {"alipay_trade_query_response":
     *          {"code":"10000",
     *          "msg":"Success",
     *          "buyer_logon_id":"ear***@sandbox.com",
     *          "buyer_pay_amount":"0.00",
     *          "buyer_user_id":"2088102179288950",
     *          "buyer_user_type":"PRIVATE",
     *          "invoice_amount":"0.00",
     *          "out_trade_no":"201908232050141939",
     *          "point_amount":"0.00",
     *          "receipt_amount":"0.00",
     *          "send_pay_date":"2019-08-23 20:53:16",
     *          "total_amount":"100.00",
     *          "trade_no":"2019082322001488951000087642",
     *          "trade_status":"TRADE_SUCCESS"},
     *          "sign":"uOH+lp8yAsaD3EEp/7fR7T7VQYM1BT+d18i8trjJ1bd2xvrNaYtoW3J6F+GsmW6jmEvsBVfWpo1EkPaCDcP6Vqd9Isbwy+Ce5i/LSmBHgEgZFEs5p+6tCe3n5O3kSGxIqKqtgRd6EKIFiVneS7zMChf4161bgINe9ZQg/qBDI4+l+WIaRX39rilFXgT09nLpZv8q/cxJ9Q2EBXPbg+73RCqTwDgtZecED6Q+wVOe63H5Oz83uP3SwtaS/TLSQ0/jTsIN47CWw89EQZ3EJ5x4BeZlDWv59LDTw21p0gzWzPxAlVxXMzzLbUk3QEXF9zz6LB7nhkekIDqI3pLMR0mcTA=="}
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("/api/alipayQuery")
    @ResponseBody
    public Object alipayQuery(HttpServletRequest request,
                              @RequestParam(value = "out_trade_no", required = true) String out_trade_no) throws UnsupportedEncodingException, AlipayApiException {

        /*调用支付宝接口*/

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"}");

        //请求 商户调用支付宝接口，同步返回查询结果
        String result = alipayClient.execute(alipayRequest).getBody();

        //将结果返回给调用者
        return result;
    }
}
