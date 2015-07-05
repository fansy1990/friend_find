<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">计算向量之间的距离--MR任务</div>
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		如果有MR监控页面，请先关闭，在提交MR任务<br>
	</div>
	<br>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			<tr>
				<td><label for="name">输入路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="caldistance_input_id" data-options="required:true" style="width:300px"
					value="/user/root/_filter/preparevectors" /></td>

			</tr>
			<tr>
				<td><label for="name">输出路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="caldistance_output_id" data-options="required:true" style="width:300px"
					value="/user/root/_filter/caldistance" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="caldistance_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">计算距离</a></td>
			</tr>

		</table>
	</div>
	<script type="text/javascript" src="js/preprocess.js"></script>
</body>

