import org.json.JSONObject;
import org.json.JSONArray;
import java.io.PrintWriter;
public class CLDownload
{
	private JSONArray calles;
	private JSONArray lineas;
	private JSONArray paradas;
	
	public CLDownload()
	{
		calles = new JSONArray();
		lineas = new JSONArray();
		paradas = new JSONArray();
	}

	private void addCalle(JSONObject c)
	{
		for(int i = 0 ; i < calles.length();i++) 
			if (calles.getJSONObject(i).getString("id").equals(c.getString("id")) )
				return;
		calles.put(c);
		//System.console().writer().println("Adding calle " + c.getString("desc"));
	}
	
	private void addColectivo(Integer id, String linea, String name, String bandera)
	{
		JSONObject c = new JSONObject();
		c.put("id",id);
		c.put("linea",linea);
		c.put("name",name);
		c.put("bandera",bandera);
		c.put("cl",true);
		lineas.put(c);
		//System.console().writer().println("Adding colectivo " + name);
	}
	
	private void addParada(Integer idColectivo, Integer idCalle, Integer idInter, Integer parada, String desc)
	{
		JSONObject c = new JSONObject();
		c.put("idColectivo",idColectivo);
		c.put("idCalle",idCalle);
		c.put("idInter",idInter);
		c.put("parada",parada);
		c.put("desc",desc);
		paradas.put(c);
		//System.console().writer().println("Adding parada " + idColectivo + " " + idCalle + "  " + idInter + "  " + parada);
	}
	
	public void toDB()
	{
		CLBase db = new CLBase();
		db.deleteTable();
		for(int j = 0 ; j < calles.length();j++) {
			db.insertCalle(calles.getJSONObject(j));
		}
		for(int j = 0 ; j < lineas.length();j++) {
			db.updateColectivo(lineas.getJSONObject(j));
		}
		for(int j = 0 ; j < paradas.length();j++) {
			db.insertParadas(paradas.getJSONObject(j));
		}
		db.Close();
	}
	
	public void toMYSQL()
	{
		MYSQLBase db = new MYSQLBase();
		db.deleteTable();
		for(int j = 0 ; j < calles.length();j++) {
			db.insertCalle(calles.getJSONObject(j));
		}
		for(int j = 0 ; j < lineas.length();j++) {
			db.updateColectivo(lineas.getJSONObject(j));
		}
		for(int j = 0 ; j < paradas.length();j++) {
			db.insertParadas(paradas.getJSONObject(j));
		}
		db.Close();
	}
	
	public void toFile()
	{
		try {
		PrintWriter fcalle = new PrintWriter("calles.txt");
		PrintWriter fparadas = new PrintWriter("paradas.txt");
		PrintWriter flineas = new PrintWriter("lineas.txt");
		fcalle.println(calles.toString());
		fparadas.println(paradas.toString());
		flineas.println(lineas.toString());
		fcalle.close();
		fparadas.close();
		flineas.close();
		} catch (Exception e) {return;}
	}
	
	public void print()
	{
		System.console().writer().println(calles.toString());
		System.console().writer().println(lineas.toString());
		System.console().writer().println(paradas.toString());
	}
	
	public void download()
	{
		WebParsing h = new WebParsing();		
		JSONArray line = h.getLineas();
		for(int i = 0 ; i < line.length();i++) {
			System.console().writer().println("Bajando la linea " + i);	
			JSONObject oC = line.getJSONObject(i);
			JSONArray ids = oC.getJSONArray("ids");			
			for(int j = 0 ; j < ids.length();j++) {
				int idColectivo = ids.getInt(j);
				addColectivo(idColectivo,oC.getString("linea"),oC.getString("name"),"");
			
				JSONArray JAcalle = h.getCalle(idColectivo);
				for(int k = 0 ; k < JAcalle.length();k++) {
					JSONObject oCalle = JAcalle.getJSONObject(k);
					Integer idCalle = Integer.parseInt(oCalle.getString("id"));
					addCalle(oCalle);
			
					JSONArray JAinter = h.getInterseccion(idColectivo,idCalle);		
					for(int l = 0 ; l < JAinter.length();l++) {
						JSONObject oInter = JAinter.getJSONObject(l);
						Integer idInter = Integer.parseInt(oInter.getString("id"));
						addCalle(oInter);
						
						JSONArray info = h.getInfoParadas(idColectivo,idCalle,idInter);
						
						for(int ww = 0 ; ww < info.length();ww++) {
							JSONObject op = info.getJSONObject(ww);
							addParada(idColectivo,idCalle,idInter, op.getInt("parada") , op.getString("desc")); 
						}
						
					}
				}			
				
			}
			
		}
		
	}

}
