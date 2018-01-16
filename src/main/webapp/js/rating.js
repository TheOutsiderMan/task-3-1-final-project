$(function() {
	
	$.rating = function(e, o) {
        this.options = $.extend({
            image: '../img/stars.png',
            width: 32,
            stars: 10,
            minimal: 0,
            readOnly: false,
            url: '',
            type: 'post',
            auth: false,
            msgOK: '',
            msgNotOk: '',
            login: '',
            movieID: '',
            errorMsg: "",
            click: function() {}
        }, o || {});
        this.el = $(e);
        this.left = 0;
        this.val = parseFloat($('.val', e).val()) || 0;
        if (this.val > this.options.stars) this.val = this.options.stars;
        if (this.val < 0) this.val = 0;
        this.old = this.val;
        this.votes = parseInt($('.votes', e).val()) || '';
        this.vote_wrap = $('<div class="vote-wrap"></div>');
        this.vote_block = $('<div class="vote-block"></div>');
        this.vote_hover = $('<div class="vote-hover"></div>');
        this.vote_stars = $('<div class="vote-stars"></div>');
        this.vote_active = $('<div class="vote-active"></div>');
        this.init();
    };
    var $r = $.rating;
    $r.fn = $r.prototype = {
        rating: '0.1'
    };
    $r.fn.extend = $r.extend = $.extend;
    $r.fn.extend({
        init: function() {
            this.render();
            if (this.options.readOnly) return;
            var self = this,
                left = 0,
                width = 0;
            this.vote_hover.on('mousemove mouseover', function(e) {
                if (self.options.readOnly) return;
                var $this = $(this),
                    mark = 0;
                left = e.clientX > 0 ? e.clientX : e.pageX;
                width = left - $this.offset().left - 2;
                var max = self.options.width * self.options.stars,
                    min = self.options.minimal * self.options.width;
                if (width > max) width = max;
                if (width < min) width = min;
                mark = Math.round(width / self.options.width * 10) / 10;
                width = Math.ceil(width / self.options.width) * self.options.width;
                self.vote_active.css({
                    'width': width,
                    'background-position': '0 0'
                });
            }).on('mouseout', function() {
                if (self.options.readOnly) return;
                self.reset();
            }).on('click.rating', function() {
                if (!self.options.auth) {
                	$('#rating-movie-' + self.options.movieID).after("<div class='alert alert-warning alert-dismissible fade show movie-alert' role='alert'>" + self.options.msgNotOk + 
            				"<button type='button' class='close' data-dismiss='alert' aria-label='Close'>" +
            				"<span aria-hidden='true'>&times;</span>" +
            				"</button>" +
            				"</div>");
                	return;
                }
            	if (self.options.readOnly) return;
                var mark = Math.round(width / self.options.width * 10) / 10;
                if (mark > self.options.stars) mark = self.options.stars;
                if (mark < 0) mark = 0;
                self.old = self.val;
                self.val = (self.val * self.votes + mark) / (self.votes + 1);
                self.val.toFixed(1);
                if (self.options.url != '' && self.options.login != '') {
                    self.send(mark, self.options.login, self.options.movieID);
                }
                self.options.readOnly = true;
                self.options.click.apply(this, [mark]);
            });
        },
        set: function() {
            this.vote_active.css({
                'width': this.val * this.options.width,
                'background-position': '0 -96'
            });
        },
        reset: function() {
            this.vote_active.css({
                'width': this.old * this.options.width,
                'background-position': '0 -64px'
            });
        },
        render: function() {
        	$(".alert").alert('close');
            this.el.html(this.vote_wrap.append(this.vote_hover.css({
                padding: '0 4px',
                height: this.options.width,
                width: this.options.width * this.options.stars
            })));
            this.vote_block.append(this.vote_stars.css({
                height: this.options.width,
                width: this.options.width * this.options.stars,
                background: "url('" + this.options.image + "') 0 -32px"
            }), this.vote_active.css({
                height: this.options.width,
                width: this.val * this.options.width,
                background: "url('" + this.options.image + "') 0 -64px"
            })).appendTo(this.vote_hover);
         },
        send: function(mark, login, movieID) {
        	var self = this;
        	$.ajax({
                url: self.options.url,
                type: self.options.type,
                data: {
                	action: "vote",
                    mark: self.options.mark,
                    login: self.options.login,
                    movieID: self.options.movieID
                },
                dataType: 'json',
                success: function(data) {
                    self.votes++;
                    $('#rating-movie-' + self.options.movieID).after("<div class='alert alert-success alert-dismissible fade show movie-success' role='alert'>" + self.options.msgOK +  
                    		"<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
                    self.set();
                },
                error: function() {
                	$('#rating-movie-' + self.options.movieID).after("<div class='alert alert-warning alert-dismissible fade show movie-alert' role='alert'>"+ self.options.errorMsg +"<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
                	self.reset();
				}
            });
        }
    });
    $.fn.rating = function(o) {
        if (typeof o == 'string') {
            var instance = $(this).data('rating'),
                args = Array.prototype.slice.call(arguments, 1);
            return instance[o].apply(instance, args);
        } else {
            return this.each(function() {
                var instance = $(this).data('rating');
                if (instance) {
                    if (o) {
                        $.extend(instance.options, o);
                    }
                    instance.init();
                } else {
                    $(this).data('rating', new $r(this, o));
                }
            });
        }
    };
});
	