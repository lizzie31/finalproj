package Model;

import java.util.ArrayList;
import java.util.List;

import final_project.DBconn;

public class Part {
	
 private String text;        //the content of the part
 private int PaperNumner;     // the paper this part belong
 private int PartNumber;
 private Histogram histo;        //the histograma of this part 
 private double DistanceFromPrev; // the distance from his T prev (ZVt)
 private double StandartDevesion=0;// if it the center part 
 
 public double getStandartDevesion() {
	return StandartDevesion;
}
public void setStandartDevesion(double standartDevesion) {
	StandartDevesion = standartDevesion;
}
private List<Double> DistanceFromCurrArticle;
 private List<Double> DistanceFromDBArticles;
 private int generatorFlag;
 

 
 
 
 public int getGeneratorFlag() {
	return generatorFlag;
}
public void setGeneratorFlag(int generatorFlag) {
	this.generatorFlag = generatorFlag;
}

public Part()
{

}
public Part(String text, int paperNum ,int partNumber)
 {
	 this.text = text;
	 this.PaperNumner = paperNum;
	 this.PartNumber=partNumber;
	 this.DistanceFromPrev=0;
	 
	
 }

public Part(String text, int paperNum, int partNumber, Histogram histogram,double spearmanCo) {
	
	 this.text = text;
	 this.PaperNumner = paperNum;
	 this.PartNumber=partNumber;
	 this.DistanceFromPrev=spearmanCo;
	 this.histo=histogram;
	 
	// TODO Auto-generated constructor stub
}


public Part(String text, int paperNum, int partNumber, Histogram histogram) {
	
	 this.text = text;
	 this.PaperNumner = paperNum;
	 this.PartNumber=partNumber;
	 this.histo=histogram;
	 
	// TODO Auto-generated constructor stub
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}

public double getDistanceFromPrev() {
	return this.DistanceFromPrev;
}
public void setDistanceFromPrev(double DistanceFromPre) {
	this.DistanceFromPrev = DistanceFromPre;
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
public List<Double> getDistanceFromDBArticles() {
	return DistanceFromDBArticles;
}
public void setDistanceFromDBArticles(List<Double> distanceFromDBArticles) {
	DistanceFromDBArticles = distanceFromDBArticles;
}
public List<Double> getDistanceFromCurrArticle() {
	return DistanceFromCurrArticle;
}
public void setDistanceFromCurrArticle(List<Double> distanceFromCurrArticle) {
	DistanceFromCurrArticle = distanceFromCurrArticle;
}

}


