import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class WebParsing
{
	public JSONArray getLineas()
	{
		JSONArray list = new JSONArray();
		try {		
			HTTPMethod h = new HTTPMethod();
			String web = h.getHTTPGetString("http://www.etr.gov.ar/cuandollega.php",new JSONObject());
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			int start = web.indexOf("<select") - 2;
			int end = web.indexOf("</select>") + 10;
			String webF = web.substring(start,end).replaceAll("&iacute;","i") ;	
			Document doc = dBuilder.parse(new InputSource( new StringReader(webF)));
			NodeList nList = doc.getElementsByTagName("option");
			for (int i = 0;i < nList.getLength() ; i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nList.item(i);
					String slinea = e.getAttribute("value");
					String sid    = e.getAttribute("idLinea");
					String nombre = e.getFirstChild().getNodeValue();
					String jo     = "{ \"linea\": \"" + slinea + "\" , \"ids\": [ " + sid + "] , \"name\": \"" + nombre + "\"   }"  ;
						
					JSONObject o = new JSONObject( jo  );
					//System.console().writer().println(o.toString());
					list.put(o);
				}
			}	
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}
	
	
	public JSONArray getCalle(Integer idColectivo)
	{
		HTTPMethod h = new HTTPMethod();
		JSONObject o = new JSONObject();
		o.put("accion", "getCalle");
		o.put("idLinea", idColectivo.toString());
		String s = h.getHTTPGetString("http://www.etr.gov.ar/ajax/getData.php",o);
		try {
			JSONArray a = new JSONArray(s.substring(1));
			return a;	
		} catch (Exception e) { return new JSONArray(); }
	}
	
	public JSONArray getInterseccion(Integer idColectivo,Integer idCalle)
	{
		HTTPMethod h = new HTTPMethod();
		JSONObject o = new JSONObject();
		o.put("accion", "getInterseccion");
		o.put("idLinea", idColectivo.toString());
		o.put("idCalle", idCalle.toString());
		String s = h.getHTTPGetString("http://www.etr.gov.ar/ajax/getData.php",o);
		JSONArray a = new JSONArray(s.substring(1));
		return a;
		
	}
	
	public static String remove1(String input) {
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i=0; i<original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		return output;
	}
	
	public JSONArray getInfoParadas(Integer idColectivo,Integer idCalle,Integer idInt)
	{
		try {
			HTTPMethod h = new HTTPMethod();
			JSONObject o = new JSONObject();
			JSONObject r;
			o.put("accion", "getInfoParadas");
			o.put("idLinea", idColectivo.toString());
			o.put("idCalle", idCalle.toString());
			o.put("idInt", idInt.toString());
			String web = h.getHTTPGetString("http://www.etr.gov.ar/ajax/getData.php",o);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			int start = 1;
			int end = web.indexOf("<script") - 5;
			
			//System.console().writer().println(idCalle + "  " + idInt + "  " + web);
			
			String webF = web.substring(start,end).replaceAll("&nbsp;"," ").replaceAll("<br>","<br/>") ;	
			Document doc = dBuilder.parse(new InputSource( new StringReader(webF)));
			NodeList nList = doc.getElementsByTagName("tr");
			
			//System.console().writer().println(idCalle + "  " + idInt + "  " + webF);
			//if (idCalle == 1473 && idInt == 1083)
			//	System.console().writer().println("Numero de paradas: " + ( nList.getLength() - 1) );
			JSONArray paradas = new JSONArray();
			for (int i = 1;i < nList.getLength() ; i++) { 
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					try {
						NodeList tdList = ((Element) nList.item(i) ).getElementsByTagName("td");
						//System.console().writer().println(tdList.getLength());
						Element e1 = (Element) tdList.item(0);
						Element e2 = (Element) tdList.item(1);
						
						String parada = e1.getFirstChild().getFirstChild().getNodeValue();
						String texto = e2.getFirstChild().getNodeValue();
						String bandera = "";
						String desc = texto;
						if (texto.indexOf(">") > 0) {
							String [] ss = texto.split(">");
							desc = ss[1].trim();
							bandera = ss[0].trim();
						}
						String jo     = "{ \"parada\": " + parada + " , \"desc\": \"" + desc + "\" , \"bandera\": \"" + bandera + "\"  }"  ;
						//System.console().writer().println(jo );
						paradas.put(new JSONObject(jo));	
					} catch (Exception e) {} 
				}
			}
			return paradas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//***************************************************************************************************
	//***************************************************************************************************
	
	// Desde la página de recorridos
	public JSONArray getLineasFromRecorridos()
	{
		JSONArray list = new JSONArray();
		try {		
			HTTPMethod h = new HTTPMethod();
			String web = h.getHTTPGetString("http://www.emr.gov.ar/recorridos.php",new JSONObject());
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			int startI = web.indexOf("</select>") + 10;
			int start = web.indexOf("<select",startI) - 2;
			int end = web.indexOf("</select>",startI) + 10;
			String webF = web.substring(start,end).replaceAll("&iacute;","i") ;	
			Document doc = dBuilder.parse(new InputSource( new StringReader(webF)));
			NodeList nList = doc.getElementsByTagName("option");
			for (int i = 1;i < nList.getLength() ; i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nList.item(i);
					String slinea = e.getAttribute("value");
					String nombre    = e.getAttribute("descLinea");
					
					int in = nombre.lastIndexOf(" ");
					if (slinea.equals("47")) in = nombre.indexOf(" ");
					String lastWord = nombre.substring(in + 1);
					nombre = nombre.substring(0,in);
					
					String jo     = "{\"id\":" + slinea  + " ,\"linea\": \"" + "\" , \"bandera\": \"" + lastWord + "\",\"name\": \"" + nombre + "\", \"cl\":false   }"  ;
					JSONObject o = new JSONObject(jo);
					//System.console().writer().println(jo);
					list.put(o);
				}
			}	
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}
	
	
	// Desde la página de recorridos
	public JSONArray getRecorridos(Integer idLinea)
	{
		HTTPMethod h = new HTTPMethod();
		JSONObject o = new JSONObject();
		o.put("idlinea", idLinea.toString());
		String s = h.getHTTPGetString("http://www.emr.gov.ar/includes/chtupV2/ajax/getSentidoLinea.php",o);
		try {
			JSONArray a = new JSONArray(s);
			return a;	
		} catch (Exception e) {e.printStackTrace(); return new JSONArray(); }
	}
	
	// Desde la página de recorridos
	public JSONArray getPointRecorrido(Integer idLinea,String direccion)
	{
		HTTPMethod h = new HTTPMethod();
		String name = idLinea.toString() + direccion + ".kml";
		String url = "https://sites.google.com/site/etrkml/kml/" + name; 
		try {					
			String web = h.getHTTPGetString(url,new JSONObject());
			return parserKML(web);
		} catch (Exception e) {
			try {
				System.console().writer().println("Descargando y deszipiando ");
				UnzipUtility unzip = new UnzipUtility();
				h.donwloadFile(url,"doc.zip");				
				unzip.unzip("doc.zip","docs");
				String s = h.openFile("docs/doc.kml");
				return parserKML(s);
				
			} catch (Exception ee) {
				ee.printStackTrace();
				return new JSONArray(); 
			}
		}
	}
	
	public JSONArray parserKML(String web) throws Exception
	{
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource( new StringReader(web)));
		JSONArray list = new JSONArray();	
		NodeList nList = doc.getElementsByTagName("coordinates");
		for (int i = 0;i < nList.getLength() ; i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nList.item(i);
				String coordenadas = e.getFirstChild().getNodeValue();							
				String [] coor = coordenadas.replace("\n","").split(" ");
				for (int j = 0;j < coor.length ; j++) {
					String cood1 = coor[j];						
					if (!cood1.trim().equals("")) {
						String c [] = cood1.split(",");
						String jo  = "{ \"lat\": \"" + c[1]+ "\" , \"lon\": \"" + c[0] + "\",\"num\": \"" + j + "\"   }"  ;	
						JSONObject o = new JSONObject(jo);
						list.put(o);	
					}
				}					
			}
		}
		return list;
	}
	
	//  http://www.emr.gov.ar/includes/chtupV2/ajax/getSentidoLinea.php?idlinea=1	
	
}
