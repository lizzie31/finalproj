package Model;

public class Histogram {
	
    private final int[] freq;   // freq[i] = # occurences of value i 
    
    // Create a new histogram. 
    public Histogram(int n) {
        freq = new int[n+1];      // 
    }

	public Histogram(int[] histogram) {
		this.freq=histogram;
	}

	public int[] getFreq() {
		return freq;
	}


}
