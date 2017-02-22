<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<!DOCTYPE html>
<html data-ng-app="app">
<head>
<meta charset="UTF-8">
<!--兼容ie：让IE运行最新渲染模式或者采用chrome的渲染模式-->
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
<!--兼容ie：让IE运行ie11渲染模式-->
<meta http-equiv="X-UA-Compatible" content="IE=11" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet"
	href="/tdata/assets/js/plugins/bootstrap/dist/css/bootstrap.css"
	type="text/css" />
<link rel="stylesheet"
	href="/tdata/assets/js/plugins/font-awesome/css/font-awesome.min.css"
	type="text/css" />
<link rel="stylesheet" href="/tdata/assets/css/app.css" type="text/css" />
<link rel="stylesheet" href="/tdata/assets/css/font.css" />
<link rel="stylesheet" href="/tdata/assets/css/animate.min.css" />
<link rel="stylesheet" href="/tdata/assets/css/angular-aside.css" />
<style>
.form-control-line {
	width: 45%;
	display: inline;
}
</style>
<title>首页</title>
<!-- 登录错误 -->
<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
</head>
<body>
	<div class="container w-xxl w-auto-xs"
		ng-init="app.settings.container = false;">
		<a href class="navbar-brand block m-t">管理系统</a>
		<div class="m-b-lg">
			<div class="wrapper text-center">
				<strong>请你登录</strong>
			</div>
			<div class="wrapper text-center text-danger">
				<%=error==null?"":"com.web.security.CaptchaException".equals(error)?"验证码错误，请重试！":"密码或者用户名错误，请重新输入！" %>
			</div>
			<form name="form" action="login" method="post" id="loginForm"
				class="form-validation">
				<div class="text-danger wrapper text-center"></div>
				<div class="list-group list-group-sm">
					<div class="list-group-item">
						<input type="text" name="username" placeholder="Name"
							class="form-control no-border" ng-model="账号" required>
					</div>
					<div class="list-group-item">
						<input type="password" placeholder="密码" name="password"
							class="form-control  no-border" ng-model="password" required>
					</div>
					<div class="list-group-item">
						<img class="form-control form-control-line no-border"
							id="validateImg"
							src= "/tdata/ValidateCodeServlet" 
							title="换一张"
							onClick="this.src='/tdata/ValidateCodeServlet?'+ new Date().getTime();"
							 />
						<input class="form-control form-control-line no-border"
							type="text" name="validateCode" id="validateCode"
							class="form-control" placeholder="验证码"  />
					</div>
				</div>
				<button type="submit" class="btn btn-lg btn-primary btn-block">登录</button>
			</form>
		</div>
		
	</div>
	
</body>
</html>