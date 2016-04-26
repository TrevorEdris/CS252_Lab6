
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.imageio.ImageIO;


/**
 * Servlet implementation class WebServlet
 */

@MultipartConfig()
public class WebServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String tag = request.getParameter("gamertag");
		String killFlagStr = request.getParameter("killFlag");
		//int killFlag = 1;
		
		
		//if(killFlagStr.equals("Deaths")) {killFlag = 0;}
		
		/*
		int maxX = 0, maxY = 0, maxZ = 0, minX = 0, minY = 0, minZ = 0;
		
		//boolean connected = DatabaseHandler.connect();
		//System.out.println(connected);
		
		String[] maps = {"cebd854f-f206-11e4-b46e-24be05e24f7e", 
						 "c7edbf0f-f206-11e4-aa52-24be05e24f7e", 
						 "cd844200-f206-11e4-9393-24be05e24f7e", 
						 "cdb934b0-f206-11e4-8810-24be05e24f7e", 
						 "cc040aa1-f206-11e4-a3e0-24be05e24f7e", 
						 "caacb800-f206-11e4-81ab-24be05e24f7e", 
						 "cdee4e70-f206-11e4-87a2-24be05e24f7e", 
						 "cb914b9e-f206-11e4-b447-24be05e24f7e", 
						 "ce1dc2de-f206-11e4-a646-24be05e24f7e"};
		
		ArrayList<byte[]> mapBytes = new ArrayList<byte[]>(); 
		
		for(int j = 0; j<8; ++j) {
			int[][] arr = new int[50][6];
			
			switch (j){
				case 0:
					maxX = HeatmapGenerator.Map.Coliseum.maxX();
					maxY = HeatmapGenerator.Map.Coliseum.maxY();
					minX = HeatmapGenerator.Map.Coliseum.minX();
					minY = HeatmapGenerator.Map.Coliseum.minY();
					break;
				case 1:
					maxX = HeatmapGenerator.Map.Crossfire.maxX();
					maxY = HeatmapGenerator.Map.Crossfire.maxY();
					minX = HeatmapGenerator.Map.Crossfire.minX();
					minY = HeatmapGenerator.Map.Crossfire.minY();
					break;
				case 2:
					maxX = HeatmapGenerator.Map.Eden.maxX();
					maxY = HeatmapGenerator.Map.Eden.maxY();
					minX = HeatmapGenerator.Map.Eden.minX();
					minY = HeatmapGenerator.Map.Eden.minY();
					break;
				case 3:
					maxX = HeatmapGenerator.Map.Empire.maxX();
					maxY = HeatmapGenerator.Map.Empire.maxY();
					minX = HeatmapGenerator.Map.Empire.minX();
					minY = HeatmapGenerator.Map.Empire.minY();
					break;
				case 4:
					maxX = HeatmapGenerator.Map.Fathom.maxX();
					maxY = HeatmapGenerator.Map.Fathom.maxY();
					minX = HeatmapGenerator.Map.Fathom.minX();
					minY = HeatmapGenerator.Map.Fathom.minY();
					break;
				case 5:
					maxX = HeatmapGenerator.Map.Plaza.maxX();
					maxY = HeatmapGenerator.Map.Plaza.maxY();
					minX = HeatmapGenerator.Map.Plaza.minX();
					minY = HeatmapGenerator.Map.Plaza.minY();
					break;
				case 6:
					maxX = HeatmapGenerator.Map.Regret.maxX();
					maxY = HeatmapGenerator.Map.Regret.maxY();
					minX = HeatmapGenerator.Map.Regret.minX();
					minY = HeatmapGenerator.Map.Regret.minY();
					break;
				case 7:
					maxX = HeatmapGenerator.Map.The_Rig.maxX();
					maxY = HeatmapGenerator.Map.The_Rig.maxY();
					minX = HeatmapGenerator.Map.The_Rig.minX();
					minY = HeatmapGenerator.Map.The_Rig.minY();
					break;
				case 8:
					maxX = HeatmapGenerator.Map.Truth.maxX();
					maxY = HeatmapGenerator.Map.Truth.maxY();
					minX = HeatmapGenerator.Map.Truth.minX();
					minY = HeatmapGenerator.Map.Truth.minY();
					break;
			}	
			
			generateKillDeath(arr, minX-1, maxX-1, minY-1, maxY-1, 0, 0);
			
			/*
			for(int i = 0; i < 49; ++i) {
				DatabaseHandler.insertIntoEvents(tag, maps[j], arr[i][0], arr[i][1], arr[i][2], 1);
				DatabaseHandler.insertIntoEvents(tag, maps[j], arr[i][3], arr[i][4], arr[i][5], 0);
			}
			*/
		/*	
			if(killFlag == 0){
				mapBytes.add(HeatmapGenerator.generateMap(arr, tag, maps[j], HeatmapGenerator.MapOptions.DEATHS));
			}else{
				mapBytes.add(HeatmapGenerator.generateMap(arr, tag, maps[j], HeatmapGenerator.MapOptions.KILLS));
			}


		}
			        		
	        	
		//String altitude = null;
		String coliseum = Base64.getEncoder().encodeToString(mapBytes.get(0));
		String crossfire = Base64.getEncoder().encodeToString(mapBytes.get(1));
		String eden = Base64.getEncoder().encodeToString(mapBytes.get(2));
		String empire = Base64.getEncoder().encodeToString(mapBytes.get(3));
		String fathom = Base64.getEncoder().encodeToString(mapBytes.get(4));
		//String gambol = null;
		//String orion = null;
		//String pegasus = null;
		String plaza = Base64.getEncoder().encodeToString(mapBytes.get(5));
		String regret = Base64.getEncoder().encodeToString(mapBytes.get(6));
		String the_rig = Base64.getEncoder().encodeToString(mapBytes.get(7));
		//String trench = null;
		//String trident = null;
		String truth = Base64.getEncoder().encodeToString(mapBytes.get(8));
		*/
			
		request.setAttribute("gamertag", tag);
		request.setAttribute("killFlag", killFlagStr);
		
		//request.setAttribute("altitude", altitude);
		//request.setAttribute("coliseum", coliseum);
		//request.setAttribute("crossfire", crossfire);
		//request.setAttribute("eden", eden);
		//request.setAttribute("empire", empire);
		//request.setAttribute("fathom", fathom);
		//request.setAttribute("gambol", gambol);
		//request.setAttribute("orion", orion);
		//request.setAttribute("pegasus", pegasus);
		//request.setAttribute("plaza", plaza);
		//request.setAttribute("regret", regret);
		//request.setAttribute("the_rig", the_rig);
		//request.setAttribute("trench", trench);
		//request.setAttribute("trident", trident);
		//request.setAttribute("truth", truth);
		
		request.getRequestDispatcher("heatmap.jsp").forward(request, response);

	}
	
	void generateKillDeath(int[][] arr, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		
		Random rG = new Random();
		for(int i = 0; i < 49; ++i) {
			HeatmapTester.addKillDeath(arr, i, (rG.nextInt(xMax - xMin + 1)+ xMin), 
											   (rG.nextInt(yMax - yMin + 1)+ yMin), 
											   (rG.nextInt(zMax - zMin + 1)+ zMin), 
											   (rG.nextInt(xMax - xMin + 1)+ xMin), 
											   (rG.nextInt(yMax - yMin + 1)+ yMin), 
											   (rG.nextInt(zMax - zMin + 1)+ zMin));
		}
		
	}

}
