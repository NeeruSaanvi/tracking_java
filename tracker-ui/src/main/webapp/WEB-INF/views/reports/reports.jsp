<%@page import="com.tracker.ui.jasper.JasperFormat"%>
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
						Reports
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
					<div class="kt-portlet__head-wrapper">
						<div class="kt-portlet__head-actions">
							<a class="btn btn-brand btn-elevate btn-icon-sm" href="#" onclick="return generateReport(<%=JasperFormat.EXCEL_FORMAT.getId() %>, 'Reports')" >
								Export
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="kt-portlet__body">
				<form name="reportFilterForm" id="reportFilterForm">
				
				<div class="row kt-margin-b-20">
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>Date:</label>
						<div class="input-daterange input-group" id="kt_datepicker">
							<input type="text" class="form-control kt-input" id="fromDate" name="fromDate" placeholder="From" data-col-index="5" />
							<div class="input-group-append">
								<span class="input-group-text"><i class="la la-ellipsis-h"></i></span>
							</div>
							<input type="text" class="form-control kt-input" id="toDate" name="toDate" placeholder="To" data-col-index="5" />
						</div>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Staff:</label>
						<select class="form-control kt-select2" id="staff_select2" name="staff" multiple="multiple">
							<c:forEach var="members" items="${membersList}" varStatus="counter">
								<option value="${members.userId}">${members.firstLastName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Keywords:</label>
						<select class="form-control kt-select2" id="keyword_select2" name="keyword" multiple="multiple">
							<c:forEach var="keyword" items="${keywordsList}" varStatus="counter">
								<option value="${keyword}">${keyword}</option>
							</c:forEach>							
						</select>
					</div>
					<div class="col-lg-3 kt-margin-b-10-tablet-and-mobile">
						<label>By Team:</label>
						<select class="form-control kt-select2" id="team" name="team" multiple="multiple" style="width:100%">
							<c:forEach var="team" items="${teamList}" varStatus="counter">
								<option value="${team.teamId}">${team.teamName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="kt-separator kt-separator--md kt-separator--dashed"></div>
				<div class="row">
					<div class="col-lg-12">
						<button class="btn btn-primary btn-brand--icon" id="kt_search">
							<span>
								<i class="la la-search"></i>
								<span>Search</span>
							</span>
						</button>
					</div>
				</div>
				</form>
				<div class="kt-separator kt-separator--border-dashed kt-separator--space-md"></div>
				<div class="row">
					<div class="col-lg-4">
						<!--begin::Facebook Chart-->
						<div class="kt-portlet kt-portlet--tab">
							<div class="kt-portlet__head">
								<div class="kt-portlet__head-label">
									<span class="kt-portlet__head-icon kt-hidden">
										<i class="la la-gear"></i>
									</span>
									<h3 class="kt-portlet__head-title">
										Facebook
									</h3>
								</div>
							</div>
							<div class="kt-portlet__body">
								<div id="fbChart" style="height:300px;"></div>
							</div>
						</div>

						<!--end::Facebook Char-->
					</div>
					
					<div class="col-lg-4">
						<!--begin::Insta chart -->
						<div class="kt-portlet kt-portlet--tab">
							<div class="kt-portlet__head">
								<div class="kt-portlet__head-label">
									<span class="kt-portlet__head-icon kt-hidden">
										<i class="la la-gear"></i>
									</span>
									<h3 class="kt-portlet__head-title">
										Instagram
									</h3>
								</div>
							</div>
							<div class="kt-portlet__body">
								<div id="inChart" style="height:300px;"></div>
							</div>
						</div>

						<!--end::Insta chart-->
					</div>
					
					<div class="col-lg-4">
						<!--begin::Twitter chart -->
						<div class="kt-portlet kt-portlet--tab">
							<div class="kt-portlet__head">
								<div class="kt-portlet__head-label">
									<span class="kt-portlet__head-icon kt-hidden">
										<i class="la la-gear"></i>
									</span>
									<h3 class="kt-portlet__head-title">
										Twitter
									</h3>
								</div>
							</div>
							<div class="kt-portlet__body">
								<div id="twChart" style="height:300px;"></div>
							</div>
						</div>

						<!--end::Twitter chart-->
					</div>
					
				</div>
				<div class="row">
					<div class="col-lg-4">
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Posts</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalPostFb"></span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Interactions</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalInteractionsFb">
								<%-- <fmt:formatNumber type = "number" maxFractionDigits = "3" value = "${totalsObj.fbTotalInteractions}" />  --%>
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Effectiveness Rate</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="effectivenessRateFb">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg Posts Per Member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgPostsPerMemberFb">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg. Interactions Per member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgInteractionsPerMemberFb">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Posts Per Week</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="postsPerWeekFb">
							</span>
						</p>
					</div>
					<div class="col-lg-4">
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Posts</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalPostIn"></span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Interactions</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalInteractionsIn">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Effectiveness Rate</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="effectivenessRateIn">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg Posts Per Member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgPostsPerMemberIn">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg. Interactions Per member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgInteractionsPerMemberIn">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Posts Per Week</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="postsPerWeekIn">
							</span>
						</p>
					</div>
					<div class="col-lg-4">
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Posts</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalPostTw"></span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Total Interactions</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="totalInteractionsTw">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Effectiveness Rate</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="effectivenessRateTw">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg Posts Per Member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgPostsPerMemberTw">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Avg. Interactions Per member</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="avgInteractionsPerMemberTw">
							</span>
						</p>
						<p class="kt-widget4__text" style="display: flex; justify-content: space-between; font-size: 15px;">
							<span>Posts Per Week</span>
							<span class="kt-widget4__number kt-font-info" style="padding-left: 30px; font-weight: bold; font-weight: bold;" id="postsPerWeekTw">
							</span>
						</p>
					</div>
				</div>
				
				<div class="kt-separator kt-separator--border-dashed kt-separator--space-md"></div>
				<table class="table table-striped- table-bordered table-hover table-checkable" id="webPostTable">
					<thead>
                       	<tr>
                       		<th class="dt-header-right">Pro-Staff</th>
                            <th class="dt-header-right"><i class="flaticon-facebook-logo-button  kt-font-brand"></i><br> Feeds</th>
                            <th class="dt-header"><i class="flaticon-twitter-logo-button  kt-font-brand"></i><br> Feeds</th>
                            <th class="dt-header-right"> <i class="flaticon-instagram-logo  kt-font-brand"></i><br> Feeds</th>
                            <th class="dt-header-right"><i class="flaticon-youtube  kt-font-brand"></i><br> Feeds</th>
                            <th class="dt-header-right"><i class="flaticon-trophy  kt-font-brand"></i><br>Tournaments</th>
                            <th class="dt-header-right"><i class="flaticon-speech-bubble-1 kt-font-brand"></i><br>Entries</th>
                            <th class="dt-header-right"><i class="flaticon-event-calendar-symbol kt-font-brand"></i><br>Events</th>
                            <th class="dt-header-right"><i class="flaticon-paper-plane kt-font-brand"></i><br>Leads</th>
                            <th class="dt-header-right"><i class="flaticon2-rocket-1 kt-font-brand"></i><br>Print</th>
                            <th class="dt-header-right">AT Score</th>
                          </tr>
                    	</thead>
                    <tbody>
                    </tbody>
				</table>
			</div>
		</div>
	</div>
	
</div>


<script type="text/javascript">

	function generateReport(format, reportName) {
		var fromDate = $('#fromDate').val();
		var toDate = $('#toDate').val();
		
		var staffArray = [];
		var keywordArray = [];
		var teamArray = [];
		
		staffs = $('#staff_select2').val();
		keywords = $('#keyword_select2').val();
		teams = $('#team').val();
		
		for(var i=0; i< staffs.length; i++){
			staffArray.push(staffs[i]);
		}
		
		for(var j=0; j< keywords.length; j++){
			keywordArray.push(keywords[j]);
		}
		
		for(var k=0; k< teams.length; k++){
			teamArray.push(teams[k]);
		}
		
		var staff = staffArray.join();
		var keyword = encodeURIComponent(keywordArray.join());
		var team = teamArray.join();
		
		
		window.open("<%=request.getContextPath() %>/reportExporttoExcel?download=true&fromDate="+fromDate+"&toDate="+toDate+"&keyword="+keyword+"&staff="+staff+"&team="+team, "_blank");
		
		return false;
	}

	function editDeleteBankAccountRenderer(data, type, row) { 
		return "<img src=\"resources/assets/media/icons/svg/Design/Edit.svg\" onclick='updateWeb(" + data + ", "+JSON.stringify(row)+" )' />"+ 
		"<img src=\"resources/assets/media/icons/svg/Code/Plus.svg\"/>";
	}
	
	function fbrender(data, type, row) {		
		return "Post["+row.fbFeedCount+"]<br> Like["+row.fbLikeCountFormatter+"]<br> Comments["+row.fbCommentCount+"]<br> Share["+row.fbShareCount+"]";
	}
	
	function twrender(data, type, row) {		
		return "Tweets["+row.twFeedCount+"]<br> ReTweets["+row.twReTweetCount+"]<br> Favourites["+row.twFavCount+"]";
	}
	
	function inrender(data, type, row) {		
		return "Post["+row.inFeedCount+"]<br> Likes["+row.inLikeCountFormatter+"]<br> Comments["+row.inCommentCount+"]";
	}
	
	function ytrender(data, type, row) {		
		return "Post["+row.ytFeedCount+"]<br> Views["+row.ytViewCount+"]";
	}

	function getWebPostColumnData() {
		
		return [
			{ data : 'name', width: '100px', defaultContent : '', className: 'dt-left' },
			{ data : 'fbFeedCount', width: '225px', render: fbrender, defaultContent : '', className: 'dt-left' } ,
			{ data : 'twFeedCount', width: '225px', render: twrender, defaultContent : '', className: 'dt-left' },
			{ data : 'inFeedCount', width: '225px', render: inrender, defaultContent : '', className: 'dt-left' },
			{ data : 'ytFeedCount', width: '225px', render: ytrender, defaultContent : '', className: 'dt-left' },
			{ data : 'tournamentsCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'blogCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'eventCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'leadsCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'printCount', width: '225px', defaultContent : '', className: 'dt-left' },
			{ data : 'totalActivityFormatter', width: '225px', defaultContent : '', className: 'dt-left' }			
		];
	}
	
	function reportParameters(data) {
		beforeDataSubmit(data);	
		
		data.fromDate = $('#fromDate').val();
		data.toDate = $('#toDate').val();
		data.keyword = $('#keyword_select2').val();
		data.staff = $('#staff_select2').val();
		data.team = $('#team').val();
		//data.ratings = $('#ratings').val();
		//data.category = $('#category_select2').val();	
	}

	function initReportList() {
		
		$('#webPostTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/reports',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: reportParameters,
			},
			columns 		: getWebPostColumnData(),
			oLanguage		: getDatatablesLoadingText(),
			order			: [[ 0, 'asc' ]],
			paging 			: true,
			ordering 		: true,
			info 			: true,
			displayLength	: window.innerHeight < 1000 ? 25 : 25,
			lengthMenu: [5, 10, 25, 50],
		});
	}
	
	
	
	jQuery(document).ready(function () {
		
		var fbchart = Morris.Bar({
            element: 'fbChart',
            xkey: 'month',
            ykeys: ['postsPerMember', 'effectivenessRate'],
            labels: ['Posts Per Member', 'Effectiveness Rate'],
            barColors: ['#2abe81', '#24a5ff']
        });
		
		var inchart = Morris.Bar({
            element: 'inChart',
            xkey: 'month',
            ykeys: ['postsPerMember', 'effectivenessRate'],
            labels: ['Posts Per Member', 'Effectiveness Rate'],
            barColors: ['#2abe81', '#24a5ff']
        });
		
		var twchart = Morris.Bar({
            element: 'twChart',
            xkey: 'month',
            ykeys: ['postsPerMember', 'effectivenessRate'],
            labels: ['Posts Per Member', 'Effectiveness Rate'],
            barColors: ['#2abe81', '#24a5ff']
        });
		
		$('#kt_search').on('click', function(e) {
			e.preventDefault();
			initReportList(); 
			initFBChart(fbchart);
			initINChart(inchart);
			initTWChart(twchart);
			initReportStat();
		});
		
		var myDate = new Date();
		var today =(myDate.getMonth()+1) + '/' + myDate.getDate() + '/' +
		        myDate.getFullYear();
		$("#toDate").val(today);
		
		var days = 7; // Days you want to subtract
		var pdate = new Date();
		var plast = new Date(pdate.getTime() - (days * 24 * 60 * 60 * 1000));
		var pday =plast.getDate();
		var pmonth=plast.getMonth()+1;
		var pyear=plast.getFullYear();
		
		var previous7Day =(pmonth) + '/' + (pday) + '/' +pyear;
		$("#fromDate").val(previous7Day);
		
    	
    	$('#kt_datepicker').datepicker({
			todayHighlight: true,
			autoclose: true,
			templates: {
				leftArrow: '<i class="la la-angle-left"></i>',
				rightArrow: '<i class="la la-angle-right"></i>',
			},
		});
    	
    	$('#keyword_select2, #staff_select2, #team').select2({
            placeholder: "All",
        });
    	
    	initReportList(); 	
    	initReportStat();
    	
    	initFBChart(fbchart);
    	initINChart(inchart);
    	initTWChart(twchart);
    	
    });
	
	function initReportStat(){
		var param = {
				fromDate : $('#fromDate').val(),
				toDate : $('#toDate').val(),
				keyword : $('#keyword_select2').val(),
				staff : $('#staff_select2').val(),
				media : $('#media').val(),
				ratings : $('#ratings').val(),
				category : $('#category_select2').val(),
				graphType: 'FB'
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reports/totalstat',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(obj) {
				$('#loader').html('');
				$('#totalPostFb').html(obj.totalPostFbFormatter);
				$('#totalInteractionsFb').html(obj.totalInteractionsFbFormatter);
				$('#effectivenessRateFb').html(obj.effectivenessRateFb);
				$('#avgPostsPerMemberFb').html(obj.avgPostsPerMemberFb);
				$('#avgInteractionsPerMemberFb').html(obj.avgInteractionsPerMemberFb);
				$('#postsPerWeekFb').html(obj.postsPerWeekFb);
				
				$('#totalPostIn').html(obj.totalPostInFormatter);
				$('#totalInteractionsIn').html(obj.totalInteractionsInFormatter);
				$('#effectivenessRateIn').html(obj.effectivenessRateIn);
				$('#avgPostsPerMemberIn').html(obj.avgPostsPerMemberIn);
				$('#avgInteractionsPerMemberIn').html(obj.avgInteractionsPerMemberIn);
				$('#postsPerWeekIn').html(obj.postsPerWeekIn);
				
				$('#totalPostTw').html(obj.totalPostTwFormatter);
				$('#totalInteractionsTw').html(obj.totalInteractionsTwFormatter);
				$('#effectivenessRateTw').html(obj.effectivenessRateTw);
				$('#avgPostsPerMemberTw').html(obj.avgPostsPerMemberTw);
				$('#avgInteractionsPerMemberTw').html(obj.avgInteractionsPerMemberTw);
				$('#postsPerWeekTw').html(obj.postsPerWeekTw);
			}
		});
	}
	
	function initFBChart(fbchart){
		
		var param = {
				fromDate : $('#fromDate').val(),
				toDate : $('#toDate').val(),
				keyword : $('#keyword_select2').val(),
				staff : $('#staff_select2').val(),
				media : $('#media').val(),
				ratings : $('#ratings').val(),
				category : $('#category_select2').val(),
				graphType: 'FB'
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reports/chart',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(obj) {
				$('#loader').html('');
				if(obj.length > 0 ){
					fbchart.setData(obj);
				}
			}
		});
	}
	
	function initINChart(inchart){
		
		var param = {
				fromDate : $('#fromDate').val(),
				toDate : $('#toDate').val(),
				keyword : $('#keyword_select2').val(),
				staff : $('#staff_select2').val(),
				media : $('#media').val(),
				ratings : $('#ratings').val(),
				category : $('#category_select2').val(),
				graphType: 'IN'
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reports/chart',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(obj) {
				$('#loader').html('');
				if(obj.length > 0 ){
					inchart.setData(obj);
				}
			}
		});
	}
	
	function initTWChart(twchart){
		
		var param = {
				fromDate : $('#fromDate').val(),
				toDate : $('#toDate').val(),
				keyword : $('#keyword_select2').val(),
				staff : $('#staff_select2').val(),
				media : $('#media').val(),
				ratings : $('#ratings').val(),
				category : $('#category_select2').val(),
				graphType: 'TW'
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reports/chart',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(obj) {
				$('#loader').html('');
				if(obj.length > 0 ){
					twchart.setData(obj);
				}
			}
		});
	}
	
	function initYTChart(ytchart){
		
		var param = {
				fromDate : $('#fromDate').val(),
				toDate : $('#toDate').val(),
				keyword : $('#keyword_select2').val(),
				staff : $('#staff_select2').val(),
				media : $('#media').val(),
				ratings : $('#ratings').val(),
				category : $('#category_select2').val(),
				graphType: 'YT'
		};
		
		$.ajax({
			url : '<%=request.getContextPath()%>/rest/reports/chart',
			type : 'GET',
			data : param,
			async 		: true,
			beforeSend	: function(xhr){
				$('#loader').html('<button class="btn btn-brand btn-icon kt-spinner kt-spinner--center kt-spinner--sm kt-spinner--light"></button>');
			},
			success : function(obj) {
				$('#loader').html('');
				if(obj.length > 0 ){
					ytchart.setData(obj);
				}
			}
		});
	}

 </script>