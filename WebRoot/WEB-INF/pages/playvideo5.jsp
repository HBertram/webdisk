<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src = "./js/clipboard.min.js"></script>
<script src = "./js/jquery-3.2.1.js"></script>
<style>
.left {
	left:0px;
	bottom:-30px;
	position: absolute;
	width : 200px;
	text-overflow : ellipsis;
	white-space:nowrap;
	overflow:hidden;
	}
.right {
	right:0px; 
	bottom:-30px; 
	position: absolute;
	width : 200px;
	text-overflow : ellipsis;
	white-space:nowrap;
	overflow:hidden;
	}
.box {
	display:inline-block; 
	margin: 0px auto; 
	position:relative
	}
</style>
<title>${videoname }</title>
</head>
<body style="text-align: center; background-color: rgb(0, 0, 0);">

<div class = "box">
	<video style="max-height: 100%; max-width: 100%; margin:0 auto;" controls="" autoplay="" name="media">
		<source id = 'source' src="${videopath }" type="video/mp4">
		浏览器不支持html5
	</video>
	<div id = "forclip" data-clipboard-text="${videopath }"/>
	<div class = "left"><a href="${prevideopath }" title = "${prevideoname }">前一个:${prevideoname }</a></div>
	<div class = "right"><a href="${nextvideopath }" title = "${nextvideoname }">下一个:${nextvideoname }</a></div>
	<br/> <br/><br/>
</div>
<button id="btn" class="js-copy" data-clipboard-text="">复制视频地址</button>
<script>
	$('#btn').attr('data-clipboard-text', $('#source')[0].src);
	var clipboard = new Clipboard("#btn");
    //复制成功执行的回调，可选
    clipboard.on('success', function(e) {
        alert("复制成功 " + e.text);
    });
    //复制失败执行的回调，可选
    clipboard.on('error', function(e) {
        alert("复制失败 " + e.text);
    });
</script>
</body>
</html>