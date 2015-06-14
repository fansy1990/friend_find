var editFlag = undefined;
$(function() {

	// loginuserid
	$('#userdataid').datagrid(
			{
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
				url : 'dB/dB_getTableData.action',
				queryParams: {
					tableName: 'UserData' //,
				//	subject: 'datagrid'
				},
				idField:'id',
				columns :[[
						{
							field : 'displayName',
							title : '用户名',
							width : '120',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						},
						{
							field : 'downVotes',
							title : '反对数',
							width : '150',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						},
						{
							field : 'upVotes',
							title : '支持数',
							width : '150',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						},
						{
							field : 'views',
							title : '浏览数',
							width : '150',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						},
						{
							field : 'reputation',
							title : '声望值',
							width : '150',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						},
						{
							field : 'aboutMe',
							title : '关于我',
							width : '200',
							editor: {//设置其为可编辑
		                        type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
		                        options: {
		                        	required: true//设置编辑规则属性
		                    }
						}
						}
						 ]],
						 toolbar: [{//在dategrid表单的头部添加按钮
				                text: "添加",
				                iconCls: "icon-add",
				                handler: function () {
				                    if (editFlag != undefined) {
				                        $("#userdataid").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
				                    }
				                    if (editFlag == undefined) {//防止同时打开过多添加行
				                        $("#userdataid").datagrid('insertRow', {//在指定行添加数据，appendRow是在最后一行添加数据
				                            index: 0,    // 行数从0开始计算
				                            row: {
				                                username: '请输入用户名',
				                                password: '请输入密码',
				                                description: '用户描述'
				                            }
				                        });
				                        $("#userdataid").datagrid('beginEdit', 0);//开启编辑并传入要编辑的行
				                        editFlag = 0;
				                    }
				                }
				            }, '-', {//'-'就是在两个按钮的中间加一个竖线分割，看着舒服
				                text: "删除",
				                iconCls: "icon-remove",
				                handler: function () {
				                    //选中要删除的行
				                    var row = $("#userdataid").datagrid('getSelected');
				                    var rowIndex = $('#userdataid').datagrid('getRowIndex', row);
				                    console.info(row+","+rowIndex);
				                    if (row.id) {//选中几行的话触发事件
				                    	deleteRow(row.id,rowIndex);
//				                        $.messager.confirm("提示", "您确定要删除数据吗？", function (res) {//提示是否删除
//				                            if (res) {
//				                                deleteRow(row.id);
//				                            }
//				                        });
				                    }
				                }
				            }, '-', {
				                text: "修改",
				                iconCls: "icon-edit",
				                handler: function () {
				                    //选中一行进行编辑
				                    var rows = $("#userdataid").datagrid('getSelections');
				                    if (rows.length == 1) {//选中一行的话触发事件
				                        if (editFlag != undefined) {
				                            $("#userdataid").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
				                        }
				                        if (editFlag == undefined) {
				                            var index = $("#userdataid").datagrid('getRowIndex', rows[0]);//获取选定行的索引
				                            $("#userdataid").datagrid('beginEdit', index);//开启编辑并传入要编辑的行
				                            editFlag = index;
				                        }
				                    }
				                }
				            }, '-', {
				                text: "保存",
				                iconCls: "icon-save",
				                handler: function () {
				                    $("#userdataid").datagrid('endEdit', editFlag);
				                }
				            }, '-', {
				                text: "撤销",
				                iconCls: "icon-redo",
				                handler: function () {
				                    editFlag = undefined;
				                    $("#userdataid").datagrid('rejectChanges');
				                }
				            }, '-'],
				            onAfterEdit: function (rowIndex, rowData, changes) {//在添加完毕endEdit，保存时触发
				                console.info(rowData);//在火狐浏览器的控制台下可看到传递到后台的数据，这里我们就可以利用这些数据异步到后台添加，添加完成后，刷新datagrid
				                saveRow(rowIndex,rowData);
				                editFlag = undefined;//重置
				            }, onDblClickCell: function (rowIndex, field, value) {//双击该行修改内容
				                if (editFlag != undefined) {
				                    $("#userdataid").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
				                }
				                if (editFlag == undefined) {
				                    $("#userdataid").datagrid('beginEdit', rowIndex);//开启编辑并传入要编辑的行
				                    editFlag = rowIndex;
				                }
				            }
				    }); 
	//loginuserid
});

function saveRow(index,node){  
//	console.info("node.id:"+node.id+",node.username:"+node.username);
//	var json={id:node.id,custKey:node.custKey,custValue:node.custValue,description:node.description};
	var json={id:node.id,aboutMe:node.aboutMe,age:node.age,creationDate:node.creationDate,
			displayName:node.displayName,downVotes:node.downVotes,emailHash:node.emailHash,
			lastAccessDate:node.lastAccessDate,location:node.location,reputation:node.reputation,
			upVotes:node.upVotes,views:node.views,websiteUrl:node.websiteUrl};
	}
	var encodeJson=JSON.stringify(json);
    $.ajax({  
        url : 'dB/dB_updateOrSave.action',  
        type : 'POST',  
        data : {json:encodeJson,tableName:"UserData"},  
        timeout : 60000,  
        success : function(data, textStatus, jqXHR) {     
            var msg = '';  
            if (data == "success") {  
            	console.info('保存成功！');
                $.messager.alert('提示', '保存成功！', 'info', function() {  
                	$("#userdataid").datagrid('refreshRow', index);  
                	$("#userdataid").datagrid('reload');  
                });  
            } else{  
                msg = "保存失败！";  
                console.info(msg);
                $.messager.alert('提示', msg , 'error', function() {  
                	$("#userdataid").datagrid('beginEdit', index);  
                });  
            }                     

        }  
    });  
      
}  