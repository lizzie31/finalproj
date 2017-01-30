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
	
	public static final int T=5; // for ZVt formula , number of previous papers
	
	public static final int N=81; //number of papers parst in our db;
	
	public static  DBconn DbConn=new DBconn();
    public  ArrayList<Paper> papers= new ArrayList<Paper>();  //array of all papers
    public  ArrayList<Part> AllParts=new ArrayList<Part>();  // array of all parts
	public  Dictionary dic;               // N-gram dic of all 3-grams 
	public  Part CenterPart;
	public  String FilePath;               //new file path
	public  String FileName;
	
     public MainApp(String FilePath, String FileName){
    	 
    	 DbConn.openConnectionDB();
         this.FilePath=FilePath;
         this.FileName = FileName;
     }
     
     public int[] Start() throws IOException, SQLException
 	{	   
 	   
       // MainApp MA= new MainApp();
        dic =DbConn.GetDicFromDB();
        double StandardDeviation;
        papers=DbConn.GetPapersFromDB(dic);
        AllParts=DbConn.GetPartsFromDB(dic);; 
  	   
   Paper newPaper=createOneNewPaper(FileName,FilePath,dic);         //Preparing the given paper for using
  // FindCenterpPartInCloude(AllParts,papers); 
  	 CenterPart = DbConn.getCenterPart(dic.getDic().size());

  	
   return  CheckOnePpaper2(CenterPart,newPaper,papers); 
 	  
    }
	
	

	public double calculateDZVt(Paper paper1,Paper paper2,int PartNumber1,int PartNumber2,int j) //compute distance between 2 different pars (DZVt) 
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
	private int[] CheckOnePpaper2(Part centerPart, Paper newPaper, ArrayList<Paper> papers) {
		
		int [] flagsArr = new int[newPaper.getParts().size()-T];
		int k =0;
		double DZVt=0;
		Paper CenterPartPaper = papers.get(centerPart.getPaperNumner()-1); //get the center part from DB
		for(Part paperPart1:newPaper.getParts())  //go throw of all new paper's parts and calculate the distance to the center part in the cloud  
	 	{
			 int j = centerPart.getPartNumber();
			 DZVt = 0;
	 	     if(paperPart1.getPartNumber()>T)
	 	     {	
	 	    	     DZVt=calculateDZVt(CenterPartPaper,newPaper,centerPart.getPartNumber(),paperPart1.getPartNumber(),j);
					 DZVt = DZVt/centerPart.getStandartDevesion();             // check if the distance is bigger the standard devision*2 
					 if(DZVt>2)
					      flagsArr[k]=1;                      //if bigger put 1 (out of cloud)
                     else flagsArr[k]=0;                     //else , o - in the cloud
					 k++;
	 	     }
	 	 	   		 		 
	 	   }
		  return CheckFlags(flagsArr);
			
		}

		private int[] CheckFlags(int[] flagsArr) {         //check how much parts are in/out the cloud. if more then half out this paper is real  
			
			int counter=0;
			  for(int i = 0; i< flagsArr.length; i++)
			   {
				  if(flagsArr[i]==1) counter++;
			   }
			  return flagsArr;
			/*if (counter  >= flagsArr.length/2)
			{
				return 1;
			}
			else return 0;*/
		}
		
	

	private void FindCenterpPartInCloude(ArrayList<Part> allParts, ArrayList<Paper> papers) throws SQLException {  // we call this func just one time and save this part in DB  
		double MinDistance = 0;
		  double DZVt=0;
		  double nowDZVt=0;
		  int firstPartFlag=1;
		Part MinPart = new Part();
		
		double DZvtPow = 0;
		double MinDZvtPow = 0;
		double StandartDevision = 0;
		
		   for(Part SelectedPart:allParts)   // go throw all parts in DB 
		   {
			
			  Paper SelectedPartPaper = papers.get(SelectedPart.getPaperNumner()-1);
			   for(Part dbPart:allParts)           //  for each part compute distance to all parts in DB
			   {  
				   
			   Paper dbPartPaper = papers.get(dbPart.getPaperNumner()-1);
			   int j=dbPart.getPartNumber()-1;
			   nowDZVt=calculateDZVt(SelectedPartPaper,dbPartPaper,SelectedPart.getPartNumber(),dbPart.getPartNumber(),j); //calculate the distance 
		       DZvtPow = DZvtPow + Math.pow((nowDZVt), 2);  //Compute (distance)^2 for standard devision if needed 
		       DZVt=DZVt+(Math.abs(nowDZVt));           // sum all distance of specific part to all part in DB  
		       nowDZVt=0;
			   }
			   DZVt=DZVt/N;           //sum of all distance/N (N-number off all part)
			   if (firstPartFlag ==1)       //if we are checking the first part
			   {
				   MinDistance = DZVt;     //save the sum as min
				   firstPartFlag=0;         // 
				   Part p = new Part(null,SelectedPart.getPaperNumner()-1,SelectedPart.getPartNumber(),SelectedPart.getHistogram(),SelectedPart.getDistanceFromPrev());
				   MinPart=p;   // save this part as the center part
				   MinDZvtPow = DZvtPow;    // save for standard devision if needed
			   }
			   else if(DZVt<MinDistance)     //check if  the current   distance sum is bigger from the current minimum
			  {
				 MinDistance = DZVt;
				 MinPart  = new Part(null,SelectedPart.getPaperNumner()-1,SelectedPart.getPartNumber(),SelectedPart.getHistogram(),SelectedPart.getDistanceFromPrev());
				 MinDZvtPow = 	DZvtPow;			
			  }
			  StandartDevision =Math.sqrt(MinDZvtPow/N); // compute standard devision
			  DZVt = 0;
			  DZvtPow=0;
		   }
		   
		   DbConn.InsertCenterPoint(MinPart,StandartDevision); //save the center part we find in DB 
		
	}


	

/*private static void CalculateDistanceFromDBArticles(Paper newPaper,ArrayList<Paper> papers)
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
    	
    
 }*/

public static Paper createOnePaper(String file_name,File path,Dictionary dic) throws IOException, SQLException  //Prepare new paper for use
{
	  String content;
	  PDDocument paper=PDDocument.load(path);
      PDFTextStripper textStripper = new PDFTextStripper();   //convert pdf to text
      content  = (textStripper.getText(paper)).replaceAll("\\W", "");// remove all punctuation and spaces
      content = content.replaceAll("\\d", "");                  // remove all numbers
      paper.close();
      Paper p=new Paper(content,Integer.parseInt(file_name));  //save the new paper
      CreateParts(p,dic.getDic());                   //parts devision of this paper
      
      return p;
}


public static Paper createOneNewPaper(String file_name,String Filepath,Dictionary dic) throws IOException, SQLException
{
	  String content;
	  File file = new File(Filepath);
	  PDDocument paper=PDDocument.load(file);
      PDFTextStripper textStripper = new PDFTextStripper();
      content  = (textStripper.getText(paper)).replaceAll("\\W", "");
      content = content.replaceAll("\\d", "");
      paper.close();
      Paper p=new Paper(content);
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

public static float calculateSpearman(Histogram a,Histogram b) //for DZVt formula
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

public static Dictionary CreateNgramsDic() throws SQLException, IOException  //we call this func just one time and save the 3-gram dic in DB 
{//creating ngrams dictionary and inserting it to DB
	 String str=null;
	 int flag;
	 Dictionary d=new Dictionary();
	 String file_name;
	 String content = "";
	 
	 for (int i=1; i<12;i++) //go throw of all generator papers in db and  each one for use 
	 {
		file_name = Integer.toString(i);
		File path=new File("C:/articles/"+ file_name + ".pdf");
		PDDocument paper=PDDocument.load(path);
	    PDFTextStripper textStripper = new PDFTextStripper();
	    content  =content+ (textStripper.getText(paper)); // append all papers 
	    paper.close();
	 }
	 
	 String StrWithoutpunctuation = content.replaceAll("\\W", "");//remove punctuation and spaces 
	 StrWithoutpunctuation = StrWithoutpunctuation.replaceAll("\\d", "");   // // remove all numbers
	 
	 for (int i=1;i<StrWithoutpunctuation.length()-3;i++) // go throw all the papers text and get all 3-grams in dic
	 {
		 str = StrWithoutpunctuation.substring(i,i+3);
			
		 if((d.contains(d.getDic(),str))==false)
		 {
			 String ngram = str;			
			 d.getDic().add(str);
		 }
		
	 }
	 
	 DbConn.createDBDictionary(d); //save the dic in DB
	 return d;
}




}
