package View;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;







import java.util.concurrent.Executors;

import javax.swing.JTextArea;

import final_project.MainApp;

import java.awt.Color;
import java.awt.SystemColor;

public class LoadFileWindow extends JFrame implements ActionListener  {

	//private JFrame frmApp;
	private JFileChooser fileChooser= new JFileChooser(); 
	private String filePath;
	private String fileName;
	private JButton btnBrowse;	
	private int[] flagsArr =null;       //result arr
	private int counter = 0;   // count how many part were out of cloud
	StringBuilder sb = new StringBuilder();
	JTextArea textArea;
	private int flag=2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new LoadFileWindow().setVisible(true);;
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoadFileWindow() {
		
		setTitle("Opening Window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBackground(new Color(204, 204, 255));
     	setSize(200,100);
		setBounds(100, 100, 649, 330);
		Container frame = getContentPane();
	    JPanel jp = new JPanel();
		//Image img = null;
	    jp.setBackground(new Color(204, 204, 255));
		jp.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(259, 170, 150, 19);
		jp.add(textArea);
		
		JLabel label = new JLabel("");
		



		
	   ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/img/ort.png"));
    
	  // dlabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("resources/images/logo.png")));
		JLabel lblWelcome = new JLabel("Detecting of artificially generated scientific manuscripts");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Segoe Print", Font.BOLD, 18));
		lblWelcome.setBounds(86, 94, 504, 33);
		jp.add(lblWelcome);
		label.setIcon(img);
		label.setBounds(259, -23, 150, 150);
		jp.add(label);
		
		JButton btnNewButton = new JButton("Check");
		btnNewButton.addActionListener(this);
		
		
		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
		
			
			public void actionPerformed(ActionEvent e) {
				
			
				
				try{
					
					if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
						
						 File file =fileChooser.getSelectedFile();						
						 filePath = file.getPath();
						  fileName = fileChooser.getSelectedFile().getName();
						  
						  if(fileName.contains(".pdf")==false)                 //check if it pdf file
					       {
					    	   JOptionPane.showMessageDialog(frame, "Please enter only pdf file", "Warning",
								        JOptionPane.WARNING_MESSAGE); 
					    	   fileName=null;
					       }
						  else							  
							  sb.append(fileChooser.getSelectedFile().getName());     // shoe the chosen file name
					   					     
					     
						 }
						
					
					else {
						JOptionPane.showMessageDialog(frame, "No file was choosen", "Warning",
						        JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception e1){
					e1.printStackTrace();
				}
			textArea.setText(sb.toString());
			}
			
		});
		//centreWindow(frmApp);
		
		JLabel lblChooseAPaper = new JLabel("choose a paper");
		lblChooseAPaper.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblChooseAPaper.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseAPaper.setBounds(141, 166, 94, 17);
		jp.add(lblChooseAPaper);
		btnBrowse.setBounds(419, 165, 82, 23);
		jp.add(btnBrowse);
		btnNewButton.setBounds(297, 226, 82, 23);
		jp.add(btnNewButton);
		frame.add(jp);
		
	}
	
	
	
	public void actionPerformed(ActionEvent ae) {
		
		Loading l = new Loading(); //show loadin window
		
		
		Executors.newSingleThreadExecutor().execute(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	
		    	if(fileName==null)
		    	{
		    		JOptionPane.showMessageDialog(getContentPane(), "Please insert file", "Warning",
					        JOptionPane.WARNING_MESSAGE);
		    	}
		   else{
			l.setVisible(true);
		    MainApp MA = new MainApp(filePath,fileName);
		    	
			try {
				flagsArr = MA.Start();                          //start app
			} catch (IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (flagsArr == null) {  //wait for result
				
			}
			
			for(int i=0; i<flagsArr.length;i++)
				if(flagsArr[i]==1) counter++;
			
			if(counter > (flagsArr.length)/2)      
			{
				l.dispose();
				new ByhumanMessege(counter,flagsArr.length).setVisible(true);
				counter =0;
				flagsArr=null;	
				textArea.setText(null);
			}
		
			else{
				l.dispose();
				new ByGneratorMessege(counter,flagsArr.length).setVisible(true);
				counter =0;
				flagsArr=null;
				textArea.setText(null);
			}
		    }	
		    }
		});
				
	}
	
		


	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}


}


class Loading  extends JFrame  {

	public Loading() {
		
		setTitle("Loading");
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
		setSize(384,146);
		//centreWindow(frame);
     
			
		
		Container frame = getContentPane();
	    JPanel jp = new JPanel();
	    
		  jp.setBackground(new Color(204, 204, 255));
				jp.setLayout(null);
	    
		JLabel label = new JLabel("");
		
		 ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/img/clock.gif"));
		label.setIcon( img);
		label.setBounds(10, 11, 76, 77);
		jp.add(label);
		
		JLabel lblK = new JLabel("Please Wait...Loading...");
		lblK.setFont(new Font("Segoe Print", Font.BOLD, 18));
		lblK.setBounds(102, 35, 211, 38);
		jp.add(lblK);
		
	    frame.add(jp);
	}
	
}

