import java.sql.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class CLBase
{
	private Connection c = null;
	private Statement stmt = null;
	
	public CLBase ()
	{
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS colectivos (id INTEGER, name TEXT, bandera TEXT , linea TEXT, cl Boolean)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS calles (id INTEGER, desc TEXT)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS paradas (idColectivo INTEGER, idCalle INTEGER,idInter INTEGER, parada INTEGER , desc TEXT)");
			
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS recorridos (id INTEGER, sentido TEXT , desc TEXT)");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rcdreng (id INTEGER, sentido TEXT , num INTEGER, lat TEXT, lon TEXT)");
			
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS geostreetD (idCalle INTEGER, idInter INTEGER,lat DOUBLE,lng DOUBLE, sin_lat DOUBLE , cos_lat DOUBLE , sin_lng DOUBLE, cos_lng DOUBLE)");
			
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
			String sql = "INSERT INTO calles (id,desc) " +
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
			String sql = "INSERT INTO paradas (idColectivo,idCalle,idInter,parada,desc) " +
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
	
	/*public static void main( String args[] )
	{
   
      stmt = c.createStatement();
      String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                   "VALUES (1, 'Paul', 32, 'California', 20000.00 );"; 
      stmt.executeUpdate(sql);

      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
            "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );"; 
      stmt.executeUpdate(sql);

      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
            "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );"; 
      stmt.executeUpdate(sql);

      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
            "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );"; 
      stmt.executeUpdate(sql);

      stmt.close();
      c.commit();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Records created successfully");
  }*/
}
