<%@page import="com.tracker.ui.utils.UserUtils"%>
<%@page import="com.tracker.services.utils.PasswordUtil"%>

<style>
.hidden{
	visibility: hidden;
}
</style>

<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						Edit Score 
					</h3>
				</div>
				
			</div>
			<div class="kt-portlet__body">
				<div id="loader"></div>
				<div id="sucessErrorMsgDiv"></div>
			
				<div class="row kt-margin-b-20">
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">Facebook Post:</label>
							<input type="hidden" id="id" value="${userSponsorScores.id }">
							<input type="text" class="form-control" name="fbpost" id="fbpost" value="${userSponsorScores.fbpost }">
						</div>
						
						<div class="form-group col-lg-3">
							<label>Facebook Likes:</label>
							<input type="text" class="form-control" name="fblike" id="fblike" value="${userSponsorScores.fblike }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Facebook Comments:</label>
							<input type="text" class="form-control"  name="fbcomments" id="fbcomments" value="${userSponsorScores.fbcomments }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Facebook Shares:</label>
							<input type="text" class="form-control"  name="fbshares" id="fbshares" value="${userSponsorScores.fbshares }"/>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">Tweets:</label>
							<input type="text" class="form-control" name="twttweets" id="twttweets" value="${userSponsorScores.twttweets }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Re-Tweets:</label>
							<input type="text" class="form-control" name="twtretweets" id="twtretweets" value="${userSponsorScores.twtretweets }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Twitter Favorites:</label>
							<input type="text" class="form-control"  name="twtfavourites" id="twtfavourites" value="${userSponsorScores.twtfavourites }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">Instagram Posts:</label>
							<input type="text" class="form-control" name="instaposts" id="instaposts" value="${userSponsorScores.instaposts }">
						</div>
						
						<div class="form-group col-lg-3">
							<label>Instagram Likes:</label>
							<input type="text" class="form-control" name="instalikes" id="instalikes" value="${userSponsorScores.instalikes }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Instagram Comments:</label>
							<input type="text" class="form-control"  name="instacomments" id="instacomments" value="${userSponsorScores.instacomments }"/>
						</div>
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">YouTube Posts:</label>
							<input type="text" class="form-control" name="ytposts" id="ytposts" value="${userSponsorScores.ytposts }">
						</div>
						
						<div class="form-group col-lg-3">
							<label>YouTube Likes:</label>
							<input type="text" class="form-control" name="ytlikes" id="ytlikes" value="${userSponsorScores.ytlikes }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>YouTube Views:</label>
							<input type="text" class="form-control"  name="ytviews" id="ytviews" value="${userSponsorScores.ytviews }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">Leads Submitted:</label>
							<input type="text" class="form-control" name="leadsubmt" id="leadsubmt" value="${userSponsorScores.leadsubmt }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Events Worked:</label>
							<input type="text" class="form-control" name="eventworked" id="eventworked" value="${userSponsorScores.eventworked }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label>Tournaments:</label>
							<input type="text" class="form-control"  name="tournaments" id="tournaments" value="${userSponsorScores.tournaments }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
						
						
					</div>
					
					<div class="row">
						<div class="form-group col-lg-3">
							<label for="reportType">Web Hits:</label>
							<input type="text" class="form-control" name="webhits" id="webhits" value="${userSponsorScores.webhits }"/>
						</div>
						
						<div class="form-group col-lg-3">
							<label for="reportType">Print Coverage:</label>
							<input type="text" class="form-control" name="prints" id="prints" value="${userSponsorScores.prints }"/>
						</div>
						
						
						
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
						
						<div class="form-group col-lg-3">
							<input type="text" class="form-control hidden" />
						</div>
					</div>
					
				</div>
				
			</div>
			
			<div class="kt-portlet__foot">
				<div class="kt-form__actions">
					<button type="button" id="scoreSettingsDataSubmit" class="btn btn-primary">Submit</button>
				</div>
			</div>
			
		</div>
	</div>
	
</div>




<script type="text/javascript">

	$('#scoreSettingsDataSubmit').click(function() {
		
		var param = {
			id: $('#id').val(),
			fbpost: $('#fbpost').val(),
			fblike: $('#fblike').val(),
			fbcomments: $('#fbcomments').val(),
			fbshares: $('#fbshares').val(),
			twttweets: $('#twttweets').val(),
			twtretweets: $('#twtretweets').val(),
			twtfavourites: $('#twtfavourites').val(),
			instaposts: $('#instaposts').val(),
			instalikes: $('#instalikes').val(),
			instacomments: $('#instacomments').val(),
			ytposts: $('#ytposts').val(),
			ytlikes: $('#ytlikes').val(),
			ytviews: $('#ytviews').val(),
			leadsubmt: $('#leadsubmt').val(),
			eventworked: $('#eventworked').val(),
			tournaments: $('#tournaments').val(),
			webhits: $('#webhits').val(),
			prints: $('#prints').val()
		
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/updateScoreSettings',
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
				}
				else {
					showError(msg.message); 
				}
			}
		});
			
	});


	jQuery(document).ready(function () {
    	
    	
    	
    });

 </script>