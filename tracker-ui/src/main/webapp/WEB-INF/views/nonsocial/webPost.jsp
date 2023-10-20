
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
						Web
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="webPostTable">
					<thead>
                       	<tr>
                       		<th class="dt-header-right">Action</th>
                       		<th class="dt-header-right">Member Name</th>
                            <th class="dt-header-right">Date</th>
                            <th class="dt-header">Website Name</th>
                            <th class="dt-header-right">URL</th>
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
				<h5 class="modal-title" id="exampleModalLabel">Web</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<!--begin::Form-->
				<form class="kt-form" id="webPostForm">
					<div class="kt-portlet__body">
						<div id="loader"></div>
						
						<div class="kt-section kt-section--first">
							<div class="form-group">
								<label>Website Name:</label>
								<input type="text" name="blogname" id="blogname" class="form-control" placeholder="Enter full name">
								<input type="hidden" name="blogId" id="blogId" />
								<span class="form-text text-muted">Please enter your full name</span>
							</div>
							<div class="form-group">
								<label>Website URL:</label>
								<input type="text" name="link" id="link" class="form-control" placeholder="Enter email">
								<span class="form-text text-muted">We'll never share your email with anyone else</span>
							</div>
							<div class="form-group">
								<label>Coverage Type:</label>
								<input type="text" name="coveragetype" id="coveragetype" class="form-control" placeholder="Enter email">
								<span class="form-text text-muted">We'll never share your email with anyone else</span>
							</div>							
						</div>
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
							<button type="button" id="webPostDataSubmit" class="btn btn-primary">Submit</button>
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

	function updateWeb(data, row){	
		if(row == "" || row == undefined){
			 $("#blogname").val('');
			 $("#link").val('');
			 $("#blogId").val('');
			 $("#coveragetype").val('');
		}else{
			 $("#blogname").val(row['blogname']);
			 $("#link").val(row['link']);
			 $("#blogId").val(row['blogId']);
			 $("#coveragetype").val(row['coveragetype'])
		}
		  
		$("#webPostModal").modal("show");
		
	}

	function editDeleteBankAccountRenderer(data, type, row) { 
		return '<button type="button" class="btn btn-brand btn-elevate btn-pill btn-sm">View</button>';
	}

	function getWebPostColumnData() {
        
		return [
			{ data : 'blogId', width: '100px', render: editDeleteBankAccountRenderer, defaultContent : '', className: 'dt-left' },
			{ data : 'firstLastname', width: '125px', defaultContent : '', className: 'dt-left' },
			{ data : 'postedDate', width: '125px', defaultContent : '', orderable: true, className: 'dt-left' },
			{ data : 'blogname', width: '325px', defaultContent : '' }, 			 
			{ data : 'link', width: '325px', defaultContent : '', className: 'dt-left' }
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
				url 		: '<%=request.getContextPath()%>/rest/webPostList',
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