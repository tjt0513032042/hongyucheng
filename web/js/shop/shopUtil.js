/**
 * Created by admin on 2017/11/26.
 */
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