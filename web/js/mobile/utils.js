/**
 * Created by admin on 2017/12/9.
 */

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

function paramString2obj (form) {
    var serializedParams = $(form).serialize();
    var obj={};
    function evalThem (str) {
        var strAry = new Array();
        strAry = str.split("=");
        //使用decodeURIComponent解析uri 组件编码
        for(var i = 0; i < strAry.length; i++){
            strAry[i] = decodeURIComponent(strAry[i]);
        }
        var attributeName = strAry[0];
        var attributeValue = strAry[1].trim();
        //如果值中包含"="符号，需要合并值
        if(strAry.length > 2){
            for(var i = 2;i<strAry.length;i++){
                attributeValue += "="+strAry[i].trim();
            }
        }
        if(!attributeValue){
            return ;
        }
        var attriNames = attributeName.split("."),
            curObj = obj;
        for(var i = 0; i < (attriNames.length - 1); i++){
            curObj[attriNames[i]]?"":(curObj[attriNames[i]] = {});
            curObj = curObj[attriNames[i]];
        }
        //使用赋值方式obj[attributeName] = attributeValue.trim();替换
        //eval("obj."+attributeName+"=\""+attributeValue.trim()+"\";");
        //解决值attributeValue中包含单引号、双引号时无法处理的问题
        curObj[attriNames[i]] = attributeValue.trim();
    };
    var properties = serializedParams.split("&");
    for (var i = 0; i < properties.length; i++) {
        //处理每一个键值对
        evalThem(properties[i]);
    };
    return obj;
}