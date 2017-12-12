/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    $('#searchButton').on('click', function () {
        searchList($('#searchForm input[name=name]').val(), $('#searchForm input[name=phone]').val(), 0, pageSize);
    });
    $('#addButton').on('click', function () {
        toSaveUserInfo();
    });

    $('#searchButton').trigger('click');
});

function searchList(name, phone, pageNo, pageSize) {
    var url = getRoot() + '/user/queryUserList.do';
    var params = {
        name: name,
        phone: phone,
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
                        htmlContent.push('<tr class="contentDatas odd" userId="' + data.id + '">');
                    } else {
                        htmlContent.push('<tr class="contentDatas" userId="' + data.id + '">');
                    }
                    htmlContent.push('<td>');
                    htmlContent.push(data.name);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    if (data.role == 0) {
                        htmlContent.push('管理员');
                    } else if (data.role == 1) {
                        htmlContent.push('店长');
                    } else {
                        htmlContent.push('工作人员');
                    }
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    htmlContent.push(data.phone);
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    if (data.shop && null != data.shop) {
                        htmlContent.push(data.shop.shopName);
                    }
                    htmlContent.push('</td>');
                    htmlContent.push('<td>');
                    if(canOperate() || data.role != 0){
                        htmlContent.push('<a href="#" class="tablelink modify">修改</a>&nbsp;&nbsp;&nbsp;<a href="#" class="tablelink delete">删除</a>');
                    }
                    htmlContent.push('</td>');
                    htmlContent.push('</tr>')
                });
            } else {
                htmlContent.push('<tr class="contentDatas"><td colspan="5">暂无数据</td></tr>');
            }
            $('#userInfoList').append(htmlContent.join(''));
            // 初始化分页组件
            $('.paginationBox').pagination(callbackdata.total, { //点击分页时，调用的回调函数
                callback: function (pageIndex) {
                    searchList($('#searchForm input[name=name]').val(), $('#searchForm input[name=phone]').val(), pageIndex, pageSize);
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
        var userId = $(this).closest('tr').attr('userId');
        var userInfo = getUserInfo(userId);
        if (!userInfo || null == userInfo) {
            layer.msg('用户信息已不存在，无法执行该操作!');
            return;
        }
        toSaveUserInfo(userInfo);
    });
    $('table a.delete').on('click', function () {
        var userId = $(this).closest('tr').attr('userId');
        var deleteIndex = layer.msg('确定删除该用户信息?', {
            time: 0 //不自动关闭
            , btn: ['确定', '取消']
            , yes: function (index) {
                var url = getRoot() + '/user/delete.do';
                var params = {id: userId};
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
 * 打开用户信息管理界面
 * @param userInfo
 */
function toSaveUserInfo(userInfo) {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['600px', '450px'],
        btn: ['确定', '取消'],
        content: getUserInfoHtml(userInfo),
        success: function (dom) {
            // 设置jquery验证器
            var rules = {
                name: {
                    required: true
                },
                phone: {
                    required: true,
                    isMobile: true
                }
            };
            if (userInfo && null != userInfo) {
                rules.password = {required: true};
            }
            $(dom).find('form').validate({
                rules: rules,
                onfocusout: false
            });

            $(dom).find('select[name=role]').on('change', function () {
                var role = $(this).val();
                // 仅在角色为店家的时候，才显示商店选择选项
                if (role == 1) {
                    $(dom).find('input[name=shopId]').closest('li').show();
                } else {
                    $(dom).find('input[name=shopId]').closest('li').hide();
                }
            });
            $(dom).find('select[name=role]').trigger('change');
            $(dom).find('input[name=shopId]').on('click', function(){
                chooseShop(this);
            });
        },
        btn1: function (index, dom) {
            if ($(dom).find('form').valid()) {
                var params = paramString2obj($(dom).find('form'));
                params.shopId = $(dom).find('input[name=shopId]').attr('shopId');
                var url = getRoot() + '/user/saveUserInfo.do';
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
function getUserInfoHtml(userInfo) {
    var html = [];
    if (userInfo && null != userInfo) {
        html = [
            '<form>',
            '<input type="hidden" name="id" value="' + userInfo.id + '">',
            '<div class="formbody">',
            '<div class="formtitle"><span>用户信息</span></div>',
            '<ul class="forminfo">',
            '<li><label><span style="display: inline;color: red;">*</span>用户名称</label><input name="name" type="text" class="dfinput" value="' + userInfo.name + '" maxlength="10"></li>',
            '<li><label>用户角色</label>',
            '<select name="role" class="dfinput">',
            canOperate() ? '<option value="0" ' + (userInfo.role == 0 ? 'selected' : '') + '>管理员</option>' : '',
            '<option value="1" ' + (userInfo.role == 1 ? 'selected' : '') + '>店长</option>',
            '<option value="2" ' + (userInfo.role == 2 ? 'selected' : '') + '>工作人员</option>',
            '</select></li>',
            '<li><label><span style="display: inline;color: red;">*</span>手机号码</label><input name="phone" type="text" class="dfinput" value="' + userInfo.phone + '" maxlength="11"></li>',
            '<li><label>所属店家</label><input name="shopId" type="text" class="dfinput" readonly="readonly" value="' + (null == userInfo.shop ? '' : userInfo.shop.shopName) + '"></li>',
            '<li><label><span style="display: inline;color: red;">*</span>登录密码</label><input name="password" type="password" class="dfinput" value="' + userInfo.password + '" maxlength="11"></li>',
            '</ul>',
            '</div>',
            '</form>'
        ];
    } else {
        html = [
            '<form>',
            '<div class="formbody">',
            '<div class="formtitle"><span>用户信息</span></div>',
            '<ul class="forminfo">',
            '<li><label><span style="display: inline;color: red;">*</span>用户名称</label><input name="name" type="text" class="dfinput" maxlength="10"></li>',
            '<li><label>用户角色</label>',
            '<select name="role" class="dfinput">',
            canOperate() ? '<option value="0">管理员</option>' : '',
            '<option value="1">店长</option>',
            '<option value="2">工作人员</option>',
            '</select></li>',
            '<li><label><span style="display: inline;color: red;">*</span>手机号码</label><input name="phone" type="text" class="dfinput" maxlength="11"></li>',
            '<li><label>所属店家</label><input name="shopId" type="text" class="dfinput" readonly="readonly"></li>',
            '</ul>',
            '</div>',
            '</form>'
        ];
    }
    return html.join('');
}

/**
 * 初始化店家信息下拉框
 * @param userInfo
 * @returns {string}
 */
function getShopSelect(userInfo) {
    var html = [
        '<select name="shopId" class="dfinput">',
        '<option value=""></option>'
    ];
    var shops = getShops();
    if (shops && null != shops && shops.length > 0) {
        $.each(shops, function (i, shop) {
            html.push('<option value="' + shop.shopId + ' ' + ((userInfo && null != userInfo && userInfo.shopId == shop.shopId) ? 'selected' : '') + '">' + shop.shopName + '</option>');
        });
    }
    html.push('</select>');
    return html.join('');
}