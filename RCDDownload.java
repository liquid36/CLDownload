import org.json.JSONObject;
import org.json.JSONArray;
import java.io.PrintWriter;
public class RCDDownload
{
	private CLBase db;
	private MYSQLBase mysql;
	public RCDDownload()
	{
		db = new CLBase();
		db.deleteRecorridosTable();
		mysql = new MYSQLBase();
		mysql.deleteRecorridosTable();
		
	}
	
	public void close()
	{
		db.Close();
		mysql.Close();
	}
	
	public void download()
	{
		WebParsing h = new WebParsing();		
		JSONArray lines = h.getLineasFromRecorridos();
		for(int i = 0 ; i < lines.length();i++) {
			try {
				JSONObject oC = lines.getJSONObject(i);
				db.insertColec_rcd(oC);
				mysql.insertColec_rcd(oC);
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
				System.console().writer().print("Buscando puntos para " + id + " " + oC.getString("idpto").toLowerCase());
				findPuntosRecorrido(id,oC.getString("idpto").toLowerCase());
			} catch (Exception e) {e.printStackTrace(); }
		}			
	}
	
	public void findPuntosRecorrido(Integer id,String sentido)
	{
		WebParsing h = new WebParsing();		
		JSONArray recorridos = h.getPointRecorrido(id,sentido);
		String sqls = "";
		System.console().writer().println(" ->  " +  recorridos.length() +   "  puntos");
		for(int i = 0 ; i < recorridos.length();i++) {
			JSONObject oC = recorridos.getJSONObject(i);
			sqls += "(" + Integer.toString(id) + ",'" + sentido + "'," + Integer.toString(i) + 
						 " , '" + oC.getString("lat") + "','" + oC.getString("lon") +  "')"
					+ ( i + 1 == recorridos.length() ? "" : ",")	 ; 
			
			db.insertRcdReng(id,sentido, i , oC.getString("lat"),oC.getString("lon"));			
			//mysql.insertRcdReng(id,sentido, i , oC.getString("lat"),oC.getString("lon"));	
		}	
		//db.insertRcdReng(sqls);			
		mysql.insertRcdReng(sqls);		
		
	}

}


// 4 5 32 48 47 67 69 70
