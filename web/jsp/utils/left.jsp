<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/11/11
  Time: 20:19
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/utils/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>无标题文档</title>
    <script type="text/javascript">
        $(function(){
            //导航切换
            $(".menuson li").click(function(){
                $(".menuson li.active").removeClass("active")
                $(this).addClass("active");
            });

            $('.title').click(function(){
                var $ul = $(this).next('ul');
                $('dd').find('ul').slideUp();
                if($ul.is(':visible')){
                    $(this).next('ul').slideUp();
                }else{
                    $(this).next('ul').slideDown();
                }
            });
        })
    </script>


</head>

<body style="background:#f0f9fd;">
<div class="lefttop"><span></span>功能菜单</div>

<dl class="leftmenu">

    <dd>
        <div class="title">
            <span><img src="<%=basePath%>/images/leftico01.png" /></span>信息管理
        </div>
        <ul class="menuson">
            <li><cite></cite><a href="<%=basePath%>/user/list.do" target="rightFrame">人员管理</a><i></i></li>
            <li><cite></cite><a href="<%=basePath%>/shop/list.do" target="rightFrame">店家管理</a><i></i></li>
        </ul>
    </dd>
    <dd>
        <div class="title">
            <span><img src="<%=basePath%>/images/leftico02.png" /></span>抽查管理
        </div>
        <ul class="menuson">
            <li><cite></cite><a href="<%=basePath%>/checkPlan/list.do" target="rightFrame">抽查计划</a><i></i></li>
        </ul>
    </dd>
    <dd>
        <div class="title">
            <span><img src="<%=basePath%>/images/leftico02.png" /></span>报表查看
        </div>
        <ul class="menuson">
            <li><cite></cite><a href="<%=basePath%>/report/list.do" target="rightFrame">报表查看</a><i></i></li>
        </ul>
    </dd>
</dl>

</body>
</html>
