<?php

$link = mysql_connect('sam.162.243.6.106.xip.io', 'sam', 'cower1990') or die('No se pudo conectar: ' . mysql_error());
mysql_select_db('sam') or die('No se pudo seleccionar la base de datos');

$idCalle = $_POST["idCalle"];
$data = $_POST["data"];

$j = json_decode($data);


foreach($j as $o) {
	$idInter = $o->{"id"};
	$lat = $o->{"lat"};
	$lng = $o->{"lng"};
	
	$result = mysql_query("DELETE FROM geostreet WHERE idCalle = " . $idCalle . " AND idInter = " . $idInter . ";") or die('Consulta fallida: ' . mysql_error());
	$result = mysql_query("DELETE FROM geostreet WHERE idCalle = " . $idInter . " AND idInter = " . $idCalle . ";") or die('Consulta fallida: ' . mysql_error());
	if ($lat != "" && $lng != "") {
		$result = mysql_query("INSERT INTO geostreet VALUES (" . $idCalle . "," . $idInter  .  ",'" . $lat . "','" . $lng . "',0,0)");
		$result = mysql_query("INSERT INTO geostreet VALUES (" . $idInter . "," . $idCalle  .  ",'" . $lat . "','" . $lng . "',0,0)");
		
		
	}

	
}
echo "OK";
mysql_close($link);

?>
 



