<%@page import="com.tracker.ui.jasper.JasperFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
<!--
.applications table {
    width: 100%;
    margin: 15px 0;
}

.applications table td.left {
    text-align: right;
    float: none;
    width: 30%;
    font-weight: bold;
    border-right: 1px solid #ddd;
}

.applications table td {
    border-bottom: 1px solid #ddd;
}

.applications table td, .applications table th {
    padding: 8px 10px;
    vertical-align: top;
}

-->
</style>
<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						View Store 
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">

				<div class="row">
					<div class="col-md-12">
						<div class="widget-box">
							<div class="widget-content applications">
								<div class="col-sm-12">
									<table>
										<tbody>
											<tr>
												<td class="left">Member Name</td>
												<td class="right"> ${ userStore.firstLastname } </td>
											</tr>
											<tr>
												<td class="left">Store Name</td>
												<td class="right"> ${ userStore.store_name } </td>
											</tr>
											<tr>
												<td class="left">Date of check-in</td>
												<td class="right">${ userStore.store_date }</td>
											</tr>
											<tr>
												<td class="left">Time of check-in</td>
												<td class="right">
													${ userStore.store_time_CheckIN }
												</td>
											</tr>
											<tr>
												<td class="left">Did you check in with the manager on-duty?</td>
												<td class="right">
													${ userStore.store_chkIn_manager_Duty } 
												</td>
											</tr>
											<tr>
												<td class="left">Did you check in with the department manager?</td>
												<td class="right">
													${ userStore.store_chkIn_dept_manager } 
												</td>
											</tr>
											<tr>
												<td class="left">How were the inventory levels of our product in the store?</td>
												<td class="right">
													${ userStore.store_inventory_levelStr }
												</td>
											</tr>
											<tr>
												<td class="left">How did our presence on the shelves compare to competitors? Are we winning the shelf space battle?</td>
												<td class="right">
													${ userStore.store_comp_win_shelf_space }
												</td>
											</tr>
											<tr>
												<td class="left">What was the condition of our products on the shelves?</td>
												<td class="right">
													${ userStore.store_shelves_conditionStr }
												</td>
											</tr>
											<tr>
												<td class="left">If we had products on end caps of aisle, how did they look?</td>
												<td class="right">
													${ userStore.store_end_caps_aisle_lookStr }
												</td>
											</tr>
											<tr>
												<td class="left">What notes do you have about what you saw in the store?</td>
												<td class="right">
													${ userStore.store_notes }
												</td>
											</tr>
											<tr>
												<td class="left">Did you receive any feedback from employees or management?</td>
												<td class="right">
													${ userStore.store_feedback }
												</td>
											</tr>
											<tr>
												<td class="left">Any new products or trends we should be aware of?</td>
												<td class="right">
													${ userStore.store_new_product_trend }
												</td>
											</tr>
											<tr>
												<td class="left">Please list our brands that you observed or discussed?</td>
												<td class="right">
													${ userStore.store_brand_observed_discuss }
												</td>
											</tr>
											<tr>
												<td class="left">If you were attending an event, please give us the event name and how many people were there for the event?</td>
												<td class="right">
													${ userStore.store_event_name_people }
												</td>
											</tr>
											
										</tbody>
									</table>
								</div>

							</div>

							<div class="row buttons">
				             	<div class="col-md-12">
				                 <button type="button" name="acceptModal" onclick="showModal()" class="btn btn-primary">Send it</button>
       			 				 <button type="submit" name="reject" id="rejectBtn" value="Reject" class="btn btn-danger" >Back</button>
				       			</div>
				           </div>

						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
	
</div>


<!--begin::Modal-->
<div class="modal fade" id="webPostModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
		<div id="sucessErrorMsgDiv"></div>
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Store</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<!--begin::Form-->
				<form class="kt-form" id="userStoreForm">
					<div class="kt-portlet__body">
						<div id="loader"></div>
						
						<div class="kt-section kt-section--first">
							<div class="form-group">
								<label>Email:</label>
								<input type="text" name="email" id="email" class="form-control" placeholder="Enter email">
								<input type="hidden" name="storeId" id="storeId" />
								<span class="form-text text-muted">Please enter email to send</span>
							</div>														
						</div>
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
							<button type="button" id="userStoreDataSubmit" class="btn btn-primary">Submit</button>
							<button type="button" class="btn btn-brand" data-dismiss="modal">Close</button>
						</div>
					</div>
				</form>

				<!--end::Form-->
			</div>
		</div>
	</div>
</div>

<!--end::Modal-->

<script type="text/javascript">


	function showModal(){
		<%
		String storeId = request.getParameter("storeId");
		%>
		$("#storeId").val(<%=storeId%>);
		$('#webPostModal').modal('show');
	}
	
	$('#userStoreDataSubmit').click(function() {
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/saveUserStore',
			type : 'POST',
			data : JSON.stringify($("#userStoreForm").serializeObject()),
			//dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("mail has been sent successfully."); 
					$("#webPostModal").modal("hide");
					$("#email").val('');
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	$('#webPostDataSubmit').click(function() {
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/saveWebPost',
			type : 'POST',
			data : JSON.stringify($("#webPostForm").serializeObject()),
			//dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your changes have been saved successfully."); 
					$('#webPostTable').DataTable().ajax.reload( null, false );
					$("#webPostModal").modal("hide");
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	

	jQuery(document).ready(function () {
    	
		$('#rejectBtn').on('click', function(e) {
			window.location.href = "/userStore";			
		});
    	
    });

 </script>