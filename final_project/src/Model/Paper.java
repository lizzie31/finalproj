package Model;

import java.util.ArrayList;

public class Paper {
	
	private String content;
	private ArrayList<Part> parts;
	private int PaperNumber;
	
	public Paper(String content, int Papernumber) {
		
		this.content=content;
		this.PaperNumber = Papernumber;
	}
	public ArrayList<Part> getParts() {
		return parts;
	}
	public void setParts(ArrayList<Part> parts) {
		this.parts = parts;
	}
	public int getPaperNumber() {
		return PaperNumber;
	}
	public void setPaperNumber(int paperNumber) {
		PaperNumber = paperNumber;
	}
	public String getContent() {
		return content;
	}


}
