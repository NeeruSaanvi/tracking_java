<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
						Notifications
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row">
					<c:forEach var="notification" items="${notificationList}" varStatus="counter">
						<div class="col-lg-4">
							<div class="kt-section">
								<div class="kt-section__content kt-section__content--solid" >
									<h3 style="height: 180px">
										${notification.type}<br>										
										<small class="text-muted">${notification.description}</small>
									</h3>
									<div class="kt-portlet__foot kt-widget__button">
										<button class="btn btn btn-brand btn-sm" onclick="editNotification(${notification.id})" >Edit</button>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
					
				</div>				
			</div>
		</div>
	</div>
	
</div>



<script type="text/javascript">

	function editNotification(id){
		window.location.href = "/updateTemplates?id="+id
	}

	$('#editNotification').click(function() {
		
	});

	

	jQuery(document).ready(function () {
    	
    	
    });
 </script>