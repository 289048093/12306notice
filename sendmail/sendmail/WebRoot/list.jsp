<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="lizhao.entity.UserEntity"%>
<%@page import="lizhao.Constant"%>
<%@page import="lizhao.Scheduler"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>list</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="js/jquery-1.7.2.min.js">
</script>
		<script type="text/javascript" src="list.js">
</script>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<%
		    Collection<UserEntity> users = Scheduler.getUsers().values();//(Collection<UserEntity>) request.getAttribute("users");
		%>
		<table>
			<tr>
				<td width="200px">
					username
				</td>
				<td width="100px">
					randCode
				</td>
				<td width="100px">errorMsg</td>
				<td width="100px">
					input
				</td>
				<td width="100px">
					operate
				</td>
			</tr>
		</table>
			<%
			    Iterator<UserEntity> it = users.iterator();
			    UserEntity user = null;
			    while (it.hasNext()) {
			        user = it.next();
			%>

		<form action="login.do" method="post">
			<table>
				<tr>
					<td width="200px"><%=user.getUsername()%></td>
					<td width="100px">
						<input type="hidden" name="username"
							value="<%=user.getUsername()%>">
						<%
						    if (user.getStatus() == Constant.USER_STATUS_NO_LOGIN) {
						%>
						<img src="<%=user.getRandCode()%>" />
						<%
						    } else {
						            out.print("已登录。。。");
						        }
						%>
					</td>
					<td width="100px"><%out.write(user.getErrorMsg()); %></td>
					<td width="100px">
						<%
						    if (user.getStatus() == Constant.USER_STATUS_NO_LOGIN) {
						%>
						<input name="randCode" type="text" />
						<%
						    } else {
						            out.print("已登录。。。");
						        }
						%>
					</td>
					<td width="100px">
						<%
						    if (user.getStatus() == Constant.USER_STATUS_NO_LOGIN) {
						%>
						<input type="submit" value="登录" />
						<%
						    } else {
						            out.print("已登录。。。");
						        }
						%>
					</td>
				</tr>
			</table>
		</form>
				<%
				    }
				%>
	</body>
</html>
