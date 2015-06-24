<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<body>
	<div  style="padding-left: 30px;font-size: 20px;padding-top:10px;">XML文件解析到数据库</div><br>
    <div style="padding-left: 30px;font-size: 20px;padding-top:10px;">
			<table>
				<tr>
					<td><label for="name">输入路径:</label></td>
					<td><input class="easyui-validatebox" type="text"
						id="resolveFileId" data-options="required:true"  />
					</td>
					
				</tr>
				<tr>
					<td></td>
					<td>
					<a id="resolveId" href="" class="easyui-linkbutton" data-options="iconCls:'icon-door_in'">解析入库</a>
					</td>
				</tr>
				
			</table>
		</div>
	<script type="text/javascript" src="js/preprocess.js"></script>
</body>

