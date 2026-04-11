import javax.swing.JOptionPane;

public class Tutorials {
	//Swing
	//Window Builder
	
	//Not done yet me and Suhrit were just figuring things out today but more will be done later
	
	//Example of a popup window with the tutorial for general features. It is not currently connected to the main distro.
	public static void main(String[] args) {
		
		String n = new String("General\n"
				+ "------------------------------------------------------------------------------------------------------------------------------------\n"
				+ "How to open an app (e.g. photos, files, email, internet, calendar, clock, schedule, et cetera.)\n"
				+ "\n"
				+ "Click on the desired app of usage with your computer mouse/mousepad.\n"
				+ "------------------------------------------------------------------------------------------------------------------------------------\n"
				+ "How to close an app\n"
				+ "\n"
				+ "Click on the ‘x’ symbol at the top right corner of an open app window. It may prompt you to select if you would really like to shut down the corresponding application.\n"
				+ "------------------------------------------------------------------------------------------------------------------------------------\n"
				+"How to select text\n"
				+ "\n"
				+"Selecting text is useful if you want to copy text from somewhere and past it somewhere else. \nThis can be done by holding down your mouse from the start of the portion of text you would like to copy, and dragging your mouse along that line of text until the end of the portion you would like to select. \nOnce you have reached the end of the portion of text you would like to select, you do not need to hold the mouse down."
				);
	
		JOptionPane.showMessageDialog(null, n);
	
	}
}
