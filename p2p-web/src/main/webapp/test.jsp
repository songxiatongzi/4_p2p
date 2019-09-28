<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/13
  Time: 21:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <title>测试页面</title>
</head>
<body>
    <div>
        <h1>历史年华收益率为： ${historyAverageRate}</h1>
        <h1>平台注册总人数为： ${allUserCount}</h1>
        <h1>历史成交金额为： ${allBidMoney}</h1>
    </div>
</body>
</html>
