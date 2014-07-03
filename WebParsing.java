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
	public List<JSONObject> getLineas()
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
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
					System.console().writer().println(o.toString());
					list.add(o);
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
		JSONArray a = new JSONArray(s.substring(1));
		return a;	
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
	
	public JSONArray getInfoParadas(Integer idColectivo,Integer idCalle,Integer idInt)
	{
		HTTPMethod h = new HTTPMethod();
		JSONObject o = new JSONObject();
		o.put("accion", "getInfoParadas");
		o.put("idLinea", idColectivo.toString());
		o.put("idCalle", idCalle.toString());
		o.put("idInt", idInt.toString());
		String s = h.getHTTPGetString("http://www.etr.gov.ar/ajax/getData.php",o);
		JSONArray a = new JSONArray(s.substring(1));
		return a;
		
	}
	
}
