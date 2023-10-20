<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
String addUser = request.getParameter("addUser") == null ? "" :request.getParameter("addUser");

%>
<div id="loader" style="position: fixed; margin-left: 40%; margin-top: 20%; z-index: 99999999;"></div>
<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
		
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Manage User
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<div class="col-lg-4 kt-margin-b-10-tablet-and-mobile">
						<input type="text" id="nameSearch" class="form-control" placeholder="Enter name">
					</div>
					<div class="col-lg-4 kt-margin-b-10-tablet-and-mobile">
						<input type="email" id="emailSearch" class="form-control" placeholder="Enter email">
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<button class="btn btn-primary btn-brand--icon" id="kt_search">
							<span>
								<i class="la la-search"></i>
								<span>Search</span>
							</span>
						</button>
					</div>
				</div>
				
				<div class="kt-portlet__head-toolbar">
				<form method="POST" enctype="multipart/form-data" action="/rest/uploadUser">
					<input type="file" name="file" style="width: 95px;"/>
				    <input type="submit" class="btn btn-brand btn-elevate btn-icon-sm" value="Import"/>
				</form> 
				</div>
				<div class="kt-portlet__head-toolbar">
					<div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" href="/userexporttoexcel">
								Export
							</a>
						</div>
					</div>
				</div> 
				
				<div class="kt-portlet__head-toolbar">
					<div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions" >
							<a class="btn btn-brand btn-elevate btn-icon-sm" onclick='updateUser("","")'>
								<i class="la la-plus"></i>
								Add User 
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="kt-portlet__body">
				
				<table class="table table-striped- table-bordered table-hover table-checkable" id="userTable">
					<thead>
                       	<tr>
                       	 	
                            <th>Name</th>
                            <th class="longtext">Email</th>
                            <th >Phone</th>
                            <th class="longtext" >Team</th>
                            <th>Social Media</th>
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
<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="exampleModalLabel">User</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"></span>
				</button>
			</div>
			<div class="kt-portlet">
				
				<form class="kt-form kt-form--label-right" id="userForm">
				<div id="sucessErrorMsgDiv"></div>
					<div class="kt-portlet__body">
						<div class="form-group row">
							<div class="col-lg-4">
								<label>First Name:</label>
								<input type="hidden" name="userId" id="userId" />
								<input type="hidden" name="teamArray" id="teamArray" />
								
								<input type="email" name="firstName" id="firstName" class="form-control" placeholder="Enter full name">
							</div>
							<div class="col-lg-4">
								<label>Last Name:</label>
								<input type="email" name="lastName" id="lastName" class="form-control" placeholder="Enter full name">
							</div>
							<div class="col-lg-4">
								<label class="">Email:</label>
								<input type="email" name="email" id="email" class="form-control" placeholder="Enter email">
							</div>
						</div>
						<div class="form-group row">
							<div class="col-lg-4">
								<label>Address:</label>
								<div class="kt-input-icon kt-input-icon--right">
									<input type="text" name="street" id="street" class="form-control" placeholder="Enter your address">
									<span class="kt-input-icon__icon kt-input-icon__icon--right"><span><i class="la la-map-marker"></i></span></span>
								</div>
							</div>
							<div class="col-lg-4">
								<label class="">State:</label>
								<select class="form-control" id="state" name="state" style="width:100%">
									<c:forEach var="state" items="${stateMap}" varStatus="counter">
										<option value="${state.key}">${state.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-lg-4">
								<label class="">Zip Code:</label>
								<div class="kt-input-icon kt-input-icon--right">
									<input type="text" name="zipcode" id="zipcode" class="form-control" placeholder="Enter your postcode">
									<span class="kt-input-icon__icon kt-input-icon__icon--right"><span><i class="la la-bookmark-o"></i></span></span>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-lg-4">
								<label class="">Phone:</label>
								<div class="kt-input-icon kt-input-icon--right">
									<input type="text" name="phone" id="phone" class="form-control" placeholder="Enter phone number">
									<span class="kt-input-icon__icon kt-input-icon__icon--right"><span><i class="la la-info-circle"></i></span></span>
								</div>
							</div>
							<div class="col-lg-4">
								<label class="">Discount Code:</label>
								<input type="email" name="discountCode" id="discountCode" class="form-control" placeholder="Enter discount code">
							</div>
							
							<div class="col-lg-4">
								<label for="members">Team*</label>
								<div class="col-lg-12" style="padding-left: 0px;">
									<select multiple="" class="form-control" id="team" name="team" style="width:100%">
										<c:forEach var="team" items="${teamList}" varStatus="counter">
											<option value="${team.teamId}">${team.teamName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					
					</div>
					<div class="kt-portlet__foot">
						<div class="kt-form__actions">
							<div class="row">
								<div class="col-lg-4"></div>
								<div class="col-lg-8">
									<button type="button" id="userDataSubmit" class="btn btn-primary">Submit</button>
									<button type="button" class="btn btn-brand" data-dismiss="modal">Close</button>
								</div>
							</div>
						</div>
					</div>
				</form>
				
			</div>
		</div>
	</div>
</div>

<!--end::Modal-->


<script type="text/javascript">

	var deleted = 0;

	$('#userDataSubmit').click(function() {
		
		var teamArray = [];
		team = $('#team').val();
		
		for(var i=0; i< team.length; i++){
			teamArray.push(team[i]);
		}
		
		$('#teamArray').val(teamArray);
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/saveUser',
			type : 'POST',
			data : JSON.stringify($("#userForm").serializeObject()),
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your changes have been saved successfully."); 
					$('#userTable').DataTable().ajax.reload( null, false );
					$("#userModal").modal("hide");
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});
	
	function viewUser(data, row){
		window.location = "<%=request.getContextPath()%>/userProfile?userId="+data;
	}

	function updateUser(data, row){	
		if(row == "" || row == undefined){
			console.log("update user111")
			 $("#firstName").val('');
			 $("#lastName").val('');
			 $("#email").val('');
			 $("#street").val('');
			 $("#state").val('');
			 $("#zipcode").val(''); 
			 $("#phone").val('');
			 $('#team').val(''); 
		}else{
			console.log("update user112")

			 $("#userId").val(row['userId']);
			 var firstLastName = row['firstLastName'].split(" ");
			 $("#firstName").val(firstLastName[0]);
			 $("#lastName").val(firstLastName[1]);
			 $("#email").val(row['email']);
			 $("#street").val(row['street']);
			 $("#state").val(row['state']);
			 $("#zipcode").val(row['zipcode']); 
			 $("#phone").val(row['phone']);
			 $("#discountCode").val(row['discountCode']);
			 var teams = row['userTeam'];
			 $('#team').val(teams).change(); 
		}
		$("#userModal").modal("show");
		
	}
	
	function deleteUser(data, row){
		
		swal.fire({
            title: 'Are you sure want to delete?',
            text: "You won't be able to revert this!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!'
        }).then(function(result) {
            if (result.value) {
            	
            	$.ajax({
        			url : '<%=request.getContextPath()%>/rest/deleteUser?userId='+row['userId'],
        			type : 'POST',
        			async 		: true,
        			beforeSend	: function(xhr){
        				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
        			},
        			success : function(msg) {
        				deleted = 1;
        				$('#loader').html('');
        				
        				if(msg == 'success') {
        					swal.fire(
        		                    'Deleted!',
        		                    'Your staff user has been deleted.',
        		                    'success'
        		                )   
        		                $('#userTable').DataTable().ajax.reload( null, false );
        				}
        				else {
        					showError(msg.message); 
        				}
        			}
        		});
            }	
                
        });			
	}

	function photoRenderer(data, type, row){
		
		return '<div class="kt-user-card-v2">'+
			/* '<div class="kt-user-card-v2__details">'+
				'<a href="/userProfile?userId=' + row.userId + '" class="kt-user-card-v2__name">' + row.firstLastName + '</a>'+
			'</div>'+ */
			'<div class="kt-user-card-v2__details">'+
				'<a href="#" class="kt-user-card-v2__name">' + row.firstLastName + '</a>'+
			'</div>'+
		'</div>';
	}
	
	function viewRenderer(data, type, row){
		return "<button type='button' class='btn btn-brand btn-elevate btn-pill btn-sm' onclick='viewUser(" + data + ", "+JSON.stringify(row)+" )'><i class='far fa-eye'></i></button>&nbsp;"+
		"<button type='button' class='btn btn-brand btn-elevate btn-pill btn-sm' onclick='updateUser(" + data + ", "+JSON.stringify(row)+" )'><i class='far fa-edit'></i></button>&nbsp;"+
		"<button type='button' class='btn btn-brand btn-elevate btn-pill btn-sm' onclick='deleteUser(" + data + ", "+JSON.stringify(row)+" )'><i class='far fa-trash-alt'></i></button> ";
	}
	function getUserColumnData() {
		return [
			
			{ data : 'firstLastName', width: '100px', render: photoRenderer, defaultContent : '' },
			{ data : 'email', width: '50px',  defaultContent : '', className:'longtext' },
			{ data : 'phone', width: '50px', defaultContent : '' },
			{ data : 'userTeamName', width: '50px', defaultContent : '' },
			{ data : 'linkedMediaHTML', width: '80px', defaultContent : '' },
			{ data : 'userId', width: '100px',  render: viewRenderer, defaultContent : '' }
			
		];
	}
	
	function userParameters(data) {
		beforeDataSubmit(data);	
		
		data.name = $('#nameSearch').val();
		data.email = $('#emailSearch').val();
	}

	function initUsersList() {
		
		$('#userTable').dataTable({
			responsive: true,
			
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/userList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: userParameters
			},
			deferRender 	: true,
			columns 		: getUserColumnData(),
			order			: [[ 0, 'asc' ]],
			autoWidth: false,
			paging 			: true,
			ordering 		: true,
			info 			: true,
			displayLength	: window.innerHeight < 1000 ? 25 : 25,
			lengthMenu: [5, 10, 25, 50],
			fnDrawCallback: function( oSettings ) {
				$('#loader').html('');
		    },
		    preDrawCallback: function( oSettings ) {
		    	$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
		    }
		});
	} 
	
	$('#kt_search').on('click', function(e) {
		e.preventDefault();
		initUsersList(); 
	});

	jQuery(document).ready(function () {
		<%
		if(addUser.equalsIgnoreCase("1")){
		%>
			$("#userModal").modal("show");
		<%}%>
		
    	initUsersList(); 
    	
    	$('#members, #team').select2({
            placeholder: "All",
        });
    	
    	$("td#longtext").hover(function(e){   
  		  $(this).css("transform","scale(1.3)");
  		  e.stopPropagation();
  		});
    	
    });
	
	

 </script>