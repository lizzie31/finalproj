package final_project;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private static final Random ourGenerator = new Random();

    /**
     * Read a file into a string.
     *
     * @return returns a string containing all the words in the file
     *         separated by a space
     */
    public String readFile (Scanner input)
    {
        final String SPACE = " ";
        String result = "";
       
        while (input.hasNext())
        {
            result += input.next() + SPACE;
        }

        return result;
    }
   
}


