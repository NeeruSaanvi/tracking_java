

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

 <link href="resources/assets/css/lightgallery.css" rel="stylesheet">
 <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700' rel='stylesheet' type='text/css'>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 <script src="resources/assets/js/lightgallery.js"></script>
 <script src="resources/assets/js/lg-fullscreen.js"></script>
 <script src="resources/assets/js/lg-thumbnail.js"></script>

<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	<div id="sucessErrorMsgDiv"></div>
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Social Images
					</h3>
				</div>
			</div>
			<div class="kt-portlet__body">
				<div id="loader"></div>
				<div class="row" id="loadgallery" >
					<div id="caption2" style="display:none">
					    <h4>Bowness Bay</h4><p>A beautiful Sunrise this morning taken En-route to Keswick not one as planned but I'm extremely happy I was passing the right place at the right time....</p>
					</div>
					<div class="caption3" style="display:none">
					    <h4>Sunset Serenity</h4><p>A gorgeous Sunset tonight captured at Coniston Water....</p>
					</div>      
					                  
					<div id="captions">
					
						<c:forEach var="members" items="${imageList}" varStatus="counter">
							<a href="assets/upload/highResImages/${members.fileName}" data-sub-html="<h4>${members.firstLastName}</h4><p>${members.text}</p>" >
						      <img src="assets/upload/highResImages/${members.fileName}" />
						  	</a>
						</c:forEach>
					
					</div>
				</div>
			</div>
		</div>
	</div>
	
</div>



<script type="text/javascript">
$('#captions').lightGallery({
    thumbnail:true,
    animateThumb: false,
    showThumbByDefault: false
}); 
 </script>
 
 
