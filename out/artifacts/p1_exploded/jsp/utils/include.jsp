<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLDecoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    basePath= URLDecoder.decode(path,"utf-8");
%>
<script>
    var pageSize = 15;
</script>
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


<script>
    $.extend($.validator.messages, {
        required: "该内容不能为空",
        remote: "请修正该字段",
        email: "请输入正确格式的电子邮件",
        url: "请输入合法的网址",
        date: "请输入合法的日期",
        dateISO: "请输入合法的日期 (ISO).",
        number: "请输入合法的数字",
        digits: "只能输入整数",
        creditcard: "请输入合法的信用卡号",
        equalTo: "请再次输入相同的值",
        accept: "请输入拥有合法后缀名的字符串",
        maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
        minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
        rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
        range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
        max: $.validator.format("请输入一个最大为 {0} 的值"),
        min: $.validator.format("请输入一个最小为 {0} 的值")
    });
    $.extend($.validator.showErrors, function (errorMap, errorList) {
        $.each(errorList, function (i, v) {
            layer.tips(v.message, v.element, { time: 2000 });
            return false;
        });
    });
</script>