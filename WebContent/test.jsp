<%@ page language="java" contentType="text/html; charset=UTF-8"
	session="false" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>恍恍惚惚教务处</title>
</head>
<style type="text/css">
body{
	color: #000;
	font-size: 14px;
	margin: 20px auto;
}
#message{
	text-align: center;
	float: 
}
</style>

<body>

<div id="message" >
	
	本页面只供演示、测试时发post请求使用，实际使用请从客户端发送请求 <br /> <br /> <br /> <br />
	<form action="<%= request.getContextPath() %>/test.do" method="get" >
			 
			<label><input
				name="action" type="radio" value="login" checked="checked" />登陆（先登录再做其他操作） </label> 
			
			<label><input
				name="action" type="radio" value="curSemesterScore" />查当前学期成绩 </label> 
			<label><input
               	name="action" type="radio" value="curSemesterKeBiao" />查当前学期课表 </label>
			<br /><br />
			 <label><input
               	name="action" type="radio" value="curSemesterJieShao" />查当前学期课程介绍 </label>
			<br /><br />
			
			<label><input
               	name="action" type="radio" value="keBiao" />查指定学期课表 </label>
            <label><input
                name="action" type="radio" value="score" />查指定学期成绩 </label>  <br /><br />
            <input
                type="submit" value="提交" onclick="return check_locally(this)" />
            <br /><br />
           
            
	</form>
	
	<%	//返回的结果
		Object obj = request.getAttribute("result");
		if( obj != null){
			out.println(obj.toString() + "<br /><br /><br />");
		}
	%>
</div>

</body>
</html>