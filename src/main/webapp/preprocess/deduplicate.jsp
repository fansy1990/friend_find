<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">数据文件去重</div>
	<br>
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		文件去重操作在HDFS上面进行,如果有MR监控页面，请先关闭，在提交MR任务<br>
	</div>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			<tr>
				<td><label for="name">输入路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="dedup_input_id" data-options="required:true" style="width:300px"
					value="/user/root/_source/source_users.xml" /></td>

			</tr>
			<tr>
				<td><label for="name">输出路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="dedup_output_id" data-options="required:true" style="width:300px"
					value="/user/root/_filter/deduplicate" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="dedup_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">去重</a></td>
			</tr>

		</table>
	</div>
	<script type="text/javascript" src="js/preprocess.js"></script>
</body>

