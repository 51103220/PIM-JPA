var maxPaginationLinks = 2;
var formHasChanged = false;
var submitted = false;
/**********************************
 * *SELECT ON CHANGE
 **********************************/
function selectHandler() {
	$("body").on("change","select",function() {
		var class_name = $(this).attr('class').replace(/empty/g, "");
		$(this).attr('class', class_name);
	});

};
function fixBodyHeight() {
	var header_height = $(".header").outerHeight();
	var body_height = $(window).height() - header_height;
	$("#main").css("height", body_height + "px");
};

/*********************************
 * * SET AND UNSET SELECTED LINKS
 *********************************/
function setLinkSelected(link) {
	var newClass = link.attr("class") + " selected";
	link.attr("class", newClass);
};
function unsetLinkSelected(link) {
	var newClass = link.attr("class").replace(/selected/g, "");

	link.attr("class", newClass);
};
/**********************************
 * * SET AND UNSET ERROR INPUTS
 **********************************/
function setErrorInput(input, isSet, code) {
	if (isSet) {
		var newClass = input.attr("class") + " errorInput";
		input.attr("class", newClass);
		input.parent().find("p.hiddenError").css("display","table-row");
		input.parent().find("p.hiddenError").html(code);
	} else {
		var className = input.attr("class").replace(/errorInput/g, "");
		input.attr("class", className);
		input.parent().find("p.hiddenError").hide();
		input.parent().find("p.hiddenError").html(code);
	}
};
/**********************************
 * * PAGINATION: SET LINK VISIBLE
 * * OR INVISBLE
 **********************************/
function handlePagination(id){
	var links = $("#projectList .pagination .paging");
	id =  parseInt(id);
	var i =1;
	var start,end =0;
	if(id%maxPaginationLinks ==0){
		start = id -maxPaginationLinks+1;
		end = id;
	}
	else{
		start = id;
		end = id+maxPaginationLinks-1;
	}
	links.each(function(){
		var link =$(this);
		if (i>=start && i <=end){
			link.show();
		}else{
			link.hide();
		}
			
		i=i+1;
	});
};
$(document).ready(function() {
	/***************************************************************************
	 * * New Form Has Changed and on Before Unload Events
	 **************************************************************************/
	$("#main #contentBody").on("change",".general-form input", function(){
		formHasChanged = true;
	});
	window.onbeforeunload = function (e) {
        if (formHasChanged && !submitted) {
            var message = "Your changes have not been saved! Are you sure you want to leave?", e = e || window.event;
            if (e) {
                e.returnValue = message;
            }
            return message;
        }
    }
	/***************************************************************************
	 * * Dialog Delete Confirmation
	 **************************************************************************/
	 $("#dialog").dialog({
	      autoOpen: false,
	      modal: true,
	      dialogClass:"customDialog"
	 });
	/***************************************************************************
	 * * Menu item clicks
	 **************************************************************************/
	$("#main #menuList #selectList li a").click(function(e) {
		e.preventDefault();
		unsetLinkSelected($(this).parent().parent().find("a.selected"));
		var class_str = $(this).attr("class");
		if (class_str && class_str.indexOf("notAffected") >= 0) {
			setLinkSelected($(this).parent().next().find("a"));
		} else {
			setLinkSelected($(this));
		}
		// AJAX NAVIGATOR
		var url = $(this).attr("href");
		if(formHasChanged && !submitted){
			if(confirm("Your changes have not been saved! Are you sure you want to leave?")){
				formHasChanged = false;
				$.ajax({
					method : "GET",
					url : url
				}).done(function(data) {
					$("#main #contentBody").html(data);
				}).fail(function(jqXHR, textStatus,errorThrown) {
					window.location.href = $(".header #projectName").attr("href") + "errorsunexpected=" + errorThrown;
				});
			}
		}else{
			$.ajax({
				method : "GET",
				url : url
			}).done(function(data) {
				$("#main #contentBody").html(data);
			}).fail(function(jqXHR, textStatus,errorThrown) {
				window.location.href = $(".header #projectName").attr("href") + "errorsunexpected=" + jqXHR.responseText;
			});
		}
	});

	/***************************************************************************
	 * *Date Picking event
	 **************************************************************************/
	$('#main #contentBody').on("click", ".datePicker", function(){
		$(this).datepicker({ dateFormat: 'dd-mm-yy' });
	});
	$('#main #contentBody').on("focus", ".datePicker", function(){
		$(this).datepicker({ dateFormat: 'dd-mm-yy' });
	});
	$('#main #contentBody').on("click", ".datePickerIcon",function(e){
		e.preventDefault();
		$(this).parent().find("input").focus();
	});
	fixBodyHeight();

	/***************************************************************************
	 * * General Form Handling
	 **************************************************************************/
	$('#main #contentBody').on("click",".general-content .processBtn",function(e) {
		e.preventDefault();
		var btn = $(this);
		var form = $(this).parent().parent().find(".general-form").first();
		var $inputs = form.find("input");
		var $selects = form.find("select");
		var values = {};
		if(btn.data("requestRunning")){
			return;
		}
		$inputs.each(function() {
			setErrorInput($(this), false, "");
			var id = $(this);
			if(id.attr("name") == "id"){
				var val = id.val();
				if(val){
					values[id.attr("name")] = val;
				}
			}
			else{
				values[$(this).attr("name")] = $(this).val();
			}
			if($(this).attr("name") == "members"){
					var val = $(this).val().replace(/ /g,'').split(",");
					
					var placeholder = $(this).attr("placeholder");
					if(placeholder){
						val = placeholder.replace(/ /g,'').split(",");
						val.pop();
						
					}
					values[$(this).attr("name")] = val ;
			}
			if($(this).attr("name") == "startDate" || $(this).attr("name") == "endDate" ){
				if($(this).val()){
					var from = $(this).val().split("-");
					var date = new Date(from[2], from[1] - 1, from[0]);
					if (date == "Invalid Date"){
						values[$(this).attr("name")] = null;
					}
					else{
						values[$(this).attr("name")] =  date.getFullYear() + "-" + (date.getMonth()+1) +"-" + date.getDate();
					}
				}
				else{
					values[$(this).attr("name")] = null;
				}
			}
		});
		
		$selects.each(function() {
			setErrorInput($(this), false, "");
			values[$(this).attr("name")] = $(this).val();
		});
		btn.data('requestRunning', true);
		$.ajax({
			contentType : 'application/json',
			type : form.attr("method"),
			url : form.attr("action"),
			async : false,
			dataType : 'json',
			data : JSON.stringify(values),
			success : function(data) {
				if (data.status == "FAIL") {
					for (var i = 0; i < data.result.length; i++) {
						var field = data.result[i].field;
						var code = data.result[i].code;
						var message = data.result[i].defaultMessage;
						
						if (code == "NotEmpty" || code == "NotNull" || code == '') {
							$(".errorPanel").show();
							message ='';
						}
						
						$inputs.each(function() {
							if ($(this).attr("name") == field) {
								setErrorInput($(this), true, message);
							}
						});
						$selects.each(function() {
							if ($(this).attr("name") == field) {
								setErrorInput($(this), true, message);
							}
						});
						
					}
				} else {
					submitted = true;
					window.location.href = $(".header #projectName").attr("href");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
				if (XMLHttpRequest.status == 409){
					$(".errorPanel").show();
					$(".errorPanel .panelMessage").html(XMLHttpRequest.responseText);
				}else{
					window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + XMLHttpRequest.responseText;
				}
			},
			complete: function(){
				btn.data('requestRunning', false);
			}
		})
	});
	/***************************************************************************
	 * * Get Project detail Handling
	 **************************************************************************/
	$('#main #contentBody').on("click","#projectList #searchDatas .projectDetail",function(e){
		e.preventDefault();
		var url = $(this).attr("href");
		$.ajax({
			method : "GET",
			url : url
		}).done(function(data) {
			$("#main #contentBody").html(data);
		}).fail(function(jqXHR, textStatus,errorThrown) {
			if (jqXHR.status == 409){
				$(".errorPanel").show();
				$(".errorPanel .panelMessage").html(jqXHR.responseText);
			}else{
				window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + jqXHR.responseText;
			}
		});
	});
	/***************************************************************************
	 * * Search Form Handling
	 **************************************************************************/
	//Check Box
	$('#main #contentBody').on("click","#projectList #searchDatas [type=checkbox]",function(){
		var boxes = $("#projectList #searchDatas input:checked");
		if(boxes.length>0){
			$("#projectList .resultRow").show();
			$("#projectList .resultRow .totalItems").html(boxes.length+" item(s) selected");
		}else{
			$("#projectList .resultRow").hide();
		}
	});
	//Single Delete
	$('#main #contentBody').on("click","#projectList #searchDatas .deleteIcon",function(e){
		e.preventDefault();
		var link = $(this);
		$("#dialog").dialog({
	      buttons : {
	        "Delete" : function() {
	        	$.ajax({
	    			method : "POST",
	    			url : link.attr("href")
	    		}).done(function(data) {
	    			link.parent().parent().remove();
	    		}).fail(function(jqXHR, textStatus,errorThrown) {
	    			if (jqXHR.status == 409){
						$(".errorPanel").show();
						$(".errorPanel .panelMessage").html(jqXHR.responseText);
					}else{
						window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + jqXHR.responseText;
					}
	    		});
	        	$(this).dialog("close");
	        },
	        "Cancel" : function() {
	          $(this).dialog("close");
	        }
	      }
		});
		$("#dialog").html("Are you sure to delete this item ?");
		$("#dialog").dialog("open");
		
	});
	//Multiple Deletes
	$('#main #contentBody').on("click","#projectList .resultRow .deleteMultiple",function(e){
		var link = $(this);
		e.preventDefault();
		var ids = [];
		var boxes = $("#projectList #searchDatas input:checked");
	
		boxes.each(function(){
			var box = $(this);
			if(box.val() == "true"){
				ids.push(box.attr("id"));
			}
		});
		if(!ids.length){
			$(".errorPanel").show();
			$(".errorPanel .panelMessage").html("Can not delete these items");
			return;
		}
		$("#dialog").dialog({
		      buttons : {
		        "Delete" : function() {
		        	$.ajax({
		    			method : "POST",
		    			url : link.attr("href"),
		    			data: {
		    				ids: ids
		    			}
		    		}).done(function(data) {
		    			boxes.each(function(){
		    				var box = $(this);
		    				if(box.val() == "true"){
		    					box.parent().parent().remove();
		    				}
		    			});
		    			$("#projectList .resultRow").hide();
		    		}).fail(function(jqXHR, textStatus,errorThrown) {
		    			if (jqXHR.status == 409){
							$(".errorPanel").show();
							$(".errorPanel .panelMessage").html(jqXHR.responseText);
						}else{
							window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + jqXHR.responseText;
						}
		    		});
		        	$(this).dialog("close");
		        },
		        "Cancel" : function() {
		          $(this).dialog("close");
		        }
		      }
			});
			$("#dialog").html("Are you sure to delete these items ?");
			$("#dialog").dialog("open");
	});
	//Search 
	$('#main #contentBody').on("click","#projectList #searchInputs #search_btn",function(e){
		e.preventDefault();
		var btn = $(this);
		if(btn.data("requestRunning")){
			return;
		}
		var form = $("#projectList #searchInputs");
		var keywords =  $("#projectList #searchInputs #keywords").val();
		var statusKey = $("#projectList #searchInputs #statusKey").val();
		btn.data('requestRunning', true);
		$.ajax({
			method : "POST",
			url : form.attr("action"),
			data: {
				keywords: keywords,
				statusKey: statusKey
			}
		}).done(function(data) {
			$("#main #contentBody").html(data);
		}).fail(function(jqXHR, textStatus,errorThrown) {
			window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + jqXHR.responseText;
		}).complete(function(){
			btn.data('requestRunning', false);
		});
		
	});
	//Reset Search
	$('#main #contentBody').on("click","#projectList #searchInputs #reset_btn",function(e){
		e.preventDefault();
		$.ajax({
			method : "GET",
			url : "resetCriteria"
		}).done(function(data) {
			$("#main #contentBody").html(data);
		}).fail(function(jqXHR, textStatus,errorThrown) {
			window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + errorThrown;
		});
	});
	/***************************************************************************
	 * * PAGINATION
	 **************************************************************************/
	handlePagination("2");
	$('#main #contentBody').on("click","#projectList .pagination .paging",function(e){
		e.preventDefault();
		var link = $(this);
		var id = link.attr("id");
		$.ajax({
			method : "GET",
			url : link.attr("href")
		}).done(function(data) {
			$("#main #contentBody").html(data);
			handlePagination(id);
		}).fail(function(jqXHR, textStatus,errorThrown) {
			window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + errorThrown;
		});
	});
	$('#main #contentBody').on("click","#projectList .pagination .directives",function(e){
		e.preventDefault();
		var directive = $(this);
		var max = parseInt($("#projectList #paginationMax").val());
		var start = parseInt($("#projectList #paginationStart").val());
		var end = parseInt($("#projectList #paginationEnd").val());
		var id =0;
		if (directive.attr("id") == "previous"){
			id =start;
			if(start>maxPaginationLinks)
				id=id-maxPaginationLinks;
		}else{
			id =end+1; 
			if (id > max){
				id = max;
			}
		}
		var url = directive.parent().attr("href") + id;
		
		$.ajax({
			method : "GET",
			url : directive.attr("href") + id 
		}).done(function(data) {
			$("#main #contentBody").html(data);
			handlePagination(id);
		}).fail(function(jqXHR, textStatus,errorThrown) {
			window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + errorThrown;
		});
	});
	/***************************************************************************
	 * * SELECT HANDLING AND CLOSE PANEL HANDLING
	 **************************************************************************/
	
	selectHandler();
	$('#main #contentBody').on("click",".errorPanel .closePanel",function(e) {
		
		e.preventDefault();
		$(this).parent().hide();
	});
	/***************************************************************************
	 * * ENTER SUBMIT FORM AND FIRST INPUT FOCUS
	 **************************************************************************/
	 $(window).keydown(function(event){
	    if(event.keyCode == 13) {
	      event.preventDefault();
	      $(".processBtn").click();
	    }
	  });
	 
	 /***************************************************************************
		 * * TABLE HEADER SORTING
	 **************************************************************************/
	 /***************************************************************************
		 * * TABLE HEADER FILTER
	 **************************************************************************/
	 $('#main #contentBody').on("keyup","#projectList #filterInputs input",function(){
		 var value = $(this).val().split(" ");
		 var name = $(this).attr("name");
		 var tds;
		 if (name =="pNumber"){
			tds = $("#projectList #searchDatas tbody").find("td.col1");
		 }
		 if (name =="pName"){
			 tds = $("#projectList #searchDatas tbody").find("td.col2");
		 }
		 if (name =="pStatus"){
			 tds = $("#projectList #searchDatas tbody").find("td.col3");
		 }
		 if (name =="pCustomer"){
			 tds = $("#projectList #searchDatas tbody").find("td.col4");
		 }
		 if (name =="pDate"){
			 tds = $("#projectList #searchDatas tbody").find("td.col5");
		 }
		 var rows = $("#projectList #searchDatas tbody").find("tr");
		 if($(this).val() ==""){
			 rows.show();
			 return;
		 }
		 rows.hide();
		 tds.filter(function (i, v) {
	        var t = $(this);
	        for (var d = 0; d < value.length; ++d) {
	            if (t.is(":contains('" + value[d] + "')")) {
	                return true;
	            }
	        }
	        return false;	
		 }).parent().show();
		 }).focus(function () {
		    $(this).val("");
		    $(this).unbind('focus');
		 });
	 /***************************************************************************
		 * * VISA DROPDOWN
	 **************************************************************************/
	 $('#main #contentBody').on("focus",".tagsDiv .tags .tagInput input",function(e){
		$.ajax({
			method : "GET",
			url : "getVisas"
		}).done(function(data) {
			var content = "";
			var i,len= 0;
			for(i=0,len = data.length;i<len; i++){
				content = content + "<li><a tabIndex='-1' id ='" + data[i].visa +"' href='#' class='visaLink'>" +data[i].visa+": "+data[i].firstName + " "+data[i].lastName +"</a></li>"
			}
			
			 $(".visaList").html(content);
			 $(".visaList").show();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			window.location.href = $(".header #projectName").attr("href") + "/errorsunexpected=" + jqXHR.responseText;
		});
		
	 });
	 $('#main #contentBody').on("blur",".tagsDiv .tags .tagInput input",function(e){
		 $(".visaList").hide();
	 });
	 
	 $('#main #contentBody').on("mousedown",".visaList",function(e){
		 e.preventDefault();
	 }).on("click",".visaLink",function(e){
		e.preventDefault();
		var link = $(this);
		var oldContent = $(".tagsDiv .tags").html();
		var tagsLi = $(".tagsDiv .tags").find("li");
		var exists = false;
		tagsLi.each(function(){
			if ($(this).attr("id") == link.attr("id")){
				exists = true;
			}
		});
		if (!exists){
			var newContent = "<li class='tag' id='"+link.attr("id")+"'>" +link.html() +"&nbsp;<a class='tagClose' href='#'><span class='glyphicon glyphicon-remove'></span></a></li>";
			$(".tagsDiv .tags").html(newContent+oldContent);
			var placeholder = $(".tagsDiv .tags .tagInput input").attr("placeholder");
			$(".tagsDiv .tags .tagInput input").attr("placeholder", placeholder + link.attr("id") + ",");
		}
		$(".tagsDiv .tags .tagInput input").blur();
	 });
	 $('#main #contentBody').on("click", ".tagClose", function(e){
		 e.preventDefault();
		 var id = $(this).parent().attr("id");
		 $(this).parent().remove();
		 var placeholder = $(".tagsDiv .tags .tagInput input").attr("placeholder");
		 $(".tagsDiv .tags .tagInput input").attr("placeholder", placeholder.replace(id+",",""));
	 });
});