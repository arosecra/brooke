
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


function show(id) {
	$(id).css("display", "block");
}

function hide(id) {
	$(id).css("display", "none");
}