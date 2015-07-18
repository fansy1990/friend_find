<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

  
  <body>
  <div style="margin:10px 0 40px 0;"></div>
	<div style="padding:20px 20px;">

	<div style="padding-left: 30px;font-size: 15px;padding-top:10px;"><br>
		此页面用于寻找聚类中心，看图找到聚类中心的范围（x轴大于多少，y轴大于多少，在寻找聚类中心页面使用）<br>
		注意：决策图展示的不包含局部密度最大的点，所以在执行分类时需要把决策图中的聚类中心个数加1<br>
	
	<br>
	<br>
	<a id="drawId" href="" class="easyui-linkbutton" 
		data-options="iconCls:'icon-color_wheel',size:'large',iconAlign:'top'">画图</a>
		
		<a id="showId" href="" class="easyui-linkbutton" 
		data-options="iconCls:'icon-chart_line',size:'large',iconAlign:'top'">展示决策图</a>
	</div>
	</div>
	<div id="windowId" class="easyui-window" title="Decision Chart" data-options="minimizable:false,closed:true" 
	style="width:550px;height:550px;padding:10px;">
		<img id="picId" src="" />
	</div>
	<script type="text/javascript" src="js/draw_decision_chart.js"></script>
  </body>
