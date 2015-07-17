<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">分类数据入库</div>
	<br>
	
	<br>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			<tr>
				<td><label for="name">分类数据路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="group2db_input_id" data-options="required:true" style="width:300px"
					value="/user/root/_center" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="group2db_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">入库</a></td>
			</tr>
		</table>
	</div>
	
	<script type="text/javascript" src="js/recommend.js"></script>
</body>

