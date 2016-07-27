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
	$(document).ready(function() {
		$.ajax({
			url :'/getUsers',
			datatype:'json',
			success: function(response){
				if(response!=null)
				{
					var splitingNames=response.split(",");
					console.log(splitingNames);
					console.log(response +"is the Response.");
					var parsedResponse=JSON.parse(splitingNames);
					console.log(parsedResponse);
					for(i=0;i<parsedResponse.length;i++)
					{
						$("#displayUsers").prepend("<div id='users'>"+parsedResponse[i]+"</div>");
					}
				}
			}
		});
		$.ajax({
			url:'/fetchUpdates',
			datatype:'json',
			success: function(feeds){
				console.log(feeds);
				var splitingTheResponse=feeds.split(",");
				console.log(splitingTheResponse);
				var parsingResponse=JSON.parse(splitingTheResponse);
				console.log(parsingResponse);
				for (i = 0; i < parsingResponse.length; i++) 
				{
					/* var istDate = (new Date(parsingResponse[i].date)).toUTCString();	
				    var date = new Date(istDate);
					var newIstDate = date.toString();
					newIstDate=newIstDate.split(' ').slice(0, 5).join(' '); */
					$("#container").prepend("<div id='feeds'>"+ "<h4>"+ parsingResponse[i]+ "</h4>"+ "<p>"+ parsingResponse[i+1]+ "<div>");
				}
			}
		});
		$("#button_update").click(function() {
			var feed = $("#feedTextId").val();
			var userName = $("#username").text();
			if (feed != "") {
					$.ajax({
						url : '/updateservlet',
						type : 'post',
						dataType : 'json',
						data : {"userName" : userName,"feed" : feed},
						success : function(data) 
						{
							if (data!= "") 
							{
								console.log(data);
								var data1=data[0];
								var data2=data[1];
								var data3=data[2];
								$("#container").prepend("<div id='feeds'>"+ "<h4>"+ data2+ "</h4>"+ "<p>"+ data1+ "<div>");
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
	<%
		response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server 
		response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance 
		response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale" 
		response.setHeader("Pragma", "no-cache");
	%>
	<%
		if(session.getAttribute("name")==null)
		{
			response.sendRedirect("/logout");
		}
	%>
</head>
<body background="bgimg4.jpg" id="update_body"
	background="C:\Users\User\Desktop\HTML Programs\Proj\bgimg4.jpg">
	<button onclick="location.href='/logout'" class="logout" width="48"
		height="48">Logout</button>
	<hr>
	<p id='username'>
		<strong>Welcome:<% String userName=(String) session.getAttribute("name");
		out.println(userName);%></strong>
	</p>
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
	</div>
	<h3>All Updates</h3>
	<div id="container"></div>
</body>
</html>