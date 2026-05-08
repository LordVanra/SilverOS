
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class QAS {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>Quick Access Shortcuts</b><br>"
			+ "<br>"
			+ "<b>How to access a shortcut to access common websites of use from the desktop/homepage:</b><br><br>"
			+ "Shortcuts to common websites can be found on the homepage alongside all the other applications. Upon clicking the corresponding icons, you will be automatically taken to the website.<br>"
			+ "<br>"
			+ "<b>Common Keyboard Shortcuts</b>";


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new QAS().createUI());
	}

	private void createUI() {
		//Setting up the JFrame
		JFrame frame = new JFrame("Quick Access Shortcuts");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 800);
		frame.setResizable(false);		
		frame.setLocationRelativeTo(null); //Centers the JFrame

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

		contentPanel.add(createImage("/Images/KBS.png", 500));

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
		//Rescaling the image
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
