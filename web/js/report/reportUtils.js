/**
 * Created by admin on 2017/11/26.
 */
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
        desc = '<span style="color: green;">√</span>';
    } else {
        desc = '<span style="color: red;">×</span>';
    }
    return desc;
}

function getRecordType(value) {
    if ('0' == value) {
        return "开店表";
    } else {
        return "闭店表";
    }
}