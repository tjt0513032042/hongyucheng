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

/**
 * 获取用户信息
 * @param userId
 * @returns {*}
 */
function getUserInfo(userId) {
    var url = getRoot() + '/user/getUserInfo.do';
    var params = {'id': userId};
    var userInfo = null;
    sendAjax(url, params, function (callback) {
        if (callback) {
            userInfo = callback;
        }
    }, false);
    return userInfo;
}

/**
 * 获取所有商家信息
 */
function getShops() {
    var url = getRoot() + '/shop/getAllshops.do';
    var params = {};
    var shops = null;
    sendAjax(url, params, function (callback) {
        if (callback) {
            shops = callback;
        }
    }, false);
    return shops;
}

function getDevices() {
    var url = getRoot() + '/device/getAllDevices.do';
    var params = {};
    var infos = null;
    sendAjax(url, params, function (callback) {
        if (callback) {
            infos = callback;
        }
    }, false);
    return infos;
}

function nullToString(val) {
    if (val == null) {
        return '';
    }
    return val;
}

function initDatePicker(dateinput, opt) {
    if (dateinput) {
        if (!opt) {
            opt = {};
        }
        opt = $.extend(opt, {
            numberOfMonths: 1,//显示几个月
            showButtonPanel: true,//是否显示按钮面板
            dateFormat: 'yy-mm-dd',//日期格式
            clearText: "清除",//清除日期的按钮名称
            closeText: "关闭",//关闭选择框的按钮名称
            showMonthAfterYear: true,//是否把月放在年的后面
            yearSuffix: '年', //年的后缀
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
            dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
            dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
            beforeShow : function( input ) {
                setTimeout(function() {
                    var buttonPane = $(input)
                        .datepicker( "widget" )
                        .find( ".ui-datepicker-buttonpane" );
                    $( "<button>", {
                        text: "清除",
                        click: function() {
                            $.datepicker._clearDate(input);
                        }
                    }).addClass("ui-state-default ui-priority-primary ui-corner-all").appendTo( buttonPane );
                }, 1 );
            }
        });
        $(dateinput).datepicker(opt);
    }
}

function transFormShopType(type) {
    type = type + '';
    var desc = '';
    if (type == '1') {
        desc = '零售';
    } else if (type == '2') {
        desc = '服务';
    } else if (type == '3') {
        desc = '娱乐';
    } else if (type == '4') {
        desc = '其他';
    } else if (type == '0') {
        desc = '餐饮';
    }
    return desc;
}