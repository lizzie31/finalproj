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



public class MainApp {
	
	public static final int T=5;
	public static final int K=10;
	public static final int N=81;
	
	public static DBconn DbConn=new DBconn();
    public static ArrayList<Paper> papers= new ArrayList<Paper>();
    public static ArrayList<Part> AllParts=new ArrayList<Part>();
	public static Dictionary dic;
	public static Part CenterPart;
     public MainApp(){
    	 
    	 DbConn.openConnectionDB();
      
    
     }
     
	public static void main(String [] args) throws IOException, SQLException
	{	   
	   
       MainApp MA= new MainApp();
       dic =DbConn.GetDicFromDB();
       double StandardDeviation;
       papers=DbConn.GetPapersFromDB(dic);
       sortParts(papers);
       AllParts=DbConn.GetPartsFromDB(dic);; 
 	   String file_name = "7";
 	   File path=new File("C:/newarticle/"+ file_name + ".pdf");
 	   Paper newPaper=createOnePaper(file_name,path,dic); 
 	   CenterPart = DbConn.getCenterPart(dic.getDic().size());	
       //CheckOnePpaper2(CenterPart,newPaper,papers); 
      CheckOnePpaper1(newPaper,papers);
 	  
    }
	
	

	public static double calculateDZVt(Paper paper1,Paper paper2,int PartNumber1,int PartNumber2,int j)
	{
		
		      double zvt1=0;
			  double zvt2=0;
			      for(int i=PartNumber1-1;i>=PartNumber1-T;i--)
			      {	
			    	  
			        zvt1=zvt1+calculateSpearman(paper1.getParts().get(PartNumber1-1).getHisto(),paper2.getParts().get(j-1).getHisto());
			        zvt2=zvt2+calculateSpearman(paper1.getParts().get(i-1).getHisto(),paper2.getParts().get(PartNumber2-1).getHisto()); 
			        j--;
			      }
		      zvt1=zvt1/T;
		      zvt2=zvt2/T;
		      return Math.abs(paper1.getParts().get(PartNumber1-1).getDistanceFromPrev()+paper2.getParts().get(PartNumber2-1).getDistanceFromPrev()-zvt1-zvt2);
		
	
		
	}
	
    public static void sortParts(ArrayList<Paper> papers)
    {
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
	     }
    }
	private static void CheckOnePpaper2(Part centerPart, Paper newPaper, ArrayList<Paper> papers) {
		
		int [] flagsArr = new int[newPaper.getParts().size()-T];
		int k =0;
		double DZVt=0;
		Paper CenterPartPaper = papers.get(centerPart.getPaperNumner()-1);
		for(Part paperPart1:newPaper.getParts())
	 	{
			 int j = centerPart.getPartNumber();
			 DZVt = 0;
	 	     if(paperPart1.getPartNumber()>T)
	 	     {	
	 	    	     DZVt=calculateDZVt(CenterPartPaper,newPaper,centerPart.getPartNumber(),paperPart1.getPartNumber(),j);
					 DZVt = DZVt/CenterPart.getStandartDevesion();
					 if(DZVt>2)
					      flagsArr[k]=1;
                     else flagsArr[k]=0;
					 k++;
	 	     }
	 	 	   		 		 
	 	   }
		   for(int i = 0; i< flagsArr.length; i++)
		   {
			   System.out.println(flagsArr[i]);
		   }
		
	}

	private static void FindCenterpPartInCloude(ArrayList<Part> allParts, ArrayList<Paper> papers) throws SQLException {
		double MinDistance = 0;
		  double DZVt=0;
		  double nowDZVt=0;
		  int firstPartFlag=1;
		Part MinPart = new Part();
		
		double DZvtPow = 0;
		double MinDZvtPow = 0;
		double StandartDevision = 0;
		
		   for(Part SelectedPart:allParts)
		   {
			
			  Paper SelectedPartPaper = papers.get(SelectedPart.getPaperNumner()-1);
			   for(Part dbPart:allParts)
			   {  
				   
			   Paper dbPartPaper = papers.get(dbPart.getPaperNumner()-1);
			   int j=dbPart.getPartNumber()-1;
			   nowDZVt=calculateDZVt(SelectedPartPaper,dbPartPaper,SelectedPart.getPartNumber(),dbPart.getPartNumber(),j);
		       DZvtPow = DZvtPow + Math.pow((nowDZVt), 2);
		       DZVt=DZVt+(Math.abs(nowDZVt)); 
		       nowDZVt=0;
			   }
			   DZVt=DZVt/N;
			   if (firstPartFlag ==1)
			   {
				   MinDistance = DZVt;
				   firstPartFlag=0;
				   Part p = new Part(null,SelectedPart.getPaperNumner()-1,SelectedPart.getPartNumber(),SelectedPart.getHistogram(),SelectedPart.getDistanceFromPrev());
				   MinPart=p;
				   MinDZvtPow = DZvtPow;
			   }
			   else if(DZVt<MinDistance)
			  {
				 MinDistance = DZVt;
				 MinPart  = new Part(null,SelectedPart.getPaperNumner()-1,SelectedPart.getPartNumber(),SelectedPart.getHistogram(),SelectedPart.getDistanceFromPrev());
				 MinDZvtPow = 	DZvtPow;			
			  }
			  StandartDevision =Math.sqrt(MinDZvtPow/N);
			  DZVt = 0;
			  DZvtPow=0;
		   }
		   
		   DbConn.InsertCenterPoint(MinPart,StandartDevision);
		
	}



	public static void CheckOnePpaper1(Paper paper, ArrayList<Paper> papers)
	{
		   CalculateDistanceFromDBArticles(paper,papers);
	 	   CalculateDistanceFromSameArticles(paper);   
	 	   UpdateGeneratorFlag(paper);	
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
		    		if( paperPart.getDistanceFromCurrArticle().size()<k)
		    		{
		    	 if(paperPart.getDistanceFromDBArticles().get(j)<paperPart.getDistanceFromCurrArticle().get(k))
		    		 j++;
		    	 else k++;
		    	 }
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
		   double DZVt=0;
	 	   for(Part paperPart1:newPaper.getParts())
	 	   {
	 		  List<Double> distanceFromSamePaper=new ArrayList<>();
	 	     if(paperPart1.getPartNumber()>T)
	 	     {
	 	 	   for(int j=T+1;j<newPaper.getParts().size();j++)
	 	 	   {
	 	 		   
	 	 		   DZVt=calculateDZVt(newPaper,newPaper,paperPart1.getPartNumber(),j,j);
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
				    	  
				        zvt1=zvt1+calculateSpearman(paperPart.getHisto(),DBpaper.getParts().get(j-1).getHisto());
				        zvt2=zvt2+calculateSpearman(newPaper.getParts().get(i-1).getHisto(),DBpart.getHisto()); 
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
		temp1= Math.pow((a.getFreq()[x])-(b.getFreq()[x]),2);
	    SpearmanCor=(long) (SpearmanCor+temp1);}

	SpearmanCor=6*SpearmanCor;// 6*sigma(di^2)
	temp3=a.getFreq().length;
	temp2=(long) Math.pow(temp3,2);
	temp2=temp3*(temp2-1);
	SpearmanCor=(SpearmanCor/temp2)*10000000; //6*sigma(di^2)/n*(n^2-1)
	
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
