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
    $('#addButton').on('click', function () {
        toSave();
    });

    $('#searchButton').trigger('click');

    // 初始化时间控件
    initDatePicker($('input[name=start]'));
    initDatePicker($('input[name=end]'));
});

function searchList(param, pageNo, pageSize) {
    var url = getRoot() + '/report/queryReports.do';
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

            //registerFunc();
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

function createPlanListHtml(datas) {
    var htmlContent = [];
    if (datas && datas.length > 0) {
        $.each(datas, function (i, data) {
            if (i > 0 && i % 2 == 1) {
                htmlContent.push('<tr class="contentDatas odd" infoId="' + data.shopId + '">');
            } else {
                htmlContent.push('<tr class="contentDatas" infoId="' + data.shopId + '">');
            }
            htmlContent.push('<td>');
            htmlContent.push(data.checkDate);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(data.shopName);
            htmlContent.push('</td>');            
            htmlContent.push('<td>');
            htmlContent.push(data.checkName);
            htmlContent.push('</td>');
            htmlContent.push('<td>');
            htmlContent.push(getCheckValue(data.checkValue));
            htmlContent.push('</td>');
            htmlContent.push('</tr>')
        });
    }
    return htmlContent;
}


function getCheckValue(value){
	if('1' == value){
		return "是";
	}else{
		return "否";
	}
}

