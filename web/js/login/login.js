$(document).ready(function () {


    $('input.loginbtn').on('click', function () {
        if($('#userName').val() == '' || $('#password').val() == ''){
            layer.msg("用户名和密码不能为空!");
            return;
        }
        var url = getRoot() + "/login/auth.do";
        var data = paramString2obj($('form'));
        data.type = 'pc';
        sendAjax(url, data, function (callback) {
            if(callback.flag){
                var userInfo = callback.data;
                var url = getRoot() + '/login/main.do';
                $('#mainForm').attr('action', url);
                $('#mainForm').submit();
            }else{
                layer.msg(callback.msg);
            }
        });
    });

    $('#btn_reset').on('click', function () {
        $('form').find('input').val('');
    });
});


