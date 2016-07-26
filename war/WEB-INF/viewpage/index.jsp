<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
<head>
<title>Home Page</title>
<link rel="stylesheet" type="text/css" href="home.css">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script type="text/javascript">
			$(document).ready(function(){
				$("#signUpButton").click(function(){
					var userName=$("#userNameId").val();
					var password=$("#passwordId").val();
					var confirmPassword=$("#confirmPasswordId").val();
					var email=$("#emailId").val();
					
					var indexAt=email.indexOf('@');
					var dot=email.lastIndexOf('.');
					
					console.log("index: "+ indexAt );
					console.log(userName);
					console.log(password);
					console.log(confirmPassword);
					console.log(email);
					if(userName!="" && password!="" && email!="")
					{
						$.ajax({
							url : '/signup',
							type: 'post',
							 dataType: 'json',
							data: {userName,password,confirmPassword,email},
							success: function(data) {
								if(data!="")
								{
									console.log(" Name: "+data);
									if(data.trim() === email)
									{
										$("#myModal1").modal();
										$("#userNameId").focus();
										$("#signUpFooter").append("<p><strong>User Already Exists.</strong></p>");
									}	
									else if(data.trim() === "false")
									{
										window.location.href = "/signupData";
									}
								}
				            }
						});
					}
					else
					{
						$("#signUpFooter").append("<p><strong>Enter the details properly.</strong></p>");
						$("#userNameId").val("");
						$("#passwordId").val("");
						$("#confirmPasswordId").val("");
						$("#emailId").val("");
						$("#userNameId").focus();
					}
				});
				$("#loginButton").click(function(){
					var email=$("#loginEmailId").val();
					var password=$("#loginPasswordId").val();
					console.log(email);
					console.log(password);
					
					var indexAt=email.indexOf('@');
					var dot=email.lastIndexOf('.');
					console.log("index: "+ indexAt);
					console.log("dotIndex: "+dot);
					if(email!="" && password!="" && indexAt>1 && dot> indexAt+2 && dot+2 < email.length)
					{
						$.ajax({
							url : '/login',
							type: 'post',
							dataType: 'json',
							data: {email,password},
							success: function(data) {
								if(data!="")
								{
									console.log(" email: "+data);
									if(data==email)
									{
										$("#myModal").hide()
										$("#errorModal").modal();	
										$("#register").click(function(){
											$("#errorModal").hide();
											$("#myModal1").modal();
										});
									}
									else if(data.trim() === "false")
									{
										window.location.href = "/update";
									}
								}
				            }
						});
					}
					else
					{
						$("#loginFooter").append("<p><strong>Enter proper emailId.</strong></p>");
						$("#loginEmailId").val("");
						$("#loginPasswordId").val("");
						$("#loginEmailId").focus();
					}
				});
				
			});
		</script>
</head>
<body id="home_body" background="bgimg4.jpg">
	<div class="loginContainer">
		<h2><em>Click here to Share Your Feeds</em></h2>
		<!-- Trigger the modal with a button -->
		<button type="button" class="btn btn-info btn-lg" id="loginModalBtnId" data-toggle="modal"
			data-target="#myModal" id="myBtn">Login</button>
		<div class="modal fade" id="myModal" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Login</h4>
					</div>
					<div class="modal-body">
						<!-- <form method="post" action="/login" class="login_form"> -->
							<table class="login_table">
								<tr>
									<td>EMAIL:</td>
									<td><input type="email" name="username" id="loginEmailId" /></td>
								</tr>
								<tr>
									<td>PASSWORD:</td>
									<td><input type="password" name="password" id="loginPasswordId"/></td>
								</tr>
								<tr>
									<td><input type="submit" value="Login" width="50" id="loginButton"
										height="25" class="login" style="background-color: #5cb85c" /></td>
								</tr>
							</table>
						<!-- </form> -->
					</div>
					<div class="modal-footer" id="loginFooter">
        			</div>
				</div>
			</div>
		</div>
	</div>
	<div class="signUpContainer">
		<!-- Trigger the modal with a button -->
		<h4></h4>
		<button type="button" id="signUpBtn" class="btn btn-info btn-sg" data-toggle="modal"
			data-target="#myModal1"><strong>SignUp</strong></button>
		<div class="modal fade" id="myModal1" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">SignUp</h4>
					</div>
					<div class="modal-body">
						<!-- <form method="post" action="/signup" class="signup_form"> -->
							<table class="signup_table">
								<tr>
									<td>UserName</td>
									<td><input type="text" name="userName" id="userNameId"/></td>
								</tr>
								<tr>
									<td>Password</td>
									<td><input type="password" name="password" id="passwordId"/></td>
								</tr>
								<tr>
									<td>Confirm Password</td>
									<td><input type="password" name="confirmPassword" id="confirmPasswordId" /></td>
								</tr>
								<tr>
									<td>Email</td>
									<td><input type="email" name="email" id="emailId"/></td>
								</tr>
								<tr>
									<td><input type="submit" value="SignUp" width="50" id="signUpButton"
										height="25" class="signup" style="background-color: #5cb85c" /></td>
								</tr>
							</table>
						<!-- </form> -->
					</div>
					<div class="modal-footer" id="signUpFooter">
        			</div>
				</div>
			</div>
		</div>
	</div>
	<div class="errorContainer">
		<div class="modal fade" id="errorModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Modal Header</h4>
        </div>
        <div class="modal-body">
          <p>User doesn't exist. Click on SignUp to get Registered.</p>
          <button id="register">SignUp</button>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
	</div>
</body>
</html>