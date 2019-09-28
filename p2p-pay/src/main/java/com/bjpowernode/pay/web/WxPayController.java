package com.bjpowernode.pay.web;

import com.bjpowernode.http.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.awt.SunHints;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/26
 * @description 微信支付控制器
 */
@Controller
public class WxPayController {
    //统一响应格式
    //将不同的接口的信息转换为相同的格式

    /**
     * @param request      请求参数
     * @param body         商品描述
     * @param out_trade_no 商户订单号(建议根据当前系统时间加随机序列来生成订单号)
     * @param total_fee    标价金额
     * @return 返回的jso格式字符串  result_code和return_code 返回值都为SUCCESS 说明是成功的
     * {
     *     "nonce_str": "xSur4BMEJhuA82sB",
     *     "code_url": "weixin://wxpay/bizpayurl?pr=EKmzQmV",
     *     "appid": "wx8a3fcf509313fd74",
     *     "sign": "52E5EF19E8DD06EC5B1407FA020424F8",
     *     "trade_type": "NATIVE",
     *     "return_msg": "OK",
     *     "result_code": "SUCCESS",  成功
     *     "mch_id": "1361137902",
     *     "return_code": "SUCCESS",  成功
     *     "prepay_id": "wx26204451613318011bcf6d2a1720178000"
     * }
     */
    @RequestMapping("/api/WxPay")
    @ResponseBody
    public Object WxPay(HttpServletRequest request,
                        @RequestParam(value = "body", required = true) String body,
                        @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                        @RequestParam(value = "total_fee", required = true) String total_fee) throws Exception {

        //准备map集合的请求参数
        Map<String, String> requestDataMap = new HashMap<String, String>();
        //公众账号id
        requestDataMap.put("appid", "wx8a3fcf509313fd74");
        //商户号
        requestDataMap.put("mch_id", "1361137902");
        //随机字符串
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述【这里的商品描述不能写死】
        requestDataMap.put("body", body);
        //商户订单号
        requestDataMap.put("out_trade_no", out_trade_no);
        //标价金额 单位为分（multiply 乘法）
        BigDecimal bigDecimal = new BigDecimal(total_fee);
        //bigDecimal 为什么一定要乘以 bigDecimal类型
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        int intValue = multiply.intValue();
        requestDataMap.put("total_fee", String.valueOf(intValue));
        //终端ip
        requestDataMap.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        requestDataMap.put("notify_url", "http://localhost:9090/pay/api/wxpayNotity");
        //交易类型
        requestDataMap.put("trade_type", "NATIVE");
        //商品id(唯一值，将订单号作为唯一值，传过去)
        requestDataMap.put("product_id", out_trade_no);
        //签名【根据参数计算出来签名值】
        String signature = WXPayUtil.generateSignature(requestDataMap, "367151c5fd0d50f1e34a68a802d6bbca");
        requestDataMap.put("sign", signature);

        //将map集合转换成为xml格式请求参数
        String requestDateXml = WXPayUtil.mapToXml(requestDataMap);
        //调用统一下单api ，传递xml格式的请求参数
        String responseDataXml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder", requestDateXml);
        //将xml格式相应参数转换为map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);

        return responseDataMap;
    }

}
