<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  
  <body>
  <div  data-options="region:'north',border:false" style="height:60px;padding:50px 50px 10px 50px;">
 	 <h1> Hadoop集群任务实时监控</h1><br><hr><br>
  </div>
 
   <table id="monitorid" class ="easyui-datagrid" >		</table>
		
<div id="returnMsg_monitor" style="color:red;padding-left: 30px;font-size: 15px;padding-top:10px;"></div>

	<script type="text/javascript">
		// 自动定时刷新 1s
	 	var monitor_cf_interval= setInterval("monitor_refresh()",3000);
		
	</script>
	<script type="text/javascript" src="js/monitor.js"></script>
  </body>

 