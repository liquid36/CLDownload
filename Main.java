import org.json.JSONObject;
import org.json.JSONArray;
public class Main
{
	public static void main(String [] args)
	{
		HTTPMethod h = new HTTPMethod();
		/*JSONObject o = new JSONObject();
		o.put("accion", "getCalle");
		o.put("idLinea", "1");
		String s = h.getHTTPGetString("http://www.etr.gov.ar/ajax/getData.php",o);
      
		JSONArray a = new JSONArray(s.substring(1));
		for (int i = 0; i < a.length(); i ++)
			System.console().writer().println(a.getJSONObject(i).toString());
		*/
		
		//String ss = h.getHTTPGetString("http://www.etr.gov.ar/cuandollega.php",new JSONObject());
		
		WebParsing w = new WebParsing();
		w.getLineas();

	}

}
