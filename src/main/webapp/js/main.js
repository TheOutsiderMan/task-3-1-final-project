$(function() {
	$(".delete-movie").click(function(){
		var self = this;
		var user = $(this).attr("data-user");
		var movie = $(this).attr("data-movie");
		var error = $(this).attr("data-error");
		var success = $(this).attr("data-success");
		var page = $(this).attr("data-page");
		$.ajax({
			url: 'app',
			type: 'post',
			data: {
				action: "delete_user_mark",
				login: user,
				movieID: movie,
				page: page
			},
			success: function(){
				self.after("<div class='alert alert-success alert-dismissible fade show movie-success' role='alert'>" + success + 
        				"<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
        				"<span aria-hidden='true'>&times;</span>" +
        				"</button>" +
        				"</div>");
			},
			error: function() {
				self.after("<div class='alert alert-warning alert-dismissible fade show movie-alert' role='alert'>" + error + 
            				"<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
            				"<span aria-hidden='true'>&times;</span>" +
            				"</button>" +
            				"</div>");
				}
		})
	});
});

