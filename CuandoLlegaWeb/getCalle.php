<?php

$link = mysql_connect( ) or die('No se pudo conectar: ' . mysql_error());
mysql_select_db(' ') or die('No se pudo seleccionar la base de datos');

if ($_GET["action"] == "calle") {	
	
	//$query = 'SELECT DISTINCT idCalle , descc FROM (SELECT idCalle FROM paradas) AS temp INNER JOIN calles WHERE idCalle = id ORDER BY descc;';
	$query = 'SELECT id, descc, geoNum , allNum FROM calles '
		   . 'LEFT JOIN (select paradas.idCalle idA ,count(*) geoNum FROM paradas LEFT JOIN geostreet ON paradas.idCalle = geostreet.idCalle AND paradas.idInter = geostreet.idInter  WHERE geostreet.idCalle IS NULL GROUP BY paradas.idCalle) A ON id = IdA '  
		   .'INNER JOIN (select idCalle idB, count(*) allNum FROM paradas GROUP BY idCalle) B ON id = idB ORDER BY descc;' ;
	 
	 
	$result = mysql_query($query) or die('Consulta fallida: ' . mysql_error());

	$numResults = mysql_num_rows($result);
	$counter = 0;
	echo "[\n";
	while ($line = mysql_fetch_array($result, MYSQL_NUM)) {
		if (++$counter == $numResults)  printf("{ \"id\": %d , \"name\": \"%s\" , \"geo\": %d , \"all\": %d  }\n ",$line[0],$line[1],$line[2],$line[3]);
		else printf("{ \"id\": %d , \"name\": \"%s\" ,\"geo\": %d , \"all\": %d    },\n ",$line[0],$line[1],$line[2],$line[3]);
	}
	echo "]";

	mysql_free_result($result);
} else if ($_GET["action"] == "interseccion") {
	$calle = $_GET["calle"];
	
	$query = 'SELECT DISTINCT temp.idInter , descc, lat, lng FROM (SELECT idInter FROM paradas WHERE idCalle = ' . $calle . ') AS temp ' 
	         . ' INNER JOIN calles ON temp.idInter = id ' 
	         . ' LEFT JOIN geostreet ON geostreet.idCalle = ' . $calle . ' AND geostreet.idInter = temp.idInter ' 
	         . 'ORDER BY descc;';
	$result = mysql_query($query) or die('Consulta fallida: ' . mysql_error());

	$numResults = mysql_num_rows($result);
	$counter = 0;
	echo "[\n";
	while ($line = mysql_fetch_array($result, MYSQL_NUM)) {
		if (++$counter == $numResults)  printf("{ \"id\": %d , \"name\": \"%s\", \"lat\": \"%s\", \"lng\": \"%s\"  }\n ",$line[0],$line[1],$line[2],$line[3]);
		else printf("{ \"id\": %d , \"name\": \"%s\", \"lat\": \"%s\", \"lng\": \"%s\"  },\n ",$line[0],$line[1],$line[2],$line[3]);
	}
	echo "]";

	mysql_free_result($result);
	
}

mysql_close($link);

/*
 
 select * FROM paradas 
  	    LEFT JOIN geostreet ON paradas.idCalle = geostreet.idCalle AND paradas.idInter = geostreet.idInter 
  	    WHERE paradas.idCalle = 1290;



SELECT id, descc, geoNum , allNum FROM calles  
LEFT JOIN (select paradas.idCalle idA ,count(*) geoNum FROM paradas 
   LEFT JOIN geostreet ON paradas.idCalle = geostreet.idCalle AND paradas.idInter = geostreet.idInter 
   WHERE geostreet.idCalle IS NULL GROUP BY paradas.idCalle) A ON id = IdA
INNER JOIN (select idCalle idB, count(*) allNum FROM paradas GROUP BY idCalle) B ON id = idB;


"SELECT id, name, lat, lon, ( 6356 * acos( cos( radians(-32.947341) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(-60.707036) ) + sin( radians(32.947341) ) * sin( radians( lat ) ) ) ) AS distance FROM pois HAVING distance < 5000 ORDER BY distance ASC

0.005360


SELECT idCalle,IdInter,lat,lng, ((acos(    sin(radians(lat)) * sin(radians(-32.947341)) 
                                         + cos(radians(lat)) * cos(radians(-32.947341)) 
                                                 * cos(radians(-60.707036) - radians(lng)))
                                * 6378.137) * 1000) AS distance FROM geostreetD HAVING distance < 500 order by distance ASC; 
* 
* 
select * from geostreetD where (abs(lat + 32.947341) < 0.005360  ) AND abs(lng + 60.707036) < 0.005360;

insert into geostreetD select idCalle,idInter,lat,lng,sin(radians(lat)),cos(radians(lat)),sin(radians(lng)),cos(radians(lng)) from geostreet ;
*/

?>




