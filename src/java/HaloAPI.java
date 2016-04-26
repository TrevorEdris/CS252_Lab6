import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
//import javax.json.*;
import org.json.*;
import org.json.JSONObject.*;
import org.json.JSONArray.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.*;
import org.apache.http.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.nio.file.*;
import java.awt.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class HaloAPI {
	
	//array indices for each map, in the order they appear in the metadata api
	HashMap<String, Integer> maps = null;
	
	//this will contain the key used to call the api
	String apiKey = null;
	
	public void populateMaps() {
		maps.put("c74c9d0f-f206-11e4-8330-24be05e24f7e", 0); //Alpine
		maps.put("c7edbf0f-f206-11e4-aa52-24be05e24f7e", 1); //Breakout Arena
		maps.put("cebd854f-f206-11e4-b46e-24be05e24f7e", 2); //Coliseum
		maps.put("cd844200-f206-11e4-9393-24be05e24f7e", 3); //Eden
		maps.put("cdb934b0-f206-11e4-8810-24be05e24f7e", 4); //Empire
		maps.put("cc040aa1-f206-11e4-a3e0-24be05e24f7e", 5); //Fathom
		maps.put("caacb800-f206-11e4-81ab-24be05e24f7e", 6); //Plaza
		maps.put("cdee4e70-f206-11e4-87a2-24be05e24f7e", 7); //Regret
		maps.put("cb914b9e-f206-11e4-b447-24be05e24f7e", 8); //The Rig
 		maps.put("ce1dc2de-f206-11e4-a646-24be05e24f7e", 9); //Truth
		maps.put("c7805740-f206-11e4-982c-24be05e24f7e", 10); //Glacier	
		maps.put("c7b7baf0-f206-11e4-ae9a-24be05e24f7e", 11); //Parallax
		maps.put("ca737f8f-f206-11e4-a7e2-24be05e24f7e", 12); //Overgrowth
		maps.put("cbcea2c0-f206-11e4-8c4a-24be05e24f7e", 13); //Riptide
		maps.put("cc74f4e1-f206-11e4-ad66-24be05e24f7e", 14); //Torque
		maps.put("ce89a40f-f206-11e4-b83f-24be05e24f7e", 15); //Tyrant
	}
	
	public void getKey() {
		//the primary API key is the first line of APIKEYS.txt
		try{
		BufferedReader br = new BufferedReader(new FileReader("APIKEYS.txt"));
		apiKey = br.readLine();
		}catch(Exception e){e.printStackTrace();}
	}
	
	//returns the most recent 9 matches in the form of an array of arraylists of strings
	//arg gamertag: the user's gamertag
	//return: a list of matches
	public ArrayList<String> getMatches(String gamertag) {
		String mostRecentMatch = new String();
		//get the most recent match from the database here
		
		HttpClient httpclient = HttpClients.createDefault();

		String url = "https://www.haloapi.com/stats/h5/players/" + gamertag + "/matches?count=9";
		if (maps == null)
			populateMaps();
			
		if (apiKey == null)
			getKey();
		
		ArrayList<String> matches;
		
        try
        {
            URIBuilder builder = new URIBuilder(url);
            
            builder.setParameter("start", "0");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", apiKey);


            // Request body
            StringEntity reqEntity = new StringEntity("{body}");
            //request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
	
			matches = new ArrayList<String>();
			
            if (entity != null) 
            {
            	//make the entity into a string and parse it as a json array
                String result = EntityUtils.toString(entity);
                JSONObject fullJSON = new JSONObject(result);
              	JSONArray ja = fullJSON.getJSONArray("Results");
              	for (int i = 0; i < ja.length(); i++) {
	           		JSONObject jo = ja.getJSONObject(i);
	           		String mapID = jo.getString("MapId");
	           		JSONObject id = jo.getJSONObject("Id");
	           		String matchID = id.getString("MatchId");
	           		
	           		//skip the rest if this is not arena
	           		if (!id.getString("GameMode").equals("1"))
	           			continue;
	           			
	           		int index = maps.get("mapID");
	           		if (index >= 0 && index <= 15) {
	           			matches.add(matchID);
	          		}
	            }
         	}
         	return matches;
     	}
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
	}
	
	//returns a list of the specified player's kills if kills is true, a list of their deaths otherwise
	//arg matches: a string array of the matches to check
	//arg gamertag: the user's gamertag
	//arg kills: check kills if true, deaths if false
	//return: an arraylist of the locations of the events (kills/deaths)
	public ArrayList<Double[]> getEvents(ArrayList<String> matches, String gamertag, boolean kills) {
		ArrayList<Double[]> events = new ArrayList<Double[]>();
		
		for (String s : matches){
		
			HttpClient httpclient = HttpClients.createDefault();
	
			String url = "https://www.haloapi.com/stats/h5/matches/" + s + "/events";
				
			if (apiKey == null)
				getKey();
			
	        try
	        {
	            URIBuilder builder = new URIBuilder(url);
	            
	            builder.setParameter("start", "0");
	
	            URI uri = builder.build();
	            HttpGet request = new HttpGet(uri);
	            request.setHeader("Ocp-Apim-Subscription-Key", apiKey);
	
	
	            // Request body
	            StringEntity reqEntity = new StringEntity("{body}");
	            //request.setEntity(reqEntity);
	
	            HttpResponse response = httpclient.execute(request);
	            HttpEntity entity = response.getEntity();
				
	            if (entity != null) 
	            {
	            	//make the entity into a string and parse it as a json array
	                String result = EntityUtils.toString(entity);
	                JSONObject fullJSON = new JSONObject(result);
	              	JSONArray ja = fullJSON.getJSONArray("GameEvents");
	              	for (int i = 0; i < ja.length(); i++) {
	              		JSONObject jo = ja.getJSONObject(i);
	              		JSONObject player;
	              		if (kills)
	              			player = jo.getJSONObject("Killer");
	              		else
	              			player = jo.getJSONObject("Victim");
	              		
	              		//if the killer/victim is not the specified user or if this is not a kill event, skip the rest
	              		if (!player.getString("Gamertag").equals(gamertag))
	              			continue;
	              			
	              		JSONObject location;
	              		if (kills)
	              			location = jo.getJSONObject("KillerWorldLocation");
	              		else
	              			location = jo.getJSONObject("VictimWorldLocation");
	              		Double[] array = {location.getDouble("x"), location.getDouble("y"), (location).getDouble("z")};
	              		events.add(array);
	              	}
	         	}
	     	}
	        catch (Exception e)
	        {
	            System.out.println(e.getMessage());
	            return null;
	        }
		}	
		return events;
	}
	
	//calls the api, stores data in the database
	public void callAPI(String gamertag, boolean kills) {
		ArrayList<String> 	matches = getMatches(gamertag);
		ArrayList<Double[]> events;
		for (int i = 0; i < 16; i++) {
			if (matches.get(i).isEmpty())
				continue;
			events = getEvents(matches, gamertag, kills);
			//database shit here
			
			
			
		}
	}
	
}