<%@page import="org.springframework.security.web.WebAttributes"%>
<%@page import="com.tracker.commons.Constants" %>
<%@page import="com.tracker.ui.utils.UserUtils"%>
<%@page import="com.tracker.commons.models.User"%>
 <%@page import="java.util.Arrays"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

 <%
 ServletContext sc = request.getSession().getServletContext();
 WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
 String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
 boolean result = Arrays.stream(profiles).anyMatch("sportsman"::equals);
 %>
<!DOCTYPE html>
 
<html lang="en">

	<!-- begin::Head -->
	<head>
		<meta charset="utf-8" />
		
		<%if(result){ %>
			<title> AmbassadorTrack.net - <tiles:getAsString name="title" /></title>
		<%}else{ %>
			<title> AnglerTrack.net - <tiles:getAsString name="title" /></title>
		<%} %>
		<meta name="description" content="Team Management">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no">

		<!--begin::Web font -->
		<script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.16/webfont.js"></script>
		<script>
			WebFont.load({
				google: {
					"families": ["Poppins:300,400,500,600,700", "Roboto:300,400,500,600,700"]
				},
				active: function() {
					sessionStorage.fonts = true;
				}
			});
		</script>
		
		<!--end::Web font -->

		<!--begin::Base Styles -->
		<link href="resources/external_assests/vendors/base/vendors.bundle.css" rel="stylesheet" type="text/css" />

		<!--RTL version:<link href="resources/external_assests/vendors/base/vendors.bundle.rtl.css" rel="stylesheet" type="text/css" />-->
		<link href="resources/external_assests/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" />

		<!--RTL version:<link href="resources/external_assests/demo/default/base/style.bundle.rtl.css" rel="stylesheet" type="text/css" />-->

		<!--end::Base Styles -->
		<link rel="shortcut icon" href="custom_resources/images/logo_small.png" />
		
		 <!--begin::Base Scripts -->
		<script src="resources/external_assests/vendors/base/vendors.bundle.js" type="text/javascript"></script>
		<script src="resources/external_assests/demo/default/base/scripts.bundle.js" type="text/javascript"></script>

		<!--end::Base Scripts -->

		<!--end::Page Snippets -->
		
		<!--begin::custom Snippets -->
		<script src="custom_resources/js/custom.js" type="text/javascript"></script>

		<!--end::custom Snippets --> 
	    
	</head>

	<!-- end::Head -->

	<!-- begin::Body -->
	<body class="m--skin- m-header--fixed m-header--fixed-mobile m-aside-left--enabled m-aside-left--skin-dark m-aside-left--fixed m-aside-left--offcanvas m-footer--push m-aside--offcanvas-default">

		<tiles:insertAttribute name="body" />
		
	</body>

	<!-- end::Body -->
</html>