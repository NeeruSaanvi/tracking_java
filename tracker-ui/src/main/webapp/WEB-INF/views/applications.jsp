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
						Application
					</h3>
				</div>
				<<!-- <div class="kt-portlet__head-toolbar">
				</div> -->
			</div>
			<div  class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="webPostTable">
					<thead>
                       	<tr>
                       		<!-- <th class="dt-header">Action</th> -->
                       		<th >Applicant Name</th>
                       		<th >Email</th>
                       		<th >Contact No.</th>
                       		<th >Status</th>  
                       		<th >Dated</th>
                       		<th >Action</th>                      		                            
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
	
	function viewApplication(userId, link){
		if(link == 'ambassadorlink'){
			window.location.href = '<%=request.getContextPath()%>/viewApplication?id='+userId+'&link=ambassadorlink';
		}else{
			window.location.href = '<%=request.getContextPath()%>/viewApplication?id='+userId+'&link=';
		}
	}

	function editDeleteBankAccountRenderer(data, type, row) {
		return '<input name="chkb[]" type="checkbox" value="'+data+'" />'
	}
	
	function deleteApplication(userId, link){
		var companyId = '<%= com.tracker.ui.utils.UserUtils.getLoggedInUserId() %>';
		$.ajax({
			url: '/rest/deleteApplication/'+companyId+'/'+userId+'/'+link,
			method:'DELETE',
			success:function(){
				$('#webPostTable').DataTable().ajax.reload( null, false );
				showMessage("Application has been deleted.");
			},
			error:function(ex){
				showError("Unable to delete application.");
			}
		})
	}

	function viewRenderer(data, type, row){
		var rowData = JSON.stringify(row);
		var memType = row['memType']
		
		return "<button type=\"button\" onclick=\"viewApplication(" + data + ",'"+memType+"' )\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\"><i class='far fa-eye'></i></button>&nbsp;"+
		"<button type=\"button\" onclick=\"deleteApplication(" + data + ",'"+memType+"' )\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\"><i class='far fa-trash-alt'></i></button>";
	}
	
	function statusRenderer(data, type, row){
		if(data == 'ZRejected'){
			data = "Rejected";
			return "<span style='color: red;'>"+data+"</span>";
		}
		return data;
	}

	function getWebPostColumnData() {
		
		return [
			{ data : 'firstLastName', width: '225px', defaultContent : '' },
			{ data : 'email', width: '225px', defaultContent : '', orderable: true},
			{ data : 'phone', width: '225px', defaultContent : '' }, 			 
			{ data : 'status', width: '125px', defaultContent : '', render: statusRenderer },
			{ data : 'registrationDate', width: '125px', defaultContent : '' },
			{ data : 'userId', width: '100px', render: viewRenderer, defaultContent : ''}
		];
	}

	function initTrendsList() {
		
		$('#webPostTable').dataTable({
			responsive: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/applicationList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
			},
			columns 		: getWebPostColumnData(),
			order			: [[ 3, 'asc' ],[ 4, 'desc' ]],
			
		});
	}

	jQuery(document).ready(function () {
    	initTrendsList(); 
    	
    	$('#kt_search').on('click', function(e) {
			e.preventDefault();
			
			var bulk_action	= $('#bact').val();			
			var checked_num = $('input[name="chkb[]"]:checked').length;

			if(bulk_action == '' || bulk_action.length==0){
				alert("Please select required Action");
			}
			else if(!checked_num){
				alert("Please select Applicants.");	
			}
			else{
				if(confirm("Sure to Apply Bulk Action over Selection?")){
				
					var e = document.getElementById("bact");
					ids=document.getElementsByName('chkb[]');
					obj = e.options[e.selectedIndex].value;
					val=obj;
					temp=val.split("#"); 
					
				  check=document.getElementsByName('chkb[]');
				  var idlist='';
				  j=0;
				  for(i=0;i<check.length;i++){
					  if(check[i].checked){
						  idlist = idlist+'#'+check[i].value;
						  j++;
					  }
				  }
				  
					$.ajax({
						type: 'POST',                          
						data:{ids:idlist,action:temp[0]},
						url : '<%=request.getContextPath()%>/rest/updateApplications',
						cache: false,
						success: function(data){ 		
							location.reload();	                   
						}           
					});
					
				}
			}	
			return false;
			
		});
    	
    });

 </script>