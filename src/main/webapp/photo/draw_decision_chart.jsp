<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

  
  <body>
  <div style="margin:10px 0 40px 0;"></div>
	<div style="padding:20px 20px;">
	<a id="drawId" href="" class="easyui-linkbutton" 
		data-options="iconCls:'icon-large_chart',size:'large',iconAlign:'top'">画图</a>
		<br>
		<a id="showId" href="" class="easyui-linkbutton" 
		data-options="iconCls:'icon-large_picture',size:'large',iconAlign:'top'">展示决策图</a>
	</div>
	<div id="windowId" class="easyui-window" title="Decision Chart" data-options="minimizable:false,closed:true" 
	style="width:550px;height:550px;padding:10px;">
		<img id="picId" src="" />
	</div>
	<script type="text/javascript" src="js/draw_decision_chart.js"></script>
  </body>
