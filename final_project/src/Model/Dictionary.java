package Model;

import java.awt.List;
import java.util.ArrayList;

public class Dictionary {
	
	private ArrayList <NGram> Dic;

	public ArrayList<NGram> getDic() {
		return Dic;
	}

	public void setDic(ArrayList<NGram> dic) {
		Dic = dic;
	}

	public Dictionary()
	{
		this.Dic = new ArrayList<NGram>();
	}
}