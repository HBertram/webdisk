var showpic = function(event) {
	event.preventDefault();
	var back = $('#bigpic');
	back.removeAttr('hidden');
	
	var target = event.target;
	currentpic = $(target);
	setbgimg();
};

var quit = function(event) {
	$('#bigpic').attr('hidden', 'hidden');
	currentpic = undefined;
};

var right = function(evnet) {
	var nextimg = currentpic.parents('.singlepic').next().find('img')
	if (nextimg.length <= 0)
		nextimg = $('.singlepic').first().find('img');
	currentpic = nextimg;
	setbgimg();
};

var left = function(event) {
	var nextimg = currentpic.parents('.singlepic').prev().find('img')
	if (nextimg.length <= 0)
		nextimg = $('.singlepic').last().find('img');
	currentpic = nextimg;
	setbgimg();
}


var setbgimg = function() {
	$div = $('#showpic');
	src = currentpic.attr('src');
	src = 'url("' + src + '")';
	src = src.split('\\').join('/');
	//$div[0].style.backgroundImage = src;
	$div.css("background-image", src);
}

var playaudio = function (event) {
	event.preventDefault();
	audio = $('audio');
	audio.attr('autoplay', 'true');
	audio.attr('loop', 'loop');
	audio.attr('src', event.target.href);
	audio[0].play();
}

var setHover = function(event) {
	event.target.src = "./img/playhover.png";
}
var setOut = function(event) {
	event.target.src = "./img/playbut.png";
}