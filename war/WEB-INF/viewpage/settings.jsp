<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="home.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Settings</title>
</head>
<body background="bgimg4.jpg">
	<button onclick="location.href='/logout'" class="logout" width="48"
		height="48">Logout</button>
	<hr>
	<%
		String name=request.getParameter("name");
		String mail=request.getParameter("mail");
		String userMailCopy = mail;
		char[] tempVariable = userMailCopy.toCharArray();
		char[] userMailCharArray = new char[tempVariable.length - 2];
		for (int i = 0; i <= userMailCharArray.length - 2; i++) {
			userMailCharArray[i] = tempVariable[i];
		}
		char[] userMailCharArray1 = new char[userMailCharArray.length - 1];
		for (int i = 0; i < userMailCharArray.length - 2; i++) {
			userMailCharArray1[i] = userMailCharArray[i];
		}
		out.println("<div id='settingsDiv'>");
		out.println("<p> UserName:");
		out.println(name);
		out.println("</p>");
		out.println("<p> MailId:");
		out.println(userMailCharArray1);
		out.println("</p>");
		out.println("</div>");
	%>
</body>
</html>