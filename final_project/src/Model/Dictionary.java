package Model;

import java.awt.List;
import java.util.ArrayList;

public class Dictionary {
	

	private ArrayList<String> Dic;
	
	  public Dictionary(ArrayList<String> dic) {
		  this.Dic=dic;
	  }
	
   
	public ArrayList<String> getDic() {
		return Dic;
	}

	public void setDic(ArrayList<String> dic) {
		Dic = dic;
	}

	public Dictionary()
	{
		this.Dic = new ArrayList<String>();
	}
	
public boolean contains(ArrayList<String> list, String name) {
	    for (String item : list) {
	        if (item.equals(name)) {
	            return true;
	        }
	    }
	    return false;
	}

}
