package Model;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.pdfbox.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.ExtractText;


import java.io.IOException;

public class NGram
{
    // like a dice, generates a series of random numbers
 private String ngram;
  
 public NGram(String ngram)
 {
	 this.ngram=ngram;
 }
}


