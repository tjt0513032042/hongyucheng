<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLDecoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    basePath = URLDecoder.decode(path, "utf-8");
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script src="https://apps.bdimg.com/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/js/jquery-mobile/jquery.mobile-1.4.5.min.css">
    <script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<style>
th {
    border-bottom: 1px solid #d6d6d6;
}

tr:nth-child(even) {
    background: #e9e9e9;
}
</style>
</head>
<body>

<div data-role="page" id="pageone">
    <div data-role="header">
        <h1 id="title">${checkRecords.shopInfo.shopName}-<c:if test="${checkRecords.recordType == 0}">开店表</c:if>
        	<c:if test="${checkRecords.recordType == 1}">闭店表</c:if>
        </h1>
        <a href="#" class="ui-btn ui-icon-back ui-btn-icon-left back">返回</a>
    </div>
    <form id="backForm" action="" method="post">
        <input name="id" type="hidden" value="${user.id}"/>
    </form>
        <table  data-role="table" data-mode="columntoggle" class="ui-responsive" data-column-btn-text="列选择" id="myTable">
            <thead>
            <tr>
                <th style="width:38px" data-priority="1">标识</th>
                <th data-priority="1">检查项目</th>
                <th style="width:38px" data-priority="1">结果</th>
            </tr>
            </thead>
            <tbody id="mainDiv">
				<c:forEach items="${checkRecords.details}" var="details">
					<tr>
						<td>${details.optionCode}</td>
						<td>${details.optionName}</td>
						<td>
							<input type="checkbox" name="optionResult" id="${details.optionCode}" 
							<c:if test="${details.optionResult == 0}">
								checked 
							</c:if>
							 onchange="saveOptionRes('${checkRecords.recordId}',this)"/>
						</td>
					</tr>
				</c:forEach>

            </tbody>
        </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('.back').on('click', function () {
            var url = getRoot() + '/mobile/mobileMain.do';
            $('#backForm').attr('action', url);
            $('#backForm').submit();
        });
    });

    function saveOptionRes(recordId, tar) {
        var optCode = $(tar).attr("id");
        var optRes = 1;
        if ($(tar).is(':checked')) {
            optRes = 0;
        }
        var params = {
            shopId: '${user.shopId}',
            checkType: '${checkType}',
            recordId: recordId,
            optionCode: optCode,
            optionResult: optRes
        };
        var url = getRoot() + '/mobile/saveCheckRecordDetail.do';
        sendAjax(url, params, function (callbackdata) {
            if (callbackdata) {
                layer.msg(callbackdata);
            } else {
                layer.msg("网络错误，保存数据失败！");
            }
        });
    }

    function getRoot() {
        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
        var curWwwPath = window.document.location.href;
        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht = curWwwPath.substring(0, pos);
        //获取带"/"的项目名，如：/uimcardprj
        var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        return (localhostPaht + projectName);
    }

    /**
     * 发送ajax请求
     * @param url
     * @param params
     * @param callbackfunction
     * @param async
     */
    function sendAjax(url, params, callbackfunction, async, contentType) {
        if (!callbackfunction || !jQuery.isFunction(callbackfunction)) {
            callbackfunction = function (callback) {
            }
        }
        if (async == undefined) {
            async = true
        }
        var opts = {
            url: url,
            data: params,
            async: async,
            dataType: 'json',
            type: 'POST',
            success: callbackfunction
        };
        if (contentType) {
            opts.contentType = contentType;
        }
        $.ajax(opts);
    }
</script>
</body>
</html>

