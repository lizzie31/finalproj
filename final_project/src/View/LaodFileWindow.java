package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Scanner;

import javax.swing.JTextArea;

public class LaodFileWindow {

	private JFrame frmApp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LaodFileWindow window = new LaodFileWindow();
					window.frmApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LaodFileWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmApp = new JFrame();
		frmApp.setTitle("App");
	
		frmApp.setBounds(100, 100, 649, 330);
		frmApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmApp.getContentPane().setLayout(null);
		
		JLabel lblWelcome = new JLabel("Detecting of artificially generated scientific manuscripts");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Segoe Print", Font.BOLD, 18));
		lblWelcome.setBounds(30, 101, 574, 50);
		frmApp.getContentPane().add(lblWelcome);
		
		JLabel lblChooseAPaper = new JLabel("choose a paper");
		lblChooseAPaper.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblChooseAPaper.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseAPaper.setBounds(51, 185, 200, 32);
		frmApp.getContentPane().add(lblChooseAPaper);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(238, 189, 138, 23);
		frmApp.getContentPane().add(textArea);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser= new JFileChooser();
				StringBuilder sb = new StringBuilder();
				String filePath;
				
				try{
					
					if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
						 File file =fileChooser.getSelectedFile();
				
						 filePath = file.getPath();
						sb.append(fileChooser.getSelectedFile().getName());
						 Scanner input = new Scanner(file);
						 
					     input.close();
						 }
						
					
					else {
						JOptionPane.showMessageDialog(frmApp, "No file was choosen", "Warning",
						        JOptionPane.WARNING_MESSAGE);
					}
				}catch (Exception e1){
					e1.printStackTrace();
				}
			textArea.setText(sb.toString());
			}
			
		});
		btnBrowse.setBounds(415, 192, 89, 23);
		frmApp.getContentPane().add(btnBrowse);
		
		JLabel label = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/ort.png")).getImage();
		label.setIcon(new ImageIcon( img));
		label.setBounds(232, 22, 258, 77);
		frmApp.getContentPane().add(label);
		
		
	}
}
