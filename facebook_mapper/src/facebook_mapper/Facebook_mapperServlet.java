package facebook_mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.servlet.http.*;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


@SuppressWarnings("serial")
public class Facebook_mapperServlet extends HttpServlet
{
	//function to get the hash
	private String getHash(String sessionToken, String AppSecret) throws Exception 
	{
	    StringBuilder seed = new StringBuilder();
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    seed.append(sessionToken + AppSecret);
	    byte[] hash = md.digest(seed.toString().getBytes("UTF-8"));
	    md.reset();
	    return String.format("%0"+(hash.length*2)+"x", new BigInteger(1,hash));
	}
	
	//the function gets a api query url and makes as api call, return JSONArray
	public JSONArray ApiCall(String call_url)
	{
		try 
		{
			URL url = new URL(call_url);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(0);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String result = rd.readLine();  
	        JSONArray js = new JSONArray(result);
	        
	        rd.close();
	        
	        return js;
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//the function gets a name of a "Like" and returns 3 results suggestions
	public JSONArray map_likes(String query, String token, String hash)
	{
		//get the first product in search
		String api_query=ApplicationSettings.ApiCall+"search/products?"+token+"&hash="+hash+"&q="+query+"&limit=2&orderBy=relevancy";
		JSONArray resultJS=ApiCall(api_query);
		
		String api_query2=ApplicationSettings.ApiCall+"search/products?"+token+"&hash="+hash+"&q="+query+"&limit=1&orderBy=top_sellers";
		JSONArray resultJS2=ApiCall(api_query2);
		

		try 
		{
			resultJS.put(resultJS2.getJSONObject(0));
		}
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultJS;
		//get the first product in category
		
		
	}
	
	//implementation of Get
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.setContentType("text/plain");
		
		//getting token and hash
		String token=req.getQueryString();
		String hash="";
		try 
		{
			 hash=getHash(token.substring(6),ApplicationSettings.AppSecret);

			
			//String api_query=ApplicationSettings.ApiCall+"search/products?"+token+"&hash="+hash+"&q=clothes";
			//----------------------------------------------------------------------------------
			JSONArray resultJS=map_likes("baseball",token,hash);
			
			resp.setContentType("text/html");
			resp.getWriter().println("<html>");
			resp.getWriter().println("<body>");
			
			resp.getWriter().println("Baseball 123");
			resp.getWriter().println("<br>");
	        for(int j=0;j<resultJS.length();j++)
	        {
		        JSONObject jso = resultJS.getJSONObject(j);
		        
		        resp.getWriter().println(jso.getString("name"));
		        resp.getWriter().println("<br>");
		      
	        }
	        resp.getWriter().println("<br>");
	        resp.getWriter().println("<br>");
	        
	        resp.getWriter().println("Shakira");
	        resp.getWriter().println("<br>");
			resultJS=map_likes("shakira",token,hash);
			
	        for(int j=0;j<resultJS.length();j++)
	        {
		        JSONObject jso = resultJS.getJSONObject(j);
		        
		        resp.getWriter().println(jso.getString("name"));
		        resp.getWriter().println("<br>");
		      
	        }
	        resp.getWriter().println("<br>");
	        resp.getWriter().println("<br>");
	        
	        resp.getWriter().println("Jerusalem");
	        resp.getWriter().println("<br>");
			resultJS=map_likes("jerusalem",token,hash);
			
	        for(int j=0;j<resultJS.length();j++)
	        {
		        JSONObject jso = resultJS.getJSONObject(j);
		        
		        resp.getWriter().println(jso.getString("name"));
		        resp.getWriter().println("<br>");
		      
	        }
	        resp.getWriter().println("<br>");
	        resp.getWriter().println("<br>");
	        
	        resp.getWriter().println("</body>");
	        resp.getWriter().println("</html>");
	        //--------------------------------------------------------------------------------
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
