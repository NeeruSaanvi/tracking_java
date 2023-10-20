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
						Annual Rankings
					</h3>
				</div>
				<div class="kt-portlet__head-toolbar">
				</div>
			</div>
			<div class="kt-portlet__body">
				<div class="row kt-margin-b-20">
					
				</div>
				<table class="table table-striped- table-bordered table-hover table-checkable" id="webPostTable">
					<thead>
                       	<tr>
                       		<th class="dt-header">Rank</th>
                       		<th class="dt-header">PRO-NAME</th>
                       		<th class="dt-header">SCORE</th>
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
	
	function viewApplication(userId){
		window.location.href = '<%=request.getContextPath()%>/viewApplication?id='+userId;
	}

	function editDeleteBankAccountRenderer(data, type, row) { 
		return '<input name="chkb[]" type="checkbox" value="'+data+'" />'
	}
	
	function viewRenderer(data, type, row){
		return "<button type=\"button\" onclick=\"viewApplication(" + data + " )\" class=\"btn btn-brand btn-elevate btn-pill btn-sm\">View</button>";
	}

	function getWebPostColumnData() {
		
		return [
			{ data : 'rank', width: '100px', defaultContent : '', className: 'dt-left' },
			{ data : 'prostaffName', width: '125px', defaultContent : '', className: 'dt-left' },
			{ data : 'rankScore', width: '125px', defaultContent : '', orderable: true, className: 'dt-left' }			
		];
	}

	function initTrendsList() {
		
		$('#webPostTable').dataTable({
			responsive: true,
			dom: `<'row'<'col-sm-12'tr>>
			<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
			processing 		: true,
			serverSide 		: true,
			destroy 		: true,
			ajax 			: {
				url 		: '<%=request.getContextPath()%>/rest/rankList',
				dataType 	: 'json',
				contentType : 'application/json',
				dataSrc 	: 'data',
				data		: beforeDataSubmit,
			},
			columns 		: getWebPostColumnData(),
			oLanguage		: getDatatablesLoadingText(),
			order			: [[ 0, 'asc' ]],
			paging 			: false,
			ordering 		: true,
			info 			: true
		});
	}

	jQuery(document).ready(function () {
    	initTrendsList(); 
    });

 </script>