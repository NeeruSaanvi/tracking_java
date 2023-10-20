<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>

.hovereffect {
  width: 100%;
  height: 100%;
  float: left;
  overflow: hidden;
  position: relative;
  text-align: center;
  cursor: default;
}

.hovereffect .overlay {
  width: 100%;
  height: 100%;
  position: absolute;
  overflow: hidden;
  top: 0;
  left: 0;
  background-color: rgba(0,0,0,0.6);
  opacity: 0;
  filter: alpha(opacity=0);
  -webkit-transform: translate(460px, -100px) rotate(180deg);
  -ms-transform: translate(460px, -100px) rotate(180deg);
  transform: translate(460px, -100px) rotate(180deg);
  -webkit-transition: all 0.2s 0.4s ease-in-out;
  transition: all 0.2s 0.4s ease-in-out;
}

.hovereffect img {
  display: block;
  position: relative;
  -webkit-transition: all 0.2s ease-in;
  transition: all 0.2s ease-in;
}

.hovereffect h2 {
  text-transform: uppercase;
  color: #fff;
  text-align: center;
  position: relative;
  font-size: 17px;
  padding: 10px;
  background: rgba(0, 0, 0, 0.6);
}

.hovereffect a.info {
  width: 90%;
  height: 90%;
  display: inline-block;
  text-decoration: none;
  padding: 7px 14px;
  text-transform: uppercase;
  color: #fff;
  border: 1px solid #fff;
  margin: 10px 0 0 0;
  background-color: transparent;
  -webkit-transform: translateY(-200px);
  -ms-transform: translateY(-200px);
  transform: translateY(-200px);
  -webkit-transition: all 0.2s ease-in-out;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
}

.hovereffect a.info:hover {
}

.hovereffect:hover .overlay {
  opacity: 1;
  filter: alpha(opacity=100);
  -webkit-transition-delay: 0s;
  transition-delay: 0s;
  -webkit-transform: translate(0px, 0px);
  -ms-transform: translate(0px, 0px);
  transform: translate(0px, 0px);
}

.hovereffect:hover h2 {
  -webkit-transform: translateY(0px);
  -ms-transform: translateY(0px);
  transform: translateY(0px);
  -webkit-transition-delay: 0.5s;
  transition-delay: 0.5s;
}

.hovereffect:hover a.info {
  -webkit-transform: translateY(0px);
  -ms-transform: translateY(0px);
  transform: translateY(0px);
  -webkit-transition-delay: 0.3s;
  transition-delay: 0.3s;
}

</style>
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
						Video
					</h3>
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row kt-margin-b-20">
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Member:</label>
						<select class="form-control kt-select2" id="staff" name="staff">
							<option value="0">All Staff</option>
							<c:forEach var="members" items="${membersList}" varStatus="counter">
								<option value="${members.userId}">${members.firstLastName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Team:</label>
						<select class="form-control kt-select2" id="team" name="team">
							<option value="0">All Team</option>
							<c:forEach var="members" items="${teamList}" varStatus="counter">
								<option value="${members.teamId}">${members.teamName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Keywords:</label>
						<select class="form-control kt-select2" id="keyword" name="keyword">
							<option value="0">All Keywords</option>
							<c:forEach var="keyword" items="${keywordsList}" varStatus="counter">
								<option value="${keyword}">${keyword}</option>
							</c:forEach>							
						</select>
					</div>
				</div>
				<div id="loader"></div>
				<div class="row" id="loadgallery" >
					
				</div>
			</div>
		</div>
	</div>
	
</div>




<script type="text/javascript">

	function initVideo(){
		var param = {
				staff : $('#staff').val(),
				team : $('#team').val(),
				keyword : $('#keyword').val()
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/ytVideo',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
				$('#loadgallery').html('');	
			},
			success : function(obj) {
				$('#loader').html('');	
				
				var data = obj.data;
				
				for(var i=0; i<data.length; i++){
					
					var h = '<div class="col-md-55 float_left" style="margin:10px;">'+
					    '<div class="hovereffect">'+
					        	'<img class="img-responsive" src="'+data[i].thumb+'" style="display: block; width:200px; height: 200px" alt="">'+
					        '<div class="overlay">'+
					            
					            '<a class="info" href="'+data[i].video_url+'" target="_blank">'+
					            	'<i class="fa fa-thumbs-up"></i><span class="number">'+data[i].total_likes+'</span>'+
					            	'&nbsp;&nbsp;<i class="fa fa-comments"></i><span class="number">'+data[i].total_views+'</span>'+
					            '</a>'+
					        '</div>'+
					    '</div>'+
					'</div>';
					
					$('#loadgallery').append(h);
				}
			}
		});
	}
	
	$("#team").change(function () {
		initVideo();
    });
	
	$("#keyword").change(function () {
		initVideo();
    });
	
	$("#staff").change(function () {
		initVideo();
    });

	jQuery(document).ready(function () {
		initVideo();
    	
    });

 </script>