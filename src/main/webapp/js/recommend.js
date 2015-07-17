$(function(){
	// =====group2db
	$('#group2db_submit_id').bind('click', function(){
		var input_i=$('#group2db_input_id').val();
		// 弹出进度框
		popupProgressbar('数据入库','用户类别数据解析并入库中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_group2db.action',{input:input_i});
	});
	
	// group2db
	
	
	// group
	$('#group_check_id').bind('click', function(){
		var input_i=$('#group2db_input_id').val();
		// 弹出进度框
		popupProgressbar('聚类中心','聚类中心解析中...',1000);
		// ajax 异步提交任务
		getCenterAndDisplay("cloud/cloud_groupcheck.action");
	});
	
	$('#group_pic_id').bind('click', function(){
		console.info('not done');
	});
	
	// group
});

/**
 * 获取中心点并展示
 * @param url_
 */
function getCenterAndDisplay(url_){
	$.ajax({
		url : url_,
		data: {},
		async:true,
		dataType:"json",
		context : document.body,
		success : function(data) {
			closeProgressbar();
			var retMsg;
			if("true"==data.flag){
				retMsg='操作成功！';
				// 展示返回数据
				$('#group_return_id').html(data.html);
			}else{
				retMsg='操作失败！失败原因：'+data.msg;
			}
			
			$.messager.show({
				title : '提示',
				msg : retMsg
			});
			
		}
	});
	
}