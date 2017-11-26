/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    $('#searchButton').on('click', function () {
        var params = {
            start: $('input[name=start]').val(),
            end: $('input[name=end]').val(),
            shopName: $('input[name=shopName]').val(),
        };
        searchList(params, 0, pageSize);
    });
    $('#searchButton').trigger('click');

    // 初始化时间控件
    initDatePicker($('input[name=start]'));
    initDatePicker($('input[name=end]'));
});

function searchList(param, pageNo, pageSize) {
    var url = getRoot() + '/report/queryCheckRecords.do';
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
                htmlContent = createListHtml(datas);
            } else {
                htmlContent.push('<tr class="contentDatas"><td colspan="6">暂无数据</td></tr>');
            }
            $('#userInfoList').append(htmlContent.join(''));
            $('.tablelist tbody tr:odd').addClass('odd');
            // 初始化分页组件
            $('.paginationBox').pagination(callbackdata.total, { //点击分页时，调用的回调函数
                callback: function (pageIndex) {
                    var params = {
                        start: $('input[name=start]').val(),
                        end: $('input[name=end]').val(),
                        shopName: $('input[name=shopName]').val(),
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

function createListHtml(datas) {
    var htmlContent = [];
    if (datas && datas.length > 0) {
        $.each(datas, function (i, data) {
            if (i > 0 && i % 2 == 1) {
                htmlContent.push('<tr class="contentDatas odd" infoId="' + data.recordId + '">');
            } else {
                htmlContent.push('<tr class="contentDatas" infoId="' + data.recordId + '">');
            }
            htmlContent.push('<td>');
            htmlContent.push(data.shopInfo.shopName);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(data.checkDateStr);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(data.user.name);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(data.user.phone);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getRecordType(data.recordType));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push('<a href="#" class="tablelink view">查看</a>');
            if (data.checkFlag) {
                htmlContent.push('&nbsp;&nbsp;&nbsp;<a href="#" class="tablelink viewResult">抽查结果</a>');
            }
            htmlContent.push('</td>');
            htmlContent.push('</tr>')
        });
    }
    return htmlContent;
}


function getRecordType(value) {
    if ('0' == value) {
        return "开店表";
    } else {
        return "闭店表";
    }
}

function registerFunc() {
    // 查看开店闭店详情
    $('#userInfoList a.view').on('click', function () {
        var recordId = $(this).closest('tr').attr('infoId');
        showCheckDetail(recordId);
    });
    // 查看抽查结果
    $('#userInfoList a.viewResult').on('click', function () {
        var recordId = $(this).closest('tr').attr('infoId');
        // TODO
    });
}

function showCheckDetail(recordId) {
    var info = getCheckRecords(recordId);
    if (info == null || !info) {
        layer.msg('记录已不存在,无法查看详情!')
        return;
    }

    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: true,
        area: ['600px', '750px'],
        content: getDetailsHtml(info)
    });
}

function getDetailsHtml(info) {
    var title = info.shopInfo.shopName + '-' + getRecordType(info.recordType);
    var html = [
        '<form style="padding-left: 5px; padding-right: 5px;">',
        '<input type="hidden" name="shopId" value="' + info.shopId + '">',
        '<div class="formbody" style="padding-bottom: 0px;">',
        '<div class="formtitle"><span>' + title + '</span></div>',
        '</div>',
        '<ul class="forminfo">',
        '<li><label>填写人</label><label>' + info.user.name + '</label></li>',
        '<li><label>填写时间</label><label style="width: 200px;">' + info.checkDateStr + '</label></li>',
        '</ul>',
        '<table name="checkPlanTable" class="tablelist dataTable table table-border table-bordered table-bg table-hover table-sort">',
        '<thead>',
        '<th>检查项目</th>',
        '<th width="20%">检查结果</th>',
        '</thead>',
        '<tbody style="max-height: 520px; overflow-x: hidden; overflow-y: auto;">'
    ];
    if (info.details && info.details != null && info.details.length > 0) {
        $.each(info.details, function (i, detail) {
            html.push('<tr>');
            html.push('<td style="text-align: left !important;">');
            html.push(detail.optionName);
            html.push('</td>');
            html.push('<td>');
            html.push(transformOptionResult(detail.optionResult));
            html.push('</td>');
            html.push('</tr>');
        });
    }
    html.push('</tbody></table></form>');
    return html.join('');
}

function getCheckRecords(recordId) {
    var info = null;
    var url = getRoot() + '/report/getCheckRecords.do';
    var params = {recordId: recordId};
    sendAjax(url, params, function (callback) {
        info = callback;
    }, false);
    return info;
}

function transformOptionResult(result) {
    var desc = '';
    if (result == 0 || result == '0') {
        desc = '<span>√</span>';
    } else {
        desc = '<span>×</span>';
    }
    return desc;
}

