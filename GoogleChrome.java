import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GoogleChrome {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "Google Chrome <br><br>\n\r"
			+ "Google Chrome is a web browser to access the internet. This will allow you to watch videos, find pictures, open websites, etc.<br><br>\r\n"
			+ "<strong>How to find something on the internet</strong><br>\r\n"
			+ "In the search bar spanning across the length of the screen, click once with your mouse and type in the desired matter you wish to find search results for. The browser does the hard work for you, don’t worry! All you have to do is type whatever you’re looking for :)<br>\r\n"
			+ "<br>\r\n"
			+ "<strong>AI Overview</strong><br>\r\n"
			+ "AI overview is a helpful tool in which the search query will be summarized and provided directly in simple terms."
			+ " An AI overview will be identified by a blue star, along with <q>AI Overview</q> at the beginning.";

	private String p2 = "Next to some information, the overview will provide links to its sources. To view these sources, the gray oval can be clicked which will bring up a menu to the sources.<br>";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GoogleChrome().createUI());
	}

	private void createUI() {
		JFrame frame = new JFrame("Google Chrome Help");
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

		contentPanel.add(createImage("/Images/AIO1.png", 250));

		contentPanel.add(Box.createVerticalStrut(20));

		contentPanel.add(createText(p2));

		contentPanel.add(Box.createVerticalStrut(20));

		contentPanel.add(createImage("/Images/AIO2.png", 400));
		
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