/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    // 初始化时间控件
    initDatePicker($('input[name=date]'));

    $('#searchButton').on('click', function () {
        var params = {
            date: $('input[name=date]').val()
        };
        searchList(params, 0, pageSize);
    });
});

function searchList(param, pageNo, pageSize) {
    var url = getRoot() + '/summary/queryDayList.do';
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
                htmlContent.push('<tr class="contentDatas"><td colspan="7">暂无数据</td></tr>');
            }
            $('#userInfoList tbody').append(htmlContent.join(''));
            $('.tablelist tbody tr:odd').addClass('odd');

            // 初始化分页组件
            $('.paginationBox').pagination(callbackdata.total, { //点击分页时，调用的回调函数
                callback: function (pageIndex) {
                    searchList({
                        date: $('input[name=date]').val()
                    }, pageIndex, pageSize);
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
                htmlContent.push('<tr class="contentDatas odd">');
            } else {
                htmlContent.push('<tr class="contentDatas">');
            }
            htmlContent.push('<td>');
            htmlContent.push(data.shopInfo.shopName);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getStatus(data, 0));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getCheckDate(data, 0));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getStatus(data, 1));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getCheckDate(data, 1));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getCheckStatus(data));
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getOperate(data));
            htmlContent.push('</td>');
            htmlContent.push('</tr>')
        });
    }
    return htmlContent;
}

function getOperate(data) {
    var html = [];
    if (null != data.openRecord) {
        html.push('<a href="#" class="tablelink view" rname="开店表" recordId="' + data.openRecord.recordId + '">开店表</a>&nbsp;&nbsp;&nbsp;');
    }
    if (null != data.closeRecord) {
        html.push('<a href="#" class="tablelink view" rname="闭店表" recordId="' + data.closeRecord.recordId + '">闭店表</a>&nbsp;&nbsp;&nbsp;');
    }
    if (null != data.checkResult) {
        html.push('<a href="#" class="tablelink viewcheckresult" rname="检查结果" planId="' + data.checkResult.planId + '" shopId="' + data.checkResult.shopId + '">检查结果</a>');
    }
    return html.join('');
}

/**
 * 获取抽查状态
 * @param data
 */
function getCheckStatus(data) {
    var desc = '-';
    if (null != data.checkResult) {
        if (data.checkResult.status == 1) {
            desc = '合格';
        } else if (data.checkResult.status == 0) {
            desc = '不合格';
        }
    }
    return desc;
}

/**
 * 获取开闭店状态
 * @param data
 * @param type 0:开店 1:闭店
 */
function getStatus(data, type) {
    var record = null;
    if (type == 0) {
        record = data.openRecord;
    } else {
        record = data.closeRecord;
    }

    var desc = '-';
    if (null != record) {
        if (type == 0) {
            desc = data.openFlag ? '合格' : '不合格';
        } else {
            desc = data.closeFlag ? '合格' : '不合格';
        }
    }
    return desc;
}

/**
 *
 * @param data
 * @param type
 */
function getCheckDate(data, type) {
    var record = null;
    if (type == 0) {
        record = data.openRecord;
    } else {
        record = data.closeRecord;
    }
    if (null != record) {
        return record.checkDateStr;
    }
    return '-'
}

function registerFunc() {
    // 查看开店闭店详情
    $('#userInfoList a.view').on('click', function () {
        var recordId = $(this).attr('recordId');
        showCheckDetail(recordId);
    });
    // 查看抽查结果
    $('#userInfoList a.viewcheckresult').on('click', function () {
        var planId = $(this).attr('planId');
        var shopId = $(this).attr('shopId');
        var info = getCheckPlanInfo(planId);
        if (!info || null == info) {
            layer.msg('抽查计划不存在，无法执行该操作!');
            return;
        }
        showCheckResult(info, parseInt(shopId));
    });
}

