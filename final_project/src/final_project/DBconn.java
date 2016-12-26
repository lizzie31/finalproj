package final_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
            	spearman=(double)this.GetPrevSpearmanFromDB(i, partnum);
            	Histogram histogram=this.GetHistogramFromDB(i,partnum,d.getDic().size());
                Part paperPart=new Part(contant,i,partnum,histogram,spearman);
            	paper.getParts().add(paperPart); 
             }
           }
           
           return papers;
	}
	public ArrayList<Part> GetPartsFromDB(Dictionary d)  throws SQLException {
		
		ArrayList<Part>AllParts = new ArrayList<Part>();
		Statement sta = conn.createStatement();
		 String query = "select * from PartDistanceFromPrev";
         ResultSet rs = sta.executeQuery(query);
         
         while (rs.next())
         {
        	int PaperNumber=rs.getInt(1);
        	int partnum=rs.getInt(2);
        	Double Didtance = rs.getDouble(3);
        	Histogram histogram=this.GetHistogramFromDB(PaperNumber,partnum,d.getDic().size());
        	
            Part paperPart= new Part(null,PaperNumber,partnum,histogram,Didtance);
        	AllParts.add(paperPart); 
         }
		return AllParts;
	}
	
	public void createParts(Part p) throws SQLException //inert one part to db
	{
		String quary = "INSERT INTO Parts VALUES(?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(quary);
		preparedStatement.setString(1, p.getText());
		preparedStatement.setInt(2, p.getPartNumber());
		preparedStatement.setInt(3, p.getPaperNumner());
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
           while (rs.next()) 
        	   ngrams.add(rs.getString(1));       	        
           Dictionary Dic=new Dictionary(ngrams);
           
         return Dic;
	}
	
	
	public Histogram GetHistogramFromDB(int papernum,int partnum,int n) throws SQLException
	{
	       int[] Histo=new int[n+1];
           Statement sta = conn.createStatement();
           String query = "select NgramIndexInDic,Rank from Histograms where paperPart="+partnum+"AND PaperNumber="+papernum;
           ResultSet rs = sta.executeQuery(query); 
           while (rs.next()) 
        	   Histo[rs.getInt(1)]=rs.getInt(2);   
           Histogram histogram=new Histogram(Histo);
           
         return histogram;
		
	}
	
	public void InsertDBSpearmanFromPrev(int paperNum,int partNum,double SpearmanCor) throws SQLException
	{
		String quary = "INSERT INTO PartDistanceFromPrev VALUES(?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(quary);
		preparedStatement.setInt(1,paperNum);
		preparedStatement.setInt(2,partNum);
		preparedStatement.setFloat(3,(float)SpearmanCor);
		preparedStatement .executeUpdate();
		
	}
	
	public double GetPrevSpearmanFromDB(int paperNum,int partNum) throws SQLException
	{
		if(partNum>T)
		{
	        Statement sta = conn.createStatement();
	        String query = "select DistanceFromPrev from PartDistanceFromPrev where paperNumber="+paperNum+"AND partNumber="+partNum;
	        ResultSet rs = sta.executeQuery(query);
	        while (rs.next()) 
	          return rs.getFloat(1);	           
		}
		return 0;
        
	}
	public void InsertCenterPoint(Part minPart, double StandartDevesion) throws SQLException {
		
		String quary = "INSERT INTO CenterPart VALUES(?, ?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(quary);
		preparedStatement.setInt(1,minPart.getPaperNumner());
		preparedStatement.setInt(2,minPart.getPartNumber());
		preparedStatement.setFloat(3,(float) (minPart.getDistanceFromPrev()));
				
		preparedStatement.setFloat(4, (float) StandartDevesion);
		preparedStatement .executeUpdate();
		
	}
	public Part getCenterPart(int size) throws SQLException {
		
		Part CenterPart = new Part();
		  
	        Statement sta = conn.createStatement();
	        String query = "select * from CenterPart";
	        ResultSet rs = sta.executeQuery(query);
	        
	        rs.next();
	        int paperNumber =	  rs.getInt(1);
	        
	        CenterPart.setPaperNumner(paperNumber);  
	        CenterPart.setPartNumber(rs.getInt(2));
	        CenterPart.setDistanceFromPrev(rs.getFloat(3));	        
	        Histogram histo = GetHistogramFromDB(CenterPart.getPaperNumner(),CenterPart.getPartNumber(),size);
	        CenterPart.setHisto(histo);
	        CenterPart.setStandartDevesion(rs.getFloat(4));
		return CenterPart;
	}
	

	


	
	
	

}
