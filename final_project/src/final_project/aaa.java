package final_project;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import Model.Dictionary;
import Model.Histogram;
import Model.Paper;
import Model.Part;



public class aaa {
	
	public static final int T=5;
	public static final int K=5;
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
 	   CalculateDistanceFromDBArticles(newPaper,papers);
 	   CalculateDistanceFromSameArticles(newPaper);   
 	  UpdateGeneratorFlag(newPaper);
    }
	
	public static void UpdateGeneratorFlag(Paper newPaper)
	{
		  for(int t=T;t<newPaper.getParts().size();t++)
		   {
	 		  int k=0;
	 		  int j=0;
	 		  Part paperPart=newPaper.getParts().get(t);
		     for(int i=0;i<K;i++)
		     {
		    	 if((paperPart.getDistanceFromDBArticles()!=null)&&(paperPart.getDistanceFromCurrArticle()!= null))
		    	 {
		    	 if(paperPart.getDistanceFromDBArticles().get(j)<paperPart.getDistanceFromCurrArticle().get(k))
		    		 j++;
		    	 else k++;
		    	 }
		    		 
		     }
		     
		     if(j>k)
		    	 paperPart.setGeneratorFlag(1);
		     else
		    	 paperPart.setGeneratorFlag(0);
		     
		     System.out.println(paperPart.getGeneratorFlag());
		   }
	}
	
	private static void CalculateDistanceFromSameArticles(Paper newPaper)
	{

	 	   
	 	   for(Part paperPart1:newPaper.getParts())
	 	   {
	 		  List<Double> distanceFromSamePaper=new ArrayList<>();
	 	     if(paperPart1.getPartNumber()>T)
	 	     {
	 	 	   for(int j=6;j<newPaper.getParts().size();j++)
	 	 	   {
					  double zvt1=0;
	 	 			  double zvt2=0;
	 	 			  double DZVt=0;
	 			
	 					  int t=j-1;
	 				      for(int i=paperPart1.getPartNumber()-1;i>=paperPart1.getPartNumber()-T;i--)
	 				      {	
	 				    	  
	 				        zvt1=zvt1+calculateSpearman(paperPart1.getHisto(),newPaper.getParts().get(t).getHisto());
	 				        zvt2=zvt2+calculateSpearman(newPaper.getParts().get(i).getHisto(),newPaper.getParts().get(j).getHisto()); 
	 				        t--;
	 				      }
					      zvt1=zvt1/T;
					      zvt2=zvt2/T;
					      DZVt=Math.abs(paperPart1.getDistanceFromPrev()+newPaper.getParts().get(t).getDistanceFromPrev()-zvt1-zvt2);
					      distanceFromSamePaper.add(DZVt);
	 				  }

	 	 	   }
				     Collections.sort(distanceFromSamePaper); 
		 		     paperPart1.setDistanceFromCurrArticle(distanceFromSamePaper);
		 		  
	 	   }
	 	   
	 	   
	}
private static void CalculateDistanceFromDBArticles(Paper newPaper,ArrayList<Paper> papers)
{
	Collections.sort(newPaper.getParts(), new Comparator<Part>() {
        @Override
        public int compare(Part part2, Part part1)
        {

            return  part2.getPartNumber()-part1.getPartNumber();
        }
       });
	   for(Part paperPart:newPaper.getParts())
	   {
		  if(paperPart.getPartNumber()>T)
		  {
			  List<Double> distanceFromDBParts=new ArrayList<>();
	   
		     for(Paper DBpaper:papers)
		     {  
	    	Collections.sort(DBpaper.getParts(), new Comparator<Part>()
	    	{
				 @Override
				   public int compare(Part part2, Part part1)
				   {
				      return  part2.getPartNumber()-part1.getPartNumber();
				   }
				});
			   
			   for(Part DBpart:DBpaper.getParts())
			   {
				  double zvt1=0;
	 			  double zvt2=0;
	 			  double DZVt=0;
	 			  int j=DBpart.getPartNumber();
				  if(j>T)
				  {
					  j=j-1;
				      for(int i=paperPart.getPartNumber()-1;i>paperPart.getPartNumber()-T;i--)
				      {	
				    	  
				        zvt1=zvt1+calculateSpearman(paperPart.getHisto(),DBpaper.getParts().get(j).getHisto());
				        zvt2=zvt2+calculateSpearman(newPaper.getParts().get(i).getHisto(),DBpart.getHisto()); 
				        j--;
				      }
			      zvt1=zvt1/T;
			      zvt2=zvt2/T;
			      DZVt=Math.abs(paperPart.getDistanceFromPrev()+DBpart.getDistanceFromPrev()-zvt1-zvt2);
			      distanceFromDBParts.add(DZVt);
			            
				   }
				  
			   }
		    }
		    Collections.sort(distanceFromDBParts); 
		    paperPart.setDistanceFromDBArticles(distanceFromDBParts);
		  }
	   }

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
	      double zvt=0;
	      String subPaperStr=paper.getContent().substring(i);
	      if(subPaperStr.length()>1000)
	    	  subPaperStr=paper.getContent().substring(i,i+1001);
	      Histogram CurrHisto=CreateHistogram(dic,subPaperStr);
	      if(partIndex>T) //if the part dosen't have t previous parts, don't calculate the correlation
	      for(int k=partIndex-1;k>=partIndex-T;k--)//go over t previous parts of the specific part
	      {
	    	 Histogram prevHisto=paper.getParts().get(k-1).getHisto(); //get previous histogram from DB
	    	 zvt=zvt+calculateSpearman(CurrHisto,prevHisto);	
	      }
	          
	      zvt=zvt/T;// zvt=sigma(spearman(di,dj))/T	
	      
	    	 part = new Part(subPaperStr,paper.getPaperNumber(),partIndex,CurrHisto,zvt);    	
	       //DbConn.createHistograms(part);
	       // if(zvt!=0)
	       //  DbConn.InsertDBSpearmanFromPrev(paper.getPaperNumber(),partIndex,zvt);
	      //DbConn.createParts(part);
	        paper.getParts().add(part);//add part to paper
	        partIndex++;
	    	
	    }
	    	
}

public static float calculateSpearman(Histogram a,Histogram b)
{
	
	float SpearmanCor=0;
	double temp1=0;
	long  temp2=0;
	long  temp3=0;
	for(int x=0; x<a.getFreq().length; x++){ //go over the current and previous histograms and calculate sigma(di^2)
		temp1= Math.pow(a.getFreq()[x]-b.getFreq()[x],2);
	    SpearmanCor=(long) (SpearmanCor+temp1);}

	SpearmanCor=6*SpearmanCor;// 6*sigma(di^2)
	temp3=a.getFreq().length;
	temp2=(long) Math.pow(temp3,2);
	temp2=temp3*(temp2-1);
	SpearmanCor=(SpearmanCor/temp2)*100000000; //6*sigma(di^2)/n*(n^2-1)
	
	return SpearmanCor;//spearman(di)
	
}

public static Histogram CreateHistogram(ArrayList<String> dic,String content)
{
	Histogram histo=new Histogram(dic.size());
	for(int i=1;i<content.length()-3;i++)
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
