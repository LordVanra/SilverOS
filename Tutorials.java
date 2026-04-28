
//import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
//import javax.swing.JLabel;
//import java.awt.Image;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;



	 public class Tutorials {
		
		public static void main(String[] args) {
		
			
			JFrame frame = new JFrame();
			frame.setTitle("JFrameOne");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(500,400);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
		
			JPanel panel = new JPanel();
			
			//panel.setLayout();
			
			//frame.add(panel, BorderLayout.CENTER);
			
			JTextArea e = new JTextArea();
			
			//JLabel f = new JLabel();
			
			e.setFont(new Font("Verdana",1,20));
			
			e.setText("hihihihihihihihiihhihihihihihihihihihihihihhihihihihihihihihhihihihihi");
			
			e.setEditable(false);
			
			e.setLineWrap(true);
			
			e.setWrapStyleWord(true);
			
			e.setBounds(0, 0, 500, 400);
			
			//f.setText("ihihihihihihihihihihihihihihihihihi");
			
			//JTextField text =new JTextField(12);
			
			
			//text.setBounds(5,5,50,50);
			
			panel.add(e);
			//panel.add(f);
			
			
	       JScrollPane scrollPane = new JScrollPane(panel);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(50, 30, 300, 50);
	        scrollPane.setBounds(0, 0, 500, 400);
	       JPanel contentPane = new JPanel(null);
	       contentPane.setPreferredSize(new Dimension(500, 400));
	        contentPane.add(scrollPane);
	        frame.setContentPane(contentPane);
	        frame.pack();
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			frame.setVisible(true);
		}
	}
	
