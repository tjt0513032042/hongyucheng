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

</head>
<body>

<div data-role="page" id="pageone">
    <div data-role="header">
        <h1 id="title"></h1>
    </div>
    <form id="backForm" action="" method="post">
        <input name="id" type="hidden" value="${user.id}"/>
        <input type="submit" data-inline="true" value="返回" class="back"/>
    </form>
    <div data-role="main" class="ui-content">
        <table data-role="table" data-mode="columntoggle" class="ui-responsive ui-shadow"
               data-column-btn-text="隐藏列" id="myTable" style="border:1px">
            <thead>
            <tr>
                <th style="width:38px" data-priority="1">标识</th>
                <th data-priority="1">检查项目</th>
                <th style="width:38px" data-priority="1">结果</th>
            </tr>
            </thead>
            <tbody id="mainDiv">


            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('input.back').on('click', function () {
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

    $(document).on("pageinit", function (event) {
        var params = {
            shopId: '${user.shopId}',
            checkType: '${checkType}'
        };
        searchList(params);
    });

    function initContent() {
        // 清理界面内容
    }

    function getRecordType(value) {
        if ('0' == value) {
            return "开店表";
        } else {
            return "闭店表";
        }
    }

    /* 将检查结果转换成checkbox */
    function transformOptionResult(detail) {
        var desc = '';
        var recordId = detail.recordId,
            optionCode = detail.optionCode,
            optionResult = detail.optionResult;
        if (detail.optionResult == 0 || detail.optionResult == '0') {
            desc = '<input type="checkbox" name="optionResult" id="' + optionCode + '" checked  onchange="saveOptionRes(' + recordId + ',this)"/>';
        } else {
            desc = '<input type="checkbox" name="optionResult" id="' + optionCode + '" onchange="saveOptionRes(' + recordId + ',this)"/>';
        }
        return desc;
    }
    function getDetailsHtml(info) {
        var title = info.shopInfo.shopName + '-' + getRecordType(info.recordType);
        $('#title').append(title);
        var html = [];
        if (info.details && info.details != null && info.details.length > 0) {
            $.each(info.details, function (i, detail) {
                html.push('<tr>');
                html.push('<td>');
                html.push(detail.optionCode);
                html.push('</td>');
                html.push('<td>');
                html.push(detail.optionName);
                html.push('</td>');
                html.push('<td>');
                html.push(transformOptionResult(detail));
                html.push('</td>');
                html.push('</tr>');
            });
        }
        return html.join('');
    }

    function searchList(param) {
        var url = getRoot() + '/mobile/getCheckRecords.do';
        sendAjax(url, param, function (callbackdata) {
            if (callbackdata) {
                // 清理界面内容
                initContent();
                // 加载列表数据
                var datas = callbackdata;
                var htmlContent = [];
                if (datas.shopInfo == null) {
                    layer.msg('用户尚未指定店家,无法添加开店、闭店表!');
                    return;
                }
                htmlContent = getDetailsHtml(datas);
            } else {
                htmlContent.push('<tr class="contentDatas"><td colspan="2">暂无数据</td></tr>');
            }
            $('#mainDiv').append(htmlContent);
            $("#mainDiv").trigger('create');
            $('.tablelist tbody tr:odd').addClass('odd');
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

