import org.json.JSONObject;
import org.json.JSONArray;
import java.io.PrintWriter;
public class RCDDownload
{
	private CLBase db;
	public RCDDownload()
	{
		db = new CLBase();
		db.deleteRecorridosTable();
	}
	
	public void close()
	{
		db.Close();
	}
	
	public void download()
	{
		WebParsing h = new WebParsing();		
		JSONArray lines = h.getLineasFromRecorridos();
		for(int i = 0 ; i < lines.length();i++) {
			try {
				JSONObject oC = lines.getJSONObject(i);
				db.insertColectivo(oC);
				System.console().writer().println("Buscando Recorridos para " + oC.getInt("id") );
				findRecorridos(oC.getInt("id"));
			} catch (Exception e) {e.printStackTrace(); }
		}	
	}
	
	
	public void findRecorridos(Integer id)
	{
		WebParsing h = new WebParsing();		
		JSONArray recorridos = h.getRecorridos(id);
		for(int i = 0 ; i < recorridos.length();i++) {
			JSONObject oC = recorridos.getJSONObject(i);
			db.insertRecorrido(id, oC.getString("idpto").toLowerCase() , oC.getString("desc"));
			try {
				System.console().writer().println("Buscando puntos para " + id + " " + oC.getString("idpto").toLowerCase());
				findPuntosRecorrido(id,oC.getString("idpto").toLowerCase());
			} catch (Exception e) {e.printStackTrace(); }
		}			
	}
	
	public void findPuntosRecorrido(Integer id,String sentido)
	{
		WebParsing h = new WebParsing();		
		JSONArray recorridos = h.getPointRecorrido(id,sentido);
		System.console().writer().println("Encontre " +  recorridos.length() +   "  puntos para " + id + " " + sentido );
		for(int i = 0 ; i < recorridos.length();i++) {
			JSONObject oC = recorridos.getJSONObject(i);
			db.insertRcdReng(id,sentido, i , oC.getString("lat"),oC.getString("lon"));
			
		}		
	}

}


// 4 5 32 48 47 67 69 70
