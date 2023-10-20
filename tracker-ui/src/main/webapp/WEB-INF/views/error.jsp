<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Error Page - Coinxoom</title>

  <link href="resources/css/404.css" media="all" rel="stylesheet" type="text/css">

  <link rel="shortcut icon" type="image/x-icon" href="resources/img/bookmark.ico">

</head>

<body>
  <header class="page-container page-container-responsive space-top-4">
    <a href="/" >
     <h3 class="text-center"><img src="resources/img/logo.png" alt="logo" class="logo-img"> Coinxoom.com</h3>
    </a>
  </header>

  <div class="page-container page-container-responsive">
    <div class="row space-top-8 space-8 row-table">
        <div class="col-5 col-middle">
          <h1 class="text-jumbo text-ginormous">Oops!</h1>
          <h2>We can't seem to find the page you're looking for or some error occurred.</h2>
          <h6> ${exception.message}</h6>
          <ul class="list-unstyled">
            <li>Here are some helpful links instead:</li>
            <li><a href="/">Home</a></li>
            <li><a href="/faq">faq</a></li>
            <li><a href="/help">Help</a></li>
            <li><a href="/login">Login Page</a></li>
          </ul>
        </div>
        <div class="col-5 col-middle text-center">
          <img src="resources/img/404.gif" width="313" height="428" alt="Girl has dropped her ice cream.">
        </div>
      </div>
    </div>
  </div>
</body>
</html>
