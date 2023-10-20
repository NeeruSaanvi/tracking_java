<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Schedule Report
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" onclick='updateScheduleReport("","")'>
								<i class="la la-plus"></i>
								Add Report 
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="scheduleReportTable">
					<thead>
                       	<tr>
                       		<th class="dt-header-right">Action</th>
                            <th class="dt-header-right">Report Name</th>
                            <th class="dt-header">Next Scheduled Delivery</th>
                          </tr>
                    	</thead>
                    <tbody>
                    </tbody>
				</table>
			</div>
		</div>
	</div>
	
</div>

<!--begin::Modal-->
<div class="modal fade" id="scheduleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Schedule Report</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<!--begin::Form-->
				<form class="kt-form" id="scheduleReportForm">
					<div class="kt-portlet__body">
						<div id="loader"></div>
						<div id="sucessErrorMsgDiv"></div>
						
						<div class="kt-section kt-section--first">
							<div class="form-group">
								<label>Report Name *</label>
								<input type="hidden" name="scheduleId" id="scheduleId">
								<input type="text" name="reportName" id="reportName" class="form-control" placeholder="Enter full name">
							</div>
							<div class="form-group">
								<label for="members">Team*</label>
								<div class="col-lg-12" style="padding-left: 0px;">
									<select multiple="" class="form-control" id="team" name="team" style="width:100%">
										<option value="All" selected="selected">All</option>
										<c:forEach var="team" items="${teamList}" varStatus="counter">
											<option value="${team.teamId}">${team.teamName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label for="members">Member*</label>
								<div class="col-lg-12" style="padding-left: 0px;">
									<select multiple="" class="form-control" id="members" name="members" style="width:100%">
										<option value="All" selected="selected">All</option>
										<c:forEach var="members" items="${membersList}" varStatus="counter">
											<option value="${members.userId}">${members.firstLastName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="row">
								<div class="form-group col-lg-3">
									<label for="reportType">Report Type*</label>
									<select class="form-control" name="reportType" id="reportType">
										<option value="social">Social</option>
										<option value="social_nonsocial">Social + NonSocial</option>
									</select>
								</div>
								<div class="form-group col-lg-4">
									<label>Report Start Date *</label>
										<input type="text" class="form-control" id="startDate" name="startDate" placeholder="Select date" />
								</div>
								
								<div class="form-group col-lg-5">
									<label>Report Frequency *</label>
									<div class="kt-radio-inline">
										<label class="kt-radio kt-radio--bold kt-radio--brand">
											<input type="radio" name="frequency" value="Weekly"> Weekly
											<span></span>
										</label>
										<label class="kt-radio kt-radio--bold kt-radio--brand">
											<input type="radio" name="frequency" value="Monthly"> Monthly
											<span></span>
										</label>
										<label class="kt-radio kt-radio--bold kt-radio--brand">
											<input type="radio" name="frequency" value="Quartely"> Quarterly
											<span></span>
										</label>
									</div>
								</div>
								
							</div>
							<div class="row">
							</div>
							
							<div class="form-group">
								<label for="reportType">Recipients</label>
								<div class="row">
									<div class="col-lg-4">
										<input type="text" name="recipient1" id="recipient1" class="form-control" placeholder="Enter email address" />	
									</div>
									<div class="col-lg-4">
										<input type="text" name="recipient2" id="recipient2" class="form-control" placeholder="Enter email address" />	
									</div>
									<div class="col-lg-4">
										<input type="text" name="recipient3" id="recipient3" class="form-control" placeholder="Enter email address" />	
									</div>
								</div>
								<div class="row" style="padding-top: 10px;">
									<div class="col-lg-4">
										<input type="text" name="recipient4" id="recipient4" class="form-control" placeholder="Enter email address" />	
									</div>
									<div class="col-lg-4">
										<input type="text" name="recipient5" id="recipient5" class="form-control" placeholder="Enter email address" />	
									</div>
								</div>
							</div>
											
						</div>
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
							<button type="button" id="scheduleReportDataSubmit" class="btn btn-primary">Submit</button>
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

	$('#scheduleReportDataSubmit').click(function() {
		
		var param = {
			scheduleId: $('#scheduleId').val(),
			reportName: $('#reportName').val(),
			members: $('#members').val(),
			team: $('#team').val(),
			frequency: $("input[name='frequency']:checked").val(),
			startDate: $('#startDate').val(),
			reportType: $('#reportType').val(),
			perviousChangeInclude: $('#perviousChangeInclude').val(),
			recipient1: $('#recipient1').val(),
			recipient2: $('#recipient2').val(),
			recipient3: $('#recipient3').val(),
			recipient4: $('#recipient4').val(),
			recipient5: $('#recipient5').val()
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/saveScheduleReport',
			type : 'POST',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your changes have been saved successfully."); 
					$('#scheduleReportTable').DataTable().ajax.reload( null, false );
					$("#scheduleModal").modal("hide");
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	function updateScheduleReport(data, row){	
		if(row == "" || row == undefined){
			$('#scheduleId').val('');
			$('#reportName').val('');
			$('#members').val('');
			$('#team').val('');
		}else{
			 
			$('#scheduleId').val(row['scheduleId']);
			$('#reportName').val(row['reportName']);
			
			var members = row['members'];
			if(members != '' && members != undefined){
				var nameArr = members.split(',');
				$('#members').val(nameArr).change();
			}
			
			var team = row['team'];			
			if(team != null && team != '' && team != undefined){
				var nameArr = team.split(',');
				$('#team').val(nameArr).change();
			}
			
			var freq = row['frequency'];
			$('input[name=frequency][value="'+freq+'"]').attr('checked', true); 
			
			var sDate = row['startDate'];
			
			if(sDate != null && sDate != undefined){
				$('#startDate').val(sDate.substring(0, 10));
			}
			
			$('#reportType').val(row['reportType']);
			var perviousChangeInclude = row['perviousChangeInclude'];
			if(perviousChangeInclude){
				$('#perviousChangeInclude').val('true');
			}else{
				$('#perviousChangeInclude').val('false');	
			}
			
			$('#recipient1').val(row['recipient1']);
			$('#recipient2').val(row['recipient2']);
			$('#recipient3').val(row['recipient3']);
			$('#recipient4').val(row['recipient4']);
			$('#recipient5').val(row['recipient5']);
		}
		  
		$("#scheduleModal").modal("show");
		
	}

	function editDeleteScheduleReportRenderer(data, type, row) { 
		return "<img src=\"resources/assets/media/icons/svg/Design/Edit.svg\" onclick='updateScheduleReport(" + data + ", "+JSON.stringify(row)+" )' />"; 
	}
	
	function dateRenderer(data, type, row) { 
		return data.substring(0, 10);
	}

	function getScheduleReportColumnData() {
		
		return [
			{ data : 'scheduleId', width: '100px', render: editDeleteScheduleReportRenderer, defaultContent : '', className: 'dt-left' },
			{ data : 'reportName', width: '125px', defaultContent : '', className: 'dt-left' },
			{ data : 'startDate', width: '225px', render: dateRenderer, defaultContent : '', orderable: true, className: 'dt-left' }
		];
	}

	function initTrendsList() {
		
		$('#scheduleReportTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/scheduleReportList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: beforeDataSubmit,
			},
			columns 		: getScheduleReportColumnData(),
			oLanguage		: getDatatablesLoadingText(),
			order			: [[ 0, 'asc' ]],
			paging 			: false,
			ordering 		: true,
			info 			: false,
			displayLength	: window.innerHeight < 1000 ? 10 : 25,
			lengthMenu: [10, 25, 50],
		});
	}
	

	jQuery(document).ready(function () {
		
		initTrendsList();
		
		var dateTypeVar = $('#startDate').datepicker({
			todayHighlight: true,
			format: 'yyyy-mm-dd' ,
			//minDate: new Date(),
			startDate: "+0d" ,
			autoclose: true,
			templates: {
				leftArrow: '<i class="la la-angle-left"></i>',
				rightArrow: '<i class="la la-angle-right"></i>',
			},
		}); 
		
		$('#members, #team').select2({
            placeholder: "All",
        });
        
		$('#team').on('select2:select', function (e) {
		    var data = e.params.data;
		    if(data != null && data.id != 'All'){
		    	$('#members').prop('disabled', true);
		    }
		});
		
		$('#members').on('select2:select', function (e) {
		    var data = e.params.data;
		    if(data != null && data.id != 'All'){
		    	$('#team').prop('disabled', true);
		    }
		});
		
		$('#members').on('change', function (e) {
		    var data = $('#members').val();
		    if(data == 'All'){
		    	$('#team').prop('disabled', false);
		    }
		});
		
		$('#team').on('change', function (e) {
		    var data = $('#team').val();
		    if(data == 'All'){
		    	$('#members').prop('disabled', false);
		    }
		});
		
		
		
    });

 </script>