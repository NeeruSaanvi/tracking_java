<!--
Template Name: Metronic - Bootstrap 4 HTML, React, Angular 10 & VueJS Admin Dashboard Theme
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Dribbble: www.dribbble.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: https://1.envato.market/EA4JP
Renew Support: https://1.envato.market/EA4JP
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
https://preview.keenthemes.com/metronic/demo7/index.html 
-->
<%@page import="com.tracker.ui.utils.UserUtils"%>
<%@page import="com.tracker.commons.models.User"%>
 <%@page import="java.util.Arrays"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%
 ServletContext sc = request.getSession().getServletContext();
 WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
 String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
 boolean result = Arrays.stream(profiles).anyMatch("sportsman"::equals);
 User user = UserUtils.getLoggedInUser();
 %>
<!-- begin:: Header -->
					<div id="kt_header" class="kt-header kt-grid kt-grid--ver  kt-header--fixed " style1="background-color: white;" style1="background-color: #736D70;">

						<!-- begin:: Aside -->
						<div class="kt-header__brand kt-grid__item  " id="kt_header_brand" style="background: none !important">
							<div class="kt-header__brand-logo">
								<a href="/dashboard">
									<%if(result){ %>
										<img alt="Logo" src="custom_resources/images/Ambassador_Track_small_logo.png">
									<%}else{ %>
										<img alt="Logo" src="custom_resources/images/logo_small.png">
									<%} %>
								</a>
							</div>
						</div>

						<!-- end:: Aside -->

						<!-- begin:: Title -->
						<h3 class="kt-header__title kt-grid__item" >
							<span class="btn btn-success btn-sm btn-bold btn-font-md" id="reloadData">Reload Data</span>
						</h3>

						<!-- end:: Title -->
 
						<!-- begin:: Header Topbar -->
						<div class="kt-header__topbar">

							<!--begin: User bar -->
							<div class="kt-header__topbar-item kt-header__topbar-item--user">
								<div class="kt-header__topbar-wrapper" data-toggle="dropdown" data-offset="10px,0px">
									<span class="kt-hidden kt-header__topbar-welcome">Hi,</span>
									<span class="kt-hidden kt-header__topbar-username">"<%=user.getLoginName() %>"</span>
									<img class="kt-hidden" alt="Pic" src="<%=user.getAtProfilePic() %>" />
									<span class="kt-header__topbar-icon kt-hidden-"><i class="flaticon2-user-outline-symbol"></i></span>
								</div>
								<div class="dropdown-menu dropdown-menu-fit dropdown-menu-right dropdown-menu-anim dropdown-menu-xl">

									<!--begin: Head -->
									<div class="kt-user-card kt-user-card--skin-dark kt-notification-item-padding-x" style="background-image: url(resources/assets/media/misc/bg-1.jpg)">
										<div class="kt-user-card__avatar">
											<img class="kt-hidden" alt="Pic" src="resources/assets/media/users/300_25.jpg" />

											<!--use below badge element instead the user avatar to display username's first letter(remove kt-hidden class to display it) -->
											<span class="kt-badge kt-badge--lg kt-badge--rounded kt-badge--bold kt-font-success">
												<%=user.getFirstLastName().substring(0, 1).toUpperCase() %>
											</span>
										</div>
										<div class="kt-user-card__name">
											<%=user.getFirstLastName() %>
										</div>
										<div class="kt-user-card__badge">
											<span class="btn btn-success btn-sm btn-bold btn-font-md">0 messages</span>
										</div>
									</div>

									<!--end: Head -->

									<!--begin: Navigation -->
									<div class="kt-notification">
										<!-- <a href="#" class="kt-notification__item">
											<div class="kt-notification__item-icon">
												<i class="flaticon2-calendar-3 kt-font-success"></i>
											</div>
											<div class="kt-notification__item-details">
												<div class="kt-notification__item-title kt-font-bold">
													Edit Profile
												</div>
												<div class="kt-notification__item-time">
													Account settings and more
												</div>
											</div>
										</a>
										<a href="#" class="kt-notification__item">
											<div class="kt-notification__item-icon">
												<i class="flaticon2-mail kt-font-warning"></i>
											</div>
											<div class="kt-notification__item-details">
												<div class="kt-notification__item-title kt-font-bold">
													My Messages
												</div>
												<div class="kt-notification__item-time">
													Inbox and tasks
												</div>
											</div>
										</a>
										<a href="#" class="kt-notification__item">
											<div class="kt-notification__item-icon">
												<i class="flaticon2-rocket-1 kt-font-danger"></i>
											</div>
											<div class="kt-notification__item-details">
												<div class="kt-notification__item-title kt-font-bold">
													Settings
												</div>
												<div class="kt-notification__item-time">
													Update Password
												</div>
											</div>
										</a> -->
										 
										<div class="kt-notification__custom kt-space-between">
											<a href="/logout" class="btn btn-label btn-label-brand btn-sm btn-bold">Sign Out</a>
										</div>
									</div>

									<!--end: Navigation -->
								</div>
							</div>

							<!--end: User bar -->

							<!--end: Quick panel toggler -->
						</div>

						<!-- end:: Header Topbar -->
					</div>

					<!-- end:: Header -->

<script type="text/javascript">

	$('#reloadData').click(function() {
		
		swal.fire({
              title: 'Dashbboard data reaload!',
              text: 'Please wait till data load successfully..',
              allowOutsideClick: false,
              //timer: 5000,              
              onOpen: function() {
                  swal.showLoading()
              }
          })
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reloadData',
			type : 'POST',
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
			},
			success : function(msg) {
				swal.close();
				window.location.reload();
			}
		});
			
	});
</script>