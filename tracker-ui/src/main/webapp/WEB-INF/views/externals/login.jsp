 <%@page import="java.util.Arrays"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="com.tracker.ui.utils.UserUtils"%>
<%@page import="org.springframework.security.web.WebAttributes"%>
 <%@page import="com.tracker.commons.Constants" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
 <%
 ServletContext sc = request.getSession().getServletContext();
 WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
 String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
 boolean result = Arrays.stream(profiles).anyMatch("sportsman"::equals);
 %>
 
<!-- begin:: Page -->
<div class="m-grid m-grid--hor m-grid--root m-page">
	<div class="m-grid__item m-grid__item--fluid m-grid m-grid--hor m-login m-login--signin m-login--2 m-login-2--skin-2" id="m_login" style="background-image: url(resources/external_assests/app/media/img/bg/bg-3.jpg);">
		<div class="m-grid__item m-grid__item--fluid	m-login__wrapper">
			<div class="m-login__container">
				
				<div class="m-login__logo">
					<a href="#">
					<%if(result){ %>
						<img src="custom_resources/images/Ambassador_Track.png" width="50%" height="50%">
					<%}else{ %>
						<img src="custom_resources/images/bluelogofront.png" width="50%" height="50%">
					<%} %>
						
					</a>
				</div>
				
				<div class="alert alert-danger" role="alert" id="alertId" style="display:none;"> </div>
				<div class="alert alert-success" role="alert" id="successAlertId" style="display:none;"></div> 
				
				<div class="m-login__signin">
					<div class="m-login__head">
						<h3 class="m-login__title">Sign In</h3>
					</div> 
					
					<form id="loginForm" action="/doLogin" class="m-login__form m-form" method="post" novalidate>
					    
					    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					
						<div class="form-group m-form__group">
							<input class="form-control m-input" type="text" placeholder="Email" name="username" id="username" autocomplete="off">
						</div>
						<div class="form-group m-form__group">
							<input class="form-control m-input m-login__form-input--last" type="password" placeholder="Password" name="password" id="password">
						</div>
						<div class="row m-login__form-sub">
							<div class="col m--align-left m-login__form-left">
								<label class="m-checkbox  m-checkbox--focus">
									<input type="checkbox" id="rememberChkBox" name="rememberChkBox"> Remember me
									<span></span>
								</label>
							</div>
							<div class="col m--align-right m-login__form-right">
								<a href="javascript:;" id="m_login_forget_password" class="m-link">Forget Password ?</a>
							</div>
						</div>
						<div class="m-login__form-action">
							<button id="m_login_signin_submit" class="btn btn-focus m-btn m-btn--pill m-btn--custom m-btn--air m-login__btn m-login__btn--primary">Sign In</button>
						</div>
					</form>
				</div>
				<div class="m-login__signup">
					<div class="m-login__head">
						<h3 class="m-login__title">Sign Up</h3>
						<div class="m-login__desc">Enter your details to create your account:</div>
					</div>
					<form class="m-login__form m-form" action="">
						<div class="form-group m-form__group">
							<input class="form-control m-input" type="text" placeholder="Fullname" name="fullname">
						</div>
						<div class="form-group m-form__group">
							<input class="form-control m-input" type="text" placeholder="Email" name="email" autocomplete="off">
						</div>
						<div class="form-group m-form__group">
							<input class="form-control m-input" type="password" placeholder="Password" name="password">
						</div>
						<div class="form-group m-form__group">
							<input class="form-control m-input m-login__form-input--last" type="password" placeholder="Confirm Password" name="rpassword">
						</div>
						<div class="row form-group m-form__group m-login__form-sub">
							<div class="col m--align-left">
								<label class="m-checkbox m-checkbox--focus">
									<input type="checkbox" name="agree">I Agree the
									<a href="#" class="m-link m-link--focus">terms and conditions</a>.
									<span></span>
								</label>
								<span class="m-form__help"></span>
							</div>
						</div>
						<div class="m-login__form-action">
							<button id="m_login_signup_submit" class="btn btn-focus m-btn m-btn--pill m-btn--custom m-btn--air  m-login__btn">Sign Up</button>&nbsp;&nbsp;
							<button id="m_login_signup_cancel" class="btn btn-outline-focus m-btn m-btn--pill m-btn--custom  m-login__btn">Cancel</button>
						</div>
					</form>
				</div>
				<div class="m-login__forget-password">
					<div class="m-login__head">
						<h3 class="m-login__title">Forgotten Password ?</h3>
						<div class="m-login__desc">Enter your email to reset your password:</div>
					</div>
					<form class="m-login__form m-form" id="forgotPasswordForm" action="#" method="POST">
					 
                				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            
						<div class="form-group m-form__group">
							<input class="form-control m-input" type="text" placeholder="Email" name="email" id="m_email" autocomplete="off">
						</div>
						<div class="m-login__form-action">
							<button id="m_login_forget_password_submit" class="btn btn-focus m-btn m-btn--pill m-btn--custom m-btn--air  m-login__btn m-login__btn--primaryr">Request</button>&nbsp;&nbsp;
							<button id="m_login_forget_password_cancel" class="btn btn-outline-focus m-btn m-btn--pill m-btn--custom m-login__btn">Cancel</button>
						</div>
					</form>
				</div>				
			</div>
		</div>
	</div>
</div>

<!-- end:: Page -->



<script type="text/javascript">
 
	$(document).ready(function() {
		
		<% if(request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) { %>		   					 
			//showErrorOnDiv("<%= ((Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() %>", 'alertId');
	 	<% } %> 
	 
	 	<% if(request.getAttribute(Constants.ERROR_ATTRIBUTE) != null) { %>		   					 
	 		showErrorOnDiv("<%= ((String) request.getAttribute(Constants.ERROR_ATTRIBUTE)) %>", 'alertId');
	 	<% } %> 
	 	
	 	<% if(request.getAttribute(Constants.MESSAGE_ATTRIBUTE) != null) { %>		   					 
	 		showMessageOnDiv("<%= ((String) request.getAttribute(Constants.MESSAGE_ATTRIBUTE)) %>", "successAlertId"); 
	 	<% } %>
	 	
	 	
        checkRememberMe();
	});
	
    function checkRememberMe() {
        if (localStorage.chkbox && localStorage.chkbox !== '') {
            $('#rememberChkBox').attr('checked', 'checked');
            $('#username').val(localStorage.username);
            $('#password').val(localStorage.pass);
        } 
        else {
            $('#rememberChkBox').removeAttr('checked');
            $('#username').val('');
            $('#password').val('');
        }

        $('#rememberChkBox').click(function () {
            if ($('#rememberChkBox').is(':checked')) {
                // save username and password
                localStorage.username = $('#username').val();
                localStorage.pass = $('#password').val();
                localStorage.chkbox = $('#rememberChkBox').val();
            } 
            else {
                localStorage.username = '';
                localStorage.pass = '';
                localStorage.chkbox = '';
            }
        });
    }
	
	var SnippetLogin = function() {
	    var e = $("#m_login"),
	        displayAlert = function(e, i, a) {
		    	var l = $('<div class="m-alert alert alert-' + i + ' alert-dismissible" role="alert">\t\t\t\t\t\t<span></span>\t\t</div>');
	            e.find(".alert").remove(), 
	            l.prependTo(e), 
	            mUtil.animateClass(l[0], "fadeIn animated"), 
	            l.find("span").html(a),
	            setTimeout('$("#m_login").find(".alert").remove();', 6000);
	        },
	        resetAndFlipbackToLoginPage = function() {
	            e.removeClass("m-login--forget-password"), 
	            e.removeClass("m-login--signup"), 
	            e.addClass("m-login--signin"), 
	            mUtil.animateClass(e.find(".m-login__signin")[0], "flipInX animated")
	        },
	        initializeFunc = function() {
	            $("#m_login_forget_password").click(function(i) {
	                i.preventDefault(), 
	                e.removeClass("m-login--signin"), 
	                e.removeClass("m-login--signup"), 
	                e.addClass("m-login--forget-password"), 
	                mUtil.animateClass(e.find(".m-login__forget-password")[0], "flipInX animated")
	            }), 
	            $("#m_login_forget_password_cancel").click(function(e) {
	                e.preventDefault(), 
	                resetAndFlipbackToLoginPage()
	            }), 
	            $("#m_login_signup").click(function(i) {
	                i.preventDefault(), 
	                e.removeClass("m-login--forget-password"), 
	                e.removeClass("m-login--signin"), 
	                e.addClass("m-login--signup"), 
	                mUtil.animateClass(e.find(".m-login__signup")[0], "flipInX animated")
	            }), 
	            $("#m_login_signup_cancel").click(function(e) {
	                e.preventDefault(), 
	                resetAndFlipbackToLoginPage()
	            })
	        };
	    return {
	        init: function() {
	        	initializeFunc(), 
	        	
	            $("#m_login_signin_submit").click(function(e) {
	                e.preventDefault();
	                
	                var loginSubmitButton = $(this),
	                loginForm = $(this).closest("form");
	                
	                loginForm.validate({
	                    rules: {
	                        email: {
	                            required: !0,
	                            email: !0
	                        },
	                        password: {
	                            required: !0
	                        }
	                    }
	                }),
	                
	                loginForm.valid() && 
	                (
	                	loginSubmitButton.addClass("m-loader m-loader--right m-loader--light").attr("disabled", !0), 
		                loginForm.ajaxSubmit({
		                    url: "<%=request.getContextPath()%>/doLogin",
		                    success: function(msg, httpStatus, r, s) {
			                    loginSubmitButton.removeClass("m-loader m-loader--right m-loader--light").attr("disabled", !1); 
		                        
			                    if(httpStatus === 'success') {
			                        if(msg.status === 'success') {		    		   
			                            localStorage.username = $('#username').val();
			                            localStorage.pass = $('#password').val();
			                            localStorage.chkbox = $('#rememberChkBox').val();
			                        	window.location = '<%=request.getContextPath()%>/dashboard'; 						
				  					}
				  					else {
			                             localStorage.username = '';
			                             localStorage.pass = '';
			                             localStorage.chkbox = '';
				  						displayAlert(loginForm, "danger", msg.message);
				  					} 
		                        }
		                        else {
		                        	displayAlert(loginForm, "danger", "There was an error submitting your request. Please retry again.");
		                        } 
		                    }
		                }) 
		           )
	            }),
	            
	            $("#m_login_forget_password_submit").click(function(l) {
	                
	            	l.preventDefault();
	                
	                var forgetPasswordSubmitButton = $(this),
	                forgetPasswordForm = $(this).closest("form");
	                
	                forgetPasswordForm.validate({
	                    rules: {
	                        email: {
	                            required: !0,
	                            email: !0
	                        }
	                    }
	                }),
	                
	                forgetPasswordForm.valid() && 
	                (
	                	forgetPasswordSubmitButton.addClass("m-loader m-loader--right m-loader--light").attr("disabled", !0), 
		                forgetPasswordForm.ajaxSubmit({
		                    url: "<%=request.getContextPath()%>/forgotPasswordSendEmail",
		                    success: function(msg, httpStatus, r, s) {
	                        	forgetPasswordSubmitButton.removeClass("m-loader m-loader--right m-loader--light").attr("disabled", !1);
	                        	
	                        	if(httpStatus === 'success') {
			                        if(msg === 'success') {		    		   
			                        	forgetPasswordForm.clearForm();
			                            forgetPasswordForm.validate().resetForm(); 
			                            resetAndFlipbackToLoginPage();
			                            
			                            var loginForm = e.find(".m-login__signin form");
			                            loginForm.clearForm(); 
			                            loginForm.validate().resetForm();
			                             
			                            displayAlert(loginForm, "success", "A password reset email has been sent. Please check your email to proceed further.");					
				  					}
				  					else {
				  						displayAlert(forgetPasswordForm, "danger", msg.message);
				  					} 
		                        }
		                        else {
		                        	displayAlert(forgetPasswordForm, "danger", "There was an error submitting your request. Please retry again.");
		                        }  
		                    }
		                })
		             )  
	            })
	        }
	    }
	}();
	$(document).ready(function() {
	   SnippetLogin.init()
	});

</script>