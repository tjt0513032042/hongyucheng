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
                // $.cookie('userId', userInfo.id);
                // $.cookie('userName', userInfo.name);
                // $(window).data('userInfo', userInfo);
                var url = getRoot() + '/login/main.do';
                // window.location.href = url;

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


