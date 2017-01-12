<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="jquery-1.10.2.min.js"></script>
<script type="text/javascript">

function test(){
	$.ajax({
		type : 'post',
		url : '/line/linePo',
		data : '{"id":1}',
		contentType: "application/json; charset=utf-8",
		cache : false,
		dataType : 'json',
		success : function(data) {
			if(data.returnCode==1){
			}else{
				swal("系统提示", data.msg,"error");
			}
		}
	});
}


</script>
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
	<input type="button" onclick="test()" value="登录">
</body>
</html>