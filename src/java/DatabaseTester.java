import java.sql.Blob;

public class DatabaseTester {
/*
	
	public static void test(String[] args){
		
		DatabaseHandler.connect();
		byte[] b = new byte[]{0,0,0,0,0,0,0,0};
		DatabaseHandler.insertIntoEvents("Trev 5612", "testMapID", 0, 0, 0, 1);
		DatabaseHandler.insertIntoBlobs("Trev 5612", "testMapID", 1, b);
		
		String blobStr = DatabaseHandler.getBlobAsString("Trev 5612", "testMapID", 1);
		print(blobStr + "\n");
		
		Blob blob = DatabaseHandler.getBlob("Trev 5612", "testMapID", 1);
		if(blob != null){
			print("Blob test success\n");
		}else{
			print("Blob test fail\n");
		}
		
		String json = DatabaseHandler.getJsonEvents("Trev 5612", "testMapID", 1);
		print(json);
	}
*/	
	
	
	
	private static void print(String s){
		System.out.print(s);
	}
	
}
