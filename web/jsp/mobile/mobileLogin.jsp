<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.net.URLDecoder"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    basePath= URLDecoder.decode(path,"utf-8");
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>

<link rel="stylesheet" href="<%=basePath%>/js/jquery-mobile/jquery.mobile-1.4.5.min.css" />
<script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>

</head>

<script language="javascript">
    
    $(document).ready(function () {
    $('input.loginbtn').on('click', function () {
        if($('#userName').val() == '' || $('#password').val() == ''){
            layer.msg("用户名和密码不能为空!");
            return;
        }
        var url = getRoot() + "/login/auth.do";
        var data = $('form').serializeArray();
        sendAjax(url, data, function (userInfo) {
            if (userInfo) {
                var url = getRoot() + '/mobile/mobileMain.do';

                $('#mainForm').attr('action', url);
                $('#mainForm input[name=id]').val(userInfo.id);
                $('#mainForm').submit();
            } else {
                layer.msg("用户名或密码错误!");
            }
        });
    });

    $('#btn_reset').on('click', function () {
        $('form').find('input').val('');
    });
});

/**
 * Created by admin on 2017/11/8.
 */
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
<head>
    <title>登陆</title>
</head>
<body>
   <!--登陆页面-->
<div data-role="page" id="pageLogin">

    <div data-role="header">
        <h1 role="heading">欢迎登陆</h1>
    </div>

    <div data-role="main" class="ui-content">
        <form method="get" action="">

            <div class="ui-field-contain">
                <label for="name">姓名:</label>

                <input type="text" name="userName" id="userName">

                <br/>

                <label for="password">密码:</label>

                <input type="password" name="password" id="password">
                <div style="margin-top: 20%;">
                	<input type="button" class = "loginbtn" value="登录"/>
                	<input type="button" id="btn_reset" value="重置"/>
                </div>
            </div>

        </form>
    </div>

    <div data-role="footer" style="text-align: center"  data-position="fixed">
        <p>© 2017 WONDER CITY. All Rights Reserved.</p>
    </div>
    <form id="mainForm" action="" method="get">
        <input name="id" type="hidden">
    </form>
</div>
</body>
</html>
