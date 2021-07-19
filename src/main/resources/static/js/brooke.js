

function show(id) {
	$(id).css("display", "block");
}

function hide(id) {
	$(id).css("display", "none");
}

function enable(catalog) {
	$(".tab-pane").css("display", "none");
	$("."+catalog).css("display", "block");
}

function addtocategory(id, book, catalog, category) {
	var url = "/manage/book/"+book+"/addtocategory/"+catalog+"/"+category
	$.ajax({
        url: url,
        type: 'GET',
        dataType: 'html', 
        success: function(res) {
            $('#'+id).prop('disabled', true);
        }
    });
}

function saveNewToCEntry(book, pageindex) {
	var toc = $("#toc-entry").val();
	var url = "/book/addtotoc/"+book+"/"+pageindex+"/"+toc
	$.ajax({
        url: url,
        type: 'GET',
        dataType: 'html', 
        success: function(res) {
            hide('#add-toc-modal');
        }
    });
}
