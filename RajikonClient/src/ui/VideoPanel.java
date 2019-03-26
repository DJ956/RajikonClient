package ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VideoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private volatile BufferedImage image = null;
	
	private JLabel imageLabel;
	
	public VideoPanel() {
		setSize(640, 480);
		setBackground(Color.BLACK);
		imageLabel = new JLabel();
		add(imageLabel);
	}
	
	public void updateImage(BufferedImage update){
		image = update;
		if(image != null && image instanceof BufferedImage){
			ImageIcon icon = new ImageIcon(image);
			imageLabel.setIcon(icon);
		}
	}
	
}
