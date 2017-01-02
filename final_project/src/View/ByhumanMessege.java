package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ByhumanMessege extends JFrame  {

	

	public ByhumanMessege() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBackground(new Color(204, 204, 255));
		setLocation(400,100);
		setBounds(200, 200, 334, 150);
		setSize(594,275);
		//centreWindow(frame);
	
			
		
		Container frame = getContentPane();
	    JPanel jp = new JPanel();
	    
		  jp.setBackground(new Color(204, 204, 255));
				jp.setLayout(null);
				
				
			    
				JLabel label = new JLabel("");
			  Image img = new ImageIcon(this.getClass().getResource("/ok.png")).getImage();
				label.setIcon(new ImageIcon( img));
				label.setBounds(209, 68, 176, 181);
				jp.add(label);
				
				JLabel lblK = new JLabel("The paper is real and was written by human");
				lblK.setFont(new Font("Segoe Print", Font.BOLD, 18));
				lblK.setBounds(65, 0, 456, 102);
				jp.add(lblK);
				
			    frame.add(jp);
	}

}
