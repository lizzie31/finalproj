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
	String content = null; ////dfsdfds
	 BufferedWriter writer = null;
	 BufferedReader reader = null;
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
    try
    {
        writer = new BufferedWriter( new FileWriter( "try.txt"));
        writer.write( StrWithoutpunctuation);

    }
    finally
    {
        try
        {
            if ( writer != null)
            writer.close( );
        }
        catch ( IOException e)
        {
        }
    }
    try
    {
        reader = new BufferedReader( new FileReader( "try.txt"));
        String line;
        while ((line = reader.readLine()) != null) { 
        CreateNgramsDic(line);
        }
    }
    finally
    {
        try
        {
            if ( reader != null)
            	reader.close( );
        }
        catch ( IOException e)
        {
        }
    }

    
	}
public static void CreateNgramsDic(String StrWithoutpunctuation) 
{
	 String str=null;
	 int flag;
	 Dictionary d = new Dictionary();
	 for (int i=1;i<StrWithoutpunctuation.length();i++)
	 {
		 str = StrWithoutpunctuation.substring(i,i+3);
	
			 NGram ngram = new NGram(str);
			 d.getDic().add(ngram);
			 System.out.println(str);
		}
		 }
		 
	

}
