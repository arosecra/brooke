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