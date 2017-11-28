<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ include file="/jsp/utils/include.jsp" %>
<head>
    <title>Report</title>
</head>
<script type="text/javascript" src="<%=basePath%>/js/summary/daylist.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/report/reportUtils.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/shop/shopUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/checkPlan/checkPlanUtil.js"></script>
<body style="height: 100%;">
<div class="rightinfo" style="height: calc(100% - 16px);">

    <div class="tools">
        <form id="searchForm">
            <ul class="toolbar">
                <li style="border: 0px !important;">
                    <label>日期</label>&nbsp;<input type="text" name="date" class="dfinput" style="width: 211px !important;" readonly="readonly">
                </li>
                <li id="searchButton"><span style="margin-top: 2px !important;"><img src="<%=basePath%>/images/ico06.png"/></span>查询</li>
            </ul>
        </form>
        <br><br><br>
    </div>
    <div id="content" style="height: calc(100% - 43px); overflow-y: auto;">
        <table id="userInfoList" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">
            <thead>
                <th>商家名称</th>
                <th>开店状态</th>
                <th>开店时间</th>
                <th>闭店状态</th>
                <th>闭店时间</th>
                <th>抽查状态</th>
                <th>操作</th>
            </thead>
            <tbody></tbody>
        </table>
        <div class="paginationBox"></div>
    </div>
</div>

<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>