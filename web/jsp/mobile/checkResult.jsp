<!--
	商家抽查页面
  -->
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.net.URLDecoder"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    basePath= URLDecoder.decode(path,"utf-8");
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/layer/layer.js"></script>

<link rel="stylesheet" href="<%=basePath%>/js/jquery-mobile/jquery.mobile-1.4.5.min.css">
<script src="https://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
</head>

<script language="javascript">

$(document).on("pageinit",function(event){
    $('input.submit').on('click', function () {
        if($('#shopId').val() == '-1'){
            layer.msg("请选择需要抽查的商家!");
            return;
        }
        
         var url = getRoot() + '/mobile/saveCheckResult.do';

		var formData = new FormData();
		var index = 0;
		var len = $('input[name="imageNames"]').length;
        $('input[name="imageNames"]').each(function () {
            if ($(this).val() != '') {
                var v_name = "imageNames_" + index;
                formData.append(v_name, $(this)[0].files[0]);
                index++;
            }
        });
		
		
		formData.append('shopId', $('#shopId').val());
		formData.append('planId', $('#planId').val()); 
		
		formData.append('status', $('#status').val());
		formData.append('description', $('#description').val()); 
		$.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			processData : false,
			contentType : false
		}).done(function(res) {
			 layer.msg(res);
			 window.location.reload();
		}).fail(function(res) {});
    });
    
    $('#shopId').on('change', function () {
 
    var params ={
    shopId:$('#shopId').val(),
    planId:$('#planId').val()
    }
         var url = getRoot() + '/mobile/queryCheckResult.do';
    	sendAjax(url, params, function (callbackdata) {
        refreshPage(callbackdata);
    });
    });
   searchShopList();
});

function refreshPage(checkResult){
        if (checkResult) {
            // 加载列表数据
            if(checkResult.shopId){
	           	$('#description').val(checkResult.description);
	        	//$("#status").val(checkResult.status);
	        	$("#status").find("option[value='"+checkResult.status+"']").attr("selected",true);
	        	$("#status").selectmenu('refresh', true);
	        	/* var fileName = checkResult.imageNames
	        	alert(fileName); */
				if(''!= checkResult.imageNames){
					$(checkResult.imageNames.split(';')).each(function(i,v){
						//console.log(v);
						var fileName = v;
					var imgUrl = '<%=basePath%>/check_result_images/'+fileName;	
					var imgTr =	'<tr><td width="90%"><img class="image"  src="'+imgUrl+'" width="100%"/></td><td>'
								+'<a href="javascript:void(0);" name="del"  onclick="delImage(this)"><img src="<%=basePath%>/images/authority/3_del.png"/></a>'
								+'<input type="hidden" name="imageName" value="'+fileName+'"></td></tr>'
		        	$('#imageTable').append(imgTr);
					})
				}
            }else{
				clearPage();
            }
        }

}

function clearPage(){
    $('#description').val('');
	$("#status").find("option[value='1']").attr("selected",true);
	$("#status").selectmenu('refresh', true);
}

function searchShopList(param, pageNo, pageSize) {
	    var params;
    var url = getRoot() + '/mobile/queryCheckShopListPlan.do';
    sendAjax(url, params, function (callbackdata) {
        if (callbackdata) {
            // 加载列表数据
            var datas = callbackdata.result;
            if(datas.length == 0){
            	layer.msg("今日没有抽查计划!");
            	 var option = $("<option>").val("-1").text("今日没有抽查计划!"); 
	       		$("#shopId").append(option);
            }else{
            	var shopInfoList = datas.shopList;
	            getShopNamesHtml(datas);
            }
        }else{
       	 layer.msg("今日没有抽查计划!");
        } 
    });
}

function getShopNamesHtml(datas) {
	//查询的是当天的抽查计划，checkPlan只会有一个
    $.each(datas, function (i, checkPlan) {
    	var shopList = checkPlan.shopList;
		$('#planId').val(checkPlan.planId);
    	 $.each(shopList, function (i, shopInfo) {
    	 if(0 == i){
    		 var option = $("<option>").val("-1").text("请选择需要抽查的商家"); 
	       $("#shopId").append(option);
    	 }
    	 var option = $("<option>").val(shopInfo.shopId).text(shopInfo.shopName); 
	       $("#shopId").append(option); 
    	 })
    });
 $("#shopId").selectmenu('refresh', true); 
}

function delImage(tar){
	var tr = $(tar).parent().parent().parent();
	var v = tr.find('input:first').val();
	var params = {
		imageName: v,
		planId : $('#planId').val(),
		shopId : $('#shopId').val() 
	};
    var url = getRoot() + '/mobile/deleteImage.do';
    sendAjax(url, params, function (callbackdata) {
        if (callbackdata) {
            $(tar).closest('tr').remove();
        } else {
            layer.msg('图片删除失败!');
		}
    });
}

/**
 * Created by admin on 2017/11/8.
 */
function getRoot() {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}

/**
 * 发送ajax请求
 * @param url
 * @param params
 * @param callbackfunction
 * @param async
 */
function sendAjax(url, params, callbackfunction, async, contentType) {
    if (!callbackfunction || !jQuery.isFunction(callbackfunction)) {
        callbackfunction = function (callback) {

        }
    }
    if (async == undefined) {
        async = true
    }
    var opts = {
        url: url,
        data: params,
        async: async,
        dataType: 'json',
        type: 'POST',
        success: callbackfunction
    };
    if (contentType) {
        opts.contentType = contentType;
    }
    $.ajax(opts);
	}
function addCloneTr(){
	var cloneTr = $('tr[name="cloneTr"]:first').clone(true);
	$(cloneTr).find('input').val('');
	$(cloneTr).find('a[name="add"]:first').hide();
	$(cloneTr).find('a[name="del"]:first').show();
	$('#imageNames').append(cloneTr);
}

function delCloneTr(tar){
var tr = $(tar).parent().parent();
	$(tr).remove();

}	
</script>
<body>
	<!--登陆页面-->
	<div data-role="page" id="pageLogin">

		<div data-role="header">
			<h1 role="heading">商家抽查</h1>
		</div>

		<div data-role="main" class="ui-content">
			<form method="post" action="" id="mainForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="planId" id="planId" />
				<div class="ui-field-contain">
					<fieldset data-role="fieldcontain">
						<label for="shopId">选择商家:</label> <select name="shopId"
							id="shopId">
						</select>
					</fieldset>
					<br /> <label for="status">抽查结果:</label> <select name="status"
						id="status">
						<option value="1">合格</option>
						<option value="0">不合格</option>
					</select> <label for="description">备注:</label>
					<textarea rows="3" cols="10" name="description" id="description"></textarea>
					<label for="imageNames">上传图片:</label>
					<table id="imageNames" width="100%">
						<tr name = "cloneTr">
							<td width="90%"><input name="imageNames" type="file"/></td>
							<td><a href="javascript:void(0);" name="del" style="display:none"  onclick="delCloneTr(this)"><img src="<%=basePath%>/images/authority/3_del.png"/></a>
							<a href="javascript:void(0);" name="add"  onclick="addCloneTr(this)"><img src="<%=basePath%>/images/authority/add.jpg"/></a>
							</td>
					 	</tr>
					</table>
					<label for="imageTable">已上传图片:</label>
					<table id="imageTable" width="100%">
					</table>
					<div style="margin-top: 20%;">
						<input type="button" class="submit" value="提交" />
					</div>
				</div>
			</form>
		</div>

		<div data-role="footer" style="text-align: center"
			data-position="fixed">
			<p>© 2017 WONDER CITY. All Rights Reserved.</p>
		</div>
	</div>
</body>
</html>
