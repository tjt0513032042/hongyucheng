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
    boolean operateflag = false;
    if (null != obj) {
        user = (User) obj;
        if(user.getRole().intValue() == 0 && user.getName().equals("sysadmin")){
            operateflag = true;
        }
    }
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

<!-- jquery ui -->
<link type="text/css" href="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.min.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
<link type="text/css" href="<%=basePath%>/js/jquery-ui-1.12.1/jquery-ui.theme.min.css" rel="stylesheet"/>

<script>
    <%--var userInfo = <%=user%>;--%>
    <%--if (null == userInfo) {--%>
        <%--window.location.href = getRoot() + '/login/toLogin.do';--%>
    <%--}--%>

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
    })
    $.extend($.validator.showErrors, function (errorMap, errorList) {
        $.each(errorList, function (i, v) {
            layer.tips(v.message, v.element, {time: 2000});
            return false;
        });
    });

    // 手机号码验证
    jQuery.validator.addMethod("isMobile", function(value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写手机号码");

    function canOperate(){
        var flag = <%=operateflag%>;
        return flag;
    }

    $.datepicker.regional['zh-CN'] = {
        closeText: '关闭',
        prevText: '<上月',
        nextText: '下月>',
        currentText: '今天',
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
            '七月', '八月', '九月', '十月', '十一月', '十二月'],
        monthNamesShort: ['一', '二', '三', '四', '五', '六',
            '七', '八', '九', '十', '十一', '十二'],
        dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
        dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
        weekHeader: '周',
        dateFormat: 'yy-mm-dd',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: '年'
    };
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);

    $.datepicker._gotoToday = function (id) {
        var target = $(id);
        var inst = this._getInst(target[0]);
        if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
            inst.selectedDay = inst.currentDay;
            inst.drawMonth = inst.selectedMonth = inst.currentMonth;
            inst.drawYear = inst.selectedYear = inst.currentYear;
        }
        else {
            var date = new Date();
            inst.selectedDay = date.getDate();
            inst.drawMonth = inst.selectedMonth = date.getMonth();
            inst.drawYear = inst.selectedYear = date.getFullYear();
            this._setDateDatepicker(target, date);
            this._selectDate(id, this._getDateDatepicker(target));
        }
        this._notifyChange(inst);
        this._adjustDate(target);
    }
</script>