
var picId_path;
$(function() {

	$('#drawId').bind('click', function(){
		console.info("draw pic");
		// 弹出进度框
		var win = $.messager.progress({
			title : 'Please waiting',
			msg : '画决策图中...',
			interval : '1200' //设置时间间隔较长 
		});
		$.ajax({
			url : "cloud/cloud_drawDecisionChart.action",
//			data: {tableName:tableName},
			async:true,
			dataType:"json",
			context : document.body,
			success : function(data) {
//				console.info(data);
				$.messager.progress('close');
				var retMsg;
				if("true"==data.flag){
					picId_path=data.path;
					retMsg='决策图生成成功！';
				}else{
					retMsg='决策图生成失败！';
				}
				$.messager.show({
					title : '提示',
					msg : retMsg
				});
			}
		});
		
	});
	
	$('#showId').bind('click', function(){
		console.info("show pic");
		// 弹出进
		console.info("picId_src:"+picId_path);
//		$("#picId").attr("src",picId_path);
		$("#picId").attr("src","pictures/decision_chart.png");
		
		$('#windowId').window('refresh');
		$('#windowId').window('open');
	});
	
	
});

