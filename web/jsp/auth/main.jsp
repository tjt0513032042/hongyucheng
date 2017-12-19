<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/8
  Time: 21:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/utils/include.jsp"%>
<html>
<script type="text/javascript" src="<%=basePath%>/js/login/main.js"></script>
<head>
    <title>主页${userInfo.name}</title>
</head>
<frameset rows="88,*" cols="*" frameborder="no" border="0" framespacing="0">
    <frame src="<%=basePath%>/jsp/utils/top.jsp" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame" />
    <frameset cols="187,*" frameborder="no" border="0" framespacing="0">
        <frame src="<%=basePath%>/jsp/utils/left.jsp" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" title="leftFrame" />
        <frame src="<%=basePath%>/user/list.do" name="rightFrame" id="rightFrame" title="rightFrame" />
    </frameset>
</frameset>
<noframes><body>
</body></noframes>
</html>