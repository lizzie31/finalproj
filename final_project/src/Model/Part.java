package Model;

import java.util.ArrayList;

import final_project.DBconn;

public class Part {
	
 private String text;
 private int PaperNumner; 
 private int PartNumber;
 private Histogram histo;

 
 
 
 public Part(String text, int paperNum ,int partNumber)
 {
	 this.text = text;
	 this.PaperNumner = paperNum;
	 this.PartNumber=partNumber;
	
 }
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}

public Histogram getHistogram() {
	return histo;
}
public void setHistogram(Histogram histogram) {
	this.histo = histogram;
}
public int getPaperNumner() {
	return PaperNumner;
}
public void setPaperNumner(int paperNumner) {
	PaperNumner = paperNumner;
}

public void CreateHistogram(ArrayList<String> dic, DBconn DbConn)
{
	int v=dic.size();
	this.histo=new Histogram(dic.size());
	for(int i=0;i<text.length()-3;i++)
	{
	String ngram=text.substring(i, i+3);
	if(dic.contains(ngram))
	{	
		histo.getFreq()[dic.indexOf(ngram)]++;
	}

	
}
	
	DbConn.createHistograms(this);
}
public int getPartNumber() {
	return PartNumber;
}
public void setPartNumber(int partNumber) {
	PartNumber = partNumber;
}
public Histogram getHisto() {
	return histo;
}
public void setHisto(Histogram histo) {
	this.histo = histo;
}
}

