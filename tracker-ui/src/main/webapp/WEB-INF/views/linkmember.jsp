<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Login</title>
<meta name="description" content="" />
<meta name="viewport" content="width=device-width" />
<base href="/" />
<link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="/webjars/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/css/font-awesome.min.css"></link>
</head>
 
<body>
 
 <div class="container">
 <h1>Login Using</h1>
 
  
 <form action="/connect/facebook" method="POST" style="display: inline">
 <input type="hidden" name="scope" value="public_profile,email" />
 <button type="submit" class="btn btn-primary">
 Facebook <span class="fa fa-facebook"></span>
 </button>
 </form>
 
<form action="/connect/facebook" method="POST">
   <input type="hidden" name="scope" value="read_stream" />
   <div><p>You aren't connected to Facebook yet.</p></div>
   <p><button type="submit">Login With Facebook</button></p>
  </form>
 </div>
</body>
</html>