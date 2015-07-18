$(function(){
	$("#div_recommend_layer").hide();
	
	
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
	
	// recommend_button_id
	$('#recommend_button_id').bind('click', function(){
		var id_=$('#recommend_input_id').val();
		// 弹出进度框
//		popupProgressbar('聚类中心','聚类中心解析中...',1000);
		// ajax 异步提交任务
		getRecommendAndDisplay("dB/dB_getRecommendData.action",{id:id_});
	});
	//recommend_id
	
	
	// reocmmend
	var id_=$('#recommend_input_id').val();
	
	$('#recommend_id').datagrid({
		border : false,
		fitColumns : false,
		singleSelect : true,
		width : 600,
		height : 250,
		nowrap : false,
		fit : true,
		pagination : true,// 分页控件
		pageSize : 4, // 每页记录数，需要和pageList保持倍数关系
		pageList : [ 4, 8, 12 ],
		rownumbers : true,// 行号
		pagePosition : 'top',
		url : 'dB/dB_getRecommendData.action',
//		url:'',
		queryParams: {
			id: id_ // ,
		// subject: 'datagrid'
		},
		idField:'id',
		columns :[[
				{
					field : 'displayName',
					title : '用户名',
					width : '120'
				},{
					field : 'downVotes',
					title : '反对数',
					width : '150'
				},{
					field : 'upVotes',
					title : '支持数',
					width : '150'
				},{
					field : 'views',
					title : '浏览数',
					width : '150',
				},{
					field : 'reputation',
					title : '声望值',
					width : '150'
				},{
					field : 'aboutMe',
					title : '关于我',
					width : '300'
				}
				 ]]
		    }); 
	
	// recomend
});

/**
 * 获取中心点并展示
 * 
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


/**
 * 获取推荐信息，并展示
 * @param url_
 * @param data
 * @returns
 */
function getRecommendAndDisplay(url_,data_){
	$.ajax({
		url : url_,
		data: data_,
		async:true,
		dataType:"json",
		context : document.body,
		success : function(data) {
			closeProgressbar();
			var retMsg;
			if("true"==data.flag){
//				retMsg='操作成功！';
				// 展示推荐的用户信息
//				$("#div_recommend_layer").show();
//				document.getElementById("recommend_id").style.display="inline";
				$("#recommend_layer_id").show();
//				$('#recommend_id').datagrid('reload',data.rows);// 设置多一遍
			}else{
//				retMsg='操作失败！失败原因：'+data.msg;
//				$("#div_recommend_layer").hide();
//				document.getElementById("recommend_id").style.display="none";
				$("#recommend_layer_id").hide();
			}
			// 展示返回数据
			$('#recommend_return_id').html(data.html);
			
			
		}
	});
	
}