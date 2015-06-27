import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.List;
import java.util.ArrayList; 


public class HTTPMethod 
{

	
	public boolean CopyFile(InputStream in,String file)
    {		
        try {
			OutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0)
                out.write(buff, 0, read);
            in.close();
            out.close();
            return true;
        } catch (Exception e) {e.printStackTrace(); return  false;}
    }

	public void donwloadFile(String URL,String desc)
	{
		InputStream in = getHTTPGet(URL,new JSONObject());
		CopyFile(in,desc);
	}
	
	public String openFile(String File) 
    {
		try { 
			FileInputStream fis   = new FileInputStream(File);
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = fis.read()) != -1){
				builder.append((char)ch);
			}
			byte[] asciiArray = builder.toString().getBytes("UTF-8");	
			return new String(asciiArray);
		} catch (Exception e) {e.printStackTrace(); return "";}
    }

    public InputStream getHTTPGet(String URL, JSONObject o) {
	InputStream content = null;
        try {
			String vars = "";
			Object [] array = o.keySet().toArray();
			for(int i=0; i<array.length; i++) 
				vars += array[i].toString() + "=" + o.getString(array[i].toString()) +  (i < array.length - 1? "&" : "");	    
			//System.console().writer().println(URL.replaceAll(" ", "%20") + "?" + vars);	
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(URL.replaceAll(" ", "%20") + "?" + vars ));
			content = response.getEntity().getContent();
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
        return content;
    }

    public InputStream getHTTPPost(String URL, JSONObject o) {
		InputStream content = null;
        try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			String [] array = (String []) o.keySet().toArray();
			for(int i=0; i<array.length; i++) 
				nvps.add(new BasicNameValuePair(array[i], o.getString(array[i])));
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost h = new HttpPost(URL.replaceAll(" ", "%20"));
			h.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(h);
			content = response.getEntity().getContent();
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
        return content;
    }

    
    public String getHTTPGetString(String URL, JSONObject o) 
    {
		try {
			InputStream i = getHTTPGet(URL,o);
			BufferedReader br = new BufferedReader(new InputStreamReader(i));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) 
				sb.append(line);
			byte[] asciiArray = sb.toString().getBytes("UTF-8");	
			return new String(asciiArray);
		} catch (Exception e) {e.printStackTrace();return "";}
    }
    
    public String getHTTPPostString(String URL, JSONObject o) 
    {
		try {
			InputStream i = getHTTPPost(URL,o);
			BufferedReader br = new BufferedReader(new InputStreamReader(i));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) 
				sb.append(line);
			byte[] asciiArray = sb.toString().getBytes("UTF-8");	
			return new String(asciiArray);
		} catch (Exception e) {e.printStackTrace(); return "";}
    }
}
