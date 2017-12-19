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
<script type="text/javascript" src="<%=basePath%>/js/mobile/utils.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/user/user.js"></script>
</head>

<script language="javascript">
    $(document).ready(function () {
        $('#resetPwd').on('click', function () {
	       resetPwd();
   		});
    
    });
	function resetPwd(){
        var password_old = $('#password_old').val();
        var pwdNew1 = $('#password_new').val();
        var pwdNew2 = $('#password_new2').val();

        if(password_old == ''){
            layer.msg('请输入旧密码!');
            return;
        }
        if(pwdNew1 == ''){
            layer.msg('请输入新密码!');
            return;
        }
        if(pwdNew2 == ''){
            layer.msg('请输入新密码二次确认!');
            return;
        }

        if(pwdNew1 != pwdNew2){
            layer.msg("两次输入的新密码不一致，请重新输入！");
            return;
        }
        var params ={
            oldPassword: password_old,
            newPassword : pwdNew1
        };
        var url = getRoot() + '/mobile/saveUserInfo.do';
        sendAjax(url, params, function (callback) {
            if (callback.flag) {
                layer.alert('密码修改成功!', function(){
                    var url = getRoot() + '/mobile/login.do';
                    window.location.href = url;
                });
            } else {
                layer.msg(callback.msg);
            }
        })
	}
</script>
<head>
    <title>重置密码</title>
</head>
<body>
   <!--注册页面-->
<div data-role="page" id="pageLogin">

    <div data-role="header">
        <h1 role="heading">重置密码</h1>
    </div>

    <div data-role="main" class="ui-content">
        <form method="get" action="">

            <div class="ui-field-contain">
                <label for="password_old"><span style="display: inline;color: red;">*</span>旧密码:</label>
                <input type="password" name="password_old" id="password_old" maxlength="11">
                <label for="password_new"><span style="display: inline;color: red;">*</span>新密码:</label>
                <input type="password" name="password_new" id="password_new" maxlength="11">
                <label for="password_new2"><span style="display: inline;color: red;">*</span>新密码确认:</label>
                <input type="password" name="password_new2" id="password_new2" maxlength="11">
                <div style="margin-top: 20%;">
                	<input type="button" class = "signIn" value="确定" id="resetPwd"/>
                </div>
            </div>

        </form>
    </div>

    <div data-role="footer" style="text-align: center"  data-position="fixed">
        <p>虹悦城开闭店检查系统</p>
    </div>
    <form id="mainForm" action="" method="get">
    </form>
</div>
</body>
</html>
