package final_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Dictionary;
import Model.Histogram;
import Model.Part;

public class DBconn {
	
	Connection conn;
	public  void openConnectionDB(){
		
		String url="jdbc:sqlserver://localhost:1433" ;
		String username="root";
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
	public void createHistograms(Part p)  {
		// TODO Auto-generated method stub
		int i = 0;
		int size = p.getHisto().getFreq().length;
		String quary = "INSERT INTO Histograms VALUES(?, ?, ?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(quary);
		
		for(i=0; i< size ; i++)
		{
			
		preparedStatement.setInt(1, p.getPartNumber());
		preparedStatement.setInt(2, i);
		preparedStatement.setInt(3, p.getHistogram().getFreq()[i]);

		preparedStatement .executeUpdate();
		}
		
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createParts(Part p ,int PeparNumber, int partNumber) throws SQLException
	{
		String quary = "INSERT INTO Parts VALUES(?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(quary);
		preparedStatement.setString(1, p.getText());
		preparedStatement.setInt(2, p.getPartNumber());
		preparedStatement.setInt(3, p.getPaperNumner());
	
		// execute insert SQL stetement
		preparedStatement .executeUpdate();
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
