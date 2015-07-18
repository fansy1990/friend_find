<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  
  <body>
  <div  data-options="region:'north',border:false" style="height:60px;padding:50px 50px 10px 50px;">
  <h1> 朋友推荐</h1> 
  <label>用户ID：</label>
  <input class="easyui-validatebox" type="text"
  	id="recommend_input_id" data-options="required:true" style="width:300px"
					value="9905" />
  <a id="recommend_button_id" href="" class="easyui-linkbutton" data-options="iconCls:'icon-note'">查看</a>
  
  <br><hr><br>
    </div>
	
	<div id="recommend_return_id" style="padding-left: 30px;font-size: 20px;padding-top:10px;"></div>
	<br>
	<div id="recommend_layer_id" style="width:1100px;height:300px;display:none">
		<table id="recommend_id" class ="easyui-datagrid"  >
	
		</table>
	</div>
	<!-- --> 
	<script type="text/javascript" src="js/recommend.js"></script>
	
  </body>

 