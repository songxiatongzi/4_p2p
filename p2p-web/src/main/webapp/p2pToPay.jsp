<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/23
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <title>跳转到自己的支付接口页面</title>
</head>
<body>
<%--当充值订单业务创建的时候，调用自己的支付接口，字符接口会再去调用支付宝接口--%>
<form  method="post" action="http://localhost:9090/pay/api/alipay">
    <input type="hidden" name="out_trade_no" value="${out_trade_no}">
    <input type="hidden" name="total_amount" value="${total_amount}">
    <input type="hidden" name="subject" value="${rechargeDesc}">

    <input type="submit" value="立即支付" style="display:none" >
</form>
<script>document.forms[0].submit();</script>
</body>
</html>
