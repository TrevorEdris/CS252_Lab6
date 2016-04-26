package java_stuff;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class DatabaseHandler {
	// Hostname, DB name, username, password are the 4 credentials needed to connect to database
	private static String hostname = "us-cdbr-iron-east-03.cleardb.net";
	private static String dbName = "ad_9e452fb642cc8ae";
	private static String username = "be06899b31d42d";
	private static String password = "94408d48";
	private static String port = "3306";
	private static String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName;
	private static boolean connected = false;
	
	private static Connection con = null;
	
	public DatabaseHandler(){}
	
	public static boolean connect(){
		if(!connected){
			
			try{
				Class.forName("com.mysql.jdbc.Driver");
			}catch(ClassNotFoundException e){print("Class Not Found\n");e.printStackTrace();}
			
			try{
				con = DriverManager.getConnection(url, username, password);
				if(con != null){
					connected = true;
					print("Connection to database successful!  \\(^-^)/ \n");
				}else{
					print("Connection to database failed!\n");
				}
			}catch(SQLException e){print("Connection to database failed!\n");e.printStackTrace();}
		}
		return connected;
	}
	
	public static void insertIntoEvents(String gamertag, String mapID, int x, int y, int z, int killFlag){
		
		if(!connected){
			connect();
		}
		if(!connected){
			return;
		}
		
		PreparedStatement stmt = null;
		String sql = "INSERT INTO Events VALUES(?,?,?,?,?,?);";
		int res = 0;
		
		try{
			stmt = con.prepareStatement(sql);
			
			stmt.setString(1,gamertag);
			stmt.setString(2, mapID);
			stmt.setString(3, "" + x);
			stmt.setString(4, "" + y);
			stmt.setString(5, "" + z);
			stmt.setString(6, "" + killFlag);
			//stmt.setBinaryStream(7,new ByteArrayInputStream(b), b.length);
			
			res = stmt.executeUpdate();
			
			if(res == 0){
				print("insert stmt did nothing\n");
			}
		}catch(SQLException e){e.printStackTrace();}
		
	}
	
	public static void insertIntoBlobs(String gamertag, String mapID, int killFlag, byte[] b){
		
		if(!connected){
			connect();
		}
		if(!connected){
			return;
		}
		
		PreparedStatement stmt = null;
		String sql = "INSERT INTO HeatmapBlobs VALUES(?,?,?,?);";
		int res = 0;
		
		try{
			stmt = con.prepareStatement(sql);
			
			stmt.setString(1,gamertag);
			stmt.setString(2, mapID);
			stmt.setString(3, "" + killFlag);
			stmt.setBinaryStream(4,new ByteArrayInputStream(b), b.length);
			
			res = stmt.executeUpdate();
			
			if(res == 0){
				print("insert stmt did nothing\n");
			}
		}catch(SQLException e){e.printStackTrace();}
		
	}
	
	public static Blob getBlob(String gamertag, String mapID, int killFlag){
		
		if(!connected){
			connect();
		}
		if(!connected){
			return null;
		}
		
		Blob ret = null;
		
		String sql = "SELECT ImgBlob FROM HeatmapBlobs" +
				" WHERE Gamertag=\"" + gamertag + "\"" + 
				" AND MapID=\"" + mapID + "\"" +
				" AND KillFlag=" + killFlag;
		
		PreparedStatement stmt = null;
		ResultSet result = null;
		
		try{
			stmt = con.prepareStatement(sql);
			result = stmt.executeQuery();
			
			if(result.next()){
				ret = result.getBlob("ImgBlob");
			}
			
		}catch(SQLException e){e.printStackTrace();}
		
		return ret;
	}
	
	public static String getBlobAsString(String gamertag, String mapID, int killFlag){
		
		if(!connected){
			connect();
		}
		if(!connected){
			return "";
		}
		
		Blob b = getBlob(gamertag, mapID, killFlag);
		byte[] blobAsBytes = null;
		try{
			blobAsBytes = b.getBytes(1,(int)b.length());
		}catch(SQLException e){e.printStackTrace();}
  	  	String bytesAsStr = Base64.getEncoder().encodeToString(blobAsBytes);
  	  	return bytesAsStr;
	}
	
	public static String getJsonEvents(String gamertag, String mapID, int killFlag){
		
		if(!connected){
			connect();
		}
		if(!connected){
			return"{\"Error\": \"Database not connected.\"}";
		}
		
		String ret = "{";
		String sql = "SELECT Gamertag, MapID, X, Y, Z, KillFlag FROM Events WHERE " +
				"Gamertag=\"" + gamertag + "\" AND MapID=\"" + mapID + "\" AND KillFlag=" + killFlag;
		
		PreparedStatement stmt = null;
		ResultSet result = null;
		
		try{
			stmt = con.prepareStatement(sql);
			result = stmt.executeQuery();
			
			ret = ret + "\"Events\": {";
			while(result.next()){
				 	ret = ret + "[" +
						"\"Gamertag\": \"" + result.getString("Gamertag") + "\", " +
						"\"MapID\": \"" + result.getString("MapID") + "\", " +
						"\"X\": \"" + result.getInt("X") + "\", " + 
						"\"Y\": \"" + result.getInt("Y") + "\", " + 
						"\"Z\": \"" + result.getInt("Z") + "\", " +
						"\"KillFlag\": \"" + result.getInt("KillFlag") + "\"" +  
						"],";
			}
			ret = ret.substring(0, ret.length() - 1);
			ret = ret + "}}";
		}catch(SQLException e){e.printStackTrace();}
		
		return ret;
	}
	
	private static void print(String s){
		System.out.print(s);
	}
	
}
