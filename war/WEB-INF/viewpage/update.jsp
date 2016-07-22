<%@page import="java.io.PrintWriter"%>
<%@ page import="javax.servlet.*" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Updates</title>
<link rel="stylesheet" type="text/css" href="home.css">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript">
			$(document).ready(function(){
				$("#button_update").click(function(){
					var feed=$("#feedTextId").val();
					var userName=$("#username").text();
					if(feed!="")
					{
						$.ajax({
							url : '/updateservlet',
							type: 'post',
							dataType: 'json',
							data: {"userName":userName,"feed":feed},
							success: function(data) {
								if(data!="")
								{
									console.log(data.length);
									console.log(data);
									var data1=data[0];
									var data2=data[1];
									var data3=data[2];
									console.log(data1);
									console.log(data2);
									console.log(data3);
									$("#container").prepend("<div id='feeds'>"+"<h4>"+data1+"</h4>"+"<p>"+data2+" on "+"<small>"+data3+"</small>"+"</p>"+"<div>");
									$("#feedTextId").val("");
								}
				            }
						});
					}
					else
					{
						alert("Feed should not empty.");	
					}
				});
			});
		</script>
</head>
<body background="bgimg4.jpg" id="update_body"
	background="C:\Users\User\Desktop\HTML Programs\Proj\bgimg4.jpg">
	<button onclick="location.href='/logout'" class="logout" width="48"
		height="48">Logout</button>
	<hr>
	<%
		String userName =(String) request.getAttribute("userName");
		out.println("<p id='username'><strong>Welcome: ");
		out.println(userName);
		out.println("</strong></p>");
	%>
	<input type="text" name="search" placeholder="Search..."
		class="search_box" />
	<button class="search_button">
		<strong>Search</strong>
	</button>
	<table>
		<tr>
			<td><textarea id="feedTextId" name="feedText" rows="2" cols="50"
					placeholder="Hi, you can update feeds here.."></textarea></td>
		</tr>
		<tr>
			<td><button id="button_update">
					<strong>UpdateFeed</strong>
				</button></td>
		</tr>
	</table>
	<!-- <h3 id="allUsers">All Users</h3> -->
	<div id="displayUsers">
		<h4>Harsha</h4>
	</div>
	<h3>All Updates</h3>
	<div id="container">
	</div>
</body>
</html>