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

