/**
 * Created by admin on 2017/11/27.
 */
function showCheckResult(planInfo, shopId) {
    var layerIndex = layer.open({
        title: false,
        type: 1,
        shadeClose: false,
        closeBtn: true,
        area: ['500px', '800px'],
        content: getCheckResultHtml(planInfo, shopId),
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
                html.push('结果状态:');
                html.push('</td>');
                html.push('<td width="70%" style="text-align: left;">');
                html.push(checkresult.status == 0 ? '不合格' : '合格');
                html.push('</td>');
                html.push('</tr>');
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
                    $.each(images, function (i, imageName) {
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

function getCheckResultHtml(info, shopId) {
    var shopoption = [];
    if (info.shopList && info.shopList.length > 0) {
        $.each(info.shopList, function (i, shop) {
            if (shopId) {
                if (shop.shopId == shopId) {
                    shopoption.push('<option value="' + shop.shopId + '">' + shop.shopName + '</option>');
                }
            } else {
                shopoption.push('<option value="' + shop.shopId + '">' + shop.shopName + '</option>');
            }
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

function getCheckPlanInfo(infoId) {
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