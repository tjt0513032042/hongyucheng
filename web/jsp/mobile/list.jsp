<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="com.hongyuecheng.user.entity.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    basePath = URLDecoder.decode(path, "utf-8");

    User user = null;
    Object obj = request.getSession().getAttribute("user");
    if (null != obj) {
        user = (User) obj;
    }
    Object message = request.getAttribute("msg");
    String msg = "";
    if (null != message) {
        msg = (String) message;
    }
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>/js/jquery-mobile/jquery.mobile-1.4.5.min.css">
<script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/mobile/utils.js"></script>
<script>
    var msg = '<%=msg%>';
    if(msg && msg != ''){
        layer.msg(msg);
    }
</script>
</head>
<body>
<div><img src="<%=basePath%>/images/mobile/logo.jpg" width="100%"></div>
    <ul data-role="listview" data-inset="true">
      <c:if test="${user.role == 1}">
      <li><a href="<%=basePath%>/mobile/detail.do?checkType=0" target="_top"><img src="<%=basePath%>/images/mobile/open.png"  class="ui-li-icon">开店检查</a></li>
      <li><a href="<%=basePath%>/mobile/detail.do?checkType=1" target="_top"><img src="<%=basePath%>/images/mobile/cancel.png" class="ui-li-icon">闭店检查</a></li>
      </c:if>
      <c:if test="${user.role != 1}">
      <li><a href="<%=basePath%>/mobile/detail.do?checkType=-1" target="_top"><img src="<%=basePath%>/images/mobile/admin.jpg" class="ui-li-icon">管理抽查</a></li>
      </c:if>
    </ul>
    
    <c:if test="${user.role == 1}">
	  <p style="margin-left:20px;margin-bottom :20px">
    	<font color="red">温馨提示：<br>
    	请按照规范检查填写结果，填写时间范围：<br>
    	开店填写时间：9：00-11：00<br>
    	闭店填写时间：22：00-23：00<br>
    	</font>
 	 </p>
    </c:if>
</body>
</html>

