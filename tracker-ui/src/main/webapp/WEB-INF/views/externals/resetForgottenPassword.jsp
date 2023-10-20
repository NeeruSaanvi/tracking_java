<%@page import="com.tracker.commons.Constants" %>
<%@page import="org.springframework.security.web.WebAttributes"%>
 
		<!-- begin:: Page -->
		<div class="m-grid m-grid--hor m-grid--root m-page">
			<div class="m-grid__item m-grid__item--fluid m-grid m-grid--hor m-login m-login--signin m-login--2 m-login-2--skin-2" id="m_login" style="background-image: url(resources/external_assests/app/media/img/bg/bg-3.jpg);">
				<div class="m-grid__item m-grid__item--fluid	m-login__wrapper">
					<div class="m-login__container">
						
						<div class="m-login__logo">
							<a href="#">
								<img src="custom_resources/images/bluelogofront.png" width="50%" height="50%">
							</a>
						</div>
						
						<div class="alert alert-danger" role="alert" id="alertId" style="display:none;"> </div>
						<div class="alert alert-success" role="alert" id="successAlertId" style="display:none;"></div> 
						
						<div class="m-login__signin">
							<div class="m-login__head">
								<h3 class="m-login__title">Reset your password</h3>
							</div>
							
							<form id="resetForgottenPasswordForm" action="#" method="POST" class="m-login__form m-form" method="post" novalidate>
							    
							    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							    <input type="hidden" name="email"	value="${user.passwordResetToken}" />
							
								<div class="form-group m-form__group">
									<input class="form-control m-input" type="password" placeholder="Password" name="password" autocomplete="off">
								</div>
								<div class="form-group m-form__group">
									<input class="form-control m-input m-login__form-input--last" type="password" placeholder="Confirm Password" name="confirmPassword">
								</div>
								 
								<div class="m-login__form-action">
									<button id="m_reset_password_submit" onclick="return onSubmit();" 
										class="btn btn-focus m-btn m-btn--pill m-btn--custom m-btn--air m-login__btn m-login__btn--primary">Reset Password</button>
									<button id="m_reset_password_cancel" class="btn btn-outline-focus m-btn m-btn--pill m-btn--custom m-login__btn">Cancel</button>
								</div>
							</form>
						</div>
						 
						 
					</div>
				</div>
			</div>
		</div>
 
		
		<script>
			jQuery(document).ready(function() {
				
				<% if(request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) { %>		   					 
					showErrorOnDiv("<%= ((Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage() %>", 'alertId');
			 	<% } %> 
			 
			 	<% if(request.getAttribute(Constants.ERROR_ATTRIBUTE) != null) { %>		   					 
			 		showErrorOnDiv("<%= ((String) request.getAttribute(Constants.ERROR_ATTRIBUTE)) %>", 'alertId');
			 	<% } %> 
			 	
			 	<% if(request.getAttribute(Constants.MESSAGE_ATTRIBUTE) != null) { %>		   					 
			 		showMessageOnDiv("<%= ((String) request.getAttribute(Constants.MESSAGE_ATTRIBUTE)) %>", "successAlertId"); 
			 	<% } %>
			});
			
			var SnippetLogin = function() {
			    var e = $("#m_login"),
			        displayAlert = function(e, i, a) {
			            var l = $('<div class="m-alert alert alert-' + i + ' alert-dismissible" role="alert">\t\t\t\t\t\t<span></span>\t\t</div>');
			            e.find(".alert").remove(), 
			            l.prependTo(e), 
			            mUtil.animateClass(l[0], "fadeIn animated"), 
			            l.find("span").html(a),
			            setTimeout('$("#m_login").find(".alert").remove();', 4000);
			        },
			        initializeFunc = function() {
			            
			            $("#m_reset_password_cancel").click(function(e) {
			                e.preventDefault(), 
			                window.location = '<%=request.getContextPath()%>/login'; 
			            }) 
			        };
			    return {
			        init: function() {
			        	initializeFunc(), 
			        	
			            $("#m_reset_password_submit").click(function(e) {
			                e.preventDefault();
			                
			                var resetPasswordSubmitButton = $(this),
			                restPasswordForm = $(this).closest("form");
			                
			                restPasswordForm.validate({
			                    rules: {
			                        password: {
			                            required: !0
			                        }
			                    }
			                }),
			                
			                restPasswordForm.valid() && 
			                (
			                	resetPasswordSubmitButton.addClass("m-loader m-loader--right m-loader--light").attr("disabled", !0), 
				                
			                	restPasswordForm.ajaxSubmit({
				                    url: "<%=request.getContextPath()%>/resetForgottenPasswordSubmit",
				                    success: function(msg, httpStatus, r, s) {
					                    resetPasswordSubmitButton.removeClass("m-loader m-loader--right m-loader--light").attr("disabled", !1); 
				                        
					                    if(httpStatus === 'success') {
					                        if(msg === 'success') {		    		   
						  						window.location = '<%=request.getContextPath()%>/dashboard'; 						
						  					}
						  					else {
						  						displayAlert(restPasswordForm, "danger", msg.message);
						  					} 
				                        }
				                        else {
				                        	displayAlert(restPasswordForm, "danger", "There was an error submitting your request. Please retry again.");
				                        } 
				                    }
				                }) 
				           )
			            })
			            
			             
			        }
			    }
			}();
			jQuery(document).ready(function() {
			   SnippetLogin.init()
			});
		
		</script>
      