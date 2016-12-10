package final_project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.IOException;
import java.sql.*;

import org.apache.pdfbox.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFBox;

import Model.Dictionary;
import Model.NGram;
import Model.Paper;
import Model.Part;

import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.common.function.PDFunction;
import org.apache.pdfbox.text.PDFTextStripper;


public class aaa {
	
	public static DBconn DbConn=new DBconn();
    public static ArrayList<Paper> papers= new ArrayList<Paper>();
	
     public aaa(){
    
     }
     
	public static void main(String [] args) throws IOException, SQLException
	{
	  
	   String StrWithoutpunctuation="";  
	   String file_name;
	   String content = "";
	   aaa.DbConn.openConnectionDB();
	// Dictionary d = new Dictionary();
      Dictionary d =DbConn.GetDicFromDB();
	  for (int i=1; i<12;i++)
	  {
		file_name= Integer.toString(i); 
		Paper paper=CreatePapers(file_name,d); 
		aaa.papers.add(paper);
       content=content+paper.getContent();
       
	  }
   //  StrWithoutpunctuation = content.replaceAll("\\W", "");
   //  StrWithoutpunctuation = StrWithoutpunctuation.replaceAll("\\d", "");
   //   CreateNgramsDic(StrWithoutpunctuation,d);
    //  aaa.DbConn.createDBDictionary(d);
	}

  
	
private static Paper CreatePapers(String file_name,Dictionary d) throws IOException, SQLException 
 {
	
	String content;
	File path=new File("C:/articles/"+ file_name + ".pdf");
	PDDocument paper=PDDocument.load(path);
    PDFTextStripper textStripper = new PDFTextStripper();
    content  = (textStripper.getText(paper)).replaceAll("\\W", "");
    content = content.replaceAll("\\d", "");
    paper.close();
    Paper p=new Paper(content,Integer.parseInt(file_name));	
    CreateParts(p,d.getDic());
    return p;
    	
    
 }

private static void CreateParts(Paper paper,ArrayList<String> dic) throws SQLException
{
	Part part = null;
	int j =1;
	    for(int i = 0; i<paper.getContent().length();i+=1000)
	    {
	    	String substr=paper.getContent().substring(i);
	    	if(substr.length()>1000)
	    	  part = new Part(paper.getContent().substring(i,i+1001),paper.getPaperNumber(),j);    	
	    	else
	    	  part = new Part(substr,paper.getPaperNumber(),j);
	    	DbConn.createParts(part, i, j);
	    	part.CreateHistogram(dic , DbConn);
	    	
	    	paper.getParts().add(part);
	    	j++;
	    }
	 

}

public static void CreateNgramsDic(String StrWithoutpunctuation,Dictionary d) 
{
	 String str=null;
	 int flag;
	
	 for (int i=1;i<StrWithoutpunctuation.length()-3;i++)
	 {
		 str = StrWithoutpunctuation.substring(i,i+3);
			
		 if((d.contains(d.getDic(),str))==false)
		 {
			 String ngram = str;			
			 d.getDic().add(str);
		 }
		
	 }
}


}
