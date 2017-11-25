/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    $('#searchButton').on('click', function () {
        var params = {
            shopName: $('input[name=shopName]').val(),
            start: $('input[name=start]').val(),
            end: $('input[name=end]').val(),
        };
        searchList(params, 0, pageSize);
    });
    $('#addButton').on('click', function () {
        toSave();
    });

    $('#searchButton').trigger('click');

    // 初始化时间控件
    initDatePicker($('input[name=start]'));
    initDatePicker($('input[name=end]'));
});

function searchList(param, pageNo, pageSize) {
    var url = getRoot() + '/checkPlan/queryPlans.do';
    var params = $.extend(param, {
        pageNo: pageNo,
        pageSize: pageSize
    });
    sendAjax(url, params, function (callbackdata) {
        if (callbackdata) {
            // 清理界面内容
            initContent();
            // 加载列表数据
            var datas = callbackdata.result;
            var htmlContent = [];
            if (datas && datas.length > 0) {
                htmlContent = createPlanListHtml(datas);
            } else {
                htmlContent.push('<tr class="contentDatas"><td colspan="5">暂无数据</td></tr>');
            }
            $('#userInfoList').append(htmlContent.join(''));
            $('.tablelist tbody tr:odd').addClass('odd');
            // 初始化分页组件
            $('.paginationBox').pagination(callbackdata.total, { //点击分页时，调用的回调函数
                callback: function (pageIndex) {
                    var params = {
                        shopName: $('input[name=shopName]').val(),
                        start: $('input[name=start]').val(),
                        end: $('input[name=end]').val(),
                    };
                    searchList(params, pageIndex, pageSize);
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
    $('table a.viewresult').on('click', function () {
        var infoId = $(this).closest('tr').attr('infoId');
        var info = getInfo(infoId);
        if (!info || null == info) {
            layer.msg('抽查计划不存在，无法执行该操作!');
            return;
        }
        showCheckResult(info);
    });
    $('table a.modify').on('click', function () {
        var infoId = $(this).closest('tr').attr('infoId');
        var info = getInfo(infoId);
        if (!info || null == info) {
            layer.msg('抽查计划不存在，无法执行该操作!');
            return;
        }
        toModify(info);
    });
    $('table a.delete').on('click', function () {
        var id = $(this).closest('tr').attr('infoId');
        var deleteIndex = layer.msg('确定删除该抽查计划?', {
            time: 0 //不自动关闭
            , btn: ['确定', '取消']
            , yes: function (index) {
                var url = getRoot() + '/checkPlan/delete.do';
                var params = {planId: id};
                sendAjax(url, params, function (callback) {
                    layer.close(deleteIndex);
                    if (callback.flag) {
                        layer.msg('删除成功!');
                        $('#searchButton').trigger('click');
                    } else {
                        layer.msg(callback.msg);
                    }
                })
            }
        });
    });
}

/**
 * 添加抽查计划界面
 * @param info
 */
function toSave() {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['600px', '800px'],
        btn: ['确定', '取消'],
        content: getInfoHtml(),
        success: function (dom) {
            // 设置jquery验证器
            initDatePicker($(dom).find('input.datepick'));
            $(dom).find('li[name=createplan]').on('click', function () {
                createPlan(dom);
            });
        },
        btn1: function (index, dom) {
            // 封装参数用于生成抽查计划
            var plantrs = $(dom).find("table[name=checkPlanTable] tbody > tr.contentDatas");
            if (plantrs.length == 0) {
                layer.msg('无任何抽查计划可以保存,请先生成抽查计划!');
                return;
            }
            var savePlans = [];
            $.each(plantrs, function (i, tr) {
                var datestr = $(tr).find('td.datetd').text();

                var shopIdTds = $(tr).find('table.shoptable td input');
                var shopIds = '';
                $.each(shopIdTds, function (j, shopIdTd) {
                    var shopId = $(shopIdTd).attr('shopid');
                    if (shopId && shopId.length > 0) {
                        shopIds += shopId + ',';
                    }
                });
                if (shopIds.length > 0) {
                    shopIds = shopIds.substr(0, shopIds.length - 1);
                }
                savePlans.push({checkDate: datestr, shopIds: shopIds});
            });

            layer.confirm('处于该计划内的已有计划将被覆盖,确定要保存抽查计划?', function () {
                var params = {'plans': savePlans};
                // var params = {savePlans: {'plans': savePlans}};
                var url = getRoot() + '/checkPlan/save.do';
                sendAjax(url, JSON.stringify(params), function (callback) {
                    if (callback.flag) {
                        layer.close(layerIndex);
                        layer.msg('保存成功!');
                    } else {
                        if (callback.msg) {
                            layer.msg(callback.msg);
                        }
                    }
                }, false, 'application/json; charset=utf-8');
            });
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
            '<div class="formbody">',
            '<div class="formtitle"><span>修改计划</span></div>',
            '</div>',
            '<table name="checkPlanTable" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
            '<thead>',
            '<th width="20%">抽查日期</th>',
            '<th>被抽查商家</th>',
            '<th width="10%">操作</th>',
            '</thead>',
            '<tbody style="max-height: 520px; overflow-x: hidden; overflow-y: auto;">'
        ];
        html.push('<tr class="contentDatas" style="border: solid 1px #cbcbcb;">');
        html.push('<td class="datetd">');
        html.push(info.checkDate);
        html.push('</td>');
        html.push('<td>');
        html.push("<table class='shoptable' width='100%'>");
        if (info.shopList && info.shopList.length > 0) {
            $.each(info.shopList, function (j, shop) {
                html.push('<tr width="100%">');
                html.push('<td style="text-align: left !important;">');
                html.push('<input class="dfinput" shopId="' + shop.shopId + '" value="' + shop.shopName + '" readonly="readonly">');
                html.push('<a href="#" class="tablelink deleteitem">删除</a>');
                html.push('</td>');
                html.push('</tr>');
            });
        }
        html.push("</table>");
        html.push('</td>');
        html.push('<td>');
        html.push('<a href="#" class="tablelink addShop">添加</a>');
        html.push('</td>');
        html.push('</tr>');
        html.push('</tbody></table></form>');
    } else {
        html = [
            '<form>',
            '<div class="formbody">',
            '<div class="formtitle"><span>添加计划</span></div>',
            '<ul class="forminfo">',
            '<li><label>抽查时间</label><input type="text" name="start" class="dfinput datepick" style="width: 180px !important;" readonly="readonly">~<input type="text" name="end" class="dfinput datepick" style="width: 180px !important;" readonly="readonly"></li>',
            '</ul>',
            '<ul class="forminfo toolbar">',
            '<li name="createplan"><span><img src="' + getRoot() + '/images/t01.png"/></span>生成计划</li>',
            '</ul>',
            '</div>',
            '<table name="checkPlanTable" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
            '<thead>',
            '<th width="20%">抽查日期</th>',
            '<th>被抽查商家</th>',
            '<th width="10%">操作</th>',
            '</thead>',
            '<tbody style="max-height: 520px; overflow-x: hidden; overflow-y: auto;"></tbody>',
            '</table>',
            '</form>'
        ];
    }
    return html.join('');
}

/**
 * 动态生成抽查计划
 * @param dom
 */
function createPlan(dom) {
    // 验证抽查计划的开始和结束时间
    var start = $(dom).find('input[name=start]').val();
    var end = $(dom).find('input[name=end]').val();
    if (start == '' || end == '') {
        layer.msg('抽查计划的执行时间不完整,无法生成抽查计划!');
        return;
    }
    var d1 = new Date(start.replace(/\-/g, "\/"));
    var d2 = new Date(end.replace(/\-/g, "\/"));
    if (d1 > d2) {
        layer.msg('起始时间大于结束时间,无法生成抽查计划!');
        return;
    }
    var plans;
    var url = getRoot() + '/checkPlan/createPlans.do';
    var params = {start: start, end: end};
    var callbackFunc = function (callback) {
        if (callback.flag) {
            plans = callback.data;
            var planHtml = createEditPlanHtml(plans);
            $(dom.find('table > tbody')).empty();
            $(dom.find('table > tbody')).append(planHtml.join(''));
            // 注册添加和删除响应
            $(dom.find('table > tbody')).find('a.deleteitem').on('click', function () {
                $(this).closest('tr').remove();
            });
            $(dom.find('table > tbody')).find('table input.dfinput').on('click', function () {
                // 触发商家选择界面
                chooseShop(this);
            });
            $(dom.find('table > tbody')).find('a.addShop').on('click', function () {
                addShopInput(this);
            });
        } else {
            layer.msg('抽查计划生成失败,请联系管理员!');
        }
    };
    sendAjax(url, params, callbackFunc, false);
}

function createEditPlanHtml(datas) {
    var htmlContent = [];
    if (datas && datas.length > 0) {
        $.each(datas, function (i, data) {
            var checkDate = new Date(data.checkDate);
            htmlContent.push('<tr class="contentDatas" style="border: solid 1px #cbcbcb;">');
            htmlContent.push('<td class="datetd">');
            htmlContent.push(checkDate.getFullYear() + '-' + (checkDate.getMonth() + 1) + '-' + checkDate.getDate());
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            if (data.shopList && data.shopList.length > 0) {
                htmlContent.push("<table class='shoptable' width='100%'>");
                $.each(data.shopList, function (j, shop) {
                    htmlContent.push('<tr width="100%">');
                    htmlContent.push('<td style="text-align: left !important;">');
                    htmlContent.push('<input class="dfinput" shopId="' + shop.shopId + '" value="' + shop.shopName + '" readonly="readonly">');
                    htmlContent.push('<a href="#" class="tablelink deleteitem">删除</a>');
                    htmlContent.push('</td>');
                    htmlContent.push('</tr>');
                });
                htmlContent.push("</table>");
            }
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push('<a href="#" class="tablelink addShop">添加</a>');
            htmlContent.push('</td>');
            htmlContent.push('</tr>');
        });
    }
    return htmlContent;
}

function createPlanListHtml(datas) {
    var htmlContent = [];
    if (datas && datas.length > 0) {
        $.each(datas, function (i, data) {
            if (i > 0 && i % 2 == 1) {
                htmlContent.push('<tr class="contentDatas odd" infoId="' + data.planId + '">');
            } else {
                htmlContent.push('<tr class="contentDatas" infoId="' + data.planId + '">');
            }
            htmlContent.push('<td>');
            htmlContent.push(data.checkDate);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getShopNames(data));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push('<a href="#" class="tablelink modify">修改</a>&nbsp;&nbsp;&nbsp;<a href="#" class="tablelink delete">删除</a>&nbsp;&nbsp;&nbsp;<a href="#" class="tablelink viewresult">查看结果</a>');
            htmlContent.push('</td>');
            htmlContent.push('</tr>')
        });
    }
    return htmlContent;
}

/**
 * 组装商家名称信息
 * @param plan
 * @returns {string}
 */
function getShopNames(plan) {
    var names = '';
    if (plan && plan.shopList && plan.shopList.length > 0) {
        $.each(plan.shopList, function (i, shop) {
            names += shop.shopName + ',';
        });
        names = names.substr(0, names.length - 1);
    }
    return names;
}

/**
 * 获取商家id信息
 * @param shops
 * @returns {string}
 */
function getShopIds(shops) {
    var ids = '';
    if (shops && shops.length > 0) {
        $.each(shops, function (i, shop) {
            ids += shop.shopId + ',';
        });
        ids = ids.substr(0, ids.length - 1);
    }
    return ids;
}

/**
 * 选择商家
 * @param input
 */
function chooseShop(input) {
    var shops = getShopsByName('');
    var html = [
        '<div class="tools" style="margin: 5px 0px 5px 0px !important;">',
        '<form id="searchForm">',
        '<ul class="toolbar">',
        '<li style="border: 0px !important;">',
        '<label>商店名称</label>&nbsp;<input type="text" name="shopName" class="dfinput" style="width:235px !important;">',
        '</li>',
        '<li name="searchButton"><span style="margin-top: 2px !important;"><img src="' + getRoot() + '/images/ico06.png"/></span>查询</li>',
        '</ul>',
        '</form>',
        '<br><br><br>',
        '</div>',
        '<table class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
        '<thead>',
        '<th><input type="checkbox" class="allcheck"/></th>',
        '<th>商店类别</th>',
        '<th>商店名称</th>',
        '</thead>',
        '<tbody>',
        createShopHtml(shops),
        '</tbody></table>'
    ];

    var layerIndex = layer.open({
        title: '商店选择',
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['400px', '600px'],
        btn: ['确定', '取消'],
        content: html.join(''),
        success: function (dom) {
            $(dom).find('div.layui-layer-content').attr('style', $(dom).find('div.layui-layer-content').attr('style') + 'padding-left: 5px; padding-right: 5px;');
            $(dom).find('li[name=searchButton]').on('click', function () {
                var shopName = $(dom).find('div.tools input[name=shopName]').val();
                $(dom).find('table > tbody').empty();
                $(dom).find('table > tbody').append(createShopHtml(getShopsByName(shopName)));
            });
        },
        btn1: function (index, dom) {
            var inputs = $(dom).find('input.itemcheck:checked');
            if (inputs.length > 1) {
                layer.msg('每次只能选择一个商家添加到抽查计划中!');
                return;
            }
            $(input).attr('shopId', $(inputs[0]).attr('shopId'));
            $(input).val($(inputs[0]).attr('shopName'));
            layer.close(layerIndex);
        }
    });
}

function getShopsByName(shopName) {
    var url = getRoot() + '/shop/getShopInfoByName.do';
    var param = {shopName: shopName};
    var shops = null;
    sendAjax(url, param, function (callback) {
        shops = callback;
    }, false);
    return shops;
}

function createShopHtml(shops) {
    var html = [];
    if (shops && shops.length > 0) {
        $.each(shops, function (i, shop) {
            html.push('<tr class="infotr">');
            html.push('<td><input type="checkbox" class="itemcheck" shopId="' + shop.shopId + '" shopName="' + shop.shopName + '"/></td>');
            html.push('<td>' + transFormShopType(shop.shopType) + '</td>');
            html.push('<td>' + shop.shopName + '</td>');
            html.push('</tr>');
        });
    }
    return html.join('');
}

/**
 * 进入修改界面
 * @param info
 */
function toModify(info) {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: 0,
        area: ['600px', '800px'],
        btn: ['确定', '取消'],
        content: getInfoHtml(info),
        success: function (dom) {
            // 注册添加和删除响应
            $(dom.find('table > tbody')).find('a.deleteitem').on('click', function () {
                $(this).closest('tr').remove();
            });
            $(dom.find('table > tbody')).find('table input.dfinput').on('click', function () {
                // 触发商家选择界面
                chooseShop(this);
            });
            $(dom.find('table > tbody')).find('a.addShop').on('click', function () {
                addShopInput(this);
            });
        },
        btn1: function (index, dom) {
            // 封装参数用于生成抽查计划
            var plantrs = $(dom).find("table[name=checkPlanTable] tbody > tr.contentDatas");
            var tr = $(plantrs[0]);

            var datestr = $(tr).find('td.datetd').text();
            var shopIdTds = $(tr).find('table.shoptable td input');
            var shopIds = '';
            $.each(shopIdTds, function (j, shopIdTd) {
                var shopId = $(shopIdTd).attr('shopid');
                if (shopId && shopId.length > 0) {
                    shopIds += shopId + ',';
                }
            });
            if (shopIds.length > 0) {
                shopIds = shopIds.substr(0, shopIds.length - 1);
            }
            var params = {planId: info.planId, checkDate: datestr, shopIds: shopIds};
            layer.confirm('确定修改抽查计划?', function () {
                var url = getRoot() + '/checkPlan/modifyCheckPlan.do';
                sendAjax(url, params, function (callback) {
                    if (callback.flag) {
                        layer.close(layerIndex);
                        layer.msg('修改成功!');
                    } else {
                        if (callback.msg) {
                            layer.msg(callback.msg);
                        }
                    }
                });
            });
        }
    });
}

function getInfo(infoId) {
    var url = getRoot() + '/checkPlan/getCheckPlan.do';
    var params = {'planId': infoId};
    var info = null;
    sendAjax(url, params, function (callback) {
        if (callback) {
            info = callback;
        }
    }, false);
    return info;
}

function addShopInput(addButton) {
    var table = $(addButton).closest('tr').find('table.shoptable');
    var htmlContent = [];
    htmlContent.push('<tr width="100%">');
    htmlContent.push('<td style="text-align: left !important;">');
    htmlContent.push('<input class="dfinput" readonly="readonly">');
    htmlContent.push('<a href="#" class="tablelink deleteitem">删除</a>');
    htmlContent.push('</td>');
    htmlContent.push('</tr>');
    // 添加商店输入框，并且添加响应
    $(table).append(htmlContent.join(''));
    $(table).find('tr:last input.dfinput').on('click', function () {
        // 触发选择商家界面
        chooseShop(this);
    });
    $(table).find('tr:last a.deleteitem').on('click', function () {
        $(this).closest('tr').remove();
    });
}

function showCheckResult(planInfo) {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: true,
        area: ['500px', '800px'],
        content: getCheckResultHtml(planInfo),
        success: function (dom) {
            $(dom).find('select[name=checkShop]').on('change', function () {
                var shopId = $(this).val();
                var checkresult = getCheckResult(planInfo.planId, shopId);
                // 清空上次显示内容
                $(dom).find('table tr.contentDatas').remove();
                if (!checkresult || null == checkresult) {
                    layer.msg('该商家无抽查结果可展现!');
                    return;
                }
                var html = [];
                html.push('<tr class="contentDatas" style="border: solid 1px #cbcbcb;">');
                html.push('<td class="datetd" width="30%">');
                html.push('结果描述:');
                html.push('</td>');
                html.push('<td width="70%">');
                html.push('<textarea style="width: 100%; height: 100px; resize:none; overflow: auto; float: left;" readonly="readonly">' + checkresult.description + '</textarea>');
                html.push('</td>');
                html.push('</tr>');
                html.push('<tr class="contentDatas" style="border: solid 1px #cbcbcb;">');
                html.push('<td class="datetd" width="30%">');
                html.push('上传照片:');
                html.push('</td>');
                html.push('<td>');
                if (checkresult.imageNames && checkresult.imageNames.length > 0) {
                    var images = checkresult.imageNames.split(';');
                    $.each(images, function (i, imageName){
                        var url = getRoot() + '/check_result_images/' + imageName;
                        html.push('<image src="' + url + '" style="max-width: 280px; display: block;"></image>');
                    });
                }
                html.push('</td>');
                html.push('</tr>');

                $(dom).find('table tbody').append(html.join(''));
            });
            $(dom).find('select[name=checkShop]').trigger('change');
        }
    });
}

function getCheckResultHtml(info) {
    var shopoption = [];
    if (info.shopList && info.shopList.length > 0) {
        $.each(info.shopList, function (i, shop) {
            shopoption.push('<option value="' + shop.shopId + '">' + shop.shopName + '</option>');
        });
    }
    var html = [
        '<form>',
        '<div class="formbody">',
        '<div class="formtitle"><span>抽查结果</span></div>',
        '</div>',
        '<table name="checkPlanTable" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
        '<tbody style="max-height: 520px; overflow-x: hidden; overflow-y: auto;">'
    ];
    html.push('<tr style="border: solid 1px #cbcbcb;">');
    html.push('<td class="datetd" width="30%">');
    html.push('商家选择:');
    html.push('</td>');
    html.push('<td>');
    html.push('<select name="checkShop" class="dfinput" style="width: 100%; float: left;">');
    html.push(shopoption.join(''));
    html.push('</select></td></tr>');
    html.push('</tbody></table></form>');
    return html.join('');
}

function getCheckResult(planId, shopId) {
    var url = getRoot() + '/checkResult/getCheckResult.do';
    var params = {planId: planId, shopId: shopId};
    var data = null;
    sendAjax(url, params, function (callback) {
        data = callback;
    }, false);
    return data;
}
