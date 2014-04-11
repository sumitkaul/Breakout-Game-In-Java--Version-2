package edu.indiana.p532.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

/*
 * This class initializes the main board interface of
 * the game. All the constructors are called in. The repaint
 * function the does the job of repainting the board
 * to display the updated contents.
 * @param bricks Array of Brick class
 * @param ball Object of the Ball
 * @param paddle Object of the paddle
 */

public class Board extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<Brick> bricksList;
	private Ball ball;
	private Paddle paddle;

	public Board(List<Brick> bricksList, Ball ball, Paddle paddle) {
		this.setSize(new Dimension(Constants.BOARD_LENGTH,
				Constants.BOARD_WIDTH));

		this.bricksList = bricksList;
		this.ball = ball;
		this.paddle = paddle;

		this.setBackground(Color.WHITE);

		this.setFocusable(true);
		this.requestFocusInWindow();

	}

	public void paint(Graphics g) {
		super.paint(g);
		for (Brick brick : bricksList) {
			if (!brick.isHit()) {
				g.drawImage(brick.gettingImage(), brick.getX(),
						brick.getY(), null);
			}
		}

		paddle.draw(g);
		//g.drawImage(paddle.gettingImage(), paddle.getX(), paddle.getY(), null);

		ball.draw(g);
		//g.drawImage(ball.gettingImage(), ball.getX(), ball.getY(), null);

	}

	public void draw() {
		repaint();
	}
}
