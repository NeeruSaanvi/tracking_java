<script src="resources/assets/js/jquery.fileDownload.js" type="text/javascript"></script>

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
						Events
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="webPostTable">
					<thead>
                       	<tr>
                       		<th class="dt-header">Member Name</th>
                       		<th class="dt-header">Event Name</th>
                       		<th class="dt-header">Event Date</th>
                       		<th class="dt-header">Event Location</th> 
                       		<th class="dt-header">Event Attendance</th>
                       		<th class="dt-header">Download</th>
                       		<th class="dt-header">View</th>  
                       		                   		                            
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
<div class="modal fade" id="viewEventModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Event Detail</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<!--begin::Form-->
				<form class="kt-form" id="webPostForm">
					<div class="kt-portlet__body">
						<div id="loader"></div>
						
						<div class="alert alert-info alert-block">
							<span class="bold_text"><a id="eventtitle"></a></span>
						</div>
						
						<div class="info date">	
							<span class="bold_text">
								<a id="eventStartDate"></a>
							</span>
						</div>
						
						<div class="info desc">
							<p><a id="description"></a><br/></p>
						</div>
						
						<div class="info address">
							<span class="bold_text">Address</span>
							<p><a id="venueaddress"></a></p>
						</div>
						
						
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
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


	function editDeleteBankAccountRenderer(data, type, row) { 
		return "<button type=\"button\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\" onclick='viewEvent(" + data + ", "+JSON.stringify(row)+" )'>View</button>";
	}
	
	function viewEvent(data, row){	
		$("#description").text(row.description == null ? "" : row.description);
		$("#eventtitle").text(row.eventtitle);
		$("#eventStartDate").text(row.eventStartDate);
		$("#venueaddress").text(row.venueaddress);
		  
		$("#viewEventModal").modal("show");
	}
	
	function downloadAttachmentRenderer(data, type, row){
		if(row['eventAttachment'] != null && row['eventAttachment'] != ''){
			var filename = row['eventAttachment'];
			return "<a onclick=\" $.filedownload('" + filename + "'); \" >Download</a>"
		}else{
			return "";
		}
	}
	
	
	$.filedownload = function(filename) {
		
		window.open('<%=request.getContextPath()%>/viewFiles?fileName='+filename, '_blank');
		
	}

	function getWebPostColumnData() {
		return [
			{ data : 'firstLastname', width: '125px', defaultContent : '', className: 'dt-left' },
			{ data : 'eventtitle', width: '125px', defaultContent : '', orderable: true, className: 'dt-left' },
			{ data : 'eventStartDate', width: '225px', defaultContent : '' }, 			 
			{ data : 'venueaddress', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'attendanceCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'eventId', width: '225px', render: downloadAttachmentRenderer, defaultContent : '', className: 'dt-left' },
			{ data : 'eventId', width: '100px', render: editDeleteBankAccountRenderer, defaultContent : '', className: 'dt-left' }
		];
	}

	function initTrendsList() {
		
		$('#webPostTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/eventsList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: beforeDataSubmit,
			},
			columns 		: getWebPostColumnData(),
			oLanguage		: getDatatablesLoadingText(),
			order			: [[ 0, 'asc' ]],
			paging 			: true,
			ordering 		: true,
			info 			: true,
			displayLength	: window.innerHeight < 1000 ? 5 : 25,
			lengthMenu: [5, 10, 25, 50],
		});
	}
	

	jQuery(document).ready(function () {
    	initTrendsList(); 
    	
    });

 </script>
 
 