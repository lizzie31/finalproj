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
import java.util.TreeMap;

import org.apache.pdfbox.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFBox;

import Model.Dictionary;
import Model.NGram;

import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.common.function.PDFunction;
import org.apache.pdfbox.text.PDFTextStripper;


public class aaa {
	
	public static void main(String [] args) throws IOException
	{

	String StrWithoutpunctuation=null;  
	String file_name;
	 Dictionary d = new Dictionary();
	String content = null; ////dfsdfds
	for (int i=1; i<12;i++)
	{
		file_name= Integer.toString(i); 
	
	File path=new File("C:/articles/"+ file_name + ".pdf");
	//COSDocument doc=new COSDocument.load();
	PDDocument paper=PDDocument.load(path);
    PDFTextStripper textStripper = new PDFTextStripper();
   content  = content + textStripper.getText(paper);
   StrWithoutpunctuation = content.replaceAll("\\W", "");
  StrWithoutpunctuation = StrWithoutpunctuation.replaceAll("\\d", "");
   paper.close();
   
	}
    
  // System.out.println(StrWithoutpunctuation);
  // System.out.println(StrWithoutpunctuation.length());

   
      
        CreateNgramsDic(StrWithoutpunctuation,d);
     for(NGram n : d.getDic()){
        
        System.out.println(n.getNgram());
  
     }
     System.out.println(d.getDic().size());
	}
public static void CreateNgramsDic(String StrWithoutpunctuation,Dictionary d) 
{
	 String str=null;
	 int flag;
	
	 for (int i=1;i<StrWithoutpunctuation.length()-3;i++)
	 {
		 str = StrWithoutpunctuation.substring(i,i+3);
	
			 NGram ngram = new NGram(str);
			 d.getDic().add(ngram);
		
		}
		 }
		 


}
