
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
						User Store
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<!-- <div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" onclick='updateWeb("","")'>
								<i class="la la-plus"></i>
								Add Web 
							</a>
						</div>
					</div> -->
				</div>
			</div>
			<div class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="userStoreTable">
					<thead>
                       	<tr>
                       		<th class="dt-header-right">Action</th>
                       		<th class="dt-header-right">Store Name</th>
                            <th class="dt-header-right">Store Date</th>
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
<div class="modal fade" id="webPostModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
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

	function updateUserStore(data, row){
		$("#storeId").val(row['storeId']);
		
		$("#webPostModal").modal("show");
	}
	
	function viewUserStore(data, row){
		var storeId = row['storeId'];
		window.location.href = '<%=request.getContextPath()%>/viewUserStore?storeId='+storeId;
	}

	function sendItRenderer(data, type, row) {
		return "<button type=\"button\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\" onclick='updateUserStore(" + data + ", "+JSON.stringify(row)+" )'>Send it</button>&nbsp;"+
			"<button type=\"button\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\" onclick='viewUserStore(" + data + ", "+JSON.stringify(row)+" )'>View</button>";
	}
	
	function getUserStoreColumnData() {
        
		return [
			{ data : 'store_id', width: '100px', render: sendItRenderer, defaultContent : '', className: 'dt-left' },
			{ data : 'store_name', width: '125px', defaultContent : '', className: 'dt-left' },
			{ data : 'store_date', width: '125px', defaultContent : '', orderable: true, className: 'dt-left' }
		];
	}

	function initUserStoreList() {
		
		$('#userStoreTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/userStoreList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: beforeDataSubmit,
			},
			columns 		: getUserStoreColumnData(),
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
    	initUserStoreList(); 
    });

 </script>