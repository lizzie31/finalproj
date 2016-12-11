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
import Model.Paper;
import Model.Part;

public class DBconn {
	
	public static final int T=5;
	Connection conn;
	
	public DBconn()
	{
		
		openConnectionDB();
		
	}
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
	public void createHistograms(Part p)  {
		// TODO Auto-generated method stub
		int i = 0;
		int size = p.getHisto().getFreq().length;
		String quary = "INSERT INTO Histograms VALUES(?,?,?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = conn.prepareStatement(quary);
		
		for(i=0; i< size ; i++)
		{

			
		preparedStatement.setInt(1, p.getPaperNumner());
		preparedStatement.setInt(2, p.getPartNumber());
		preparedStatement.setInt(3, i);
		preparedStatement.setInt(4, p.getHistogram().getFreq()[i]);

		preparedStatement .executeUpdate();
		}
		
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Paper> GetPapersFromDB(Dictionary d) throws SQLException
	{
		   ArrayList<Paper> papers=new ArrayList<Paper>();
		   double spearman;
           Statement sta = conn.createStatement();
           for(int i=1;i<12;i++)
           {
        	 Paper paper=new Paper(i);
        	 papers.add(paper);
             String query = "select * from Parts where paperNumber="+i;
             ResultSet rs = sta.executeQuery(query);
             while (rs.next())
             {
            	 String contant=rs.getString(1);
            	 int partnum=rs.getInt(2);
            	// if(partnum>T)
            	 //{
                 //String query1 = "select * from PartDistanceFromPrev where paperNumber="+i+" AND partNumber="+partnum;
                 //ResultSet rs1 = sta.executeQuery(query1);
            	 
            	 spearman=(double)this.GetPrevSpearmanFromDB(i, partnum);
            	// }
            	// else spearman=0;
            	 Histogram histogram=this.GetHistogramFromDB(i,partnum,d.getDic().size());
            	 Part paperPart=new Part(contant,i,partnum,histogram,spearman);
            	 paper.getParts().add(paperPart);
        	    
     	   
             }
           }
           
           return papers;
	}
	public void createParts(Part p) throws SQLException //inert one part to db
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
	
	
	public Histogram GetHistogramFromDB(int papernum,int partnum,int n) throws SQLException
	{
	       int[] Histo=new int[n+1];
           Statement sta = conn.createStatement();
           String query = "select NgramIndexInDic,Rank from Histograms where paperPart="+partnum+"AND PaperNumber="+papernum;
           ResultSet rs = sta.executeQuery(query);
           while (rs.next()) {
        	   Histo[rs.getInt(1)]=rs.getInt(2);
           }
           Histogram histogram=new Histogram(Histo);
           
           return histogram;
		
	}
	
	public void InsertDBSpearmanFromPrev(int paperNum,int partNum,float SpearmanCor) throws SQLException
	{
		String quary = "INSERT INTO PartDistanceFromPrev VALUES(?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(quary);
		preparedStatement.setInt(1,paperNum);
		preparedStatement.setInt(2,partNum);
		preparedStatement.setFloat(3,(float)SpearmanCor);
	
		// execute insert SQL stetement
		preparedStatement .executeUpdate();
		
	}
	
	public double GetPrevSpearmanFromDB(int paperNum,int partNum) throws SQLException
	{
		if(partNum>T)
		{
	           Statement sta = conn.createStatement();
	           String query = "select DistanceFromPrev from PartDistanceFromPrev where paperNumber="+paperNum+"AND partNumber="+partNum;
	           ResultSet rs = sta.executeQuery(query);
	           while (rs.next()) {
	             return rs.getFloat(1);
	           }
		}
		return 0;
        
	}
	


	
	
	

}
