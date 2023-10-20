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
										</tbody>
									</table>
								</div>
								<c:if test="${application.profile == 'Boating'}">
									<div class="col-sm-12">
										<table>
											<thead>
												<tr>
													<th colspan="2" class="btn-brand"><i class="fa fa-briefcase" aria-hidden="true"></i> BOATING INFORMATION</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td class="left">Boating Categories</td>
													<td class="right">
														${ application.boating_categories }
													</td>
												</tr>
												<tr>
													<td class="left">Boating Brands</td>
													<td class="right">
														${ application.boat_brands }
													</td>
												</tr>	
												<tr>
													<td class="left">Number of Days/Year Spend on Water?</td>
													<td class="right">
														${ application.num_days_peryear_field }
													</td>
												</tr>
												<tr>
													<td class="left">Typically boat in freshwater or saltwater?	</td>
													<td class="right">
														${ application.boat_freshwaterSaltwater }
													</td>
												</tr>
												<tr>
													<td class="left">Home body of water?</td>
													<td class="right">
														${ application.boat_homeBodywater }
													</td>
												</tr>
												<tr>
													<td class="left">Are you involved with Boating related TV/WEB shows?</td>
													<td class="right">
														${ application.tv_involvement }
													</td>
												</tr>										
											</tbody>
										</table>
									</div>
								</c:if>
								<c:if test="${application.profile == 'Hunting'}">
								
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>${ application.profile }
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
												<td class="left">Preferred Head Wear?</td>
												<td class="right">
													${ application.head_wear }
												</td>
											</tr>											
										</tbody>
									</table>
								</div>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-tint" aria-hidden="true"></i> Hunting Information</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Hunting Categories</td>
												<td class="right">
													${ application.hunting_categories }
												</td>
											</tr>
											<tr>
												<td class="left">Most Targeted Species</td>
												<td class="right">
													${ application.target_species }
												</td>
											</tr>
											<tr>
												<td class="left">Hunting Methods</td>
												<td class="right">
													${ application.primary_hunting_method }
												</td>
											</tr>
											<tr>
												<td class="left">Number of Days/Year In The Field?</td>
												<td class="right">
													${ application.num_days_peryear_field }
												</td>
											</tr>
											<tr>
												<td class="left">Are you a Guide?
												</td>
												<td class="right">
													${ application.guide }
												</td>
											</tr>
											<tr>
												<td class="left">What's The Name Of Your Guide/Outfitter Service?</td>
												<td class="right">
													${ application.guide_service_name }
												</td>
											</tr>
											<tr>
												<td class="left">How many clients per year do you guide?</td>
												<td class="right">
													${ application.guide_num_clients }
												</td>
											</tr>
											<tr>
												<td class="left">Website for Yourself or guide Service</td>
												<td class="right">
													${ application.guide_service_website }
												</td>
											</tr>
											<tr>
												<td class="left"> Are you involved with TV/WEB shows? </td>
												<td class="right">
													${ application.tv_involvement }
												</td>
											</tr>
											<tr>
												<td class="left">How often do these shows air?
												</td>
												<td class="right">
													${ application.tv_shows_air }
												</td>
											</tr>
											<tr>
												<td class="left">List shows here along with a link to an episode</td>
												<td class="right">
													${ application.tv_shows_list }
												</td>
											</tr>
											<tr>
												<td class="left">In what quarter do these shows air?</td>
												<td class="right">
													${ application.tv_show_air_quarter }
												</td>
											</tr>
											<tr>
												<td class="left">Average # of viewers per episode</td>
												<td class="right">
													${ application.tv_avg_viewers }
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
												<td class="left"> Please Share Your Experience with our Products? </td>
												<td class="right">
													${ application.exp_sponsor_products }
												</td>
											</tr>
											<tr>
												<td class="left">Willing to do any following promotional activities on our behalf</td>
												<td class="right">
													${ application.activity_on_sponsor_behalf }
												</td>
											</tr>

										</tbody>
									</table>
								</div>
								</c:if>
								<div class="col-sm-12">
									<table>
										<thead>
											<tr>
												<th colspan="2" class="btn-brand"><i class="fa fa-share-square" aria-hidden="true"></i> Social Media / Web Activity</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="left">Facebook Link</td>
												<td class="right">
													<a href="${ application.fb_page_link }" target="_blank">${ application.fb_page_link }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Facebook followers</td>
												<td class="right">
													${ application.fb_num_followers }
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Link</td>
												<td class="right">
													<a href="${ application.twt_page_link }" target="_blank">${ application.twt_page_link }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Twitter Followers</td>
												<td class="right">
													${ application.twt_num_followers }
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Link</td>
												<td class="right">
													<a href="${ application.insta_page_link }" target="_blank">${ application.insta_page_link }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Instagram Followers</td>
												<td class="right">
													${ application.insta_num_followers }
												</td>
											</tr>
											<tr>
												<td class="left">Youtube Channel</td>
												<td class="right">
													<a href="${ application.yt_channel_link }" target="_blank">${ application.yt_channel_link }</a>
												</td>
											</tr>
											<tr>
												<td class="left">Youtube Channel Subscribers</td>
												<td class="right">
													${ application.yt_num_subscribers }
												</td>
											</tr>
											<tr>
												<td class="left">List the forums</td>
												<td class="right">
													${ application.forum_details }
												</td>
											</tr>
											<tr>
												<td class="left">Forum Posting</td>
												<td class="right">
													${ application.forum_posting }
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
													${ application.additionals_sponsors } 
												</td>
											</tr>
											<tr>
												<td class="left">I usually use for photos and/or video?	</td>
												<td class="right">
													${ application.photoVideoEquipment } 
												</td>
											</tr>
											<tr>
												<td class="left">Experience with photo & video editing software?</td>
												<td class="right">
													${ application.exp_photoVideoEditing_soft } 
												</td>
											</tr>
											<tr>
												<td class="left">Why should we select you our pro-Staff?</td>
												<td class="right">
													${ application.why_to_select }
												</td>
											</tr>
											<tr>
												<td class="left">Brand camo preferred</td>
												<td class="right">
													${ application.prefer_brands }
												</td>
											</tr>
											<tr>
												<td class="left">Organization involved</td>
												<td class="right">
													${ application.organization_list }
												</td>
											</tr>
											<tr>
												<td class="left">Resume</td>
												<td class="right">
													${ application.attach_resume }
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