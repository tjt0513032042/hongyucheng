/**
 * Created by admin on 2017/11/12.
 */
$(document).ready(function () {
    // 初始化时间控件
    initDatePicker($('input[name=date]'), {
        changeMonth: true,
        changeYear: true,
        dateFormat: 'mm-yy',
        showButtonPanel: true,
        monthNamesShort: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
        closeText: '选择',
        currentText: '本月',
        isSelMon: 'true',
        onClose: function (dateText, inst) {
            var month = +$("#ui-datepicker-div .ui-datepicker-month :selected").val() + 1,
                year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            if (month < 10) {
                month = '0' + month;
            }
            this.value = year + '-' + month;
            if (typeof this.blur === 'function') {
                this.blur();
            }
        }
    });

    $('input[name=shopId]').on('click', function () {
        chooseShop(this);
    });

    $('#searchButton').on('click', function () {
        var params = {
            shopId: $('input[name=shopId]').attr('shopid'),
            date: $('input[name=date]').val()
        };
        searchList(params, 0, pageSize);
    });
});

function searchList(param, pageNo, pageSize) {
    if(!param.shopId || param.shopId == null || param.shopId == ''){
        layer.msg('请选择一个商家进行查询!');
        return;
    }
    var url = getRoot() + '/summary/queryMonthList.do';
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
            htmlContent.push(data.checkDate);
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
            desc = '<span style="color: red;">不合格</span>';
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
            desc = data.openFlag ? '合格' : '<span style="color: red;">不合格</span>';
        } else {
            desc = data.closeFlag ? '合格' : '<span style="color: red;">不合格</span>';
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

