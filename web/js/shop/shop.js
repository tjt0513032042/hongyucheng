/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    $('#searchButton').on('click', function () {
        searchList($('#searchForm input[name=shopName]').val(), 0, pageSize);
    });
    $('#addButton').on('click', function () {
        toSave();
    });

    $('#searchButton').trigger('click');
});

function searchList(name, pageNo, pageSize) {
    var url = getRoot() + '/shop/queryPageList.do';
    var params = {
        shopName: name,
        pageNo: pageNo,
        pageSize: pageSize
    };
    sendAjax(url, params, function (callbackdata) {
        if (callbackdata) {
            // 清理界面内容
            initContent();
            // 加载列表数据
            var datas = callbackdata.result;
            var htmlContent = [];
            if (datas && datas.length > 0) {
                $.each(datas, function (i, data) {
                    if (i > 0 && i % 2 == 1) {
                        htmlContent.push('<tr class="contentDatas odd" infoId="' + data.shopId + '">');
                    } else {
                        htmlContent.push('<tr class="contentDatas" infoId="' + data.shopId + '">');
                    }
                    htmlContent.push('<td>');
                    htmlContent.push(data.shopName);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    if (data.shopType == 0) {
                        htmlContent.push('餐饮');
                    } else if (data.shopType == 1) {
                        htmlContent.push('零售');
                    } else if (data.shopType == 2) {
                        htmlContent.push('服务');
                    } else if (data.shopType == 3) {
                        htmlContent.push('娱乐');
                    } else {
                        htmlContent.push('未知');
                    }
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push(data.firstPersonName);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push(data.secondPersonName);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push(data.closeShopBox);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    if (data.hasSpareKey) {
                        htmlContent.push("是");
                    } else {
                        htmlContent.push("否");
                    }
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push(data.spareKeyBox);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push('<a href="#" class="tablelink modify">修改</a>&nbsp;&nbsp;&nbsp;<a href="#" class="tablelink delete">删除</a>');
                    htmlContent.push('</td>');
                    htmlContent.push('</tr>')
                });
            } else {
                htmlContent.push('<tr class="contentDatas"><td colspan="5">暂无数据</td></tr>');
            }
            $('#userInfoList').append(htmlContent.join(''));
            $('.tablelist tbody tr:odd').addClass('odd');
            // 初始化分页组件
            $('.paginationBox').pagination(callbackdata.total, { //点击分页时，调用的回调函数
                callback: function (pageIndex) {
                    searchList($('#searchForm input[name=shopName]').val(), pageIndex, pageSize);
                },
                prev_text: '« 上一页', //显示上一页按钮的文本
                next_text: '下一页 »', //显示下一页按钮的文本
                items_per_page: pageSize, //每页显示的项数
                num_display_entries: 4, //分页插件中间显示的按钮数目
                current_page: callbackdata.pageNo, //当前页索引
                num_edge_entries: 2 //分页插件左右两边显示的按钮数目
            });

            registerFunc();
        } else {
            layer.msg('查询错误，请联系管理员!');
        }
    });
}

function initContent() {
    // 清理界面内容
    $('#userInfoList tr.contentDatas').remove();
    $('#paginationBox').empty();
}

/**
 * 为列表中的按钮注册方法
 */
function registerFunc() {
    $('table a.modify').on('click', function () {
        var infoId = $(this).closest('tr').attr('infoId');
        var info = getInfo(infoId);
        if (!info || null == info) {
            alert('商家信息已不存在，无法执行该操作!');
            return;
        }
        toSave(info);
    });
    $('table a.delete').on('click', function () {
        var id = $(this).closest('tr').attr('infoId');
        var deleteIndex = layer.msg('确定删除该商家信息?', {
            time: 0 //不自动关闭
            , btn: ['确定', '取消']
            , yes: function (index) {
                var url = getRoot() + '/shop/delete.do';
                var params = {shopId: id};
                sendAjax(url, params, function (callback) {
                    if (callback) {
                        layer.msg('删除成功!');
                        $('#searchButton').trigger('click');
                    } else {
                        layer.close(deleteIndex);
                        layer.msg('删除失败!');
                    }
                })
            }
        });
    });
}

/**
 * 打开信息管理界面
 * @param info
 */
function toSave(info) {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['600px', '700px'],
        btn: ['确定', '取消'],
        content: getInfoHtml(info),
        success: function (dom) {
            // 设置jquery验证器
            var rules = {
                shopName: {
                    required: true
                }
            };
            $(dom).find('form').validate({
                rules: rules,
                onfocusout: false
            });
        },
        btn1: function (index, dom) {
            if ($(dom).find('form').valid()) {
                var params = $(dom).find('form').serialize();
                var url = getRoot() + '/shop/saveShopInfo.do';
                sendAjax(url, params, function (callback) {
                    if (callback) {
                        if (callback.flag) {
                            layer.close(layerIndex);
                            layer.msg('操作成功!');
                            $('#searchButton').trigger('click');
                        } else {
                            layer.msg(callback.msg);
                        }
                    }
                })
            }
        }
    });
}

/**
 * 获取用户信息html
 * @param userInfo
 */
function getInfoHtml(info) {
    var html = [];
    if (info && null != info) {
        html = [
            '<form>',
            '<input type="hidden" name="shopId" value="' + info.shopId + '">',
            '<div class="formbody">',
            '<div class="formtitle"><span>商家信息</span></div>',
            '<ul class="forminfo">',
            '<li><label>商家名称</label><input name="shopName" type="text" class="dfinput" value="' + info.shopName + '" maxlength="50"></li>',
            '<li><label>商家类型</label>',
            '<select name="shopType" class="dfinput">',
            '<option value="0" ' + (info.shopType == 0 ? 'selected' : '') + '>餐饮</option>',
            '<option value="1" ' + (info.shopType == 1 ? 'selected' : '') + '>零售</option>',
            '<option value="2" ' + (info.shopType == 2 ? 'selected' : '') + '>服务</option>',
            '<option value="3" ' + (info.shopType == 3 ? 'selected' : '') + '>娱乐</option>',
            '<option value="4" ' + (info.shopType == 4 ? 'selected' : '') + '>其他</option>',
            '</select></li>',
            '<li><label>第一责任人</label><input name="firstPersonName" type="text" class="dfinput" value="' + info.firstPersonName + '" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="firstPersonPost" type="text" class="dfinput" value="' + info.firstPersonPost + '" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="firstPersonPhone" type="text" class="dfinput" value="' + info.firstPersonPhone + '" maxlength="11"></li>',
            '<li><label>第二责任人</label><input name="secondPersonName" type="text" class="dfinput" value="' + info.secondPersonName + '" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="secondPersonPost" type="text" class="dfinput" value="' + info.secondPersonPost + '" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="secondPersonPhone" type="text" class="dfinput" value="' + info.secondPersonPhone + '" maxlength="11"></li>',
            '<li><label>闭店表箱号</label><input name="closeShopBox" type="text" class="dfinput" value="' + info.closeShopBox + '" maxlength="50"></li>',
            '<li><label>是否有备用钥匙</label><input name="hasSpareKey" type="checkbox" style="width: 30px !important;" class="dfinput" ' + (info.hasSpareKey ? 'checked': '') + ' maxlength="50"></li>',
            '<li><label>备用钥匙箱号</label><input name="spareKeyBox" type="text" class="dfinput" value="' + info.spareKeyBox + '" maxlength="50"></li>',
            '<li><label>闭店运行设备</label><input name="runningDevices" type="text" class="dfinput" value="' + info.spareKeyBox + '" maxlength="50"></li>',
            // TODO 运行设备的选择做成弹出框形式，直接在外层选择  或者做成checkbox
            '</ul>',
            '</div>',
            '</form>'
        ];
    } else {
        html = [
            '<form>',
            '<div class="formbody">',
            '<div class="formtitle"><span>商家信息</span></div>',
            '<ul class="forminfo">',
            '<li><label>商家名称</label><input name="shopName" type="text" class="dfinput" " maxlength="50"></li>',
            '<li><label>商家类型</label>',
            '<select name="shopType" class="dfinput">',
            '<option value="0">餐饮</option>',
            '<option value="1">零售</option>',
            '<option value="2">服务</option>',
            '<option value="3">娱乐</option>',
            '<option value="4">其他</option>',
            '</select></li>',
            '</ul>',
            '</div>',
            '</form>'
        ];
    }
    return html.join('');
}

function getInfo(infoId) {
    var url = getRoot() + '/shop/getShopInfo.do';
    var params = {'shopId': infoId};
    var info = null;
    sendAjax(url, params, function (callback) {
        if (callback) {
            info = callback;
        }
    }, false);
    return info;
}