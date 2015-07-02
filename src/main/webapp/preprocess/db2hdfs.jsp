<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">DB过滤到HDFS</div>
	
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		DB过滤到HDFS处理包括：<br>
		<ol>
			<li>提取Mysql数据库UserData数据</li>
			<li>过滤UserData中符合规则的数据</li>
			<li>把符合规则的UserData数据序列化写入HDFS</li>
			<!-- 
			<li>归一化</li>
			<li>寻找dc阈值</li>
			 -->
		</ol>
	</div>
	<br>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			
			<tr>
				<td><label for="name">输出路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="preprocess_output_id" data-options="required:true" style="width:300px"
					value="/user/root/_filter/preparevectors" /></td>

			</tr>
			<tr>
				<td><label for="name">生成文件个数:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="preprocess_record_id" data-options="required:true" style="width:300px"
					value="4" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="preprocess_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">预处理</a></td>
			</tr>

		</table>
	</div>
	<script type="text/javascript" src="js/preprocess.js"></script>
</body>

