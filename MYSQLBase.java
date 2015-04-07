import java.sql.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class MYSQLBase
{
	private Connection c = null;
	private Statement stmt = null;
	
	public MYSQLBase ()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("","", "");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS colectivos (id INTEGER, name TEXT, bandera TEXT , linea TEXT, cl Boolean)");
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS calles (id INTEGER, descc TEXT)");
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS paradas (idColectivo INTEGER, idCalle INTEGER,idInter INTEGER, parada INTEGER , descc TEXT)");
			
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS recorridos (id INTEGER, sentido TEXT , desc TEXT)");
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rcdreng (id INTEGER, sentido TEXT , num INTEGER, lat TEXT, lon TEXT)");
			
			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS geostreetD (idCalle INTEGER, idInter INTEGER,lat DOUBLE,lng DOUBLE, sin_lat DOUBLE , cos_lat DOUBLE , sin_lng DOUBLE, cos_lng DOUBLE)");
			
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Opened database failed");
		}
	}	
	
	public void deleteRecorridosTable()
	{
		try {
			String sql = "DELETE FROM colectivos;" ;
			stmt.executeUpdate(sql);
			sql = "DELETE FROM recorridos;" ;
			stmt.executeUpdate(sql);
			sql = "DELETE FROM rcdreng;" ;
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void deleteTable()
	{
		try {
			String sql = "DELETE FROM calles;" ;
			stmt.executeUpdate(sql);
			sql = "DELETE FROM colectivos;" ;
			stmt.executeUpdate(sql);
			sql = "DELETE FROM paradas;" ;
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void insertCalle(JSONObject o)
	{
		try {
			String sql = "INSERT INTO calles (id,descc) " +
						 "VALUES (" + Integer.toString(o.getInt("id")) + ",'" + o.getString("desc") + "');"; 
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void insertColectivo(JSONObject o)
	{
		try {
			String sql = "INSERT INTO colectivos (id,name,bandera,linea,cl) " +
						 "VALUES (" + Integer.toString(o.getInt("id")) + ",'" + o.getString("name") 
						 + "','" + o.getString("bandera") + "','" + o.getString("linea")  +  "', " + (o.getBoolean("cl")?"1":"0") + " );"; 
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void insertParadas(JSONObject o)
	{
		try {
			String sql = "INSERT INTO paradas (idColectivo,idCalle,idInter,parada,descc) " +
						 "VALUES (" + Integer.toString(o.getInt("idColectivo")) + "," + Integer.toString(o.getInt("idCalle")) 
						 + "," + Integer.toString(o.getInt("idInter")) + "," + Integer.toString(o.getInt("parada"))  +  
						 ",'" + o.getString("desc") + "');"; 
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}	
	}
	
	public void Close() 
	{
		try {
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.out.println("Close Failed");
		}	
	}
	
	// Para los recorridos -------------------------------------------------------------------------------
	// Para los recorridos -------------------------------------------------------------------------------
	
	public void insertRecorrido(Integer id, String sentido ,String desc)
	{
		try {
			String sql = "INSERT INTO recorridos (id,sentido,desc) " +
						 "VALUES (" + Integer.toString(id) + ",'" + sentido + "','" + desc + "'  );"; 
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void insertRcdReng(Integer id, String sentido ,Integer num, String lat, String lon)
	{
		try {
			String sql = "INSERT INTO rcdreng (id,sentido,num,lat,lon) " +
						 "VALUES (" + Integer.toString(id) + ",'" + sentido + "'," + Integer.toString(num) + 
						 " , '" + lat + "','" + lon +  "');"; 
			stmt.executeUpdate(sql);
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
