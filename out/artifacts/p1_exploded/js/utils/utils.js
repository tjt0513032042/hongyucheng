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
function sendAjax(url, params, callbackfunction, async) {
    if (!callbackfunction || !jQuery.isFunction(callbackfunction)) {
        callbackfunction = function (callback) {

        }
    }
    if (async == undefined) {
        async = true
    }
    $.ajax({
        url: url,
        data: params,
        async: async,
        dataType: 'json',
        type: 'POST',
        success: callbackfunction
    });
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