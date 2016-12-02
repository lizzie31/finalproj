package final_project;
import java.io.File;
import java.io.FileInputStream;
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
		
	String str;
	File path=new File("C:/final project/articles/scimakelatex.19302.mike+abc.ester+def.pdf");
	//COSDocument doc=new COSDocument.load();
	PDDocument paper=PDDocument.load(path);
    PDFTextStripper textStripper = new PDFTextStripper();
    String content = textStripper.getText(paper);

    
    System.out.println(content);
	//System.out.printf("hello");
	//PDStream ps=new PDStream(paper);
//	PDDocument.
	//InputStream is=ps.createInputStream();
	}

}
