import org.json.JSONObject;
import org.json.JSONArray;

public class Main
{
	public static void main(String [] args)
	{

		/*RCDDownload rcd = new RCDDownload();
		rcd.download();
		rcd.close();

		CLDownload cd = new CLDownload();
		cd.download();
		cd.toDB();
		cd.toMYSQL();*/


		new MYSQLBase().descargarGeoStreet();

	}
}
