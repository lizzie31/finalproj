package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ByhumanMessege extends JFrame  {
 
private	int counter;
private	int length;
	

	public ByhumanMessege(int counter, int length) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.counter = counter;
		this.length = length;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBackground(new Color(204, 204, 255));
		setLocation(400,100);
		setBounds(200, 200, 334, 150);
		setSize(651,275);
		//centreWindow(frame);
	
			
		
		Container frame = getContentPane();
	    JPanel jp = new JPanel();
	    
		  jp.setBackground(new Color(204, 204, 255));
				jp.setLayout(null);
		    
				JLabel label = new JLabel("");
				ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/img/ok.png"));
				label.setIcon(img);
				label.setBounds(464, 85, 128, 126);
				jp.add(label);
				
				JLabel result = new JLabel("The paper is real and was written by human");
				result.setFont(new Font("Segoe Print", Font.BOLD, 18));
				result.setBounds(10, 78, 419, 102);
				jp.add(result);
				
				JLabel lblK = new JLabel( counter +" parts out of "+ length + " total paper parts were out of cloude");// part were out of cloud  is real and was written by human");
				lblK.setForeground(Color.RED);
				lblK.setFont(new Font("Segoe Print", Font.BOLD, 18));
				lblK.setBounds(65, 0, 527, 102);
				jp.add(lblK);
				
			    frame.add(jp);
	}

}
