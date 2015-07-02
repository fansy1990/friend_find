$(function(){
	// 绑定button
	$('#initialId').bind('click', function(){
		var tableName=$('#cc').combobox("getValue");;
		console.info(tableName);
		// 弹出进度框
		var win = $.messager.progress({
			title : 'Please waiting',
			msg : '初始化表中中...',
			interval : '1200' //设置时间间隔较长 
		});
		// ajax 异步提交任务
//		console.info('tt');
		$.ajax({
			url : "dB/dB_initialTable.action",
			data: {tableName:tableName},
			async:true,
			context : document.body,
			success : function(data) {
//				console.info(data);
				$.messager.progress('close');
				var retMsg;
				if("true"==data){
					retMsg='表初始化成功！';
				}else{
					retMsg='表初始化失败！';
				}
				$.messager.show({
					title : '提示',
					msg : retMsg
				});
			}
		});
	});
	
	// db2hdfs
	$('#db2hdfsId').bind('click', function(){
		popupProgressbar('请等待','数据库解析到云平台中...','1200');
		callByAJax('cloud/cloud_db2hdfs.action',{});
	});
	// db2hdfs
	
	
	// testid
	$('#testId').bind('click', function(){
		// 弹出进度框
		var win = $.messager.progress({
			title : 'Please waiting',
			msg : '测试中...',
			interval : '1200' //设置时间间隔较长 
		});
		// ajax 异步提交任务
		$.ajax({
			url : "test/test_test.action",
			data: {},
			async:true,
			context : document.body,
			success : function(data) {
				$.messager.progress('close');
				var retMsg;
				if("true"==data){
					retMsg='测试成功！';
				}else{
					retMsg='测试失败！';
				}
				$.messager.show({
					title : '提示',
					msg : retMsg
				});
			}
		});
	});
	// testid
});