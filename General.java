
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class General {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "General Help<br>"
			+ "<br>"
			+ "<b>How to open an app (e.g. photos, files, email, internet, calendar, clock, schedule, et cetera.)</b><br>"
			+ "Click on the desired app of usage with your computer mouse/mousepad.<br>"
			+ "<br>"
			+ "<b>How to close an app</b><br>"
			+ "Click on the ‘x’ symbol at the top right corner of an open app window. It may prompt you to select if you would really like to shut down the corresponding application.<br>"
			+ "<br>"
			+ "<b>How to select text</b><br>"
			+ "Selecting text is useful if you want to copy text from somewhere and past it somewhere else. This can be done by holding down your mouse from the start of the portion of text you would like to copy, and dragging your mouse along that line of text until the end of the portion you would like to select. Once you have reached the end of the portion of text you would like to select, you do not need to hold the mouse down.";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new General().createUI());
	}

	private void createUI() {
		JFrame frame = new JFrame("General Help");
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

		 // This statement adds 20 pixels of fixed vertical space (known as a "strut")

		contentPanel.add(createImage("/Images/Select.png", 250));

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
