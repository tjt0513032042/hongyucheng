<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/7
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ include file="/jsp/utils/include.jsp" %>
<head>
    <title>userlist</title>
</head>
<script type="text/javascript" src="<%=basePath%>/js/checkPlan/checkPlan.js"></script>
<body>
<div class="rightinfo">

    <div class="tools">
        <form id="searchForm">
            <ul class="toolbar">
                <li style="border: 0px !important;">
                    <label>商家名称</label>&nbsp;<input type="text" name="shopName" class="dfinput">
                    <label>抽查时间</label>&nbsp;<input type="text" name="start" class="dfinput" style="width: 211px !important;" readonly="readonly">~<input type="text" name="end" class="dfinput" style="width: 211px !important;" readonly="readonly">
                </li>
                <li id="searchButton"><span style="margin-top: 2px !important;"><img src="<%=basePath%>/images/ico06.png"/></span>查询</li>
                <li id="addButton"><span><img src="<%=basePath%>/images/t01.png"/></span>添加</li>
            </ul>
        </form>
        <br><br><br>
    </div>
    <div id="content">
        <table id="userInfoList" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">
            <thead>
                <th>抽查日期</th>
                <th>被抽查商家</th>
                <th>操作</th>
            </thead>
        </table>
        <div class="paginationBox"></div>
    </div>
</div>

<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
</script>
</body>
</html>
