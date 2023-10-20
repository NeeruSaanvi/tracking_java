
function showError(message) {
	$("#sucessErrorMsgDiv").css({"text-align": "center"});
	$("#sucessErrorMsgDiv").attr("role","alert");
	$("#sucessErrorMsgDiv").addClass( "alert alert-danger" );
	$("#sucessErrorMsgDiv").show();
	$("#sucessErrorMsgDiv").html(message);
	
	setTimeout('$("#sucessErrorMsgDiv").fadeOut("slow");', 5000);
}

function showMessage(message) {
	$("#sucessErrorMsgDiv").css({"text-align": "center"});
	$("#sucessErrorMsgDiv").addClass( "alert alert-success" );
	$("#sucessErrorMsgDiv").show();
	$("#sucessErrorMsgDiv").html(message);
	
	setTimeout('$("#sucessErrorMsgDiv").fadeOut("slow");', 5000);
}

function showMessageOnDiv(message, divTag) {
	$("#" + divTag).css({"text-align": "center"});
	$("#" + divTag).attr("role","alert");
	$("#" + divTag).addClass( "alert alert-success" );
	$("#" + divTag).show();
	$("#" + divTag).html(message);
	
	setTimeout('$("#' + divTag + '").fadeOut("slow");', 5000);
}

function showErrorOnDiv(message, divTag) {
	$("#" + divTag).css({"text-align": "center"});
	$("#" + divTag).attr("role","alert");
	$("#" + divTag).addClass( "alert alert-danger" );
	$("#" + divTag).show();
	$("#" + divTag).html(message);
	
	setTimeout('$("#' + divTag + '").fadeOut("slow");', 5000);
}

function showLoader(tag) {
	$("#" + tag).addClass("kt-spinner kt-spinner--right kt-spinner--md kt-spinner--light");
}

function hideLoader(tag) {
	$("#" + tag).removeClass("kt-spinner kt-spinner--right kt-spinner--md kt-spinner--light");
}

//Adding a new function 'center()' to the jQuery so any element can be displayed in the center of the screen
//For example   $(#xyz).center(); //this will display this element into the center
$.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + 
                                                $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + 
                                                $(window).scrollLeft()) + "px");
    return this;
}

function beforeDataSubmit(data) {
	
	var columnLength = data.columns.length;
	var index;
	for (index = 0; index < columnLength; index++) {
		if (data.order[0]["column"] == index) {
			var obj = data.columns[index]["data"];
			if(obj instanceof Object == true){
				data.sort = obj[0].data;
			} else {
				data.sort = data.columns[index]["data"];
			}			
		}
	}
	data.direction = data.order[0]["dir"];
	
	delete data.columns;
	delete data.order;
	//delete data.search;
	data.page = data.start;
	data.size = data.length;
	delete data.length;
	delete data.start;
}

function getDatatablesLoadingText() {
   return {
	    sDecimal 		: "",
	    sEmptyTable		: "<div style='width: 100%; text-align: center; color: #4dd6b5; font-size: 16px;'> <br><br>No data available to display<br><br></div>",
	    sInfo			: "Showing _START_ to _END_ of _TOTAL_ entries",
	    sInfoEmpty		: "Showing zero entries",
	    sInfoFiltered	: "(filtered from _MAX_ total entries)",
	    sInfoPostFix	: "",
	    sThousands		: ",",
	    sLengthMenu		: "Show _MENU_ entries",
	    sLoadingRecords	: "Loading records to display",
	    sProcessing		: "Processing the data...",
	    sSearch			: "Search:",
	    sZeroRecords	: "<br>No data records found",
	    sPaginate		: {
	        first		: "First",
	        last		: "Last",
	        next		: "Next",
	        previous	: "Previous"
	    },
	    sAria: {
	        sortAscending	:  ": activate to sort column ascending",
	        sortDescending  :  ": activate to sort column descending"
	    }
   };
}