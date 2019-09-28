package com.bjpowernode.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.constants.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/16
 * @description 描述信息
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //用户财务资金账户表
    @Autowired
    private FinanceAccountService financeAccountService;

    //投标信息表
    @Autowired
    private LoanInfoService loanInfoService;

    //用户投标信息表
    @Autowired
    private BidInfoService bidInfoService;

    //redis服务
    @Autowired
    private RedisService redisService;

    /**
     * @param request 请求参数
     * @param phone   电话号码
     * @method checkPhone 对电话进行验证
     */
    @RequestMapping("/loan/checkPhone")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam(value = "phone", required = true) String phone) {

        //根据电话查询用户信息
        //将查询的结果放到map集合中
        Map<String, Object> retMap = new HashMap<>();

        User user = userService.queryUserByPhone(phone);

        if (user != null) {
            //那么用户已经存在，电话号码就不能注册，将错误信息返回到前端
            retMap.put(Constants.ERROR_MESSAGE, "该手机号已存在，请更换手机号码");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;
    }

    /**
     * @param request 请求参数
     * @param captcha 验证码
     * @return retMap 将验证的结果返回到前端
     * @method checkCaptcha 对输入的验证码进行验证
     */
    @RequestMapping("loan/checkCaptcha")
    @ResponseBody
    public Object checkCaptcha(HttpServletRequest request,
                               @RequestParam(value = "captcha", required = true) String captcha) {

        Map<String, Object> retMap = new HashMap<>();

        //从session中获取验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);

        //将用户的验证码和session中进行对比
        if (!StringUtils.equalsIgnoreCase(captcha, sessionCaptcha)) {
            //验证码输入的与session域中存的值不相同，将错误信息返回给前端
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的验证码");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;

    }

    /**
     * 根据前端的手机号码和密码添加用户
     *
     * @param request       请求参数
     * @param phone         电话号码
     * @param loginPassword 密码
     * @return retMap 添加用户和账户的返回信息
     */
    @PostMapping("/loan/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "phone", required = true) String phone,
                           @RequestParam(value = "loginPassword", required = true) String loginPassword) {

        Map<String, Object> retMap = new HashMap<>();

        //用户注册，新增用户，新增账户,将结果封装到结果对象中 String phone,String loginPassword
        ResultObject resultObject = userService.register(phone, loginPassword);

        if (StringUtils.equalsIgnoreCase(resultObject.getErrorCode(), Constants.SUCCESS)) {
            //当返回的值success
            retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

            User user = userService.queryUserByPhone(phone);

            //当参数返回的同时，将用户信息存放到session作用域中
            request.getSession().setAttribute(Constants.SESSION_USER, user);

        } else {
            //返回值为是fail
            retMap.put(Constants.ERROR_MESSAGE, "注册失败");
            return retMap;
        }

        return retMap;
    }

    /**
     * 查询用户的可用金额
     *
     * @param request 请求参数
     * @return financeAccount 用户的可用金额
     */
    @GetMapping("/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request) {

        //从session 中获取对象信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //根据用户信息获取用户可用金额
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        return financeAccount;
    }

    /**
     * 进行实名认证
     *
     * @param realName 真实姓名
     * @param idCard   身份证号码
     * @param request  请求作用域
     * @return retMap
     * @throws Exception
     * @method verifyRealName
     */
    @PostMapping("/loan/verifyRealName")
    @ResponseBody
    public Object verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName", required = true) String realName,
                                 @RequestParam(value = "idCard", required = true) String idCard) throws Exception {

        Map<String, Object> retMap = new HashMap<>();

        //创建参数集合
        Map<String, Object> paramsMap = new HashMap<>();
        String appkey = "qqw";
        paramsMap.put("appkey", appkey);
        paramsMap.put("cardNo", idCard);
        paramsMap.put("realName", realName);


        //调用接口
        //String jsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test", paramsMap);
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"error_code\": 0,\n" +
                "        \"reason\": \"成功\",\n" +
                "        \"result\": {\n" +
                "            \"realname\": \"乐天磊\",\n" +
                "            \"idcard\": \"350721197702134399\",\n" +
                "            \"isok\": true\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        //对json格式字符串进行解析
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取json的通信表示
        String code = jsonObject.getString("code");
        if (StringUtils.equals(code, "10000")) {
            //返回错误参照码为10000成功
            //从json对象中获取对应的key的值
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");

            //对返回值进行判断
            if (isok) {
                //? 从session 中获取值，会出现更新两次的情况
                User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

                User user = new User();
                user.setId(sessionUser.getId());
                user.setName(realName);
                user.setIdCard(idCard);

                //验证成功
                int modifyUserCount = userService.modifyUserById(user);
                if (modifyUserCount > 0) {
                    //更新成功
                    retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

                    //当更新成功之后，更新session 中的值
                    sessionUser.setName(realName);
                    sessionUser.setIdCard(idCard);
                    request.getSession().setAttribute(Constants.SESSION_USER, sessionUser);

                } else {
                    //更新失败
                    retMap.put(Constants.ERROR_MESSAGE, "更新用户失败");
                    return retMap;
                }


            } else {
                retMap.put(Constants.ERROR_MESSAGE, "真实姓名和身份证号码不一致");
                return retMap;
            }

        } else {
            retMap.put(Constants.ERROR_MESSAGE, "通信异常");
            return retMap;
        }
        return retMap;
    }

    /**
     * 退出登陆，重定向到主页面
     *
     * @param request 请求作用域
     * @return index 进入到主页面，用来重新过一次数据库
     */
    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request) {

        //清空
        //request.getSession().invalidate();
        //清空session
        request.getSession().removeAttribute(Constants.SESSION_USER);

        //重定向到首页面，重定向后在重新过一次后台，将数据重新铺设到主页面上
        return "redirect:/index";
    }

    /**
     * 点击登陆按钮之后，将历史年化收益 平台总人数 全部的成交金额 展现出来
     *
     * @param request
     * @return
     */
    @RequestMapping("/loan/loadStart")
    @ResponseBody
    public Object loadStart(HttpServletRequest request) {

        Map<String, Object> retMap = new HashMap<String, Object>(16);

        //添加历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        //添加平台总人数
        Long allUserCount = userService.queryAllUserCount();
        //查询全部的成交金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        //将查询好的数据返回给前端

        //将查询好的信息添加到集合中
        retMap.put(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);
        retMap.put(Constants.ALL_USER_COUNT, allUserCount);
        retMap.put(Constants.ALL_BID_MONEY, allBidMoney);

        return retMap;
    }

    /**
     * 执行登录操作
     *
     * @param request       请求作用域
     * @param phone         电话号码
     * @param loginPassword 登陆密码
     * @return
     */
    @RequestMapping("/loan/login01")
    @ResponseBody
    public Object login01(HttpServletRequest request,
                          @RequestParam(value = "phone", required = true) String phone,
                          @RequestParam(value = "loginPassword", required = true) String loginPassword) {
        Map<String, Object> retMap = new HashMap<String, Object>(16);

        //执行用户登陆 查询用户 更新最近登录时间
        User user = userService.login(phone, loginPassword);

        //更新成功之后，将user 存放到session域中
        if (user == null) {
            retMap.put(Constants.ERROR_MESSAGE, "手机或密码不正确");
            return false;
        }

        //将用户更新到session中
        request.getSession().setAttribute(Constants.SESSION_USER, user);

        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;
    }

    /**
     * 使用短信验证码进行验证
     *
     * @param request       请求作用域
     * @param phone         用户名
     * @param loginPassword 密码
     * @param messageCode   短信验证码
     * @return retMap集合 成功返回ok 失败返回提示信息
     */
    @RequestMapping("/loan/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value = "phone", required = true) String phone,
                        @RequestParam(value = "loginPassword", required = true) String loginPassword,
                        @RequestParam(value = "messageCode", required = true) String messageCode) {
        Map<String, Object> retMap = new HashMap<String, Object>(16);

        //从redis缓存中获取指定手机号的messageCode
        String redisMessageCode = redisService.get(phone);

        //判断redis数据库中的随机数和输入的随机数是不是相同，如果相同，在进行行用户名和密码的判断
        if (StringUtils.equals(redisMessageCode, messageCode)) {
            User user = userService.login(phone, loginPassword);

            //判断用户是不是为空
            if (user == null) {
                retMap.put(Constants.ERROR_MESSAGE, "用户名或者密码不正确");
                return false;
            }

            //将用户存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER, user);

            retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        } else {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的验证码");
            return retMap;
        }

        return retMap;
    }

    /**
     * 首页点击我的小金库 ，查看个人投资信息
     *
     * @param request 用来从请求作用域中获取用户信息
     * @param model   返回数据
     * @return 返回视图列表 投资记录表 充值表 最近收益表 可用余额表
     */
    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model) {

        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //根据用户标示获取用户的账户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
        model.addAttribute("financeAccount", financeAccount);
        //将一下信息看做是一个分页查询
        //分成5页每页有5条记录数据
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        paramMap.put("uid", sessionUser.getId());
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 5);

        //根据用户标示获取投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoList(paramMap);
        model.addAttribute("bidInfoList", bidInfoList);

        //TODO
        //根据用户标示获取最近的充值记录

        //TODO
        //根据用户标示获取最近的收益记录

        return "myCenter";
    }

    /**
     * 短信发送验证码，并将结果返回
     *
     * @param request 请求
     * @param phone   电话号码
     * @return
     * @throws Exception doPost抛出异常
     */
    @RequestMapping("/loan/messageCode")
    @ResponseBody
    public Object messageCode(HttpServletRequest request,
                              @RequestParam(value = "phone", required = true) String phone) throws Exception {

        Map<String, Object> retMap = new HashMap<String, Object>(16);
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        //获取短信验证码

        //获取京东接口
        String uri = "https://way.jd.com/kaixintong/kaixintong";

        //调用京东万象106接口参数
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        paramMap.put("appkey", "a8ec919f4a22eb0a1987dbe3c0ddbccc");
        paramMap.put("mobile", phone);

        //生成一个随机数
        String messageCode = getRandomNum(4);

        //准备短信的内容
        String content = "【凯信通】您的验证码是：" + messageCode;
        paramMap.put("content", content);

        //在做这个方法的时候，会返回json和xml文件形式的返回值
        //String jsonString = HttpClientUtils.doPost(uri,paramMap);
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 0,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1111611</remainpoint>\\n <taskID>101609164</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                "}";
        //将json格式字符串转化为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取通信标示code
        String code = jsonObject.getString("code");
        //对json 对象进行解析
        if (StringUtils.equals("10000", code)) {
            //通信成功
            String resultXml = jsonObject.getString("result");
            //将xml文件转换为doc 对象
            Document document = DocumentHelper.parseText(resultXml);

            Node returnstatusNode = document.selectSingleNode("//returnstatus");

            String returnstatusNodeText = returnstatusNode.getText();

            //按断短信是不是发送成功
            if (StringUtils.equals("Success", returnstatusNodeText)) {

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
                return retMap;
            }


        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE, "通信失败");
            return retMap;
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


}
