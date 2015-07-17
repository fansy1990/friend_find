<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  
  <body>
  <div  data-options="region:'north',border:false" style="height:60px;padding:50px 50px 10px 50px;">
  <h1> 聚类中心及占比查看</h1> 
  <input class="easyui-validatebox" type="text"
  	id="group_input_id" data-options="required:true" style="width:300px"
					value="WEB-INF/classes/centervector.dat" />
  <a id="group_check_id" href="" class="easyui-linkbutton" data-options="iconCls:'icon-note'">查看</a>
  <a id="group_pic_id" href="" class="easyui-linkbutton" data-options="iconCls:'icon-color_wheel'">图形展示</a>
  <br><hr><br>
    </div>
 
   <div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		这里的数据不包括过滤的数据以及未分类的数据,即只包含已分类的数据<br>
	</div>
	
	<div id="group_return_id" style="padding-left: 30px;font-size: 20px;padding-top:10px;"></div>
	
	<script type="text/javascript" src="js/recommend.js"></script>
  </body>

 