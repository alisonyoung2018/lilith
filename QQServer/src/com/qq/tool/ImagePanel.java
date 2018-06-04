package com.qq.tool;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -231878107661071229L;
	private Image image = null;

	public ImagePanel(Image image) {
		// TODO Auto-generated constructor stub
		this.image = image;
		this.setSize(Const.SCREENWIDTH, Const.SCREENHEIGHT);
	}

	public ImagePanel(String imagePath) {
		// TODO Auto-generated constructor stub
		try {
			this.image = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		// this.setSize(Const.SCREENWIDTH, Const.SCREENHEIGHT);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		g.drawImage(image, 0, 0, this);
	}
}
