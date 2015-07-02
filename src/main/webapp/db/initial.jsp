<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'init.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body>
	<div data-options="region:'north',border:false"
		style="height:60px;padding:50px;">

		<br> <select id="cc" class="easyui-combobox" name="dept"
			style="width:200px;">
			<option value="LoginUser">用户登录表</option>
			<option value="UserData">用户数据表(不要使用此功能)</option>
			<option value="HConstants">集群配置表</option>

		</select> 
		
		<a id="initialId" href="#" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'">初始化</a> <br>
		<br>
		<br>	
		<a id="db2hdfsId" href="" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'">DB2HDFS</a> <br>		

	</div>
	<script type="text/javascript" src="js/initial.js"></script>
</body>
</html>
