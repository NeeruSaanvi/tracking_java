<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
	<div id="sucessErrorMsgDiv"></div>
		<div class="kt-portlet kt-portlet--mobile">
		<form class="kt-form kt-form--label-right" id="templateForm">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Update ${notification.type } Template
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row">
					<div class="col-lg-8">
						<label>Subject Line ( ! Not More then 300 Characters)</label>
						<input type="hidden" name="id" value="${notification.id }">
						<input type="text" name="subject" class="form-control" value="${notification.subject }" >
					</div>	
				</div>	
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>
				<div class="row">
					<div class="col-lg-8">
						<label>Body Text (! Not More then 3000 Characters)</label>
						<div class="col-lg-8 border">
						<textarea name="bodytext" id="bodytext"  class="textFormat" rows="10" >${notification.bodytext }</textarea>  </div>
					</div>	
				</div>
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>
				<div class="row">
					<div class="col-lg-8">
						<label>Signature ( ! Not More then 700 Characters)</label>
						<div class="col-lg-8 border">
						<textarea name="signature" id="signature" class="textFormat" rows="5">${notification.signature }</textarea> </div>
					</div>	
				</div>
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>	
				<div class="row">
					<div class="col-lg-8">
						<label>Attachment?</label>
						<input type="file" name="attachmentFile" class="form-control" id="attachmentFile">
					</div>
					<div class="col-lg-4">&nbsp;</div>
					<c:if test="${not empty  notification.attachment}">
						<div class="col-lg-8" style="margin-top: 10px;">
							<label><a href="${notification.attachment}" target="_blank">Download Attachment</a></label>
							<p><small style="color:#FF3300;">Caution:New upload will replace this attachment!</small></p>
						</div>		
						<div class="col-lg-4">&nbsp;</div>
					</c:if>
				</div>
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>	
				<div class="row">
					<div class="col-lg-8">
						<label>Attachment?</label>
						<input type="file" name="attachmentFile1" class="form-control" id="attachmentFile1">
					</div>
					<div class="col-lg-4">&nbsp;</div>
					<c:if test="${not empty notification.attachment1}">
						<div class="col-lg-8" style="margin-top: 10px;">
							<label><a href="${notification.attachment1}" target="_blank">Download Attachment</a></label>
							<p><small style="color:#FF3300;">Caution:New upload will replace this attachment!</small></p>
						</div>
						<div class="col-lg-4">&nbsp;</div>		
					</c:if>	
				</div>
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>	
				<div class="row">
					<div class="col-lg-8">
						<label>Attachment?</label>
						<input type="file" name="attachmentFile2" class="form-control" id="attachmentFile2">
					</div>
					<div class="col-lg-4">&nbsp;</div>
					<c:if test="${not empty notification.attachment2}">
						<div class="col-lg-8" style="margin-top: 10px;">
							<label><a href="${notification.attachment2}" target="_blank">Download Attachment</a></label>
							<p><small style="color:#FF3300;">Caution:New upload will replace this attachment!</small></p>
						</div>		
						<div class="col-lg-4">&nbsp;</div>
					</c:if>		
				</div>
				<div class="row"><div class="col-lg-12">&nbsp;</div></div>	
				<div class="row">
					<div class="col-lg-8">
						<label>Disable / Enable?</label>
						<div class="kt-radio-inline">
							<label class="kt-radio kt-radio--bold kt-radio--brand">
								<input type="radio" name="status" value="Block"> Disable
								<span></span>
							</label>
							<label class="kt-radio kt-radio--bold kt-radio--brand">
								<input type="radio" name="status" value="Active"> Enable
								<span></span>
							</label>
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
							<button type="button" id="backBtn" class="btn btn-primary">Back</button>
						</div>
					</div>
				</div>
			</div>
			</form>
		</div>
	</div>
	
</div>



<script type="text/javascript">

	$('#backBtn').click(function() {
		window.location.href = "/notifications";	
	});

	$('#userDataSubmit').click(function() {
		
		var form = $('#templateForm')[0];

		var data = new FormData(form);
		
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/updateTemplateMsg',
			enctype: 'multipart/form-data',
			type : 'POST',
			data: data,
			processData: false,
			contentType: false,
			timeout: 600000,
			beforeSend	: function(xhr){
				$('#sucessErrorMsgDiv').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(msg) {
				$('#sucessErrorMsgDiv').html('');
				if(msg == 'success') {
					showMessage("Your changes have been saved successfully."); 
					window.location.reload(); 
				}
				else {
					showError(msg.message); 
					window.location.reload();
				}
			}
		});
			
	});

	jQuery(document).ready(function () {
		
		$("input[name=status][value='${notification.status }']").prop("checked",true);
		$('#bodytext').summernote( getTextAreaMenu() );
		$('#signature').summernote( getTextAreaMenu() );
		
    });
	
	

 </script>