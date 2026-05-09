
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

/*
 * Class that creates a JFrame window for the General tutorial.
 */
public class General {
	
	private int preferredWidth = 800;

	// Defining the data here, just to help with the text formatting
	private String p1 = "<b>General Help</b><br>"
			+ "<br>"
			+ "<b>How to open an app (e.g. photos, files, email, internet, calendar, clock, schedule, et cetera.)</b><br>"
			+ "Click on the desired app of usage with your computer mouse/mousepad.<br>"
			+ "<br>"
			+ "<b>How to close an app</b><br>"
			+ "Click on the ‘x’ symbol at the top right corner of an open app window. It may prompt you to select if you would really like to shut down the corresponding application.<br>"
			+ "<br>"
			+ "<b>How to select text</b><br>"
			+ "Selecting text is useful if you want to copy text from somewhere and past it somewhere else. This can be done by holding down your mouse from the start of the portion of text you would like to copy, and dragging your mouse along that line of text until the end of the portion you would like to select. Once you have reached the end of the portion of text you would like to select, you do not need to hold the mouse down.<br><br>";
	
	private String p2 = "<b>Voice Commands</b><br><br>"
			+ "To activate voice commands, say \"help\" out loud and say one of the possible commands.";

	/*
	 * Opens the General tutorial window/runs the code.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new General().createUI());
	}

	/*
	 * Creates the JFrame, scrollPane, and conentPanel sizings and is also in charge of the main text and image layout.
	 */
	private void createUI() {
		JFrame frame = new JFrame("General Help");
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

		 // This statement adds 20 pixels of fixed vertical space (known as a "strut")

		contentPanel.add(createImage("/Images/Select.png", 150));

		contentPanel.add(Box.createVerticalStrut(20));
		
		contentPanel.add(createText(p2));

		contentPanel.add(Box.createVerticalStrut(20));

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

	// Create scalable image component
	/* Creates a buffered image for the image path and puts it in a ScaledImagePanel. The catch returns the image path.
	 * @param path: A string that is the path of the image formatted as "/Images/[imageName].png"
	 * @param preferredHeight: the preferred height of the image
	 * @return returns the ScaledImagePanel with the centered, resized image. If the catch exception occurs it will return the path of the image as a string.
	 */
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
	/*
	 * Class that creates a custom panel that allows for the image to be scaled.
	 */
	static class ScaledImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private BufferedImage image;

		/*
		 * Parameter constructor: Constructs a ScaledImagePanel with an input BufferedImage and sets up the range of the panel with the height being constant but the width can be preferably 800 but can stretch if needed.
		 * @param image: the image as a BufferedImage
		 * @param preferredHeight: the preferred height of an image
		 */
		public ScaledImagePanel(BufferedImage image, int preferredHeight) {
			this.image = image;

			setPreferredSize(new Dimension(800, preferredHeight)); // height matters for scrolling
			setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredHeight)); // allow width to stretch
		}

		@Override
		//Rescaling the image
		/*
		 * Used to center and render the image using graphics. The image is put through an aspect ratio where if the image is smaller than the panel, nothing happens, but if the image is bigger than the panel, it is resized according to the aspect ratio. Then, the image is centered and rendered.
		 * @param g: allows for rendering of shapes, text, and images using the Graphics class
		 */
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
