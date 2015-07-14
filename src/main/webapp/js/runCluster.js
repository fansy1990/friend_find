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
						title : 'MR算法监控',
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
	$('#resetid').bind('click', function() {
		console.info("resetid重置！");
		$('#formid').form('reset', {});
	});
	
	
	// cent2hdfs---
	$('#center2hdfs_submit_id').bind('click', function(){
		var k=$('#center2hdfs_k_id').val();// 聚类中心向量个数 
		var input_=$('#center2hdfs_input_id').val();
		var output_=$('#center2hdfs_output_id').val();
		var localfile=$('#center2hdfs_localfile_id').val();
		
		// 弹出进度框
		popupProgressbar('寻找聚类中心','聚类中心寻找并上传中...',1000);
		// ajax 异步提交任务
		console.info('here');
		callByAJax('cloud/cloud_center2hdfs.action',{input:input_,output:output_,record:k,method:localfile});
	});
	
	// ------cent2hdfs
	
	// runCluster2.jsp-------
		$('#runcluster2_submitid').bind('click', function(){
		var k=$('#runcluster2_k').val();// 聚类中心向量个数 
		var input_=$('#runcluster2_input').val();
		var output_=$('#runcluster2_output').val();
		var delta_=$('#runcluster2_delta').val();
		
		// 弹出进度框
		popupProgressbar('执行聚类','原始数据拷贝并执行分类中...',1000);
		// ajax 异步提交任务
		console.info('here');
		callByAJax('cloud/cloud_runCluster2.action',{input:input_,output:output_,record:k,delta:delta_});
	});
	
	// --------runCluster2.jsp
	
	
});
