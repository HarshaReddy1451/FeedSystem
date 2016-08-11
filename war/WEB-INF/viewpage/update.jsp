<%@page import="java.io.PrintWriter"%>
<%@ page import="javax.servlet.*" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>FeedSystem - Feeds</title>
<link rel="stylesheet" type="text/css" href="home.css">
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() 
	{
		$("#usersList").hide();
		$.ajax({
			url :'/getUsers',
			datatype:'json',
			success: function(response)
			{
				if(response!=null)
				{
					console.log(response +"is the Response.");
					parsedResponse=JSON.parse(response);
					console.log("Parsed Response:"+parsedResponse);
					console.log("length: "+parsedResponse.length);
					var classNameArray= new Array();
					var className=new Array();
					for(var i=0;i<parsedResponse.length;i++)
					{
						className[i]=parsedResponse[i].signUpUserName.trim("");
						$("#displayUsers").append("<div class='users'>"+parsedResponse[i].signUpUserName+"<div class="+className[i]+">("+parsedResponse[i].signUpEmail+")</div></div><br>");
						$("."+className[i]).hide();
						console.log("ClassNames: "+className[i]);
						console.log("ClassNames Length: "+className.length);
						for(var j=0;j<className.length;j++)
						{
							if(className[j]===className[j+1])
							{
								classNameArray[j]=className[j+1];
								console.log("ClassNameArray: "+classNameArray[j]);
								console.log("ClassNameArray Length: "+classNameArray.length);
								$("."+classNameArray[j]).show();
							}
						}
					}
				}
			}
		});
		$("#search").keyup(function(){
			var searchText=$("#search").val();
			var searchResults=[];
			var searchResultsList='';
			if(searchText!=null)
			{
				for(var j=0;j<parsedResponse.length;j++)
				{
					console.log("UserNames: "+parsedResponse[j].signUpUserName);
					if(searchText==searchText.toUpperCase())
					{
						if((parsedResponse[j].signUpUserName).includes(searchText))
						{
							searchResults[j]=(parsedResponse[j].signUpUserName);
						}
					}
					if(searchText==searchText.toLowerCase())
					{
						var name=(parsedResponse[j].signUpUserName).toLowerCase();
						if(name.includes(searchText))
						{
							searchResults[j]=name.charAt(0).toUpperCase()+name.slice(1);;
						}
					}
					console.log("Users:"+searchResults);
				}
			}
			if(searchResults!=null)
			{
				for(var k=0;k<searchResults.length;k++)
				{
					if(searchResults[k]!=null)
					{
						console.log("Users Users: "+searchResults[k]);
						searchResultsList=searchResultsList.concat("<li>"+searchResults[k]+"</li>");
					}
				}
				if(searchResultsList==""){
					$("#usersList ul").html("No result");
					$("#usersList").show();
				}else{
					console.log("searchList: "+searchResultsList)
					$("#usersList ul").html(searchResultsList);
					$("#usersList").show();
				}
			}
			if($("#search").val()=="")
			{
				$("#usersList ul").html("");
				console.log("Users:"+searchResults);
				$("#usersList").hide();
			}
		});
		$.ajax({
			url:'/fetchUpdates',
			datatype:'json',
			success: function(feeds){
				console.log(feeds);
				var parsingResponse=JSON.parse(feeds);
				console.log("parseingResponse: "+parsingResponse);
				console.log(parsingResponse.length);
				for(var i=0; i<parsingResponse.length;i++)
				{
					console.log("Long value:"+parsingResponse[i].date);
					var istDate = (new Date(parsingResponse[i].date)).toUTCString();
					console.log("ISTDate: "+ istDate);
				    var date = new Date(istDate);
				    console.log("Date: "+date);
					var newIstDate = date.toString();
					newIstDate=newIstDate.split(' ').slice(0,5).join(' ');
					console.log("newISTDate; "+newIstDate);
					$("#container").append("<div class='feeds'>"+ "<h4>"+ parsingResponse[i].userName+ "</h4>"+"<p>"+ parsingResponse[i].feed+" "+newIstDate+"</p><div>");
				}
			}
		});
		$("#button_update").click(function() {
			var feed = $("#feedTextId").val();
			var userName = $("#username").text();
			var userMail = $("#mailId").text();
			console.log("UserMail: "+userMail);
			if (feed != "") {
					$.ajax({
						url : '/updateservlet',
						type : 'post',
						dataType : 'json',
						data : {"userName" : userName,"feed" : feed,"userMail":userMail},
						success : function(data) 
						{
							if (data!= "") 
							{
								console.log(data);
								var data1=data[0];
								var data2=data[1];
								//var data3=Number(data[2]);
								var istDate1 = (new Date(Number(data[2]))).toUTCString();	
							    var date1 = new Date(istDate1);
								var newIstDate1 = date1.toString();
								newIstDate1=newIstDate1.split(' ').slice(0, 5).join(' ');
								$("#container").prepend("<div class='feeds'>"+ "<h4>"+ data1+ "</h4>"+ "<p>"+ data2+" "+newIstDate1+"</p><div>");
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
		response.setHeader("Cache-Control", "no-cache,no-store, must-revalidate"); //Forces caches to obtain a new copy of the page from the origin server 
		response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance 
		response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale" 
		response.setHeader("Pragma", "no-cache");
	%>
	<%
		System.out.println(session.getAttribute("name"));
		if(session.getAttribute("name")==null)
		{
			response.sendRedirect("/");
		}
	%>
</head>
<body background="bgimg4.jpg" id="update_body"
	background="C:\Users\User\Desktop\HTML Programs\Proj\bgimg4.jpg">
	<button onclick="location.href='/logout'" class="logout" width="48"
		height="48">Logout</button>
	<hr>
	<p id='username'><strong>Welcome:<% String userName=(String) session.getAttribute("name");out.println(userName);%></strong></p>
	<p id='mailId'><% String mailId=(String) session.getAttribute("mail");out.println(mailId);%></p>
	<input type="text" name="search" id="search" placeholder="Search..."
		autocomplete="on" class="search_box" />
		<div id="usersList">
			<ul></ul>
		</div>
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