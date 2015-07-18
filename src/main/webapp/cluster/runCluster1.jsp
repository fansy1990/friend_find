<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<body>
	<div  style="padding-left: 30px;font-size: 20px;padding-top:10px;">Fast Cluster 算法调用--寻找中心向量</div>
	<br>
	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		如果有MR监控页面，请先关闭，在提交MR任务<br>
	</div>
    <div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<form id="formid" method="post">
			<table>
				<tr>
					<td><label for="name">输入路径:</label></td>
					<td><input class="easyui-validatebox" type="text"
						name="input" data-options="required:true" 
						style="width:200px" value="/user/root/_filter/caldistance" />
					</td>
					
				</tr>
				
				<tr>
					<td><label for="name">距离阈值:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="delta" name="delta" data-options="required:true"  />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">密度计算方法:</label></td>
					<td>
					<select id="method" class="easyui-combobox" name="method"
						style="width:200px;">
							<option value="cutoff">Cut-off算法</option>
							<option value="gaussian">Gaussian算法</option>
						</select>
					</td>
				</tr>
				
				<tr>
					<td><label for="name">局部密度MR Reducer个数:</label></td>
					<td><input class="easyui-validatebox" type="text"
						 name="numReducerDensity" data-options="required:true" value="1" />
					</td>
				</tr>
				<tr>
					<td><label for="name">最小距离MR Reducer个数:</label></td>
					<td><input class="easyui-validatebox" type="text"
						 name="numReducerDistance" data-options="required:true" value="1" />
					</td>
				</tr>
				<tr>
					<td><label for="name">排序MR Reducer个数(支持设置1个):</label></td>
					<td><input class="easyui-validatebox" type="text"
						 name="numReducerSort" data-options="required:true" value="1" />
					</td>
				</tr>
				<tr>
					<td><a id="submitid" href="#"
						class="easyui-linkbutton">提交</a></td>
					<td><a id="resetid" href="#"
						class="easyui-linkbutton">重置</a></td>
				</tr>
			</table>
		</form>
		</div>
	<script type="text/javascript" src="js/runCluster.js"></script>
</body>

