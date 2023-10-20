<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
.Editor-editor { min-height:70px; border: 1px solid #a7abc3; }

</style>

<script type="text/javascript">
	
	
	function onIsTextChange() {
		var radioValue = $("input[name='isText']:checked").val();
		if (radioValue === 'email') {
		   	$('#messageSubjectDiv').css({"display": "flex"});
		   	$('#messageSubject').value = '';
		   	$('#messageTextDiv').hide();
		   	$('#messageDiv').show();
		}
		else {
			$('#messageSubjectDiv').css({"display": "none"});
			$('#messageSubject').value = '';
			$('#messageTextDiv').show();
			$('#messageDiv').hide();
		}
		
		return false;
	}
	
	function resetMessageForm(){
		$("#teamId").val('-1').change();
		$("#messageSubject").val('');
		$("#message").val('');
		$("#memberId").val('-1').change();
		$("#attachmentFile").val('');
	}
	
	function checkFileSize() {
	    var fileName = $("#attachmentFile").val();
	    
	    if(fileName == ''){
	    	return true;
	    }
	    
	    var fileExtension = ['jpeg', 'jpg', 'png', 'gif'];
        if ($.inArray(fileName.split('.').pop().toLowerCase(), fileExtension) == -1) {
            alert("Only '.jpeg','.jpg','.png', '.gif' formats are allowed.");
            $("#attachmentFile").val('');
            return false;
        }

	    var fileSize = ($("#attachmentFile")[0].files[0].size / 1024); //size in MB
	    if (fileSize > 500) {
	        alert("File size is too large, file size should be less than 500kb.");
	        $("#attachmentFile").val('');
	        return false;
	    }
	    
	    return true;
	    
	}

	function onMessageSubmit() {
		
		var form = $('#quickMessageForm')[0];
		
		var messageValue = $("#message").Editor("getText");
		$("#messageFormat").val(messageValue);
		
		var data = new FormData(form);
		var radioValue = $("input[name='isText']:checked").val();
		
		if (radioValue === 'email') {
			if($("#messageSubject").val() == ''){
				showErrorOnDiv('Please enter subject', 'sucessErrorMsgDivSendMessageForm');
				return;
			}
		}
		
		if (radioValue === 'text') {
			var status = checkFileSize();
			if(!status){
				return;
			}
		}
		
	   
	   $.ajax({
	        type: "POST",
	        enctype: 'multipart/form-data',
	        url: '<%=request.getContextPath()%>/rest/quickMessage',
	        data: data,
	        processData: false,
	        contentType: false,
	        cache: false,
	        timeout: 600000,
	        beforeSend	: function(xhr) {					
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success		: function(msg) {
				$('#loader').html('');
				if(msg == 'success') {		    		   
					showMessageOnDiv('Your message has been delivered succesfully', 'sucessErrorMsgDivSendMessageForm');
					resetMessageForm();
					onIsTextChange();
				}
				else {
					showErrorOnDiv(msg.message, 'sucessErrorMsgDivSendMessageForm');
				}
			},
			error		: function (xhr, ajaxOptions, thrownError) {
				$('#loader').html('');
				showErrorOnDiv("There was an error submitting your request. Please retry again.", 'sucessErrorMsgDivSendMessageForm');
			}
	    });
  			 
		
	} 
	
	
	$(document).ready(function() {
		
		$("#message").Editor(
			{
				"texteffects":false,
				"aligneffects":true,
				"textformats":false,
				"fonteffects":false,
				"actions" : false,
				"insertoptions" : false,
				"extraeffects" : false,
				"advancedoptions" : false,
				"screeneffects":false,
				"bold": true,
				"italics": true,
				"underline":true,
				"ol":false,
				"ul":false,
				"undo":false,
				"redo":false,
				"l_align":false,
				"r_align":false,
				"c_align":false,
				"justify":false,
				"insert_link":false,
				"unlink":false,
				"insert_img":false,
				"hr_line":false,
				"block_quote":false,
				"source":false,
				"strikeout":false,
				"indent":false,
				"outdent":false,
				"fonts":false,
				"styles":false,
				"print":false,
				"rm_format":false,
				"status_bar":false,
				"font_size":false,
				"color":false,
				"splchars":false,
				"insert_table":false,
				"select_all":false,
				"togglescreen":false
			}
		);
		
		
		$('input[type=radio][name=isText]').change(function() {
		    if (this.value == 'email') {
		    	$('#messageSubjectDiv').css({"display": "flex"});
				$('#messageSubject').value = '';
				$('#messageTextDiv').hide();
				$('#messageDiv').show();
		    }
		    else if (this.value == 'text') {
		    	$('#messageSubjectDiv').css({"display": "none"});
			   	$('#messageSubject').value = '';
			   	$('#messageTextDiv').show();
			   	$('#messageDiv').hide();
		    }
		});
		
		$('#Mapmembers').change(function() {
		     loadProStaffMap();		    
		});
		
		$('#Mapteam').change(function() {
		     loadProStaffMap();		    
		});
		
		$(".overlay").hide();
		
		$("select").select2();
		$("select").removeClass('form-control');
		 
      	loadProStaffMap();
      	
        var swiper = new Swiper('.swiper-container', {
      
        
            slidesPerView: 'auto',
            spaceBetween: 700,
            centeredSlides: true,
         navigation: {
           nextEl: '.swiper-button-next',
           prevEl: '.swiper-button-prev',
         },
            pagination: {
              el: '.swiper-pagination',
              clickable: true,
            },
          });
         
        
         swiper.on('slideChange', function () {
         });
    });
	
	function loadNextImage() {
		$('.swiper-slide').fadeOut('500');
		<c:forEach var="totalsObj" items="${carousilImageList}" varStatus="counter">
			<c:if test="${counter.index == 4}">
				$('.swiper-slide').html('<img src="${totalsObj.standardImage}">');
				$('.swiper-slide').css({"display": "none"});
		    </c:if>
		</c:forEach>
		$('.swiper-slide').fadeIn(2000);
		
		setTimeout(loadNextImage, 3000);
  	}
	
    var loadProStaffMap = function() {
	 	var map = new GMaps({
            div: '#proStaffMap',
            lat: 34.153159,
            lng: -84.169845,
        });
	 	
	 	var team = $('#Mapteam').val();
	 	var member = $('#Mapmembers').val();
       
		<c:forEach var="member" items="${membersMapList}" varStatus="counter">
		
			if(member == 'All'){
				if(${member.latitude} != ''){
					map.addMarker({
			            lat: ${member.latitude},
			            lng: ${member.longitude},
			             title: '<c:out value="${member.firstLastName}" escapeXml="true" />',
			            details: {
			                database_id: ${member.userId},
			                loginName: '${member.loginName}',
			                email: '${member.email}'
			            },
			            click: function(e) {
			            },
			            infoWindow: {
			                content: '<span style="color:#000"><b><c:out value="${member.firstLastName}" escapeXml="true" /><br>${member.email} <br> ${member.phone} </b><br><br>'// +
			            }
			        });
				}
				
			}else{
				
				if(team.indexOf(${member.userId}) !== -1){
					if(${member.latitude} != ''){
						map.addMarker({
				            lat: ${member.latitude},
				            lng: ${member.longitude},
				             title: '<c:out value="${member.firstLastName}" escapeXml="true" />',
				            details: {
				                database_id: ${member.userId},
				                loginName: '${member.loginName}',
				                email: '${member.email}'
				            },
				            click: function(e) {
				            },
				            infoWindow: {
				                content: '<span style="color:#000"><b><c:out value="${member.firstLastName}" escapeXml="true" /><br>${member.email} <br> ${member.phone} </b><br><br>'// +
				            }
				        });
					}
			    }
				
				if(member == ${member.userId}){
					if(${member.latitude} != ''){
						 map.addMarker({
				            lat: ${member.latitude},
				            lng: ${member.longitude},
				             title: '<c:out value="${member.firstLastName}" escapeXml="true" />',
				            details: {
				                database_id: ${member.userId},
				                loginName: '${member.loginName}',
				                email: '${member.email}'
				            },
				            click: function(e) {
				            },
				            infoWindow: {
				                content: '<span style="color:#000"><b><c:out value="${member.firstLastName}" escapeXml="true" /><br>${member.email} <br> ${member.phone} </b><br><br>'// +
				            }
				        });
					}
				}
			}
		</c:forEach> 
        map.setZoom(4);
    } 

</script>


<!--Begin:: Dashboard Root-->
<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">


	<!--begin:: Row 1-->
	<div class="kt-portlet">
		<div class="kt-portlet__body  kt-portlet__body--fit">
			<div class="row row-no-padding row-col-separator-xl">
				
				<div class="col-md-12 col-lg-6 col-xl-3">
					<!--begin:: Member Count -->
					<div class="kt-widget24">
						<div class="kt-widget24__details">
							<div class="kt-widget24__info">
								<h4 class="kt-widget24__title">
									Member Count
								</h4>
								<span class="kt-widget24__desc">
									Active
								</span>
							</div>
							<span class="kt-widget24__stats kt-font-brand">
								${ members.activeMembers }
							</span>
						</div>
						<div class="progress progress--sm">
							<div class="progress-bar kt-bg-brand" role="progressbar" 
								style="width: ${((members.activeMembers*100)/(members.activeMembers + members.inactiveMembers))}%;" 
								aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
						</div>
						<div class="kt-widget24__action">
							<span class="kt-widget24__change">
								Inactive
							</span>
							<span class="kt-widget24__number" style="color: red;">
								 ${ members.inactiveMembers }
							</span>
						</div>
					</div>

					<!--end:: Member Count -->
				</div>
				
				<div class="col-md-12 col-lg-6 col-xl-3">
				<!--begin::Most Used Keyword -->
					<div class="kt-widget24">
							<div class="kt-widget24__details">
								<div class="kt-widget24__info">
									<h4 class="kt-widget24__title">
										Most Used Keywords YTD (# of posts)
									</h4>
								</div>
							</div>
							
							<div class="kt-scroll" data-scroll="true" data-height="200" style="height: 200px;">
								<div class="kt-timeline-v2">
									<div class="kt-timeline-v2__items  kt-padding-top-25 kt-padding-bottom-30">
										<c:forEach var="keywordObj" items="${mostUsedKeywords}" varStatus="counter">								
										<div class="kt-timeline-v2__item">
											<span class="kt-timeline-v2__item-time">${counter.index+1}</span>
											<div class="kt-timeline-v2__item-cricle">
											
												<i class="fa fa-genderless kt-font-info"></i>
											</div>
											<div class="kt-timeline-v2__item-text  kt-padding-top-5">
												<span class="kt-list-timeline__text" >${keywordObj.name}</span>
											</div>
										</div>
										
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					<!--end::Most Used Keyword-->
				</div>
				
				<div class="col-md-12 col-lg-6 col-xl-3">

					<!--begin::Rank List -->
					<div class="kt-widget24">
						<div class="kt-widget24__details">
							<div class="kt-widget24__info">
								<h4 class="kt-widget24__title">
									Annual Standings 
								</h4>
							</div>
						</div>
						
						<div class="kt-scroll" data-scroll="true" data-height="200" style="height: 200px;">
							<div class="kt-timeline-v2">
								<div class="kt-timeline-v2__items  kt-padding-top-25 kt-padding-bottom-30">
									<c:forEach var="rank" items="${rankList}" varStatus="counter">									
									<div class="kt-timeline-v2__item">
										<span class="kt-timeline-v2__item-time">${counter.index+1}</span>
										<div class="kt-timeline-v2__item-cricle">
											<i class="fa fa-genderless kt-font-info"></i>
										</div>
										<div class="kt-timeline-v2__item-text  kt-padding-top-5">
											<span class="kt-list-timeline__text">${rank.prostaffName} <span class="kt-badge kt-badge--info kt-badge--inline kt-badge--pill" style="font-size: 1.0rem;">
											<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${rank.rankScore}" />
											</span></span>
										</div>
									</div>
									
									</c:forEach>
								</div>
							</div>
						</div>
					</div>

					<!--end:Rank List-->
				</div>
				
				<div class="col-md-12 col-lg-6 col-xl-3">
					<!--begin::Top Pro -->
					<div class="kt-widget24">
						<div class="kt-widget24__details">
							<div class="kt-widget24__info">
								<h4 class="kt-widget24__title">
									Top Pro
								</h4>
							</div>
						</div>
						<div class="kt-widget kt-widget--user-profile-4">
						<c:forEach var="rank" items="${rankList}" varStatus="counter">	
							<c:if test="${counter.index == 0}">
							<div class="kt-widget__head">
								<div class="kt-widget__media">
									<img class="kt-widget__img kt-hidden-" src="${rank.atProfilePic}" alt="image">
									
								</div>
								<div class="kt-widget__content">
									<div class="kt-widget__section">
										<!--   <a href="/userProfile?userId=${rank.userid}" class="kt-widget__username">  -->
										<a href="#" class="kt-widget__username">
											${rank.prostaffName}
										</a>
										<div class="kt-widget__button">
											<span class="btn btn-label-warning btn-sm">${rank.rankScore}</span>
										</div>										
									</div>
								</div>
							</div>
							</c:if>
							</c:forEach>
						</div>
					</div>
					<!--end::Top Pro -->
					
				</div>
				
			<!--End::Section-->
			</div>
		</div>
	</div>
		
	<!--Begin::Row 2-->
	<div class="row">
				
		<!--begin:: Social Activity Totals -->
		<div class="col-xl-4">
		
			<div class="kt-portlet kt-portlet--tabs kt-portlet--height-fluid">
				<div class="kt-portlet__head">
					<div class="kt-portlet__head-label">
						<h3 class="kt-portlet__head-title">
							Social Activity
						</h3>
					</div>
					<div class="kt-portlet__head-toolbar">
						<ul class="nav nav-tabs nav-tabs-line nav-tabs-bold nav-tabs-line-brand" role="tablist">
							<c:forEach var="totalsObj" items="${socialActivityTotals}" varStatus="counter">
							    <li class="nav-item">
							    	<c:if test="${counter.index < 1}">
								         <a class="nav-link active" data-toggle="tab" href="#kt_widget4_tab${counter.count}_content" role="tab">
								    </c:if>
								    <c:if test="${counter.index >= 1}">
										<a class="nav-link" data-toggle="tab" href="#kt_widget4_tab${counter.count}_content" role="tab">
									</c:if> 
										${totalsObj.totalType}
									</a>
								</li>
							</c:forEach> 
						 
						</ul>
					</div>
				</div>
				<div class="kt-portlet__body">
					<div class="tab-content">
					  
					  	<c:forEach var="totalsObj" items="${socialActivityTotals}" varStatus="counter">
							<c:if test="${counter.index < 1}">
						         <div class="tab-pane active" id="kt_widget4_tab${counter.count}_content">
						    </c:if>
						    <c:if test="${counter.index >= 1}">
								<div class="tab-pane" id="kt_widget4_tab${counter.count}_content">
							</c:if> 
								
								<div class="kt-widget12">
								
								    <div class="kt-widget12__content">
										<div class="kt-widget12__item">
											<div class="kt-widget12__info">
												<span class="kt-widget12__desc">Total Members</span>
												<span class="kt-widget12__value">${totalsObj.totalMembers}</span>
											</div>
											<div class="kt-widget12__info">
												<span class="kt-widget12__desc">Total Posts</span>
												<span class="kt-widget12__value">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.totalPosts}" />
												</span>
											</div>
										</div>
										<div class="kt-widget12__item">
											<div class="kt-widget12__info">
												<span class="kt-widget12__desc">Total Interactions</span>
												<span class="kt-widget12__value">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.totalInteractions}" /> 
												</span>
											</div>
											<div class="kt-widget12__info">
												<span class="kt-widget12__desc">Interaction Rate</span>
												<span class="kt-widget12__value">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.interactionRate}" /> 
												</span>
											</div>
											
										</div>
										
									</div>
								</div>
							</div>
						</c:forEach>     
						 
					</div>
				</div>
			</div>
		</div>

		<!--end:: Social Activity Totals-->

		
		<!--begin:: Social Activities Per Site -->
		<div class="col-xl-4">

			<div class="kt-portlet kt-portlet--tabs kt-portlet--height-fluid">
				<div class="kt-portlet__head">
					<div class="kt-portlet__head-label">
						<h3 class="kt-portlet__head-title">
							Social Per Site
						</h3>
					</div>
					<div class="kt-portlet__head-toolbar">
						<ul class="nav nav-tabs nav-tabs-line nav-tabs-bold nav-tabs-line-brand" role="tablist">
							<c:forEach var="totalsObj" items="${activitiesPerSite}" varStatus="counter">
							    <li class="nav-item">
							    	<c:if test="${counter.index < 1}">
								         <a class="nav-link active" data-toggle="tab" href="#kt_activityPerSite_tab${counter.count}_content" role="tab">
								    </c:if>
								    <c:if test="${counter.index >= 1}">
										<a class="nav-link" data-toggle="tab" href="#kt_activityPerSite_tab${counter.count}_content" role="tab">
									</c:if> 
										${totalsObj.periodType}
									</a>
								</li>
							</c:forEach> 
						 
						</ul>
					</div>
				</div>
				<div class="kt-portlet__body">
					<div class="tab-content">
					  
					  	<c:forEach var="totalsObj" items="${activitiesPerSite}" varStatus="counter">
							<c:if test="${counter.index < 1}">
						         <div class="tab-pane active" id="kt_activityPerSite_tab${counter.count}_content">
						    </c:if>
						    <c:if test="${counter.index >= 1}">
								<div class="tab-pane" id="kt_activityPerSite_tab${counter.count}_content">
							</c:if> 
							
								<div class="kt-widget4">
									<div class="kt-widget4__item">
										<div class="kt-widget4__info" style="padding: 10px 15px">
											<span class="kt-widget4__icon" style="text-align: center;">
												<i class="flaticon-facebook-logo-button  kt-font-brand"></i>
												<a href="#" class="kt-widget4__username " style="padding: 15px 0px; text-align: center;">
													Facebook
												</a>
											</span>
											
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Posts</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">${totalsObj.fbTotalPosts}</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Interactions</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.fbTotalInteractions}" /> 
												</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>I/R</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.fbInteractionRate}" />
												</span>
											</p>
										</div>
									 
										<div class="kt-widget4__info" style="padding: 10px 15px">
										
											<span class="kt-widget4__icon" style="text-align: center;">
												<i class="flaticon-twitter-logo-button  kt-font-brand"></i>
												<a href="#" class="kt-widget4__username " style="padding: 15px 0px; text-align: center;">
													Twitter
												</a>
											</span>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Posts</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">${totalsObj.twTotalPosts}</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Interactions</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.twTotalInteractions}" />
												</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>I/R</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.twInteractionRate}" />
												</span>
											</p>
										</div>
									</div>
									<div class="kt-widget4__item">
										<div class="kt-widget4__info" style="padding: 10px 15px">
											<span class="kt-widget4__icon" style="text-align: center;">
												<i class="flaticon-instagram-logo  kt-font-brand"></i>
												<a href="#" class="kt-widget4__username " style="padding: 15px 0px; text-align: center;">
													Instagram
												</a>
											</span>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Posts</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">${totalsObj.inTotalPosts}</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Interactions</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.inTotalInteractions}" />
												</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>I/R</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.inInteractionRate}" />
												</span>
											</p>
										</div>
									 
										<div class="kt-widget4__info" style="padding: 10px 15px">
											<span class="kt-widget4__icon" style="text-align: center;">
												<i class="flaticon-youtube  kt-font-brand"></i>
												<a href="#" class="kt-widget4__username " style="padding: 15px 0px; text-align: center;">
													YouTube
												</a>
											</span>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Posts</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">${totalsObj.ytTotalPosts}</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>Interactions</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.ytTotalInteractions}" />
												</span>
											</p>
											<p class="kt-widget4__text" style="display: flex; justify-content: space-between;">
												<span>I/R</span>
												<span class="kt-widget4__number kt-font-info" style="padding-left: 30px;">
													<fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.ytInteractionRate}" />
												</span>
											</p>
										</div>
									</div>
									
								 </div>
							</div>
						</c:forEach>     
						
					</div>
				</div>
			</div>

		</div>
		<!--end:: Social Activities Per Site -->
		
		<!-- begin: Quick Message -->
		<div class="col-xl-4">
		
		<div class="kt-portlet kt-portlet--tabs kt-portlet--height-fluid">
			<form class="kt-form" id="quickMessageForm">

				
					<div class="kt-portlet__head">
						<div class="kt-portlet__head-label">
							<h3 class="kt-portlet__head-title">
								Quick Message
							</h3>
						</div>
						
						<div class="kt-portlet__head-toolbar" style="padding-top: 15px;">
						
							<div class="kt-radio-inline">
								<label class="kt-radio kt-radio--bold kt-radio--success">
									<input type="radio" checked="checked" name="isText" value="email"> Email
									<span></span>
								</label>
								<label class="kt-radio kt-radio--bold kt-radio--brand">
									<input type="radio" name="isText" value="text"> Text
									<span></span>
								</label>
							</div>
						
						</div>
					</div>
					
					<div id="sucessErrorMsgDivSendMessageForm" style="display:block"></div>
					
					<!--begin::Form-->
					<div class="kt-portlet__body">
						<div id="loader"></div>
						<div class="form-group row">
							<label class="col-form-label col-lg-2 col-sm-6">Members:</label>
							<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
							<div class=" col-lg-4 col-md-4 col-sm-6">
								<select class="form-control kt-select2 select2-selection--multiple" id="memberId" name="memberId">
									<option value="-1">All</option>
									<c:forEach var="members" items="${membersList}" varStatus="counter">
										<option value="${members.userId}">${members.firstLastName}</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-form-label col-lg-2 col-sm-6">Teams:</label>
							<div class=" col-lg-4 col-md-4 col-sm-6">
							
								<select class="form-control kt-select2" id="teamId" name="teamId">
									<option value="-1">All</option>
									<c:forEach var="team" items="${teamList}" varStatus="counter">										
										<option value="${team.teamId}">${team.teamName}</option>
									</c:forEach>
								</select>
								
							</div>
						</div>
						<div class="form-group row" id="messageSubjectDiv">
							<label for="example-text-input" class="col-2 col-form-label">Subject:</label>
							<div class="col-10">
								<input id="messageSubject" type="text" name="subject" class="form-control" placeholder="Enter subject">
							</div>
						</div>
						<div class="form-group row">
							<label class="col-form-label col-lg-2 col-sm-12">Message</label>
							<input type="hidden" name="messageFormat" id="messageFormat" value="">
							<div class="col-lg-10 col-md-10 col-sm-12" id="messageDiv">
								<textarea name="message" id="message" class="form-control" rows="3" style="height: 50px;"></textarea>
							</div>
							<div class="col-lg-10 col-md-10 col-sm-12" id="messageTextDiv" style="display: none;">
								<textarea name="messageText" id="messageText" class="form-control" rows="3" style="height: 50px;"></textarea>
							</div>
						</div>
						<div class="form-group row">
							<label for="example-text-input" class="col-2 col-form-label">File:</label>
							<div class="col-10">
								<input type="file" name="attachmentFile" class="form-control" id="attachmentFile">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-9">
								<button type="button" onclick="onMessageSubmit()" class="btn btn-brand btn-sm">Send</button>
								<button type="reset" class="btn btn-secondary btn-sm">Cancel</button> 
							</div>
						</div>
					</div>
					
				
			</form>
			</div>
			<!--end::Form-->
			
		</div>
		<!-- end:: Quick Message -->
				
		<!-- begin: Pro Staff Map -->
		
		<script src="//maps.google.com/maps/api/js?key=AIzaSyA14NLvoFC6BfvGNDUqKVbciyOE_vVs8gw" type="text/javascript"></script>
		<script src="resources/assets/vendors/custom/gmaps/gmaps.js" type="text/javascript"></script>
		
		<div class="col-xl-6">

			<div class="kt-portlet kt-portlet--tabs kt-portlet--height-fluid">
				<div class="kt-portlet kt-portlet--tab">
					<div class="kt-portlet__head">
						<div class="kt-portlet__head-label">
							<span class="kt-portlet__head-icon kt-hidden">
								<i class="la la-gear"></i>
							</span>
							<h3 class="kt-portlet__head-title">
								Member Map
							</h3>
						</div>
					</div>
					<div class="kt-portlet__body">
						<div class="form-group row">
							<div class="col-lg-4">
								<label>Team:</label>
								<select class="form-control" id="Mapteam" name="team" style="width:100%" multiple>
									<c:forEach var="team" items="${teamList}" varStatus="counter">										
										<option value="${team.teamMemberStr}">${team.teamName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-lg-4">
								<label>Member:</label>
								<select class="form-control" id="Mapmembers" name="members" style="width:100%">
									<option value="All">All</option>
									<c:forEach var="members" items="${membersList}" varStatus="counter">										
										<option value="${members.userId}">${members.firstLastName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div id="proStaffMap" style="height:500px;"></div>
					</div>
				</div>
			</div>
			
		</div>
		<!-- end:: Pro Staff Map -->
		
		<!-- begin: Image Carousel -->
		<script src="custom_resources/js/swiper.min.js"></script>
		<link href="custom_resources/css/swiper.min.css" rel="stylesheet" type="text/css" />
		
	 	<style>
		 	.swiper-container {
		 	      width: 100%;
		 	      height: 550px;
		 	      margin-top: 25px;
		 	      margin-bottom: 25px;
		 	}
		 	.swiper-slide {
		 	      text-align: center;
		 	      font-size: 18px;
		 	      background: #fff;
	
		 	      /* Center slide text vertically */
		 	      display: -webkit-box;
		 	      display: -ms-flexbox;
		 	      display: -webkit-flex;
		 	      display: flex;
		 	      -webkit-box-pack: center;
		 	      -ms-flex-pack: center;
		 	      -webkit-justify-content: center;
		 	      justify-content: center;
		 	      -webkit-box-align: center;
		 	      -ms-flex-align: center;
		 	      -webkit-align-items: center;
		 	      align-items: center;
		 	      margin-bottom: 25px;
		 	}
		 	
		 	.title {
		        bottom: 20px;
		        left: 20px;
		        float: left;
		        position: absolute;
		        width: 100%;
		        z-index: 9999;
		        color: black;
		        font-size: 0.75em;
		        margin-bottom: 0px;
		    }
		      .swiper-slide img{
			     max-width: 500px;
		 	      max-height: 500px;
			  }
		    
		    .swiper-slide .caption {
			    position: absolute;
			    bottom: 50px;   
			    color: white;
			    font-weight: bold;
			    font-size:25pt;
			    font-family: 'Mark Bold' !important;
			    background: black;
    			opacity: 0.7;
			  }
    
    
	 	</style>
		
		<div class="col-xl-6">
		    <div class="kt-portlet kt-portlet--tabs kt-portlet--height-fluid">
				<div class="kt-portlet kt-portlet--tab">
					<div class="kt-portlet__head">
						<div class="kt-portlet__head-label">
							<span class="kt-portlet__head-icon kt-hidden">
								<i class="la la-gear"></i>
							</span>
							<h3 class="kt-portlet__head-title">
								Popular Posts
							</h3>
						</div>
					</div>
					<div class="swiper-container">
						<div class="swiper-wrapper" style="padding: 25px;">
							<c:forEach var="totalsObj" items="${carousilImageList}" varStatus="counter">
							    <div class="swiper-slide">
							    	<img src="${totalsObj.standardImage}">
									<div class="caption"><i class="flaticon-like"></i> ${totalsObj.likesCount}
										&nbsp;<i class="flaticon-comment"></i> ${totalsObj.commentsCount}</div>
							    </div>
							    
							</c:forEach>
						</div>
						<div class="swiper-pagination"></div>
						<div class="swiper-button-next"></div>
						<div class="swiper-button-prev"></div>
					</div>
					 
				</div>
			</div>
		</div>
		<!-- end: Image Carousel -->
		
	</div>
	<!--End::Row 2-->	 
</div>
<!--End:: Dashboard Root-->