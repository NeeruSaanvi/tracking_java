<%@page import="com.tracker.ui.utils.UserUtils"%>
<%@page import="com.tracker.services.utils.PasswordUtil"%>
<%
String md5 = PasswordUtil.encryptPassword(String.valueOf(UserUtils.getLoggedInUserId()));
%>
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
						EmbedCode
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row kt-margin-b-20">
					
					<div class="form-group">
										
								<div class="col-sm-9 col-md-9 col-lg-12">
									
									<div class="row">
										<div class="col-md-6">
											<span class="help-block text-left">General Fishing Pro Staff Application, Embed code with LOGO</span>
											<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/generalfishingprostaff/<%=md5%>"> General Fishing Pro Staff Application </a>
											</textarea>
										</div>

										<div class="col-md-6">
											<span class="help-block text-left">General Fishing Pro Staff Application, Embed code NO LOGO</span>
												<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/generalfishingprostaff/<%=md5%>"> General Fishing Pro Staff Application </a>
												</textarea>
										</div>
									</div>	
									<div class="row">&nbsp;</div>
									
									<div class="row">
										<div class="col-md-6">
											<span class="help-block text-left">Shorten General Fishing Pro Staff Application, Embed code with LOGO</span>
											<textarea name="" rows="7" class="form-control" >
											<a href="<%=request.getContextPath()%>/home/generalfishingprostaff/<%=md5%>"> General Fishing Pro Staff Application </a>
											</textarea>
										</div>

										<div class="col-md-6">
											<span class="help-block text-left">Shorten General Fishing Pro Staff Application, Embed code NO LOGO</span>
												<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/generalfishingprostaff/<%=md5%>"> General Fishing Pro Staff Application </a>
												</textarea>
										</div>
									</div>

									<div class="row">&nbsp;</div>
									<div class="row">
										<div class="col-md-6">
											<span class="help-block text-left">Team Pro Staff Application, Embed code with LOGO</span>
											<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/teamAppprostaff/<%=md5%>"> Team Pro Staff Application </a>
											</textarea>
										</div>

										<div class="col-md-6">
											<span class="help-block text-left">Team Pro Staff Application, Embed code NO LOGO</span>
											<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/teamAppprostaff/<%=md5%>/1"> Team Pro Staff Application </a>
											</textarea>
										</div>
									</div>
									
									<div class="row">&nbsp;</div>
									<div class="row">
										<div class="col-md-6">
											<span class="help-block text-left">New Application, Embed code with LOGO</span>
											<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/newapplicaton/<%=md5%>"> New Application </a>
											</textarea>
										</div>

										<div class="col-md-6">
											<span class="help-block text-left">New Application, Embed code NO LOGO</span>
											<textarea name="" rows="7" class="form-control" >
												<a href="<%=request.getContextPath()%>/home/newapplicaton/<%=md5%>/1"> New Application </a>
											</textarea>
										</div>
									</div>
								</div>
							</div>
					
				</div>
				
			</div>
		</div>
	</div>
	
</div>




<script type="text/javascript">




	jQuery(document).ready(function () {
    	
    	
    	
    });

 </script>