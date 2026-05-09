import javax.swing.*;
import java.awt.*;

/*
 * Class that creates a JFrame window for the myChart tutorial.
 */
public class myChart {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>myChart Help</b><br><br>\n\r"
			+ "myChart is an app that allows you to access medical records. "
			+ "For any questions on how to use myChart, type <b>https://mychartonline.umassmemorial.org/<br>MyChart/myChart_Patient_Guide.pdf</b> into the search bar of Google Chrome. It will bring you to a user guide for myChart. "
			+ "<br><br>For any questions not addressed in the user manual, ask your healthcare provider. <br>"
			+ "<br>"
			+ "*If extra help is needed with Google Chrome, access the Google Chrome tutorial!*";

	/*
	 * Opens the myChart tutorial window/runs the code.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new myChart().createUI());
	}

	/*
	 * Creates the JFrame, scrollPane, and conentPanel sizings and is also in charge of the main text and image layout.
	 */
	private void createUI() {
		JFrame frame = new JFrame("myChart Help");
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
	/*
	 * Takes the String text and converts it into a JLabel with html before aligning (center align) and sizing the text.
	 * @param text: A string that contains the text of the tutorial
	 * @return returns the resulting Jlabel with the wrapped, center-aligned text
	 */
	private JComponent createText(String text) {
		JLabel label = new JLabel(
				"<html><div style='text-align:center; width:600px; font-size:20px;'>" + text + "</div></html>");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setMaximumSize(new Dimension(preferredWidth, Integer.MAX_VALUE)); // Set width to 800

		return label;
	}
}