<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">寻找DC最佳阈值</div>
	
	<br>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			<tr>
				<td><label for="name">输入路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="findbestdc_input_id" data-options="required:true" style="width:300px"
					value="/user/root/_filter/caldistance" /></td>

			</tr>
<!-- 
			<tr>
				<td><label for="name">输入记录数:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="findbestdc_record_id" data-options="required:true" style="width:300px"
					value="182414550" /></td>

			</tr>
	 -->		
			<tr>
				<td><label for="name">阈值百分比:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="findbestdc_delta_id" data-options="required:true" style="width:300px"
					value="2%" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="findbestdc_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">定位最佳DC</a></td>
			</tr>

		</table>
	</div>
	
	<div id="dc_return_id" style="padding-left: 30px;font-size: 20px;padding-top:10px;"></div>
	<script type="text/javascript" src="js/preprocess.js"></script>
</body>

