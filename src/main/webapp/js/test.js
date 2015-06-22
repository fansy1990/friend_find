
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
//				fit : true,
//				pagination : true,// 不要分页控件
//				pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
//				pageList : [ 4, 8, 12 ],
				rownumbers : true,// 行号
//				pagePosition : 'top',
				
				url : null,
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
						}
						] ]
						
			    }); 
//	var json_data ='[{"jobId":1,"jobName":"jobName_times:"}]';
	var json_data={"total":1,rows:[{"jobId":1,"jobName":"jobName_times:"+jobName_i}]};
	$('#testid').datagrid('loadData',json_data);
	//monitorid
});

// 定时刷新监控信息
var jobName_i=1;
function test_refresh(){
	jobName_i=jobName_i+1;
	
	var json_data ={"total":1,rows:[{"jobId":1,"jobName":"jobName_times:"+jobName_i}]};
//	var json_data='[{"jobId":1,"jobName":'+jobName_i+'}]';
	console.info(json_data);
	$('#testid').datagrid('loadData',json_data);
	console.info("jobName_i_:"+jobName_i);
	if(jobName_i==10){
		clearInterval(test_interval);
		console.info("done");
	}
	
}
