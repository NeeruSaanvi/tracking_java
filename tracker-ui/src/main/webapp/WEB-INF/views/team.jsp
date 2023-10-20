<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
						Team
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" onclick='updateTeam("","")'>
								<i class="la la-plus"></i>
								Add Team 
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="kt-portlet__body">
				<table class="table table-striped- table-bordered table-hover table-checkable" id="teamTable">
					<thead>
                       	<tr>
                       		<th class="dt-header-right">Action</th>
                            <th class="dt-header-right">Name</th>
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
<div class="modal fade" id="teamModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">Team</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<!--begin::Form-->
				<form class="kt-form" id="teamForm">
					<div class="kt-portlet__body">
						<div id="loader"></div>
						<input type="hidden" name="membersArray" id="membersArray" />
						<div class="kt-section kt-section--first">
							<div class="form-group">
								<label>Team Name:</label>
								<input type="text" name="teamName" id="teamName" class="form-control" placeholder="Enter full name">
								<input type="hidden" name="teamId" id="teamId" />
								<span class="form-text text-muted">Please enter team name</span>
							</div>
							<div class="form-group">
								<label>Members</label>
								<select multiple="" class="form-control" id="members" name="members" style="width:100%">
									<c:forEach var="members" items="${membersList}" varStatus="counter">
										<option value="${members.userId}">${members.firstLastName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
							<button type="button" id="teamDataSubmit" class="btn btn-primary">Submit</button>
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

	$('#teamDataSubmit').click(function() {
		
		var membersArray = [];
		members = $('#members').val();
		
		for(var i=0; i< members.length; i++){
			membersArray.push(members[i]);
		}
		
		$('#membersArray').val(membersArray);
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/saveTeam',
			type : 'POST',
			data : JSON.stringify($("#teamForm").serializeObject()),
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your changes have been saved successfully."); 
					$('#teamTable').DataTable().ajax.reload( null, false );
					$("#teamModal").modal("hide");
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	function updateTeam(data, row){	
		if(row == "" || row == undefined){
			 $("#teamId").val('');
			 $("#teamName").val('');
			 $("#members").val('').change();
		}else{
			 $("#teamId").val(row['teamId']);
			 $("#teamName").val(row['teamName']);
			 var members = row['teamMembers'];
			 $('#members').val(members).change();
		}
		  
		$("#teamModal").modal("show");
		
	}
	
	function deleteTeam(data, row){
		
		swal.fire({
            title: 'Are you sure want to delete?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!'
        }).then(function(result) {
            if (result.value) {
            	
            	$.ajax({
        			url : '<%=request.getContextPath()%>/rest/deleteTeam?teamId='+row['teamId'],
        			type : 'POST',
        			async 		: true,
        			beforeSend	: function(xhr){
        				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
        			},
        			success : function(msg) {
        				$('#loader').html('');
        				if(msg == 'success') {
        					
        					swal.fire(
        		                    'Deleted!',
        		                    'Your team has been deleted.',
        		                    'success'
        		                )
        				
        					$('#teamTable').DataTable().ajax.reload( null, false );
        				}
        				else {
        					showError(msg.message); 
        				}
        			}
        		});
            }	
                
        });			
	}

	function editDeleteTeamRenderer(data, type, row) { 
		return "<button type=\"button\" onclick='updateTeam(" + data + ", "+JSON.stringify(row)+" )' class=\"btn btn-brand btn-elevate btn-pill btn-sm\"><i class='far fa-edit'></i></button>&nbsp;"+
		"<button type=\"button\" onclick='deleteTeam(" + data + ", "+JSON.stringify(row)+" )' class=\"btn btn-brand btn-elevate btn-pill btn-sm\"><i class='far fa-trash-alt'></i></button>";
	}
	
	function nameTeamRenderer(data, type, row) { 
		return "<a style='cursor: pointer;' onclick='updateTeam(null, "+JSON.stringify(row)+" )' >"+data+"</a>"; 
	}

	function getTeamColumnData() {
		
		return [
			{ data : 'teamId', width: '20%', render: editDeleteTeamRenderer, defaultContent : '', className: 'dt-left' },
			{ data : 'teamName', width: '80%', render: nameTeamRenderer, defaultContent : '', className: 'dt-left' }
		];
	}

	function initTeamList() {
		
		$('#teamTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/teamList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: beforeDataSubmit,
			},
			columns 		: getTeamColumnData(),
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
    	initTeamList(); 
    	
    	$('#members').select2({
            placeholder: "All",
        });
    	
    	
    });

 </script>