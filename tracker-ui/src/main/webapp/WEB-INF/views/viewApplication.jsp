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
	<div id="sucessErrorMsgDiv"></div>
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Applicant Details
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<!-- <div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" onclick='updateWeb("","")'>
								<i class="la la-plus"></i>
								Add Print 
							</a>
						</div>
					</div> -->
				</div>
			</div>
			<div class="kt-portlet__body">

				<div class="row">
					<div class="col-md-12">
						<div class="widget-box">
							<div class="widget-content applications">
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-user" aria-hidden="true"></i> Personal Information</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Full Name</td>
												<td class="right"> ${ application.firstLastName } </td>
											</tr>
											<tr>
												<td class="left">Date of Birth</td>
												<td class="right">${ application.dob }</td>
											</tr>
											<tr>
												<td class="left">Highest Education</td>
												<td class="right">
													${ application.highestEducation }
												</td>
											</tr>
											<tr>
												<td class="left">Home Address</td>
												<td class="right">
													${ application.street } <br/> ${ application.city }
													${ application.state } ${ application.zipcode }
												</td>
											</tr>
											<tr>
												<td class="left">Shipping Address</td>
												<td class="right">
													${ application.ship_street } <br/> ${ application.ship_city }
													${ application.ship_state } ${ application.ship_zipcode }
												</td>
											</tr>
											<tr>
												<td class="left">Email</td>
												<td class="right">
													${ application.email }
												</td>
											</tr>
											<tr>
												<td class="left">Phone</td>
												<td class="right">
													${ application.phone }
												</td>
											</tr>
											<tr>
												<td class="left">Emergency Contact</td>
												<td class="right">
													${ application.sec_phone }
												</td>
											</tr>
											<tr>
												<td class="left">3 References outside of the fishing Industry</td>
												<td class="right">
													${ application.references_1 } <br> ${ application.references_2 } <br> ${ application.references_3 }
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-briefcase" aria-hidden="true"></i> Promo Gear</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Shirt Size</td>
												<td class="right">
													${ application.shirt_size }
												</td>
											</tr>
											<tr>
												<td class="left">Glove Size</td>
												<td class="right">
													${ application.glove_size }
												</td>
											</tr>
											<tr>
												<td class="left">Pant Size</td>
												<td class="right">
													${ application.pant_size }
												</td>
											</tr>
											<tr>
												<td class="left">Preferred Head Wear?</td>
												<td class="right">
													${ application.prefer_hardware }
												</td>
											</tr>											
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-tint" aria-hidden="true"></i> Fishing Information</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Home Body of Water</td>
												<td class="right">
													${ application.home_lake_river }
												</td>
											</tr>
											<tr>
												<td class="left">Most Targeted Species</td>
												<td class="right">
													${ application.species_type }
												</td>
											</tr>
											<tr>
												<td class="left">How Many Tournaments do you Fish Every Year?</td>
												<td class="right">
													${ application.tournament_fish_year }
												</td>
											</tr>
											<tr>
												<td class="left">Do you Fish any Tournament Trail?</td>
												<td class="right">
													${ application.isfish_tournament_trails }
												</td>
											</tr>
											<tr>
												<td class="left">Do you Fish Salt Water or Fresh Water?
												</td>
												<td class="right">
													${ application.water_type }
												</td>
											</tr>
											<tr>
												<td class="left">Are you a Guide?</td>
												<td class="right">
													${ application.isguide }
												</td>
											</tr>
											<tr>
												<td class="left">Do you have Captain License</td>
												<td class="right">
													${ application.isguidelicence }
												</td>
											</tr>
											<tr>
												<td class="left">License Number</td>
												<td class="right">
													${ application.license_number }
												</td>
											</tr>
											<tr>
												<td class="left">How Many days a year do you Guide?</td>
												<td class="right">
													${ application.guideyear }
												</td>
											</tr>
											<tr>
												<td class="left">Website for Yourself or guide Service
												</td>
												<td class="right">
													${ application.iswebsite }
												</td>
											</tr>
											<tr>
												<td class="left">Top Moments of Your Fishing Career?</td>
												<td class="right">
													${ application.flyfishingcareer_1 } <br>
													${ application.flyfishingcareer_2 }
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-anchor" aria-hidden="true"></i> Product Experience</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Have You used our Products?</td>
												<td class="right">
													${ application.isusedproducts }
												</td>
											</tr>
											<tr>
												<td class="left">Which Product do you use Most?</td>
												<td class="right">
													${ application.useproducts }
												</td>
											</tr>


											<tr>
												<td class="left">Please Share Your Experience with our Products?</td>
												<td class="right">
													${ application.experiencewithproducts }
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-share-square" aria-hidden="true"></i> Social Media / Web Activity</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Social Media Platform Used</td>
												<td class="right">
													${ application.social_media_platform }
												</td>
											</tr>
											<tr>
												<td class="left">Facebook Fans (Personal)</td>
												<td class="right">
													${ application.facebook_personal_page }
												</td>
											</tr>
											<tr>
												<td class="left">Facebook Link (Personal)</td>
												<td class="right">
													<a href="${application.link_to_facebook_personal_page}" target="_blank">${ application.link_to_facebook_personal_page }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Facebook Fans (Fan/Guide)</td>
												<td class="right">
													<a href="${application.link_to_facebook_personal_page}" target="_blank">${ application.link_to_facebook_fan_page }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Followers (Personal)</td>
												<td class="right">
													${ application.twitter_personal_page }
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Link (Personal)</td>
												<td class="right">
													<a href="${ application.link_to_twitter_personal_page }" target="_blank">${ application.link_to_twitter_personal_page }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Followers (Fan/Guide)</td>
												<td class="right">
													${ application.link_to_twitter_fan_page }
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Link (Fan/Guide)</td>
												<td class="right">
													${ application.link_to_twitter_fan_page }
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Followers (Personal)</td>
												<td class="right">
													<c:if test="${application.link_to_instagram_personal_page != 'N/A'}">
														<a href="${application.link_to_instagram_personal_page}" target="_blank">
													</c:if>
													${ application.link_to_instagram_personal_page }
													<c:if test="${application.link_to_instagram_personal_page != 'N/A'}">
														</a>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Link(Personal)</td>
												<td class="right">
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														<a href="${application.instagram_fan_page}" target="_blank">
													</c:if>
													${ application.instagram_fan_page }
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														</a>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Followers (Fan/Guide)</td>
												<td class="right">
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														<a href="${application.instagram_fan_page}" target="_blank">
													</c:if>
													${ application.instagram_fan_page }
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														</a>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Link (Fan/Guide)</td>
												<td class="right">
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														<a href="${application.instagram_fan_page}" target="_blank">
													</c:if>
													${ application.instagram_fan_page }
													<c:if test="${application.instagram_fan_page != 'N/A'}">
														</a>
													</c:if>
												</td>
											</tr>
											<tr>
												<td class="left">Fishing Blogs used</td>
												<td class="right">
													${ application.active_on_fishing_blog_1 }
												</td>
											</tr>
											<tr>
												<td class="left">Do ever video Fishing Trips</td>
												<td class="right">
												</td>
											</tr>
											<tr>
												<td class="left">Links to you Fishing Videos</td>
												<td class="right">
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-info-circle" aria-hidden="true"></i> Additional Information</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Other Sponsors</td>
												<td class="right">
													${ application.other_sposors_1 } <br>
													${ application.other_sposors_2 } 
												</td>
											</tr>
											<tr>
												<td class="left">Why should we select you our pro-Staff?</td>
												<td class="right">
													${ application.select_fishing_pro_staff }
												</td>
											</tr>
											<tr>
												<td class="left">Other information you would like to share with us?</td>
												<td class="right">
													${ application.other_information }
												</td>
											</tr>
											<tr>
												<td class="left">Resume</td>
												<td class="right">
													${ application.attach_resume }
												</td>
											</tr>
											<tr>
												<td class="left">Pictures</td>
												<td class="right">
													${ application.upload_pictures }
												</td>
											</tr>
										</tbody>
									</table>
									<%-- <input type="hidden" name="staffID" id="staffID" value="<?php echo $staffdetails['userid']; ?>" /> --%>
								</div>

							</div>

							<div class="row buttons">
				             	<div class="col-md-12">
				                 <button type="button" name="acceptModal" onclick="showModal()" class="btn btn-primary">Accept</button>
       			 				 <button type="submit" name="reject" id="rejectBtn" value="Reject" class="btn btn-danger" >Reject</button>
								 <button type="button" name="export" onclick="return generateReport(<%=JasperFormat.PDF_FORMAT.getId() %>, 'Reports')" class="btn btn-primary">Download PDF</button>
				       			</div>
				           </div>

						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
	
</div>

<!-- POPUP VIEW I -->	
	<div class="modal fade" id="discountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title">Discount Code</h4>
		  </div>
			<div class="modal-body"> 	
			
			 	<div class="col-md-12 col-sm-12">
					<center><h2><strong>Enter Code</strong></h2></center>
					<center><input type="text" class="form-control" name="discount_code" id="discount_code" value="" style="width:70%;" /></center>
				</div>	
				<br/> <br/> <br/> 
					</div>		
					
					<div style="clear:both;"></div>
					 
			    <div class="modal-footer">	

				   <button type="button" id="acceptBtn" name="submit" value="Accept" class="btn btn-primary btn-sm">Continue</button>
       			</div>
			</div>
		  </div>
		</div>
<!-- Popup View Ends Here -->


<script type="text/javascript">

function generateReport(format, reportName) {
	
	<%
	String staffId = request.getParameter("id");
	String applicationFrom = request.getParameter("link");
	%>
	
	var staffId = <%=staffId%>;
	var applicationFrom = '<%=applicationFrom%>';
	
	window.open("<%=request.getContextPath() %>/viewApplicationExportPdf?download=true&staffId="+staffId+"&applicationFrom="+applicationFrom, "_blank");
	
	return false;
}

function showModal(){	
	$('#discountModal').modal('show');
}

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
    	
		
		
		$('#acceptBtn').on('click', function(e) {
			<%
			String id = request.getParameter("id");
			String link = request.getParameter("link");
			%>
			var idlist = <%=id%>;
			var discount_code = $('#discount_code').val();
			$.ajax({
				type: 'POST',                          
				data:{ids:idlist,action:'Approve', link:'<%=link%>', discount_code: discount_code},
				url : '<%=request.getContextPath()%>/rest/updateApplications',
				cache: false,
				success: function(data){ 		
					window.location.href = "/applications";
				}           
			});
			
		});
		
		$('#rejectBtn').on('click', function(e) {
			<%
			String ids = request.getParameter("id");
			String links = request.getParameter("link");
			%>
			
			var idlist = <%=ids%>;
			
			$.ajax({
				type: 'POST',                          
				data:{ids:idlist,action:'Reject', link:'<%=links%>'},
				url : '<%=request.getContextPath()%>/rest/updateApplications',
				cache: false,
				success: function(data){ 		
					window.location.href = "/applications";
				}           
			});
			
		});
    	
    });

 </script>