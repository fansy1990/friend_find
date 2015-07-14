<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">聚类中心写入HDFS</div>
	
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		DB过滤到HDFS处理包括：<br>
		<ol>
			<li>根据提供的聚类中心个数，提取出聚类中心</li>
			<li>把聚类中心序列化到HDFS中</li>
			<li>把聚类中心写入本地文件</li>
		</ol>
	</div>
	<br>
	<div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<table>
			
			<tr>
				<td><label for="name">聚类中心个数:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="center2hdfs_k_id" data-options="required:true" 
					value="10" /></td>

			</tr>
			
			<tr>
				<td><label for="name">输入路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="center2hdfs_input_id" data-options="required:true" style="width:300px"
					value="/user/root/sort/part-r-00000" /></td>

			</tr>
			<tr>
				<td><label for="name">本地路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="center2hdfs_localfile_id" data-options="required:true" style="width:300px"
					value="WEB-INF/classes/centervector.dat" /></td>

			</tr>
			<tr>
				<td><label for="name">输出路径:</label>
				</td>
				<td><input class="easyui-validatebox" type="text"
					id="center2hdfs_output_id" data-options="required:true" style="width:300px"
					value="/user/root/_center/iter_0/clustered/part-m-00000" /></td>

			</tr>
			<tr>
				<td></td>
				<td><a id="center2hdfs_submit_id" href="" class="easyui-linkbutton"
					data-options="iconCls:'icon-door_in'">确定</a></td>
			</tr>

		</table>
	</div>
	<script type="text/javascript" src="js/runCluster.js"></script>
</body>

