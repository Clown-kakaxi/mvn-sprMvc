<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <link rel="stylesheet" type="text/css" href="../css/style.css"> -->
<title>上传图片</title>
</head>
<body>
	<h6>${errors}</h1>
	<form action="/ddd/jsp/upload.do" method="post">
		<input type="file" name="file"/>
		<input type="submit" value="上传"/>
	</form>
	<hr>
</body>
</html>