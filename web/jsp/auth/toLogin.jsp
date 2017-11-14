<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<%@ include file="/jsp/utils/include.jsp"%>
<html>
<link href="<%=basePath%>/css/login.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=basePath%>/js/login/login.js"></script>
<script language="javascript">
    $(function(){
        $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
        $(window).resize(function(){
            $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
        })
    });
</script>
<head>
    <title>login</title>
</head>
<body style="background-color:#1c77ac; background-image:url(<%=basePath%>/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">



<div id="mainBody">
    <div id="cloud1" class="cloud"></div>
    <div id="cloud2" class="cloud"></div>
</div>
<div class="loginbody">

    <span class="systemlogo"></span>

    <div class="loginbox">
        <form>
            <ul>
                <li><input id="userName" name="userName" type="text" class="loginuser" onclick="JavaScript:this.value=''"/></li>
                <li><input id="password" name="password" type="password" class="loginpwd" value="密码" onclick="JavaScript:this.value=''"/></li>
                <li><input name="" type="button" class="loginbtn" value="登录" /></li>
            </ul>
        </form>
    </div>
    <form id="mainForm" action="" method="post">
        <input name="id" type="hidden">
    </form>
</div>
</body>
</html>
