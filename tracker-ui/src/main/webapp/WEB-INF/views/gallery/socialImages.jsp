<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	background-color: rgba(0, 0, 0, 0.6);
	opacity: 0;
	filter: alpha(opacity = 0);
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

.hovereffect .infotag {
	width: 230px;
	height: 130px;
	color: #fff;
	text-align: center;
	position: relative;
	padding-top: 12px;
	padding-bottom: 12px;
	background: rgba(0, 0, 0, 0.6);
}

.hovereffect .infotag h2 {
	text-transform: uppercase;
	color: #fff;
	text-align: center;
	font-size: 15px;
}

.hovereffect .infotag a {
	width: 218px;
	color: #fff;
	text-align: center;
	font-size: 12px;
	word-wrap: break-word;
}
</style>
<div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">

	<div class="kt-content  kt-grid__item kt-grid__item--fluid"
		id="kt_content">
		<div id="sucessErrorMsgDiv"></div>
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon"> <i
						class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">Social Images</h3>
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row kt-margin-b-20">
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Member:</label> <select class="form-control kt-select2"
							id="staff" name="staff">
							<option value="0">All Staff</option>
							<c:forEach var="members" items="${membersList}"
								varStatus="counter">
								<option value="${members.userId}">${members.firstLastName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Team:</label> <select class="form-control kt-select2"
							id="team" name="team">
							<option value="0">All Team</option>
							<c:forEach var="members" items="${teamList}" varStatus="counter">
								<option value="${members.teamId}">${members.teamName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Keywords:</label> <select
							class="form-control kt-select2" id="keyword" name="keyword">
							<option value="0">All Keywords</option>
							<c:forEach var="keyword" items="${keywordsList}"
								varStatus="counter">
								<option value="${keyword}">${keyword}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Period:</label> <select class="form-control kt-select2"
							id="period" name="period">
							<option value="7">last 7 days</option>
							<option value="30">last 30 days</option>
							<option value="90">last 90 days</option>
						</select>
					</div>
				</div>
				<div id="loader"></div>
				<div class="row" id="loadgallery"></div>
			</div>
		</div>
	</div>

</div>




<script type="text/javascript">

function fbs_click(TheImg) {
	console.log("check"+ TheImg)

    u=TheImg;
    // t=document.title;
   t="";
   window.open('http://www.facebook.com/sharer.php?u='+encodeURIComponent(u)+'&t='+encodeURIComponent(t),'sharer','toolbar=0,status=0,width=626,height=436');return false;
}

	function initSocialImages(){
		var param = {
				staff : $('#staff').val(),
				team : $('#team').val(),
				keyword : $('#keyword').val(),
				period : $('#period').val()
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/instaImages',
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
				console.log("check"+ data[0].link)
				for(var i=0; i<data.length; i++){
					
					var h = '<div class="col-md-55 float_left" style="border:0px solid red; margin:10px;">'+
					    '<div class="hovereffect">'+
					    '<a href="'+data[i].link+'">'+
					        '<img class="img-responsive" id="image_'+i+'" src="'+data[i].standardImage+'" style="display: block; width:230px; height: 220px" alt="" >'+
					        '</a>'+
					        '<div class="infotag">'+
						        '<h2>'+data[i].firstLastname+'</h2>'+
						        '<a>'+data[i].social_type+'</a><br/>'+
						        '<a>'+data[i].keyword+'</a> <br/>'+
						        '<div src="'+data[i].link+'" alt="" onclick=fbs_click("'+data[i].link+'")>'+
								        '<a>'+"Share"+'</a>'+
									       '</div>'+
						       '<a>'+
					            	'<i class="fa fa-thumbs-up"></i><span class="number">'+data[i].likesCount+'</span>'+
					            	'&nbsp;&nbsp;<i class="fa fa-comments"></i><span class="number">'+data[i].commentsCount+'</span>'+
					            '</a> <br/>'+
					           
				            '</div>'+
					    '</div>'+
					'</div>';
					
					$('#loadgallery').append(h);
				}
			}
		});
	}
	
	// '<a class="info" target="_blank" href="'+data[i].standardImage+'">'+
					        //    	'<span class="number" >Download</span>'+
					         //   '</a>'+
	 // '<div src="'+data[i].link+'" alt="" onclick=fbs_click("'+data[i].standardImage+'")>'+
					//	        '<a>'+"Share"+'</a>'+
						//        '</div>'+
	$("#team").change(function () {
		initSocialImages();
    });
	
	$("#keyword").change(function () {
		initSocialImages();
    });
	
	$("#period").change(function () {
		initSocialImages();
    });
	
	$("#staff").change(function () {
		initSocialImages();
    });
	
	
	
	<%-- function imageDownload(id){
	 var url = $('#image_'+id).attr('src');
		window.open("<%=request.getContextPath()%>
	/imageDownload?url=" + url,
				"_blank");  
		return false;
	} --%>

	jQuery(document).ready(function() {
		console.log("ggsaddsa");
		initSocialImages();
	});
</script>