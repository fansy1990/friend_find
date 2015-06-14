$(function(){
	// 绑定button
	$('#initialId').bind('click', function(){
		// ajax 异步提交任务
//		console.info('tt');
		$.ajax({
			url : "test/test_insertTable.action",
			data: {},
			async:true,
			context : document.body,
			success : function(data) {
//				console.info(data);
				var retMsg;
				if("true"==data){
					retMsg='登录表和集群配置表初始化成功！';
				}else{
					retMsg='登录表和集群配置表初始化失败！';
				}
				$.messager.show({
					title : '提示',
					msg : retMsg
				});
			}
		});
	});
});