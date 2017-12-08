<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    basePath = URLDecoder.decode(path, "utf-8");
%>
<link href="<%=basePath%>/css/style.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>/css/datatable.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>/css/mystyle.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="<%=basePath%>/js/utils/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/utils/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/utils/utils.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/utils/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/utils/jquery.pagination.js"></script>
<link type="text/css" href="<%=basePath%>/js/utils/pagination.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>

<!-- jquery ui -->
<link type="text/css" href="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.min.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
<link type="text/css" href="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.theme.min.css" rel="stylesheet"/>


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
    <form id="mainForm" action="" method="get">
        <input name="id" type="hidden">
    </form>
</div>
</body>
</html>
