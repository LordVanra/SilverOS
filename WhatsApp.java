
import javax.swing.*;
import java.awt.*;

public class WhatsApp {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>WhatsApp Help</b><br>"
			+ "<br>"
			+ "<b>How to set up a WhatsApp account/profile…</b><br>"
			+ "<br>"
			+ "You can only setup a WhatsApp profile if you have a smartphone and a registered phone number. If you already have this, you can then login to the account following.<br>"
			+ "<br>"
			+ "You may send messages to those you wish by typing a phone number.<br>";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new WhatsApp().createUI());
	}

	private void createUI() {
		JFrame frame = new JFrame("WhatsApp Help");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 800);
		frame.setResizable(false);

		// Main container with scroll
		JPanel outerPanel = new JPanel(new BorderLayout()); // centers content
		JScrollPane scrollPane = new JScrollPane(outerPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// Fixed-width content panel; height can grow depending on content
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setMaximumSize(new Dimension(preferredWidth, Integer.MAX_VALUE)); // allows vertical growth, enable scrolling

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

