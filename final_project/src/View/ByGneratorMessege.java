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

public class ByGneratorMessege extends JFrame  {

	

	public ByGneratorMessege() {
		
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
		setSize(594,259);
		//centreWindow(frame);
	
			
		
		Container frame = getContentPane();
	    JPanel jp = new JPanel();
	    
		  jp.setBackground(new Color(204, 204, 255));
				jp.setLayout(null);
				
				
			    
				JLabel label = new JLabel("");
			  Image img = new ImageIcon(this.getClass().getResource("/no.png")).getImage();
				label.setIcon(new ImageIcon( img));
				label.setBounds(205, 61, 176, 181);
				jp.add(label);
				
				JLabel lblK = new JLabel("The paper was written by SCGin Generator");
				lblK.setFont(new Font("Segoe Print", Font.BOLD, 18));
				lblK.setBounds(89, 11, 456, 102);
				jp.add(lblK);
				
			    frame.add(jp);
	}

}
