package Model;

public class Part {
	
 private String text;
 private int PaperNumner; 
 private int PartNumber;
 
 
 
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
public int getPaperNumner() {
	return PaperNumner;
}
public void setPaperNumner(int paperNumner) {
	PaperNumner = paperNumner;
}
}
