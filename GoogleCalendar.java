import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GoogleCalendar {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>Google Calendar</b><br><br>\n\r"
			+ "Google Calendar is a digital planner that helps you keep track of appointments, birthdays, and reminders. Think of it as a wall calendar that lives on your computer or phone!<br><br>\r\n"
			+ "<strong>How to Add an Appointment</strong><br><br>\r\n"
			+ "<b>Pick a Day:</b> Click on the day and time slot of your appointment.<br>\r\n"
			+ "<br>\r\n"
			+ "<b>Give it a Name:</b> A box will pop up. Type in what the event is (for example: \"Doctor Visit\" or \"Lunch with friend\").<br>\r\n"
			+ "<br>"
			+ "- You can type in the exact time of day the event takes place in the boxes by clicking on one and typing the corresponding time.<br>\r\n"
			+ "<br>"
			+ "- If the event runs all day, or you do not know the exact duration of your appointment, you can click on the <b>All day</b> checkbox to denote as so.<br>\r\n"
			+ "<br>"
			+ "- “Does not repeat” can be changed by clicking on the rectangular dropdown, and clicking on one of the options that appear if the event is recurring.<br>\r\n"
			+ "<br>"
			+ "- The time zone can be adjusted and a description can be written for the event by clicking on the corresponding rectangular buttons and typing.<br>\r\n"
			+ "<br>"
			+ "<b>Save it:</b> Click the blue Save button. Now it’s on your calendar!<br>\r\n";

	private String p2 = "<b>How to Change or Delete an Appointment</b><br>\n\r"
			+ "<br>"
			+ "<b>To Change:</b> Click on the event you already made. Click the <b>Pencil icon</b> to edit any details.<br>";
	
	private String p3 = "<b>To Delete:</b> Click on the event and look for the <b>Trash Can icon</b>. This will remove it if the plans change.";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GoogleCalendar().createUI());
	}

	private void createUI() {
		JFrame frame = new JFrame("Google Calendar Tutorials");
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

		contentPanel.add(createImage("/Images/GC1.png", 480));

		contentPanel.add(Box.createVerticalStrut(20));

		contentPanel.add(createText(p2));

		contentPanel.add(Box.createVerticalStrut(20));

		contentPanel.add(createImage("/Images/GC2.png", 480));
		
		contentPanel.add(Box.createVerticalStrut(20));
		
		contentPanel.add(createText(p3));

		contentPanel.add(Box.createVerticalStrut(20));

		contentPanel.add(createImage("/Images/GC3.png", 480));
		
		contentPanel.add(Box.createVerticalStrut(20));

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

	// Create scalable image component
	private JComponent createImage(String path, int preferredHeight) {

		try {
			InputStream is = getClass().getResourceAsStream(path);
			BufferedImage img = ImageIO.read(is);

			ScaledImagePanel panel = new ScaledImagePanel(img, preferredHeight);
			panel.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.setBackground(Color.WHITE);

			return panel;

		} catch (Exception e) {
			e.printStackTrace();
			return new JLabel("Image not found: " + path);
		}
	}

	// Custom panel for scaling images
	static class ScaledImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private BufferedImage image;

		public ScaledImagePanel(BufferedImage image, int preferredHeight) {
			this.image = image;

			setPreferredSize(new Dimension(800, preferredHeight)); // height matters for scrolling
			setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight)); // allow width to stretch
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (image == null)
				return;

			int panelWidth = getWidth();
			int panelHeight = getHeight();

			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();

			// ONE scale factor -> preserves aspect ratio
			// double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight
			// / imgHeight);
			double scale = Math.min(1.0, Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight));

			int drawWidth = (int) (imgWidth * scale);
			int drawHeight = (int) (imgHeight * scale);

			// Center it
			int x = (panelWidth - drawWidth) / 2;
			int y = (panelHeight - drawHeight) / 2;

			Graphics2D g2 = (Graphics2D) g;

			// Smoother scaling (important)
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g2.drawImage(image, x, y, drawWidth, drawHeight, null);
		}

	}
}