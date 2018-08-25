<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/style.css">
<title>登录jsp</title>
</head>
<body>
	<h6>${message}</h1>
	<div style="float:left;block;">
		<form method="post" action="/ddd/jsp/login-action1.do">
			<div>
				<h2>登录 login-action1.do</h2>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
		
		<form method="post" action="/ddd/jsp/login-action2.do">
			<div>
				<h2>登录 login-action2.do</h2>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
		
		<form method="post" action="/ddd/jsp/login-action3.do">
			<div>
				<h2>登录 login-action3.do</h2>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
	</div>
	
	<div  style="float:left;block;">
		<form method="post" action="/ddd/jsp/login-action4.do">
			<div>
				<h2>登录 login-action4.do</h2>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
		
		<form method="post" action="/ddd/jsp/login-action5.do">
			<div>
				<h2>登录 login-action5.do</h2>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
		
		<form method="post" action="/ddd/jsp/login-action6.do">
			<div>
				<h2>登录 login-action6.do</h2>
				<p>${next}</p>
				<p><label>用户</label><input type="text" name="name"></p>
				<p><label>密码</label><input type="password" name="pwd"></p>
				<h3><input type="submit" value="登录"></h3>
			</div>
		</form>
	</div>
</body>
</html>