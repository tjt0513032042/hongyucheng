<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/utils/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>无标题文档</title>
</head>
<script>
    $(document).ready(function () {
        var userInfo = $(parent.window).data('userInfo');
        $('span.userNameSpan').html($('#hiddenUserId', parent.document).val());
        $('#logout').on('click', function () {
            parent.window.location.href = getRoot() + '/login/toLogin.do';
        });
    });
</script>
<body style="background:url(<%=basePath%>/images/topbg.gif) repeat-x;">
<div class="topleft">
    <a href="#"><img src="<%=basePath%>/images/logo.png" title="系统首页"/></a>
</div>


<div class="topright">
    <ul>
        <li><span><img src="<%=basePath%>/images/help.png" title="帮助" class="helpimg"/></span><a href="#">帮助</a></li>
        <li><a id="logout" href="#">退出</a></li>
    </ul>

    <div class="user">
        <span class="userNameSpan">${currentUserName}</span>
        <i>消息</i>
        <b>5</b>
    </div>

</div>

</body>
</html>
