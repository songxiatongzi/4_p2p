package com.bjpowernode.p2p.test;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.constants.Constants;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.service.loan.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/24
 * @description 银行信息控制器
 */
@Controller
public class BankCardController {

    @Autowired
    private RedisService redisService;
    /**
     *
     * @param request 请求作用域
     * @param phone 电话号码
     * @return
     * @throws Exception
     */
    @RequestMapping("/test/messageCodeText")
    @ResponseBody
    public Object messageCode(HttpServletRequest request,
                              @RequestParam(value = "phone", required = true) String phone) throws Exception {

        Map<String, Object> retMap = new HashMap<String, Object>(16);
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        //获取短信验证码

        //获取京东接口(106三网短信接口)
        String uri = "https://way.jd.com/chuangxin/dxjk";

        //调用京东万象106接口参数
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        paramMap.put("appkey", "a8ec919f4a22eb0a1987dbe3c0ddbccc");
        paramMap.put("mobile", phone);

        //生成一个随机数
        String messageCode = getRandomNum(4);

        //准备短信的内容
        String content = "【创信】你的验证码是："+ messageCode +"，3分钟内有效！";
        paramMap.put("content", content);

        //在做这个方法的时候，会返回json和xml文件形式的返回值
        String jsonString = HttpClientUtils.doPost(uri,paramMap);
//        String jsonString = "{\n" +
//                "    \"code\": \"10000\",\n" +
//                "    \"charge\": false,\n" +
//                "    \"remain\": 1305,\n" +
//                "    \"msg\": \"查询成功\",\n" +
//                "    \"result\": {\n" +
//                "        \"ReturnStatus\": \"Success\",\n" +
//                "        \"Message\": \"ok\",\n" +
//                "        \"RemainPoint\": 420842,\n" +
//                "        \"TaskID\": 18424321,\n" +
//                "        \"SuccessCounts\": 1\n" +
//                "    }\n" +
//                "}";
        //将json格式字符串转化为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取通信标示code
        String code = jsonObject.getString("code");
        //对json 对象进行解析
        if (StringUtils.equals("10000", code)) {
            //通信成功
            String returnStatus =jsonObject.getJSONObject("result").getString("ReturnStatus");
            //按断短信是不是发送成功
            if (StringUtils.equals("Success", returnStatus)) {

                //将之前生成的验证码放到redis数据库中
                //电话号码 随机数 倒计时的秒数
                redisService.put(phone, messageCode, 60);

                retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

                //将产生的随机数放进集合中返回去
                /*用于测试*/
                retMap.put("messageCode", messageCode);
            } else {
                //短信验证失败
                retMap.put(Constants.ERROR_MESSAGE, "短信发送失败");

            }


        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE, "通信失败");

        }


        return retMap;
    }

    /**
     * 根据数据的参数的数量来生成相应位数的随机数
     *
     * @param count 索要创建的随机数
     * @return 随机数字符串
     */
    private String getRandomNum(int count) {

        String[] str = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        //根据随机生成的下标值，到字符数组中去取相对应的值
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            //round 将double 类型转换为整数类型
            int index = (int) Math.round(Math.random() * 9);
            //拼接字符串
            stringBuilder.append(str[index]);
        }

        //将StringBuilder 转换为字符串类型
        return stringBuilder.toString();
    }

    @RequestMapping("/test/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value = "phone",required = true) String phone,
                        @RequestParam(value = "realName",required = true) String realName,
                        @RequestParam(value = "messageCode",required = true) String messageCode,
                        @RequestParam(value = "idCard",required = true) String idCard,
                        @RequestParam(value = "bankCardNo",required = true) String bankCardNo) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>(16);
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        //获取短信验证码

        //获取京东接口(106三网短信接口)
        String uri = "https://way.jd.com/YOUYU365/keyelement";

        //调用京东万象106接口参数
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        paramMap.put("appkey", "a8ec919f4a22eb0a1987dbe3c0ddbccc");
        paramMap.put("cardPhone", phone);
        paramMap.put("accName", realName);
        paramMap.put("certificateNo", idCard);
        paramMap.put("cardNo", bankCardNo);

        String jsonString = HttpClientUtils.doPost(uri,paramMap);
//        String jsonString = "{\n" +
//                "    \"code\": \"10000\",\n" +
//                "    \"charge\": false,\n" +
//                "    \"remain\": 1305,\n" +
//                "    \"msg\": \"查询成功\",\n" +
//                "    \"result\": {\n" +
//                "        \"serialNo\": \"5590601f953b512ff9695bc58ad49269\",\n" +
//                "        \"respCode\": \"000000\",\n" +
//                "        \"respMsg\": \"验证通过\",\n" +
//                "        \"comfrom\": \"jd_query\",\n" +
//                "        \"success\": \"true\"\n" +
//                "    }\n" +
//                "}\n";
        //将json格式字符串转化为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        String code = jsonObject.getString("code");

        if (StringUtils.equals("10000", code)) {


            String success = jsonObject.getJSONObject("result").getString("success");


            //按断短信是不是发送成功
            if (StringUtils.equals("true", success)) {

                retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

            } else {
                //短信验证失败
                retMap.put(Constants.ERROR_MESSAGE, "银行卡验证成功");

            }


        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE, "银行卡验证失败");
        }

        return retMap;
    }

}
