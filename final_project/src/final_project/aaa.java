package final_project;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.commons.math3.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

import Model.Dictionary;
import Model.Histogram;
import Model.Paper;
import Model.Part;



public class aaa {
	
	public static final int T=5;
	public static DBconn DbConn=new DBconn();
    public static ArrayList<Paper> papers= new ArrayList<Paper>();
	
     public aaa(){
    
     }
     
	public static void main(String [] args) throws IOException, SQLException
	{
	   ArrayList<Paper> papers=new ArrayList<Paper>();
	   DbConn.openConnectionDB();
       Dictionary dic =DbConn.GetDicFromDB();
       papers=DbConn.GetPapersFromDB(dic);
 	   String file_name = "1";
 	   File path=new File("C:/newarticle/"+ file_name + ".pdf");
 	   Paper newPaper=createOnePaper(file_name,path,dic);
       
	   
       
    }
	


private static ArrayList<Paper> CreatePapers(Dictionary d) throws IOException, SQLException 
 {//creating all of the generator  papers and returning papers arraylist 
    ArrayList<Paper> papers= new ArrayList<Paper>();
    String file_name;
    String content = "";
    for (int i=1; i<12;i++)
    {
	  file_name = Integer.toString(i);
	  File path=new File("C:/articles/"+ file_name + ".pdf");
      Paper p=createOnePaper(file_name,path,d);	
      papers.add(p);
    }
    
    return papers;
    	
    
 }

public static Paper createOnePaper(String file_name,File path,Dictionary dic) throws IOException, SQLException
{
	  String content;
	  PDDocument paper=PDDocument.load(path);
      PDFTextStripper textStripper = new PDFTextStripper();
      content  = (textStripper.getText(paper)).replaceAll("\\W", "");
      content = content.replaceAll("\\d", "");
      paper.close();
      Paper p=new Paper(content,Integer.parseInt(file_name));
      CreateParts(p,dic.getDic());
      
      return p;
}

private static void CreateParts(Paper paper,ArrayList<String> dic) throws SQLException
{ //adding parts arraylist to a specific paper with option to insert all of the information to DB (dbconn.createparts function)
	  Part part = null;
	  int partIndex =1;
	  	    
	  for(int i = 0; i<paper.getContent().length();i+=1000) //go over all of the parts in the paper
	  {
	      double spearmanCor=0;
	      String subPaperStr=paper.getContent().substring(i);
	      if(subPaperStr.length()>1000)
	    	  subPaperStr=paper.getContent().substring(i,i+1001);
	      Histogram CurrHisto=CreateHistogram(dic,subPaperStr);
	      if(partIndex>T) //if the part dosen't have t previous parts, don't calculate the correlation
	      for(int k=partIndex-1;k>=partIndex-T;k--)//go over t previous parts of the specific part
	      {
	    	double PartOfSpearmanCor=0;
	    	Histogram prevHisto=DbConn.GetHistogramFromDB(paper.getPaperNumber(),k,dic.size()); //get previous histogram from DB
	    	for(int x=0; x<prevHisto.getFreq().length; x++) //go over the current and previous histograms and calculate sigma(di^2)
	    		PartOfSpearmanCor =PartOfSpearmanCor+ (int) Math.pow(CurrHisto.getFreq()[x]-prevHisto.getFreq()[x],2);

	    	PartOfSpearmanCor=6*PartOfSpearmanCor;// 6*sigma(di^2)
	    	PartOfSpearmanCor=PartOfSpearmanCor/(dic.size()*(Math.pow(dic.size(),2)-1)); //6*sigma(di^2)/n*(n^2-1)
	    	spearmanCor=PartOfSpearmanCor+spearmanCor;//zvt--> sigma(spearman(di,dj))
	    				
	      }
	          
	      spearmanCor=spearmanCor/T;// zvt=sigma(spearman(di,dj))/T	
	      
	      
	    	 part = new Part(subPaperStr,paper.getPaperNumber(),partIndex,CurrHisto,spearmanCor);    	
	       //DbConn.createHistograms(part);
	      //if(spearmanCor!=0)
	         //DbConn.InsertDBSpearmanFromPrev(paper.getPaperNumber(),j,(float)spearmanCor);
	      //DbConn.createParts(part);
	        paper.getParts().add(part);//add part to paper
	        partIndex++;
	    	
	    }
	    	
}

public static Histogram CreateHistogram(ArrayList<String> dic,String content)
{
	Histogram histo=new Histogram(dic.size());
	for(int i=0;i<content.length()-3;i++)
	{
	  String ngram=content.substring(i, i+3);
	  if(dic.contains(ngram))
	  {	
		 histo.getFreq()[dic.indexOf(ngram)]++;
	  } 
	}
	 return histo;
}

public static Dictionary CreateNgramsDic() throws SQLException, IOException 
{//creating ngrams dictionary and inserting it to DB
	 String str=null;
	 int flag;
	 Dictionary d=new Dictionary();
	 String file_name;
	 String content = "";
	 
	 for (int i=1; i<12;i++)
	 {
		file_name = Integer.toString(i);
		File path=new File("C:/articles/"+ file_name + ".pdf");
		PDDocument paper=PDDocument.load(path);
	    PDFTextStripper textStripper = new PDFTextStripper();
	    content  = (textStripper.getText(paper)).replaceAll("\\W", "");
	    content = content.replaceAll("\\d", "");
	    paper.close();
	 }
	 
	 String StrWithoutpunctuation = content.replaceAll("\\W", "");
	 StrWithoutpunctuation = StrWithoutpunctuation.replaceAll("\\d", "");
	 
	 for (int i=1;i<StrWithoutpunctuation.length()-3;i++)
	 {
		 str = StrWithoutpunctuation.substring(i,i+3);
			
		 if((d.contains(d.getDic(),str))==false)
		 {
			 String ngram = str;			
			 d.getDic().add(str);
		 }
		
	 }
	 
	 DbConn.createDBDictionary(d);
	 return d;
}




}
