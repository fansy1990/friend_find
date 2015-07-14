<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<body>
	<div  style="padding-left: 30px;font-size: 20px;padding-top:10px;">Fast Cluster 算法调用--执行分类</div>
	<br>
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		如果有MR监控页面，请先关闭，在提交MR任务<br>
	</div>
    <div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
			<table>
				<tr>
					<td><label for="name">输入路径:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id= "runcluster2_input" name="input" data-options="required:true"  style="width:300px"
						value="/user/root/_filter/preparevectors"/>
					</td>
					
				</tr>
				<tr>
					<td><label for="name">输出路径:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id= "runcluster2_output" name="output" data-options="required:true"  style="width:300px"
						value="/user/root/_center"/>
					</td>
					
				</tr>				
				<tr>
					<td><label for="name">距离阈值:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="runcluster2_delta" name="delta" data-options="required:true"  />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">聚类中心数:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="runcluster2_k" value="10" data-options="required:true"  />
					</td>			
				<tr>
					<td><a id="runcluster2_submitid" href=""
						class="easyui-linkbutton">提交</a></td>
					
				</tr>
			</table>
		</div>
	<script type="text/javascript" src="js/runCluster.js"></script>
</body>

