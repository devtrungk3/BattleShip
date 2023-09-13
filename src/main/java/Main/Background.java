package Main;
import java.awt.*;

import javax.swing.*;

public class Background extends JPanel {

	/**
	 * Create the panel.
	 */
	public Background() {
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Image img = new ImageIcon("background.jpg").getImage();
		g.drawImage(img, 0, 0, 595, 411, null);
	}

}
