<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<%
		response.setHeader("token","2222");
    %>
	<form action="${pageContext.request.contextPath}/user/login">
		<input type="text" name="mobileNo">
		<input type="text" name="password">
		<input type="submit" value="登录">
	</form>

    <form action="${pageContext.request.contextPath}/user/toLogin">
		<input type="submit" value="登录">
	</form>
	
	 <form action="${pageContext.request.contextPath}/user/getMobileNo" method="post">
	 	<input type="text" name="mobileNo">
		<input type="submit" value="登录">
	</form>
	
	<form action="${pageContext.request.contextPath}/user/saveUser" method="post">
		<input type="text" name="mobileNo">
		<input type="text" name="password">
		<input type="text" name="validNo">
		<input type="submit" value="登录">
	</form>
	
</body>
</html>