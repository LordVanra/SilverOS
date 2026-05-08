import javax.swing.*;
import java.awt.*;


public class Audible {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>Audible Help</b><br><br>\n\r"
			+ "<b>Logging into Audible:</b><br>\n\r"
			+ "Audible is owned by the company Amazon, so you will need an Amazon account to access Audible. If you already have an Amazon account, you can sign in to Audible with the email and password for that account.<br><br>\n\r"
			+ "<b>Navigating audible</b><br>\n\r"
			+ "At the top you can click “Browse”, which will show a menu with options to select from. If you have a specific book in mind, you can select the “🔍” or “Find your next great listen” before typing out the book you would like to listen to.\n\r";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Audible().createUI());
	}

	private void createUI() {
		JFrame frame = new JFrame("Audible Help");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 800);
		frame.setResizable(false);		
		frame.setLocationRelativeTo(null);

		// Main container with scroll
		JPanel outerPanel = new JPanel(new BorderLayout()); // centers content
		outerPanel.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(outerPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// Fixed-width content panel; height can grow depending on content
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setMaximumSize(new Dimension(preferredWidth, Integer.MAX_VALUE)); // allows vertical growth, enable scrolling
		contentPanel.setBackground(Color.WHITE);
		
		// Add content
		contentPanel.add(createText(p1));

		contentPanel.add(Box.createVerticalStrut(20));  // This statement adds 20 pixels of fixed vertical space (known as a "strut")


		// Center content panel
		outerPanel.add(contentPanel, BorderLayout.NORTH);

		frame.add(scrollPane);
		frame.setVisible(true);
	}

	// Create wrapped, centered text
	private JComponent createText(String text) {
		JLabel label = new JLabel(
				"<html><div style='text-align:center; width:600px; font-size:20px;'>" + text + "</div></html>");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setMaximumSize(new Dimension(preferredWidth, Integer.MAX_VALUE)); // Set width to 800

		return label;
	}
}
