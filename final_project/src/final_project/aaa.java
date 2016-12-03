package final_project;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import org.apache.pdfbox.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFBox;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.common.function.PDFunction;
import org.apache.pdfbox.text.PDFTextStripper;


public class aaa {
	public static void main(String [] args) throws IOException
	{
		
	String str="aa";
	String file_name;
	String content = null;
	 BufferedWriter writer = null;
	for (int i=1; i<9;i++)
	{
		file_name= Integer.toString(i); 
	
	File path=new File("C:/articles/"+ file_name + ".pdf");
	//COSDocument doc=new COSDocument.load();
	PDDocument paper=PDDocument.load(path);
    PDFTextStripper textStripper = new PDFTextStripper();
   content  = content + textStripper.getText(paper);
	}
    
    System.out.println(content);
   
    try
    {
        writer = new BufferedWriter( new FileWriter( "try.txt"));
        writer.write( content);

    }
    catch ( IOException e)
    {
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
	//System.out.printf("hello");
	//PDStream ps=new PDStream(paper);
//	PDDocument.
	//InputStream is=ps.createInputStream();
	}

}
