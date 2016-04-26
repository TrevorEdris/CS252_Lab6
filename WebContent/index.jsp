<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

	<head>

		<title>Halo 5 Heatmaps</title>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no"/>

		<meta name="apple-mobile-web-app-capable" content="yes" />

		<link rel="stylesheet" href="style.css" />

	</head>

	<body>
		
		<div class="title strokeme">
			<h1>Halo 5 Heatmaps</h1>
		</div>


		<div>		
			<img class="newappIcon" src="images/Halo_Title_Screen_001.jpg">		
		</div>

		<div class="description">
			<p>This page is currently under production...</p>
			<form action="login" method="post" enctype="multipart/form-data">
				Map Type:<br>
				<input type="radio" name="killFlag" value="kills" checked="checked">
				Kills<br>
				<input type="radio" name="killFlag" value="deaths">
				Deaths<br><br>
				<input name="gamertag" placeholder="Gamer Tag">
				<button>GO</button>
			</form>
		</div>


	</body>

</html>