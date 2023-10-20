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
<base href="/abc" />
<link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript">
   // $(document).ready(
           // function() {
            //    setInterval(function() {
               //     var randomnumber = Math.floor(Math.random() * 100);
    		        //document.getElementById("validate").style.visibility = "hidden";
    		       // document.getElementById("relink").style.visibility = "hidden";
    		     //   document.getElementById("check1").style.visibility = "visible";

    		     //  var format = /^[!@#$%^&*()_+\-=\[\]{};'"\\|,.<>\/?]*$/;

        		  // var value =  $('#check1').text();
        		 // var value = "test";
        		    //console.log("hjkhd"+ value);
        		//if (value.length >3) {
        		        // Exist text in your input
        		               		 //   console.log("hjkhd1"+ value);
        		               	//	 $('#validate').text("Relink Account")   		 
        		      //  document.getElementById("relink").style.visibility = "visible";
        		      //  document.getElementById("validate").style.visibility = "hidden";

        		 //   } else {
            		  //  console.log("hjkhd2"+ value);
	               		// $('#validate').text("Validate Account")   		 

        		      // document.getElementById("validate").style.visibility = "visible";

        		      // document.getElementById("relink").style.visibility = "hidden";
        		//    }
               // }, 5000);
           // });
</script>
<script type="text/javascript" src="/webjars/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/css/font-awesome.min.css"></link>

</head>
 
<body>
<style>
.link-account-box {
    min-height: 400px;
    height: 100%;
}
.link-acount-header {
    display: flex;
    align-items: center;
    flex-wrap: nowrap;
}
.link-account-logo {
    flex: 0 0 80px;
    max-width: 80px;
}
.link-account-logo img {
    max-width: 100%;
    height: auto;
}
.link-account-button {
    flex: 1 1 calc(100% - 80px);
    max-width: calc(100% - 80px);
    padding-left: 20px;
    text-align: right;
}

.link-acount-user-info {
    display: flex;
    align-items: center;
    flex-wrap: nowrap;
}
.link-account-icon {
    flex: 0 0 60px;
    max-width: 60px;
}
.link-account-icon img {
    max-width: 100%;
    height: auto;
}
.link-account-info-details {
    flex: 1 1 calc(100% - 60px);
    max-width: calc(100% - 60px);
    padding-left: 20px;
}
</style>
<div class="container">
     <div class="row">
         <div class="col-md-12">
             <h2 class="my-5">Link Account</h2>
             <div class="row"><div class="col-md-4">
                 <div class="link-account-box mb-3 border p-4">
                    <div class="link-acount-header mb-3">
    <div class="link-account-logo">
        <img src="https://cdn.iconscout.com/icon/free/png-256/facebook-logo-2019-1597680-1350125.png" alt=""> 
    
    </div>
                         <c:if test ="${ empty  user.getAccessToken()}">
    
    <div class="link-account-button"><a href="/useApplication" id="" class="btn btn-brand">Link Facebook Account</a></div>
    </c:if>
      <c:if test ="${ not empty  user.getAccessToken()}">
    
    <div class="link-account-button"><a href="/relink" id="" class="btn btn-brand">Re-Link</a></div>
    </c:if>
</div>
                    <div class="link-account-details">
                        <h5 class="mb-5">Facebook</h5>
<h5 class="mb-3">Current Facebook Account Linked:</h5>
                     <c:if test ="${ empty  user.getAccessToken()}">

<div class="unlink-account-content">
    <p>No facebook account is currently linked here. Please link from the button above.</p>
</div>
</c:if>
                     <c:if test ="${not empty  user.getAccessToken()}">

<div class="link-account-content" style="">
    <div class="link-acount-user-info">
    <div class="link-account-icon">
        <img src="https://freesvg.org/img/abstract-user-flat-4.png" alt=""> 
    
    </div>
    <div class="link-account-info-details"><h5 class="mb-0">${user.getUsername()}</h5>
<p class="mb-0
          ">${user.getEmail()}</p></div>
</div>
</div>
                        
                    </c:if>    
                    </div>
                   
                 </div>
                 
    </div>
 </div>
 </div></div>
 </div>
 
</body>



 
</html>
