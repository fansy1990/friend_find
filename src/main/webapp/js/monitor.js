
$(function() {

	// monitorid
	$('#monitorid').datagrid(
			{
				border : false,
				fitColumns : true,
				singleSelect : true,
				width : 800,
				height : 350,
				nowrap : false,
				fit : true,
//				pagination : true,// 不要分页控件
//				pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
//				pageList : [ 4, 8, 12 ],
				rownumbers : true,// 行号
//				pagePosition : 'top',
				
				url : 'cloud/cloud_monitor.action',
//				queryParams: {
////					jobNum: jobNum //使用全局变量，在每个页面提交时设置该变量,后台直接设置即可
//				//	subject: 'datagrid'
//				},
				idField:'jobId',
				columns : [[
						{
							field : 'jobId',
							title : 'JobID',
							width : '300'
						},
						{
							field : 'jobName',
							title : '任务名',
							width : '320'
						},
						{
							field : 'mapProgress',
							title : 'Map进度',
							width : '100'
						},
						{
							field : 'redProgress',
							title : 'Reduce进度',
							width : '100'
						}
						,
						{
							field : 'runState',
							title : '任务状态',
							width : '150'
						}
						] ]
						
			    }); 

	//monitorid
});

// 定时刷新监控信息
function monitor_refresh() {
	$.ajax({ // ajax提交
		url : 'cloud/cloud_monitor.action',
		dataType : "json",
		async:true,
		success : function(data) {
			if (data.finished == 'error') {// 获取信息错误 ，返回数据设置为0，否则正常返回
				clearInterval(monitor_cf_interval);
				console.info("monitor,finished:"+data.finished);
				$('#returnMsg_monitor').html('获取任务信息错误！');

			} else if(data.finished == 'true'){
				
				// 所有任务运行成功则停止timer
				console.info('monitor,data.finished='+data.finished);
				$('#returnMsg_monitor').html('任务运行完成！');
//				  var converData = $.parseJSON(data.rows);
				$('#monitorId').datagrid('loadData',data.rows);// 设置多一遍
				console.info(data.rows);
				clearInterval(monitor_cf_interval);
				
			}else{
				// 设置提示，并更改页面数据,多行显示job任务信息
				// ------ 设置datagrid 的数据
				console.info('monitor,data.finished='+data.finished);
				$('#returnMsg_monitor').html('正在运行任务！');
//				var converData = $.parseJSON(data.rows);
//				var converData={'total':data.total,rows:JSON.stringify(data.rows)};
//				console.info(converData);
				
//				$('#monitorId').datagrid('loadData',data.rows);//
				$('#monitorId').datagrid('reload');
				console.info("this is in false,use reload ,data.finished:"+data.finished);
			}
		}
	});
	
}

