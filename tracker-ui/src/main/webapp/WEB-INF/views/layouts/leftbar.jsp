
<%@page import="com.tracker.ui.utils.UserUtils"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!-- begin:: Aside -->
				<button class="kt-aside-close " id="kt_aside_close_btn"><i class="la la-close"></i></button>
				<div class="kt-aside  kt-aside--fixed  kt-grid__item kt-grid kt-grid--desktop kt-grid--hor-desktop" id="kt_aside">

					<!-- begin:: Aside Menu -->
					<div class="kt-aside-menu-wrapper kt-grid__item kt-grid__item--fluid" id="kt_aside_menu_wrapper">
						<div id="kt_aside_menu" class="kt-aside-menu " data-ktmenu-vertical="1" data-ktmenu-scroll="1" data-ktmenu-dropdown-timeout="500">
							<ul class="kt-menu__nav ">
								<li class="kt-menu__item  kt-menu__item--active" aria-haspopup="true">
									<a href="dashboard" class="kt-menu__link ">
										<i class="kt-menu__link-icon flaticon2-protection"></i>
										<span class="kt-menu__link-text">Dashboard</span>
									</a>
								</li>
								<li class="kt-menu__item  kt-menu__item--active" aria-haspopup="true">
									<a href="link" class="kt-menu__link ">
										<i class="kt-menu__link-icon flaticon2-link"></i>
										<span class="kt-menu__link-text">Link Account</span>
									</a>
								</li>
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon2-calendar-6"></i>
										<span class="kt-menu__link-text">Reports</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">Reports</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/reports" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Run Report</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/scheduleReports" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Schedule Report</span></a></li>
										</ul>
									</div>
								</li>
								
								<li class="kt-menu__item " aria-haspopup="true">
									<a href="/annualrankings" class="kt-menu__link ">
										<i class="kt-menu__link-icon flaticon2-box-1"></i>
										<span class="kt-menu__link-text">Annual Rankings </span>
									</a>
								</li>
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon-users-1"></i>
										<span class="kt-menu__link-text">Manage Users</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">Manage Users</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/users?addUser=1" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Add New</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/users" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Manage</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/team" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Teams</span></a></li>
										</ul>
									</div>
								</li>
								
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon-layers"></i>
										<span class="kt-menu__link-text">Applications</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">Applications</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/applications" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Applicants</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/embedcode" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Embed Code</span></a></li>
										</ul>
									</div>
								</li>
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon-photo-camera"></i>
										<span class="kt-menu__link-text">Gallery</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">Gallery</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/socialImages" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Social Images</span></a></li>
											<!-- <li class="kt-menu__item " aria-haspopup="true"><a href="/highResImage" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">High Res Images </span></a></li> -->
											<li class="kt-menu__item " aria-haspopup="true"><a href="/video" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Video</span></a></li>
										</ul>
									</div>
								</li>
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon-squares-4"></i>
										<span class="kt-menu__link-text">Non-Social</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">Non-Social</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/events" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Events</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/tournaments" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Tournaments</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/lead" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Leads</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/prints" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Print</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/webPost" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Web</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/userStore" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">User Store</span></a></li>
										</ul>
									</div>
								</li>
								
								<li class="kt-menu__item  kt-menu__item--submenu" aria-haspopup="true" data-ktmenu-submenu-toggle="hover">
									<a href="javascript:;" class="kt-menu__link kt-menu__toggle">
										<i class="kt-menu__link-icon flaticon2-user"></i>
										<span class="kt-menu__link-text">My Account</span>
										<i class="kt-menu__ver-arrow la la-angle-right"></i>
									</a>
									<div class="kt-menu__submenu "><span class="kt-menu__arrow"></span>
										<ul class="kt-menu__subnav">
											<li class="kt-menu__item  kt-menu__item--parent" aria-haspopup="true"><span class="kt-menu__link"><span class="kt-menu__link-text">My Account</span></span></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/keywords" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Keywords</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/notifications" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Notifications</span></a></li>
											<li class="kt-menu__item " aria-haspopup="true"><a href="/promoCode" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Promo Code</span></a></li>
											<!-- <li class="kt-menu__item " aria-haspopup="true"><a href="#" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Training Center</span></a></li> -->
											<li class="kt-menu__item " aria-haspopup="true"><a href="/scoreSettings" class="kt-menu__link "><i class="kt-menu__link-bullet kt-menu__link-bullet--line"><span></span></i><span class="kt-menu__link-text">Scoring Algorithm</span></a></li>
										</ul>
									</div>
								</li>
								
							</ul>
						</div>
					</div>

					<!-- end:: Aside Menu -->
				</div>

				<!-- end:: Aside -->