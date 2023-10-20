
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="resources/img/favicon.png">
    <title>Coinxoom.com - Register new user</title>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,400italic,700,800" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Raleway:300,200,100" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300,700" rel="stylesheet" type="text/css">
    <link href="resources/jslibs/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="resources/jslibs/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="resources/jslibs/jquery.nanoscroller/css/nanoscroller.css">
    <link href="resources/css/style.css" rel="stylesheet">
  </head>
  
  <style>
  
  	.overlay {
	    background: #e9e9e9;   
	    display: none;         
	    position: absolute;    
	    top: 0;                   
	    right: 0;                 
	    bottom: 0;
	    left: 0;
	    opacity: 0.5;
	    z-index: 1001;
	}
  </style>
  
  <body class="texture">
    <div id="cl-wrapper" class="sign-up-container">
      <div class="middle-sign-up">
        <div class="block-flat">
          
          	<div class="header">
          		<h3 class="text-center"><img src="resources/img/logo.png" alt="logo" class="logo-img"> Coinxoom</h3>
          	</div>
          
          <div>
              <div style="margin-bottom: 0px !important;" parsley-validate="" novalidate="" class="form-horizontal">
                  
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  
                  <div class="content">
                  	   <div class="overlay">
					  		<div id="loading-img"> <img src="resources/img/spinner.svg" /> </div>
					  </div>
					  
					  <div id="sucessErrorMsgDiv"></div>  
                  
                  	  <h6 class="title text-center" style="margin-bottom: 0px;"><strong>Sign Up</strong></h6>
                      
                       <div class="form-group">
		             		<div class="col-md-6 col-sm-6 col-xs-12">
                               	<form action="/connect/facebook" method="POST">
									<input type="hidden" name="scope" value="user_posts,email" /> 
									<input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}" />
									<button type="submit" class="btn btn-block btn-trans btn-facebook bg btn-rad"><i class="fa fa-facebook"></i> Sign in with Facebook</button>
							  	</form>
                         	</div>
		                
		               		<div class="col-md-6 col-sm-6 col-xs-12">
                          		<form action="/connect/twitter" method="POST">
									<input type="hidden" name="scope" value="user_posts,email" /> 
									<input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}" />
									<button type="submit" class="btn btn-block btn-trans btn-twitter bg btn-rad"><i class="fa fa-twitter"></i> Sign in with Twitter</button>
							  	</form>
                          </div>
                      </div>
                      
                      <hr>
                      
                      <form action="#" id="registerUserForm" parsley-validate="" novalidate="" method="POST">
              				
              			  <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}" />
              				
	                      <div class="form-group">
	                          <div class="col-sm-12">
	                              <div class="input-group">
	                                  <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
	                                  <input type="email" name="email" data-parsley-trigger="change" data-parsley-errors-container="#email-error" required="" data-parsley-required-message="Please enter your email address." placeholder="E-mail" class="form-control">
	                              </div>
	                              <div id="email-error"></div>
	                          </div>
	                      </div>
	                      
	                      <div class="form-group">
	                          <div class="col-sm-6 col-xs-12">
	                              <div class="input-group">
	                                  <span class="input-group-addon"><i class="fa fa-lock"></i></span>
	                                  <input id="pass1" type="password" name="password" data-parsley-errors-container="#password-error" placeholder="Password" required="" 
	                                  	data-parsley-minlength="8" data-parsley-required-message="Please enter your new password." data-parsley-pattern="<%=Constants.PASSWORD_VALIDATION_REGEX %>"
	                                    data-parsley-pattern-message="Your password must contain at least (1) lowercase, (1) uppercase, (1) digit, (1) special character and (8) characters long" data-parsley-required
	                                  	class="form-control">
	                              </div>
	                              <div id="password-error"></div>
	                          </div>
	                          
	                          <div class="col-sm-6 col-xs-12">
	                              <div class="input-group">
	                                  <span class="input-group-addon"><i class="fa fa-lock"></i></span>
	                                  <input data-parsley-equalto="#pass1" name="confirmPassword" data-parsley-equalto-message="Confirm password doesn't match with the original password field" type="password" data-parsley-errors-container="#confirmation-error" required="" data-parsley-required-message="Please confirm the password." placeholder="Confirm Password" class="form-control">
	                              </div>
	                              <div id="confirmation-error"></div>
	                          </div>
	                      </div>
	                      
	                      <div class="form-group">
	                          <div class="col-sm-6 col-xs-12">
	                              <div class="input-group">
	                                  <span class="input-group-addon"><i class="fa fa-user"></i></span>
	                                  <input id="firstName" type="text" name="firstName" data-parsley-errors-container="#firstName-error" data-parsley-required-message="Please enter your first name." placeholder="First Name" required="" class="form-control">
	                              </div>
	                              <div id="firstName-error"></div>
	                          </div>
	                          
	                          <div class="col-sm-6 col-xs-12">
	                              <div class="input-group">
	                                  <span class="input-group-addon"><i class="fa fa-user"></i></span>
	                                  <input id="lastName" type="text" name="lastName" type="text" data-parsley-errors-container="#lastName-error" data-parsley-required-message="Please enter your last name." required="" placeholder="last Name" class="form-control">
	                              </div>
	                              <div id="lastName-error"></div>
	                          </div>
	                      </div>
	                      
	                      <p class="spacer">By creating an account, you agree with the <a href="#">Terms</a> and <a href="#">Conditions</a>.</p>
	                      <button type="submit" class="btn btn-block btn-success btn-rad btn-lg" onclick="return onSubmit();">Sign Up</button>
	                      
                      </form>
                      
                      <p class="spacer">Already have an account? <a href="/login">Login from here</a>.</p>
                      
                  </div>
              </div>
          </div>
        </div>
        <div class="text-center out-links"><a href="#">&copy; <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> Coinxoom.com All rights reserved.</a></div>
      </div>
    </div>
    <script type="text/javascript" src="resources/jslibs/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="resources/jslibs/jquery.nanoscroller/javascripts/jquery.nanoscroller.js"></script>
    <script type="text/javascript" src="resources/js/cleanzone.js"></script>
    <script src="resources/jslibs/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="resources/js/voice-recognition.js"></script>
    <script src="resources/jslibs/jquery.parsley/dist/parsley.min.js" type="text/javascript"></script>
    <script src="resources/js/page-signup.js" type="text/javascript"></script>
    <script src="resources/js/custom.js" type="text/javascript"></script>
    <script type="text/javascript">
      	$(document).ready(function(){
      		//initialize the javascript
      		App.init();
      		App.pageSignUp();
      
      	});
      
		function onSubmit() {	
    	  
    	  	if($('#registerUserForm').parsley().validate()) {
      			 
				$.ajax({
	   				type		: 'POST',
	   				url			: '<%=request.getContextPath()%>/registerUser',
	   				data		: $('#registerUserForm').serialize(),
	   				async 		: true,
	   				beforeSend	: function(xhr) {					
	   					$("#loading").show();
	   				},
	   				success		: function(msg) {
	   					$("#loading").hide();
	   					
	   					if(msg == 'success') {		    		   
	   						window.location = '<%=request.getContextPath()%>/dashboard'; 						
	   					}
	   					else {
	   						showError(msg.message); 
	   					}
	   				},
	   				error		: function (xhr, ajaxOptions, thrownError) {
	   					$("#loading").hide();
	   					showError("There was an error submitting your request. Please retry again.");
	   				}
	   			});
    	  	}
   			
   			return false;
  		}
    </script>
  </body>
</html>