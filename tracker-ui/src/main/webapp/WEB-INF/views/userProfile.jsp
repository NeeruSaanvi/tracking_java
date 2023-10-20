<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>

.fontbig{
	font-size: 20px !important;
}

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
  /*box-shadow: 0 0 5px #fff;*/
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
		<div class="kt-portlet kt-portlet--mobile">
			<div class="kt-portlet__head kt-portlet__head--lg">
				<div class="kt-portlet__head-label">
					<span class="kt-portlet__head-icon">
						<i class="kt-font-brand flaticon2-line-chart"></i>
					</span>
					<h3 class="kt-portlet__head-title">
						User Profile
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<!-- begin:: Content -->
			<div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">

				<!--Begin::Section-->
				<div class="row">
					<div class="col-xl-12">

						<!--begin:: Widgets/Applications/User/Profile3-->
						<div class="kt-portlet kt-portlet--height-fluid">
							<div class="kt-portlet__body">
								<div class="kt-widget kt-widget--user-profile-3">
									<div class="kt-widget__top">
										<div class="kt-widget__media kt-hidden-">
											<img src="${ detailUser.profilePic }" alt="image" style="height: 110px; width: 110px;">
										</div>
										<div class="kt-widget__content">
											<div class="kt-widget__head">
												<a href="#" class="kt-widget__username fontbig">
													${ detailUser.firstLastName }
													<i class="flaticon2-correct"></i>
												</a>
											</div>
											<div class="kt-widget__subhead" style="font-size: 15px;">
												<a><i class="fa fa-birthday-cake"></i> ${ detailUser.dobFormatter }</a>
												<a><i class="flaticon2-new-email"></i>${ detailUser.email }</a>
												<a><i class="flaticon2-phone"></i>${ detailUser.phone }</a>
											</div>
											<div class="kt-widget__subhead" style="font-size: 15px;">
												<a><i class="flaticon-location"></i>${ detailUser.street }, ${ detailUser.city }, ${ detailUser.state }</a>
												<a><i class="flaticon2-group"></i></i>
													<c:forEach var="team" items="${detailUser.userTeamName}" varStatus="counter">
														${team} 
													</c:forEach>
												</a>
												<a><i class="flaticon-trophy"></i>Shirt Size : ${ detailUser.shirtSize }</a>
												<a><i class="flaticon-trophy"></i>Glove Size : ${ detailUser.gloveSize }</a>
												<a><i class="flaticon-tea-cup"></i>Discount Code : ${ detailUser.discountCode }</a>
											</div>
										</div>
									</div>
									<div class="kt-widget__bottom">
										<div class="kt-widget__item">
											<div class="kt-widget__icon">
											</div>
											<div class="kt-widget__details">
												<span class="kt-widget__title fontbig">Total Post</span>
												<span class="kt-widget__value fontbig">${userProfileTotal.totalPosts }</span>
											</div>
										</div>
										<div class="kt-widget__item">
											<div class="kt-widget__icon">
											</div>
											<div class="kt-widget__details">
												<span class="kt-widget__title fontbig">Total Interactions</span>
												<span class="kt-widget__value fontbig">${userProfileTotal.totalInteractions }</span>
											</div>
										</div>
										<div class="kt-widget__item">
											<div class="kt-widget__icon">
											</div>
											<div class="kt-widget__details">
												<span class="kt-widget__title fontbig">Total EffectivenessRate</span>
												<span class="kt-widget__value fontbig">${userProfileTotal.totalEffectivenessRate }</span>
											</div>
										</div>
										
										<div class="kt-widget__item">
											<div class="kt-widget__icon">
											</div>
											<div class="kt-widget__details">
												<span class="kt-widget__title fontbig">Annual Rank Score</span>
												<span class="kt-widget__value fontbig">${userProfileTotal.annualRankScore }</span>
											</div>
										</div>
										
									</div>
								</div>
							</div>
						</div>

						<!--end:: Widgets/Applications/User/Profile3-->
					</div>
				</div>

				<!--End::Section-->

				<!--Begin::Section-->
				<div class="row">
					<div class="kt-portlet">
						<div class="kt-portlet__body  kt-portlet__body--fit">
							<div class="row row-no-padding row-col-separator-xl">
								<div class="col-xl-12">
									<!--begin:: Widgets/Facebook Post -->
									<div class="kt-portlet kt-portlet--height-fluid">
										<div class="kt-portlet__head">
											<div class="kt-portlet__head-label">
												<h3 class="kt-portlet__head-title">
													Facebook Posts
												</h3>
											</div>
											<div class="kt-portlet__head-toolbar">
												<button type="button" class="btn btn-brand btn-sm btn-upper">${userFeed.size()}</button>
											</div>
										</div>
										<div class="kt-portlet__body kt-scroll" data-scroll="true" data-height="400" style="height: 400px;">
											<div class="kt-widget3">
												<c:forEach var="userFeedObj" items="${userFeed}" varStatus="counter">
												<div class="kt-widget3__item">
													<div class="kt-widget3__header">
														<div class="kt-widget3__user-img">
															<img class="kt-widget3__img" style="height: 42px; width: 42px" src="${detailUser.profilePic }" alt="">
														</div>
														<div class="kt-widget3__info">
															<a class="kt-widget3__username">
																${detailUser.firstLastName } on <span class="kt-widget3__time">${userFeedObj.createdTimeFormatter}</span> posted on his Wall.
															</a>
															
														</div>
														<span class="kt-widget3__status kt-font-info">
															<i class="flaticon-like"></i> ${userFeedObj.likes_count}
															&nbsp;<i class="flaticon-comment"></i> ${userFeedObj.comment_count}
															&nbsp;<i class="flaticon-share"></i> ${userFeedObj.share_count}
														</span>
													</div>
													<div class="kt-widget3__body">
														<p class="kt-widget3__text">
															<a href="${userFeedObj.link}" target="_blank">${userFeedObj.story}</a>
														</p>
													</div>
												</div>
												</c:forEach>
											</div>
										</div>
									</div>
									<!--end:: Widgets/Facebook Post-->
								</div>
							</div>
						</div>
					</div>
				</div>

				<!--End::Section-->
				
				<!--Begin::Section-->
				<div class="row">
					<div class="kt-portlet">
						<div class="kt-portlet__body  kt-portlet__body--fit">
							<div class="row row-no-padding row-col-separator-xl">
								<div class="col-xl-12" >
									
									<!--begin:: Widgets/Instagram Post -->
									<div class="kt-portlet kt-portlet--height-fluid">
										<div class="kt-portlet__head">
											<div class="kt-portlet__head-label">
												<h3 class="kt-portlet__head-title">
													Instagram Posts
												</h3>
											</div>
											<div class="kt-portlet__head-toolbar">
												<button type="button" class="btn btn-brand btn-sm btn-upper">${userInstragramFeed.size()}</button>
											</div>
										</div>
										<div class="kt-portlet__body kt-scroll" data-scroll="true" data-height="400" style="height: 400px;">
											<div class="kt-widget3">
												<c:forEach var="userFeedObj" items="${userInstragramFeed}" varStatus="counter">
												<div class="kt-widget3__item">
													<div class="kt-widget3__header">
														<div class="kt-widget3__user-img">
															<img class="kt-widget3__img" style="height: 42px; width: 42px" src="${detailUser.profilePic }" alt="">
														</div>
														<div class="kt-widget3__info">
															<a class="kt-widget3__username">
																${detailUser.firstLastName } on <span class="kt-widget3__time">${userFeedObj.createdTimeFormatter}</span> posted on his Wall.
															</a>
															
														</div>
														<span class="kt-widget3__status kt-font-info">
															<i class="flaticon-like"></i> ${userFeedObj.likesCount}
															&nbsp;<i class="flaticon-comment"></i> ${userFeedObj.commentsCount}
														</span>
													</div>
													<div class="kt-widget3__body">
														<p class="kt-widget3__text">
															<a href="${userFeedObj.link}" target="_blank">${userFeedObj.text}</a>
														</p>
													</div>
												</div>
												</c:forEach>
											</div>
										</div>
									</div>
	
									<!--end:: Widgets/Instagram Post-->
									
								</div>
							</div>
						</div>
					</div>
				</div>

				<!--End::Section-->
				
				<!--Begin::Section-->
				<div class="row">
					<div class="kt-portlet">
						<div class="kt-portlet__body  kt-portlet__body--fit">
							<div class="row row-no-padding row-col-separator-xl">
								<div class="col-xl-12" >
									<!--begin:: Widgets/Twitter Post -->
									<div class="kt-portlet kt-portlet--height-fluid">
										<div class="kt-portlet__head">
											<div class="kt-portlet__head-label">
												<h3 class="kt-portlet__head-title">
													Twitter Posts
												</h3>
											</div>
											<div class="kt-portlet__head-toolbar">
												<button type="button" class="btn btn-brand btn-sm btn-upper">${userTweets.size()}</button>
											</div>
										</div>
										<div class="kt-portlet__body kt-scroll" data-scroll="true" data-height="400" style="height: 400px;">
											<div class="kt-widget3">
												<c:forEach var="userFeedObj" items="${userTweets}" varStatus="counter">
												<div class="kt-widget3__item">
													<div class="kt-widget3__header">
														<div class="kt-widget3__user-img">
															<img class="kt-widget3__img" style="height: 42px; width: 42px" src="${detailUser.profilePic }" alt="">
														</div>
														<div class="kt-widget3__info">
															<a class="kt-widget3__username">
																${detailUser.firstLastName } on <span class="kt-widget3__time">${userFeedObj.createdAtFormatter}</span> twitt on his Wall.
															</a>
															
														</div>
														<span class="kt-widget3__status kt-font-info">
														</span>
													</div>
													<div class="kt-widget3__body">
														<p class="kt-widget3__text">
															<a href="${userFeedObj.link}" target="_blank">${userFeedObj.text}</a>
														</p>
													</div>
												</div>
												</c:forEach>
											</div>
										</div>
									</div>
	
									<!--end:: Widgets/Twitter Post-->
									
								</div>
							</div>
						</div>
					</div>
				</div>

				<!--End::Section-->
				
				<!--Begin::Section-->
				<div class="row">
					<div class="kt-portlet">
						<div class="kt-portlet__body  kt-portlet__body--fit">
							<div class="row row-no-padding row-col-separator-xl">
								<div class="col-xl-12" >
									<!--begin:: Widgets/Youtube Post -->
									<div class="kt-portlet kt-portlet--height-fluid">
										<div class="kt-portlet__head">
											<div class="kt-portlet__head-label">
												<h3 class="kt-portlet__head-title">
													Youtube Posts
												</h3>
											</div>
											<div class="kt-portlet__head-toolbar">
												<button type="button" class="btn btn-brand btn-sm btn-upper">${userYoutubeFeed.size()}</button>
											</div>
										</div>
										<div class="kt-portlet__body kt-scroll" data-scroll="true" data-height="500" style="height: 500px;">
										<div class="row">
											<c:forEach var="userFeedObj" items="${userYoutubeFeed}" varStatus="counter">
											<div class="col-md-55 float_left" style="margin:10px;">
											    <div class="hovereffect">
											        <img class="img-responsive" src="${userFeedObj.thumb}" style="display: block; width:200px; height: 200px" alt="">
											        <div class="overlay">
											            <a class="info" href="${userFeedObj.video_url}" target="_blank">
											            	<i class="fa fa-thumbs-up"></i><span class="number">${userFeedObj.total_likes}</span>
											            	&nbsp;&nbsp;<i class="fa fa-comments"></i><span class="number">${userFeedObj.total_views}</span>
											            </a>
											        </div>
											    </div>
											</div>
											</c:forEach>
											</div>
										</div>
									</div>
	
									<!--end:: Widgets/Twitter Post-->
									
								</div>
							</div>
						</div>
					</div>
				</div>

				<!--End::Section-->


				
			</div>

			<!-- end:: Content -->
		</div>
	</div>
	
</div>



<script type="text/javascript">

	jQuery(document).ready(function () {
		
    });

 </script>