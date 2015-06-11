<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<script type="text/javascript">
	$(function() {

		var formParam = {
			url : 'login/loginUser_login.action',
			success : function(result) {
			//	var r = $.parseJSON(result);
				//if (r.success) {
				console.info(result);
				if(result=='true'){
					$('#user_login_loginDialog').dialog('close');

				//	$('#sessionInfoDiv').html(formatString('[<strong>{0}</strong>]，欢迎你！', r.obj.loginName));

				//	$('#layout_east_onlineDatagrid').datagrid('load', {});
				} else {
					$.messager.show({
						title : '提示',
						msg : '用户名或密码错误！'
					});
				}
			}
		};

		$('#user_login_loginInputForm').form(formParam);

		

		$('#user_login_loginDialog').show().dialog({
			modal : true,
			title : '系统登录',
			closable : false,
			buttons : [
			// {
			//	text : '注册',
			//	handler : function() {
			//		$('#user_reg_regDialog').dialog('open');
			//	}
			//},
			 {
				text : '登录',
				handler : function() {
					$('#user_login_loginInputForm').submit();
				}
			} ]
		});

		
		var sessionInfo_userId = '${sessionInfo.userId}';
		if (sessionInfo_userId) {/*目的是，如果已经登陆过了，那么刷新页面后也不需要弹出登录窗体*/
			$('#user_login_loginDialog').dialog('close');
		}

	});
</script>
<div id="user_login_loginDialog" style="display: none;width: 300px;height: 210px;overflow: hidden;">
	<div id="user_login_loginTab">
		<div style="overflow: hidden;">
			<div>
				<div class="info">
					<div class="tip icon-tip"></div>
					<div>用户名是"admin"，密码是"admin"</div>
				</div>
			</div>
			<div align="center">
				<form method="post" id="user_login_loginInputForm">
					<table class="tableForm">
						<tr>
							<th>登录名</th>
							<td><input name="username" class="easyui-validatebox" data-options="required:true" value="admin" />
							</td>
						</tr>
						<tr>
							<th>密码</th>
							<td><input type="password" name="password" class="easyui-validatebox" data-options="required:true" style="width: 150px;" value="admin" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>