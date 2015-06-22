<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<body>
	<div  style="padding-left: 30px;font-size: 20px;padding-top:10px;">Fast Cluster 算法调用</div><br>
    <div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
		<form id="formid" method="post">
			<table>
				<tr>
					<td><label for="name">输入路径:</label></td>
					<td><input class="easyui-validatebox" type="text"
						name="input" data-options="required:true"  />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">输入分隔符:</label></td>
					<td>
					<select id="splitter" class="easyui-combobox" name="splitter"
						style="width:200px;">
							<option value=",">逗号,</option>
							<option value=";">分号;</option>
							<option value=":">冒号:</option>
							<option value="|">|号</option>
							<option value =".">句号.</option>
							<option value="-">破折号-</option>
							<option value=" ">空格' '</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="name">距离阈值:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="delta" name="delta" data-options="required:true"  />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">距离算法:</label></td>
					<td>
					<select id="method" class="easyui-combobox" name="method"
						style="width:200px;">
							<option value="gaussian">Gaussian算法</option>
							<option value="cutoff">Cut-off算法</option>
						</select>
					</td>
					
			
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

