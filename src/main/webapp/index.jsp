<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>Friend Find</title>
	<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="themes/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/basic.js"></script>
	<script type="text/javascript" src="js/test.js"></script>
	
	
</head>
<body>
<div style="height:600px;padding:50px 50px 10px 50px;">
 	 <h1> Test实时监控</h1><br><hr><br>
  
 <table id="testid" class ="easyui-datagrid" >		</table>
	</div>	
	<script type="text/javascript">
		// 自动定时刷新 1s
	 	var test_interval= setInterval("test_refresh()",3000);
		
	</script>

</body>
</html>
