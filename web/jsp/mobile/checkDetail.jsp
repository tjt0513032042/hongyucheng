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

    User user = (User) request.getSession().getAttribute("user");
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script src="https://apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/js/jquery-mobile/jquery.mobile-1.4.5.min.css">
    <script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/mobile/utils.js"></script>
    <style>
        th {
            border-bottom: 1px solid #d6d6d6;
        }

        tr:nth-child(even) {
            background: #e9e9e9;
        }

        /**
        隐藏列选项按钮
         */
        .ui-table-columntoggle-btn {
            visibility: hidden;
            position: absolute;
        }
    </style>
</head>
<body>

<div data-role="page" id="pageone">
    <div data-role="header">
        <h1 id="title">${checkRecords.shopInfo.shopName}</h1>
    </div>
    <input type="hidden" name="recordId" value="${checkRecords.recordId }">
    <input type="hidden" name="checkType" value="${checkRecords.recordType }">
    <input type="hidden" name="userId" value="<%=user.getId()%>">
    <input type="hidden" name="shopId" value="<%=user.getShopId()%>">
    <form id="backForm" action="" method="post">
        <input name="id" type="hidden" value="${user.id}"/>
    </form>
    <ul style="display: inline;">
        <li>
            <label>类型&nbsp;&nbsp;:<c:if test="${checkRecords.recordType == 0}">开店表</c:if><c:if test="${checkRecords.recordType == 1}">闭店表</c:if></label>
        </li>
        <li>
            <label>当前时间: ${checkRecords.checkDateStr}</label>
        </li>
    </ul>
    <table data-role="table" data-mode="columntoggle" class="ui-responsive" data-column-btn-text="列选择" id="myTable">
        <thead>
        <tr>
            <th style="width:38px" data-priority="1">标识</th>
            <th data-priority="1">检查项目</th>
            <th style="width:38px" data-priority="1">结果</th>
        </tr>
        </thead>
        <tbody id="mainDiv">
        <c:forEach items="${checkRecords.details}" var="details" varStatus="status">
            <tr>
                <td code="${details.optionCode}">${status.index + 1}</td>
                <td>${details.optionName}</td>
                <td>
                    <input type="checkbox" name="optionResult" id="${details.optionCode}"
                            <c:if test="${details.optionResult == 0}">
                                checked
                            </c:if>
                    />
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
    <div>
        <input type="button" class="submit" value="提交"
                <c:if test="${!submitFlag}">
                    disabled="disabled"
                </c:if>
        />
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('input.submit').on('click', function () {
            var uncheckFlag = false;
            var trs = $('#mainDiv tr');
            var details = [];
            $.each(trs, function (i, tr) {
                var code = $(tr).find('td:first').attr('code');
                var checkbox = $(tr).find('td:last input');
                var optionResult = $(checkbox).is(':checked') ? 0 : 1;
                if (optionResult == 1) {
                    uncheckFlag = true;
                }
                var detail = {optionCode: code, optionResult: optionResult};
                details.push(detail);
            });
            var params = {
                recordId: $('input[name=recordId]').val(),
                shopId: $('input[name=shopId]').val(),
                userId: $('input[name=userId]').val(),
                recordType: $('input[name=checkType]').val(),
                details: details
            };
            var msg = '确认提交?';
            if (uncheckFlag) {
                msg = '有部分选项检查不通过,是否确认提交?';
            }
            layer.confirm(msg, function () {
                var url = getRoot() + '/mobile/saveRecordDetails.do';
                sendAjax(url, JSON.stringify(params), function (callback) {
                    if (callback.flag) {
                        $('input[name=recordId]').val(callback.data.recordId);
                        if (callback.msg && callback.msg != '') {
                            layer.msg(callback.msg);
                        } else {
                            layer.msg('保存成功!');
                        }
                    } else {
                        if (callback.msg) {
                            layer.msg(callback.msg);
                        }
                    }
                }, false, 'application/json; charset=utf-8');
            });
        });
    });
</script>
</body>
</html>

