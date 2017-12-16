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
                    htmlContent.push(data.sNo);
                    htmlContent.push('</td>');
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
                    htmlContent.push(data.floor);
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
                htmlContent.push('<tr class="contentDatas"><td colspan="10">暂无数据</td></tr>');
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
                var url = getRoot() + '/shop/deleteShopInfo.do';
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
        area: ['600px', '80%'],
        btn: ['确定', '取消'],
        content: getInfoHtml(info),
        success: function (dom) {
            // 设置jquery验证器
            var rules = {
                shopName: {
                    required: true
                },
                sNo: {
                    required: true
                }
            };
            $(dom).find('form').validate({
                rules: rules,
                onfocusout: false
            });

            $(dom).find('input[name=runningDevicesName]').on('click', function () {
                chooseDevices($(dom).find('input[name=runningDevicesName]'), $(dom).find('input[name=runningDevices]'));
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
        var deviceNames = '';
        if (info.deviceInfoList && info.deviceInfoList.length > 0) {
            $.each(info.deviceInfoList, function (i, deviceInfo) {
                deviceNames += deviceInfo.deviceName + ',';
            });
            deviceNames = deviceNames.substr(0, deviceNames.length - 1);
        }
        html = [
            '<form>',
            '<input type="hidden" name="shopId" value="' + info.shopId + '">',
            '<div class="formbody">',
            '<div class="formtitle"><span>商家信息</span></div>',
            '<ul class="forminfo">',
            '<li><label><span style="display: inline;color: red;">*</span>商家编号</label><input name="sNo" type="text" class="dfinput" value="' + info.sNo + '" maxlength="20"></li>',
            '<li><label><span style="display: inline;color: red;">*</span>商家名称</label><input name="shopName" type="text" class="dfinput" value="' + info.shopName + '" maxlength="50"></li>',
            '<li><label>商家类型</label>',
            '<select name="shopType" class="dfinput">',
            '<option value="0" ' + (info.shopType == 0 ? 'selected' : '') + '>餐饮</option>',
            '<option value="1" ' + (info.shopType == 1 ? 'selected' : '') + '>零售</option>',
            '<option value="2" ' + (info.shopType == 2 ? 'selected' : '') + '>服务</option>',
            '<option value="3" ' + (info.shopType == 3 ? 'selected' : '') + '>娱乐</option>',
            '<option value="4" ' + (info.shopType == 4 ? 'selected' : '') + '>其他</option>',
            '</select></li>',
            '<li><label>商家楼层</label>',
            '<select name="floor" class="dfinput">',
            '<option value="B2" ' + (info.floor == 'B2' ? 'selected' : '') + '>B2</option>',
            '<option value="B1" ' + (info.floor == 'B1' ? 'selected' : '') + '>B1</option>',
            '<option value="1" ' + (info.floor == '1' ? 'selected' : '') + '>1F</option>',
            '<option value="2" ' + (info.floor == '2' ? 'selected' : '') + '>2F</option>',
            '<option value="3" ' + (info.floor == '3' ? 'selected' : '') + '>3F</option>',
            '<option value="4" ' + (info.floor == '4' ? 'selected' : '') + '>4F</option>',
            '<option value="5" ' + (info.floor == '5'? 'selected' : '') + '>5F</option>',
            '</select>',
            '</li>',
            '<li><label>第一责任人</label><input name="firstPersonName" type="text" class="dfinput" value="' + nullToString(info.firstPersonName) + '" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="firstPersonPost" type="text" class="dfinput" value="' + nullToString(info.firstPersonPost) + '" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="firstPersonPhone" type="text" class="dfinput" value="' + nullToString(info.firstPersonPhone) + '" maxlength="11"></li>',
            '<li><label>第二责任人</label><input name="secondPersonName" type="text" class="dfinput" value="' + nullToString(info.secondPersonName) + '" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="secondPersonPost" type="text" class="dfinput" value="' + nullToString(info.secondPersonPost) + '" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="secondPersonPhone" type="text" class="dfinput" value="' + nullToString(info.secondPersonPhone) + '" maxlength="11"></li>',
            '<li><label>闭店表箱号</label><input name="closeShopBox" type="text" class="dfinput" value="' + nullToString(info.closeShopBox) + '" maxlength="50"></li>',
            '<li><label>是否有备用钥匙</label><input name="hasSpareKey" type="checkbox" style="width: 30px !important;" class="dfinput" ' + (info.hasSpareKey ? 'checked' : '') + ' maxlength="50"></li>',
            '<li><label>备用钥匙箱号</label><input name="spareKeyBox" type="text" class="dfinput" value="' + nullToString(info.spareKeyBox) + '" maxlength="50"></li>',
            '<li><label>闭店运行设备</label><input name="runningDevicesName" type="text" class="dfinput" value="' + deviceNames + '" readonly="readonly"><input name="runningDevices" type="hidden" value="' + nullToString(info.runningDevices) + '"/></li>',
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
            '<li><label><span style="display: inline;color: red;">*</span>商家编号</label><input name="sNo" type="text" class="dfinput" maxlength="20"></li>',
            '<li><label><span style="display: inline;color: red;">*</span>商家名称</label><input name="shopName" type="text" class="dfinput" " maxlength="50"></li>',
            '<li><label>商家类型</label>',
            '<select name="shopType" class="dfinput">',
            '<option value="0">餐饮</option>',
            '<option value="1">零售</option>',
            '<option value="2">服务</option>',
            '<option value="3">娱乐</option>',
            '<option value="4">其他</option>',
            '</select></li>',
            '<li><label>商家楼层</label>',
            '<select name="floor" class="dfinput">',
            '<option value="B2">B2</option>',
            '<option value="B1">B1</option>',
            '<option value="1">1F</option>',
            '<option value="2">2F</option>',
            '<option value="3">3F</option>',
            '<option value="4">4F</option>',
            '<option value="5">5F</option>',
            '</select>',
            '</li>',
            '<li><label>第一责任人</label><input name="firstPersonName" type="text" class="dfinput" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="firstPersonPost" type="text" class="dfinput" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="firstPersonPhone" type="text" class="dfinput" maxlength="11"></li>',
            '<li><label>第二责任人</label><input name="secondPersonName" type="text" class="dfinput" maxlength="20"></li>',
            '<li><label>责任人职务</label><input name="secondPersonPost" type="text" class="dfinput" maxlength="11"></li>',
            '<li><label>责任人号码</label><input name="secondPersonPhone" type="text" class="dfinput" maxlength="11"></li>',
            '<li><label>闭店表箱号</label><input name="closeShopBox" type="text" class="dfinput" maxlength="50"></li>',
            '<li><label>是否有备用钥匙</label><input name="hasSpareKey" type="checkbox" class="dfinput" style="width: 30px !important;" maxlength="50"></li>',
            '<li><label>备用钥匙箱号</label><input name="spareKeyBox" type="text" class="dfinput" maxlength="50"></li>',
            '<li><label>闭店运行设备</label><input name="runningDevicesName" type="text" class="dfinput" readonly="readonly"><input name="runningDevices" type="hidden"/></li>',
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

/**
 * 选择闭店不关闭设备
 * @param nameInput
 * @param idInput
 */
function chooseDevices(nameInput, idInput) {
    var devices = getDevices();
    var html = [
        '<table class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
        '<thead>',
        '<th><input type="checkbox" class="allcheck"/></th>',
        '<th>设备类别</th>',
        '<th>设备名称</th>',
        '</thead>'
    ];
    $.each(devices, function (i, device) {
        html.push('<tr>');
        html.push('<td><input type="checkbox" class="itemcheck" deviceId="' + device.deviceId + '" deviceName="' + device.deviceName + '"/></td>');
        html.push('<td>' + transFormDeviceType(device.deviceType) + '</td>');
        html.push('<td>' + device.deviceName + '</td>');
        html.push('</tr>');
    });
    html.push('</table>');
    var layerIndex = layer.open({
        title: '设备选择',
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['400px', '70%'],
        btn: ['确定', '取消'],
        content: html.join(''),
        success: function (dom) {
            $(dom).find('input.allcheck').on('change', function () {
                if ($(this).is(':checked')) {
                    $(dom).find('input.itemcheck').attr('checked', 'true');
                } else {
                    $(dom).find('input.itemcheck').removeAttr('checked');
                }
            });
        },
        btn1: function (index, dom) {
            var inputs = $(dom).find('input.itemcheck:checked');
            var names = '';
            var ids = '';
            if (inputs && inputs.length > 0) {
                $.each(inputs, function (i, input) {
                    names += $(input).attr('deviceName') + ',';
                    ids += $(input).attr('deviceId') + ',';
                });
                if(names.length > 0){
                    names = names.substr(0, names.length - 1);
                }
                if(ids.length > 0){
                    ids = ids.substr(0, ids.length - 1);
                }
            }
            $(nameInput).val(names);
            $(idInput).val(ids);

            layer.close(layerIndex);
        }
    });
}

/**
 * 设备类型:0 办公类;1 安全防范类;2 照明;3 保鲜类;4 生鲜水池类;5 其他
 * @param deviceType
 */
function transFormDeviceType(deviceType) {
    var name = '';
    if (deviceType == '0') {
        name = '办公类';
    } else if (deviceType == '1') {
        name = '安全防范类';
    } else if (deviceType == '2') {
        name = '照明';
    } else if (deviceType == '3') {
        name = '保鲜类';
    } else if (deviceType == '4') {
        name = '生鲜水池类';
    } else {
        name = '其他';
    }
    return name;
}