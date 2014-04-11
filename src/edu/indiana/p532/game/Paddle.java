package edu.indiana.p532.game;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * This class initializes the paddle
 *
 * @param x
 *            X co-ordinates of the paddle
 * @param y
 *            Y co-ordinates of the paddle
 */

public class Paddle extends GameObjects{

	private static Image paddleImage;

	public Paddle(int x, int y) {

		this.x = x;
		this.y = y;
		img = new ImageIcon(getClass().getClassLoader().getResource("edu/indiana/p532/images/paddle.png"));
		paddleImage = img.getImage();
	}

	Image gettingImage() {

		return paddleImage;
	}

	public void draw(Graphics g) {
		g.drawImage(gettingImage(), getX(), getY(), null);
	}
}
