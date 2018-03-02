<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src = "./js/showpic.js"></script>
<script type="text/javascript" src = "./js/jquery-3.2.1.js"></script>
<title>文件路径显示</title>
<style>
body {
	margin : 0px;
	padding: 0px;
	border : 0px;
	line-height : 20px
	}
.clear {
	clear:both;
	}
.eightyper {
	width : 80%;
	margin:0 auto;
	}
table {
	table-layout: fixed;
	}
.titlepath {
	}
.rootpath {
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
	}
.titlepath a img{
	margin-top : 15px;
	float : left;
	}
.download {
	width:16px;
	height:28px;
	background-image:url("./img/download.png");
	background-size:12px 21px;
	background-repeat: no-repeat;
	display: table-cell;
	vertical-align:middle;
	text-align:center;
	}
.pictext {
	margin-top : 10px;
	text-align : center;
	overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
	width : 200px;
	}
.pics {
	max-width : 100%;
	margin-bottom: 20px;
	}
.singlepic {
	float : left;
	margin-top  : 10px;
	margin-right: 20px;
	}
.pic {
	border : 1px solid black;
	padding : 1px;
	display: table-cell;
	vertical-align:middle;
	text-align:center;
	width : 200px;
	height: 200px;
	position : relative;
	}
.pic img {
	max-width : 100%;
	max-height: 100%;
	}
.foot {
	height : 100px;
	}
#bigpic {
	position: fixed;
	width: 100%; 
	height: 100%; 
	left: 0; 
	top: 0; 
	bottom : 0;
	background: rgba(0, 0, 0, 0.7);
	z-index: 9998;
    }
#showpic {
	background-position:center center;
	background-repeat: no-repeat;
    width:100%;
    height:100%;
    background-size: contain;
    }
.quit {
	float:right;
	margin-top:20px;
	margin-right:20px;
	cursor:pointer;
	}
.left {
	cursor:pointer;
	float : left;
	margin-top : 30%;
	width : 100px;
	height : 100px;
	margin-left:20px;
	background-image : url("./img/left.png");
	background-size: 100px 100px;
	}
.right {
    cursor:pointer;
	float : right;
	margin-top : 30%;
	margin-right: 20px;
	width : 100px;
	height : 100px;
	background-image : url("./img/right.png");
	background-size: 100px 100px;
	}
.playbut { 
	background-image : url("./img/playbut.png");
	background-size: 100px 100px;
	background-repeat : no-repeat;
	background-position : 50px 50px;
	width : 200px;
	height: 200px;
	}
	
.playbut:hover { 
	background-image : url("./img/playhover.png");
	background-size: 100px 100px;
	background-repeat : no-repeat;
	background-position : 50px 50px;
	width : 200px;
	height: 200px;
	}

</style>
</head>
<body>
<div class = "eightyper">
<div class="titlepath">
<a href = "./path?path=${prepath }"><img width="50px" height="50px" src = "./img/return.png"/></a>
<div class="rootpath"><h2 style="float:right; display:inline"><a href="./">首页</a></h2>
<h1>${path }</h1>
<form action = "./upload" method = "post" enctype="multipart/form-data">
	<input type = "file" name = "file" />
	<input type = "text" name = "path" value = "${path }" hidden = "hidden"/>
	<input type = submit value = "上传" />
</form>
</div>
<audio loop>
</audio>
</div>
<br>
<br><c:forEach items="${value }" var="block">
	<!-- 文件夹及文件处理 -->
	<c:if test="${block.type=='dir' || block.type=='oth' || block.type=='aud'}">
	<table border = "1px">
	   <tr><th width = "60%">文件<c:if test="${block.type == 'dir'}">夹</c:if>名<br></th><th width = "10%">大小<br></th><th width = "15%">最后修改时间<br></th><th width="55px;" />O<br></tr>
	    <c:forEach items="${block.list }" var="val">
	        <tr>
	        	<td>
	        		<c:if test = "${block.type == 'dir' }">
	        			<a href = "./path?${val.querystr }">${val.name }</a>
	        		</c:if>
	        		<c:if test = "${block.type == 'oth' }">
	        			<a href = "./source?${val.querystr }">${val.name }</a>
	        		</c:if>
	        		<c:if test = "${block.type == 'aud' }">
	        			<a href = "./download?${val.querystr }" onclick = "playaudio(event)">play : ${val.name }</a>
	        		</c:if>
	        	</td>
	        	<td>${val.size }</td>
	        	<td>${val.modi }</td>
	        	<td><a href = "./download?${val.querystr }"><div class="download" float="left"></div><span>下载</span></a></td>
	       </tr>
	    </c:forEach>
	</table>
	</c:if>
	
	<!-- 图片处理-->
	<c:if test = "${block.type == 'pic' }">
		<div class = "pics">
		<c:forEach items="${block.list }" var="val">
			<div class = "singlepic">
				<div class = "pic"><a href="" onclick = "showpic(event)"><img src = "./source?${val.querystr }" /></a></div>
				<div class = "pictext"><a href = "./download?${val.querystr }" title = "${val.name }"><div class="download"></div>${val.name }</a></div>
			</div>
	    </c:forEach>
	    <div class = "clear"></div>
	    </div>
	</c:if>
	
	<!-- 视频 -->
	<c:if test = "${block.type == 'vid' }">
		<div class = "pics">
		<c:forEach items="${block.list }" var="val">
			<div class = "singlepic">
					<a href="./videoplay?${val.querystr }">
						<div class = "pic">
							<img src = "./videoshortcut?${val.querystr }" />
							<div class = "playbut" style="position: absolute;left: 0px; top: 0px;"></div>
							<span style = "color:black; position:absolute; right:5px; bottom:5px;">${val.size }</span>
						</div>
					</a>
				<div class = "pictext">
					<a href = "./download?${val.querystr }" title = "${val.name }">
						<div class="download"></div>${val.name }
					</a></div>
			</div>
	    </c:forEach>
	    <div class = "clear"></div>
	    </div>
	</c:if>
</c:forEach>
</div>
<!-- 页脚 -->
<div class = "foot"></div>
<div id = "bigpic" hidden="hidden">
	<div id = "showpic">
		<div onclick = "quit(event)" class = "quit"><img width = "50px" height="50px" src = "./img/quit.png"/></div>
		<div onclick = "left(event)" class = "left"></div>
		<div onclick = "right(event)" class = "right"></div>
	</div>
</div>
</body>
</html>