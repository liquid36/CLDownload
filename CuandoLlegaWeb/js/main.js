

var map;
var calles;
var intersecciones;
var geocoder;

var idCalle = -1;
var idInter = -1;
var w  = -1;
var estado = 1;

$(document).ready(function() {	
	var mapOptions = {zoom: 8,center: new google.maps.LatLng(-34.397, 150.644)};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	geocoder = new google.maps.Geocoder();	
	Init();
});	

function Init() {	
	$("#selectFiltro").removeAttr("disabled");
	$("#btSave").attr("disabled","disabled");
	$("#btCancel").attr("disabled","disabled");
	
	$.getJSON( "getCalle.php?action=calle", function( data ) {
		calles = data;	
		var items = [];
		for( var i = 0; i < data.length; i++ ) { 
			if ( (data[i].geo > 0  && estado == 1) ||
			     (data[i].geo == 0 && estado == 2) || estado == 3 ) {
			     
				if (data[i].geo < data[i].all)     
					items.push('<li class="list-group-item green" code="' + data[i].id + '">' +  data[i].name  +  '</li>');
				else
					items.push('<li class="list-group-item" code="' + data[i].id + '">' +  data[i].name  +  '</li>');
					
			}
		}
		$("#txtCuanto").html(items.length);
		$("#barra").html("");
		$( "<ul/>", {
			"class": "list-group",
			html: items.join( "" )
		  }).appendTo( "#barra" );
		  
		$(".list-group-item").click(function(){
			idCalle = $(this).attr("code");
			$("#txtCalle").html($(this).html());
			$.getJSON( "getCalle.php?action=interseccion&calle=" + idCalle, function( data ) {
				intersecciones = data;
				var items = [];
				$("#barra").html("");
				for( var i = 0; i < data.length; i++ ) { 
					var span = ""; 
					if (data[i].lat !== "") {
						 span = "<span class='glyphicon glyphicon-map-marker text-right'><span>"; 
						 agregarMarket(data[i]);
					}
					if (i == 0) {
						items.push('<li class="list-group-item active" index="' + i + '"  code="' + data[i].id + '">' +  data[i].name  + span + '</li>');
						idInter = data[i].id;
						w = i;
					} else
						items.push('<li class="list-group-item" index="' + i + '" code="' + data[i].id + '">' +  data[i].name  + span + '</li>');				
				}
				
				
				$("#selectFiltro").attr("disabled","disabled");
				$("#btSave").removeAttr("disabled");
				$("#btCancel").removeAttr("disabled");
				
				$( "<ul/>", {
					"class": "list-group",
					html: items.join( "" )
				  }).appendTo( "#barra" );
				
				$(".list-group-item").click(function(){
						var index = $(this).attr("index");
						$("li[code=" + idInter + "]").removeClass("active");
						w = index;
						$("li[code=" + intersecciones[w].id + "]").addClass("active");
						idInter = intersecciones[w].id;
				});
				
			});
			centrarMapa($(this).text());
		});  
		  
	});
	
	google.maps.event.clearListeners(map,'click');
	google.maps.event.addListener(map, 'click', function(event) {
		if (idCalle != 0 && idInter != 0) {
			intersecciones[w].lat =  event.latLng.lat();
			intersecciones[w].lng =  event.latLng.lng();
			$("li[code=" + idInter + "]").removeClass("active").append("<span class='glyphicon glyphicon-map-marker'><span>");
			//$("span[code=" + idInter + "]").html("(" + intersecciones[w].lat + "," + intersecciones[w].lng  + ")");
			agregarMarket(intersecciones[w]);
			
			if (w + 1 == intersecciones.length) w = 0;
			else w++;
			$("li[code=" + intersecciones[w].id + "]").addClass("active");
			idInter = intersecciones[w].id;
			
		}
	});
	 
	$("#btCancel").unbind("click");
	$("#srcBT").unbind("click");
	$("#btSave").unbind("click");
	
	
	
	$("#btCancel").click(function(){
		for(var i = 0; i < marketList.length;i++)
			marketList[i].setMap(null) ;
		marketList.length = 0;			
		Init();
	}); 
	 
	$("#srcBT").click(function(){
		var text = $("#srcInput").val();
		centrarMapa(text);
	});
	 
	$("#btSave").click(function(){
		if (idCalle != 0 && idInter != 0) {
			var datos = JSON.stringify(intersecciones); 
			$.post("saveCalle.php", {idCalle: idCalle, data : datos}, function(result){
				alert(result);
				for(var i = 0; i < marketList.length;i++)
					marketList[i].setMap(null) ;
				marketList.length = 0;
				Init();
			});
		}	
	}); 
	 
	$("select").change(function () { 
		estado = $("select option:selected").attr('value');
		Init();
	});
	 
	 
	 var marketList = [];
	 var agregarMarket = function (dato) {
		 var location = new google.maps.LatLng(dato.lat,dato.lng);
		 var marker = new google.maps.Marker({position: location,map: map});
		 marketList.push(marker);
		 
	 }
	 
	 var centrarMapa = function (street) {
		geocoder.geocode( { 'address': "Argentina,Santa Fe,Rosario, " + street}, function(results, status) {
			  if (status == google.maps.GeocoderStatus.OK) {
				map.setZoom(15);  
				map.setCenter(results[0].geometry.location);
				/*var marker = new google.maps.Marker({
					map: map,
					position: results[0].geometry.location
				});*/
			  } else {
				alert("Geocode was not successful for the following reason: " + status);
			  }
		});
	 };
	 
} 
