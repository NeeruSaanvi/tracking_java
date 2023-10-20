<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
</style>
<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	<div id="sucessErrorMsgDiv"></div>
	<div id="loader"></div>
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						PromoCode
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<form class="kt-form kt-form--label-right" id="userForm">
				<div id="sucessErrorMsgDiv"></div>
					<div class="form-group row">
						<div class="col-lg-4">
							<label>Enter Promo Code:</label>
							<input type="hidden" name="teamArray" id="teamArray" />
							
							<input type="email" name="promoCode" id="promoCode" class="form-control" placeholder="Enter full name">
						</div>
					</div>
					<div class="form-group row">
						<div class="col-lg-4">
							<label>Team:</label>
							<input type="hidden" name="userId" id="userId" />
							<select multiple="" class="form-control" id="team" name="team" style="width:100%">
								<c:forEach var="team" items="${teamList}" varStatus="counter">
									<option value="${team.teamId}">${team.teamName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-lg-4">
							<label>Member:</label>
							<select multiple="" class="form-control" id="members" name="members" style="width:100%">
								<c:forEach var="members" items="${membersList}" varStatus="counter">
									<option value="${members.userId}">${members.firstLastName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</form>

			</div>
			<div class="kt-portlet__foot">
				<div class="kt-form__actions">
					<button type="button" id="submitPromoCode" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</div>
	</div>
	
</div>


<script type="text/javascript">

	$('#submitPromoCode').click(function() {
		
		var param = {
			promoCode: $('#promoCode').val(),
			members: $('#members').val(),
			team: $('#team').val(),
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/updatePromoCode',
			type : 'POST',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your keywords have been saved successfully."); 
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	

	jQuery(document).ready(function () {
    	
		$('#members, #team').select2({
            placeholder: "All",
        });
    	
    });

 </script>