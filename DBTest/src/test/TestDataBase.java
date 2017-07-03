package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


import main.DBConnection;

public class TestDataBase {
	
	
	private static final String TEST_USER = "TestSelecteUser";
	private static final String TEST_EMAIL = "TestSelecteEmail";  
	private static final String TEST_WEBPAGE = "TestSelecteWebPage";  
	private static final String TEST_SUMMARY = "TestSelecteSummary";
	private static final String TEST_COMMENTS = "TestSelecteComment";   

	//@Test
	public void testConnection(){
		boolean result = false; 
		DBConnection connector = new DBConnection("dbtest");  
		
		try {
			result = connector.connect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connector.close();
		}
		
		
		Assert.assertEquals(true,result); 
	}
	
	//@Test
	public void testInsert(){
		DBConnection connector = new DBConnection("dbtest");
		int id = -1; 
		try {
			connector.connect(); 
			id = connector.insert("TestUser","TestEmail","TEstWebPage", "TestSumary","TestComment");
		} catch (Exception e) { 
			e.printStackTrace();
		}finally {
			connector.close();
		}
		
		Assert.assertEquals(true, id>-1);
	}
	
	

	@Test
	public void testSelect(){
		DBConnection connector = new DBConnection("dbtest");
		int id = -1; 
		ArrayList<Map> arrayResult = null;  
		
		try {
			connector.connect(); 
			id = connector.insert(
					TEST_USER,
					TEST_EMAIL,
					TEST_WEBPAGE,
					TEST_SUMMARY,
					TEST_COMMENTS);
			
			arrayResult = connector.select(id); 
			
		} catch (Exception e) { 
			e.printStackTrace();
		}finally {
			connector.close();
		}
		
		Assert.assertNotNull(arrayResult); 
		Assert.assertEquals(1, arrayResult.size()); 
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> hashMap =  (HashMap<String, String>) arrayResult.get(0); 
		
		
		Assert.assertEquals(TEST_USER, hashMap.get("user")); 
		Assert.assertEquals(TEST_EMAIL, hashMap.get("email")); 
		Assert.assertEquals(TEST_WEBPAGE, hashMap.get("webpage")); 
		Assert.assertEquals(TEST_SUMMARY, hashMap.get("summary")); 
		Assert.assertEquals(TEST_COMMENTS, hashMap.get("comments"));
		
	    DBConnection.writeResultSet(hashMap);
	}
	
	
	
	

}
