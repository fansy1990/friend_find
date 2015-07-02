/**
 * 预处理部分js
 * 
 */
$(function(){
	// =====uploadId,数据上传button绑定 click方法
	$('#uploadId').bind('click', function(){
		var input_i=$('#localFileId').val();
		// 弹出进度框
		popupProgressbar('数据上传','数据上传中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_upload.action',{input:input_i});
	});
	//=======uploadId
	
	// =====dedup_submit_id,数据去重
	$('#dedup_submit_id').bind('click', function(){
		var input_i=$('#dedup_input_id').val();
		var output_i=$('#dedup_output_id').val();
		// 弹出进度框
		popupProgressbar('提交任务','提交任务到云平台中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_deduplicate.action',{input:input_i,output:output_i});
	});
	//=======dedup_submit_id
	
	// ===== 数据下载
	$('#downloadId').bind('click', function(){
		var input_=$('#fromHDFSFileId').val();
		var output_=$('#tolocalFileId').val();
		// 弹出进度框
		popupProgressbar('数据下载','数据下载中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_download.action',{input:input_,output:output_});
	});
	// ======= 数据下载
	
	
	// ===== 解析入库
	$('#resolveId').bind('click', function(){
		var input_=$('#resolveFileId').val();
		// 弹出进度框
		popupProgressbar('数据入库','数据解析入库中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_resolve2db.action',{input:input_});
	});
	
	//findbestdc_submit_id 找到最佳阈值
	$('#findbestdc_submit_id').bind('click', function(){
		var input_=$('#findbestdc_input_id').val();
		var delta_=$('#findbestdc_delta_id').val();
		// 弹出进度框
		popupProgressbar('请等待','寻找最佳阈值DC中...',1000);
		// ajax 异步提交任务
		callByAJaxLocal_findbestdc('cloud/cloud_findbestdc.action',{input:input_,delta:delta_});
	});
	//findbestdc_submit_id
	
	//caldistance_submit_id 计算向量之间的距离
	$('#caldistance_submit_id').bind('click', function(){
		var input_=$('#caldistance_input_id').val();
		var output_=$('#caldistance_output_id').val();
		// 弹出进度框
		popupProgressbar('请等待','提交计算向量距离任务中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_caldistance.action',{input:input_,output:output_});
	});
	//caldistance_submit_id
	
	
	// ===== 数据预处理 数据库到HDFS
	$('#preprocess_submit_id').bind('click', function(){
		var record_=$('#preprocess_record_id').val();
		var output_=$('#preprocess_output_id').val();
//		var delta_=$('#preprocess_delta_id').val();
		
		// 弹出进度框
		popupProgressbar('请等待','数据库数据解析并序列化到HFDS中...',1000);
		// ajax 异步提交任务
		callByAJax('cloud/cloud_db2hdfs.action',{record:record_,output:output_});
	});
	// ======= 数据预处理
	
	
});

/**
 * 刷新
 */
function monitor_one_refresh(){
	$.ajax({ // ajax提交
		url : 'cloud/cloud_monitorone.action',
		dataType : "json",
		success : function(data) {
			if (data.finished == 'error') {// 获取信息错误 ，返回数据设置为0，否则正常返回
				clearInterval(monitor_cf_interval);
				setJobInfoValues(data);
				console.info("monitor,finished:"+data.finished);
				$.messager.show({
					title : '提示',
					msg : '任务运行失败！'
				});
			} else if(data.finished == 'true'){
				// 所有任务运行成功则停止timer
				console.info('monitor,data.finished='+data.finished);
				setJobInfoValues(data);
				clearInterval(monitor_cf_interval);
				$.messager.show({
					title : '提示',
					msg : '所有任务成功运行完成！'
				});
				
			}else{
				// 设置提示，并更改页面数据,多行显示job任务信息
				setJobInfoValues(data);
			}
		}
	});
	
}


function setJobInfoValues(data){
	$('#jobnums').val(data.jobnums);
	$('#currjob').val(data.currjob);
	$('#jobid').val(data.rows.jobId);
	$('#jobname').val(data.rows.jobName);
	$('#mapprogress').val(data.rows.mapProgress);
	$('#redprogress').val(data.rows.redProgress);
	$('#state').val(data.rows.runState);
}


function callByAJaxLocal_findbestdc(url,data_){
	$.ajax({
		url : url,
		data: data_,
		async:true,
		dataType:"json",
		context : document.body,
		success : function(data) {
//			$.messager.progress('close');
			closeProgressbar();
			console.info("data.flag:"+data.flag);
			var retMsg;
			if("true"==data.flag){
				retMsg='操作成功！DC阈值为：'+data.dc;
				$('#dc_return_id').html("推荐DC阈值为："+data.dc);
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