<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
  
  <body>
  <div  data-options="region:'north',border:false" style="height:60px;padding:50px 50px 10px 50px;">
 	 <h1> Hadoop集群任务实时监控</h1><br><hr><br>
  </div>
  <div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
	 <table>
			 <tr>
				<td><label for="name">所有任务个数:</label></td>
				<td><input class="easyui-validatebox" type="text"
						id="jobnums" data-options="required:true" value="#" />
					</td>
					
				</tr>
				<tr>
				<td><label for="name">当前任务:</label></td>
				<td><input class="easyui-validatebox" type="text"
						id="currjob" data-options="required:true" value="#" />
					</td>
					
				</tr>
   		<tr>
				<td><label for="name">JobID:</label></td>
				<td><input class="easyui-validatebox" type="text"
						id="jobid" data-options="required:true" style="width:300px" value="#" />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">JobName:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="jobname" data-options="required:true" style="width:600px"
						value="#" />
					</td>
					
				</tr>
				<tr>
					<td><label for="name">Map进度:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="mapprogress" data-options="required:true"
						value="0.0%" />
					</td>
				</tr>
				<tr>
					<td><label for="name">Reduce进度:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="redprogress" data-options="required:true"
						value="0.0%" />
					</td>
				</tr>
				<tr>
					<td><label for="name">任务执行状态:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="state" data-options="required:true"
						value="#" />
					</td>
				</tr>
			</table>
		
	</div>

	<script type="text/javascript">
		// 自动定时刷新 1s
	 	var monitor_cf_interval= setInterval("monitor_one_refresh()",3000);
		
	</script>
	<script type="text/javascript" src="js/preprocess.js"></script>
  </body>

 