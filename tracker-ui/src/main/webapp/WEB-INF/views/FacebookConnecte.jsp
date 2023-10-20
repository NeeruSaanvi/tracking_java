<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html xmlns:th="www.thymeleaf.org">
<head>
    <!-- Bootstrap core CSS -->
    <title>Spring Boot Facebook Application</title>
</head>

<body>
<!-- Page Content -->
<div class="container">
    <div class="row">
        <div class="col-lg-12 text-center">
            <h1 class="mt-5">Connected successfully!</h1>
            <p class="lead">Welcome to facebook!</p>

            <p>You can now browse your own feed <a href="/feed">here</a>.</p>
            <p>You can also see your friend list <a href="/friends">here</a>.</p>
        </div>
    </div>
</div>
</body>
</html>