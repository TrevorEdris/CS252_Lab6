<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>

		<title>Halo 5 Heatmaps</title>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no"/>

		<meta name="apple-mobile-web-app-capable" content="yes" />

		<link rel="stylesheet" href="heatmap.css" />

	</head>

	<body>
		
		<div class="title">
			<h1>Halo 5 Heatmaps</h1>
			<h1 class="text">Gamer Tag: ${gamertag}</h1>
			<h1 class="text">Map Type: ${killFlag}</h1>
			
		</div>
		
		<div>
			<form action="index.jsp" method="post" enctype="multipart/form-data">
     		 	<button>Change Gamer Tag</button>
			</form>	
			<form action="login" method="post" enctype="multipart/form-data">
				<br>Change Map Type:<br>
				<input type="radio" name="killFlag" value="kills">
				Kills<br>
				<input type="radio" name="killFlag" value="deaths">
				Deaths<br>
				<input type="hidden" name="gamertag" value="${gamertag}">
				<button>Submit</button>
			</form>
		</div>

		<div>
			<h1 id="mapName"></h1>
			<img class="rightHalf newappIcon" id="myImage">		
		</div>

		<div id='cssmenu'>
		<ul >
		   <li class='active'><span>Select Map</span></li>
		  <!-- <li id = "Altitude" onclick="changeImage(this.id)"><a href='#'><span>Altitude</span></a></li> -->
		   <li id = "Coliseum" onclick="changeImage(this.id)"><a href='#'><span>Coliseum</span></a></li>
		   <li id = "Crossfire" onclick="changeImage(this.id)"><a href='#'><span>Crossfire</span></a></li>
		   <li id = "Eden" onclick="changeImage(this.id)"><a href='#'><span>Eden</span></a></li>
		   <li id = "Empire" onclick="changeImage(this.id)"><a href='#'><span>Empire</span></a></li>
		   <li id = "Fathom" onclick="changeImage(this.id)"><a href='#'><span>Fathom</span></a></li>
		  <!-- <li id = "Gambol" onclick="changeImage(this.id)"><a href='#'><span>Gambol</span></a></li> --> 
		  <!--  <li id = "Orion" onclick="changeImage(this.id)"><a href='#'><span>Orion</span></a></li> -->
		  <!--  <li id = "Pegasus" onclick="changeImage(this.id)"><a href='#'><span>Pegasus</span></a></li> -->
		   <li id = "Plaza" onclick="changeImage(this.id)"><a href='#'><span>Plaza</span></a></li>
		   <li id = "Regret" onclick="changeImage(this.id)"><a href='#'><span>Regret</span></a></li>
		   <li id = "TheRig" onclick="changeImage(this.id)"><a href='#'><span>The Rig</span></a></li>
		  <!--  <li id = "Trench" onclick="changeImage(this.id)"><a href='#'><span>Trench</span></a></li> -->
		  <!--  <li id = "Trident" onclick="changeImage(this.id)"><a href='#'><span>Trident</span></a></li> -->
		   <li class='last'  id = "Truth" onclick="changeImage(this.id)"><a href='#'><span>Truth</span></a></li>
		</ul>
		</div>

		<script>
		function changeImage(id) {
			var image = document.getElementById('myImage');

			if(id == "Altitude") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_altitude.jpg";
				document.getElementById("mapName").innerHTML = "Altitude";
			}
			else if(id == "Coliseum") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_coliseum.jpg";
				document.getElementById("mapName").innerHTML = "Coliseum";
			}
			else if(id == "Crossfire") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_crossfire.jpg";
				document.getElementById("mapName").innerHTML = "Crossfire";
			}
			else if(id == "Eden") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_eden.jpg";
				document.getElementById("mapName").innerHTML = "Eden";
			}
			else if(id == "Empire") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_empire.jpg";
				document.getElementById("mapName").innerHTML = "Empire";
			}
			else if(id == "Fathom") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_fathom.jpg";
				document.getElementById("mapName").innerHTML = "Fathom";
			}
			else if(id == "Gambol") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_gambol.jpg";
				document.getElementById("mapName").innerHTML = "Gambol";
			}
			else if(id == "Orion") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_orion.jpg";
				document.getElementById("mapName").innerHTML = "Orion";
			}
			else if(id == "Pegasus") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_pegasus.jpg";
				document.getElementById("mapName").innerHTML = "Pegasus";
			}
			else if(id == "Plaza") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_plaza.jpg";
				document.getElementById("mapName").innerHTML = "Plaza";
			}
			else if(id == "Regret") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_regret.jpg";
				document.getElementById("mapName").innerHTML = "Regret";
			}
			else if(id == "TheRig") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_the_rig.jpg";
				document.getElementById("mapName").innerHTML = "The Rig";
			}
			else if(id == "Trench") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_trench.jpg";
				document.getElementById("mapName").innerHTML = "Trench";
			}
			else if(id == "Trident") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_trident.jpg";
				document.getElementById("mapName").innerHTML = "Trident";
			}
			else if(id == "Truth") {
				image.src = "images/Maps/Heatmaps/${gamertag}_${killFlag}_truth.jpg";
				document.getElementById("mapName").innerHTML = "Truth";
			}
			else {}

		}
		</script>
		
		

	</body>

</html>