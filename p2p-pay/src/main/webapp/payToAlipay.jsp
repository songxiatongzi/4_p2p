<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/23
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <title>跳转到支付宝支付的页面</title>
</head>
<body>
    <%--自定义支付接口调用支付宝中间需要跳转的页面
        跳转到这个页面时，页面会自动再次发送第二次请求
    --%>
    ${result}
</body>
</html>
