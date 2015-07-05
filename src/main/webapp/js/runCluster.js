$(function(){
	
	$('#submitid').bind('click', function() {
		console.info("submitid提交！");
		$('#formid').form({
			url : 'cloud/cloud_runCluster1.action', // 提交runCluster1任务，跳转到监控页面
			onSubmit : function() {
				// do some check
				// return false to prevent submit;
			},
			success : function(data) {
				// 关闭弹窗
				closeProgressbar();
				
				if (data == 'fail') {// cf算法Thread创建失败
					$.messager.alert('提示', '未能提交RunCluster1算法到云平台，请检查后台日志！');
				} else {// 已提交任务，转到监控页面
				
					// 使用单独Tab的方式
					layout_center_addTabFun({
						title : 'RunCluster1 MR算法监控',
						closable : true,
						// iconCls : node.iconCls,
						href : 'cluster/monitor_one.jsp' //使用新版监控 
					});
				}
			}
		});
		// submit the form
		$('#formid').submit();
		// 弹出progressbar
		popupProgressbar('Please waiting', '提交RunCluster1算法任务中...', '2000');
	});

	// connectclusterreset button
	$('#cfreset').bind('click', function() {
		console.info("cfreset重置！");
		$('#cfform').form('reset', {});
	});
});