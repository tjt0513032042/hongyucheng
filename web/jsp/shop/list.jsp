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
<script type="text/javascript" src="<%=basePath%>/js/shop/shop.js"></script>
<body>
<div class="rightinfo">

    <div class="tools">
        <form id="searchForm">
            <ul class="toolbar">
                <li style="border: 0px !important; background: url();">
                    <label>商家名称</label>&nbsp;<input type="text" name="shopName" class="dfinput">
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
                <th>商家名称</th>
                <th>商家类型</th>
                <th>第一责任人</th>
                <th>第二责任人</th>
                <th>闭店表箱号</th>
                <th>备用钥匙</th>
                <th>备用钥匙箱号</th>
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
