<script src="resources/assets/vendors/general/tags/jquery.tagsinput.min.js" type="text/javascript"></script>

<style>
<!--
.tagsinput {
    border: 0;
    background: transparent;
}
span.tag {
    -moz-border-radius: 2px;
    -webkit-border-radius: 2px;
    display: block;
    float: left;
    padding: 5px 9px;
    text-decoration: none;
    background-color: #ffffff;
    color: #791214;
    margin-right: 5px;
    font-weight: 500;
    margin-bottom: 5px;
    font-family: helvetica;
}
.tag {
    line-height: 1;
    background: #1ABB9C;
    /* color: #fff !important; */
    color: #791214 !important;
    border: 1px solid #ccc;
}
.tagsinput span.tag span {
    color: #333;
    font-weight: 600;
}
.tagsinput span.tag a {
	font-family: 'Open Sans', sans-serif;
    font-weight: 500;
    padding: 1px 1px 0px 1px;
    line-height: 1.3;
    width: 13px;
    height: 13px;
    text-align: center;
    display: inline-block;
    border-radius: 200px;
    -moz-border-radius: 200px;
    color: #fff!important;
    text-decoration: none;
    font-size: 9px;
    background: #888;
    float: right;
    margin-top: ;
}
.tagsinput input {
    font-size: 11px!important;
    padding: 4px 10px;
    width: 90px!important;
    text-align: center;
    border: none!important;
    box-shadow: none!important;
    -moz-box-shadow: none!important;
    border-radius: 3px!important;
    -moz-border-radius: 3px!important;
    margin: 0 0 5px 5px!important;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-size: 12px!important;
    background-color: #22B9FF !important;
    color: #fff!important;
    cursor: pointer;
}
-->
</style>
<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	<div id="sucessErrorMsgDiv"></div>
	<div id="loader"></div>
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Keywords (To add a new keyword, click "Add A Tag", enter the keyword and press enter)
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<form class="kt-form" id="keywordForm">
				<div class="row">
					<div class="col-lg-12">
						<div class="widget-box">
							<div class="widget-content">
								<input type="text" name="txtKeywords" id="txtKeywords" value="${ keywordsList }" required="required" class="tags" />
							</div>
						</div>
					</div>
				</div>
				</form>

			</div>
			<div class="kt-portlet__foot">
				<div class="kt-form__actions">
					<button type="button" id="updatewords" class="btn btn-primary">Update</button>
				</div>
			</div>
		</div>
	</div>
	
</div>


<script type="text/javascript">

	$('#updatewords').click(function() {
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/updateKeyWords',
			type : 'POST',
			data : JSON.stringify($("#keywordForm").serializeObject()),
			contentType: 'application/json; charset=UTF-8',
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#loader').html('');
				if(msg == 'success') {
					showMessage("Your keywords have been saved successfully."); 
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});

	

	jQuery(document).ready(function () {
    	
		$('#txtKeywords').tagsInput({
            width: 'auto'
        });
    	
    });

 </script>