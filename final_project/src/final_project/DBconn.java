package final_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Dictionary;

public class DBconn {
	
	Connection conn;
	public  void openConnectionDB(){
		
		String url="jdbc:sqlserver://localhost:1433" ;
		String username="try";
		String password="123456";

	    try 
	    {
	      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
	    }
	    catch (Exception ex)
	    {
		  System.out.println("DriverException: " + ex.getMessage());
	    }
	  
	  try 
	  {
	       conn = DriverManager.getConnection(url,username,password);
	       System.out.println("SQL connection succeed");
                
	    
	      
	      
	   }
	  catch (SQLException ex) 
	  {
	       System.out.println("SQLException: " + ex.getMessage());
	       System.out.println("SQLState: " + ex.getSQLState());
	       System.out.println("VendorError: " + ex.getErrorCode());
	       
	  }
	  
	  
	  
   }
	
	public void createDBDictionary(Dictionary d) throws SQLException
	{

		  int length=(d.getDic()).size();
		  String query = "INSERT INTO Dictionary VALUES(?)";
		  PreparedStatement ps = conn.prepareStatement(query);
		  
		  for(int i=0;i<length;i++)
		  {
			 String value=(d.getDic()).get(i);
			 
			 query += "('" + value + "')";
			 query = query.substring(1, query.length() - 1);
			 ps.setString(1,value);
			 ps.addBatch();
		  }
		  ps.executeBatch();	  
	 }
	
	public Dictionary GetDicFromDB() throws SQLException
	{
	       ArrayList<String> ngrams = new ArrayList<String>();
           Statement sta = conn.createStatement();
           String query = "select * from Dictionary";
           ResultSet rs = sta.executeQuery(query);
           while (rs.next()) {
        	   String temp=rs.getString(1);
        	   ngrams.add(temp);       	   
           }
           Dictionary Dic=new Dictionary(ngrams);
           
           return Dic;
	}
	
	
	

}
