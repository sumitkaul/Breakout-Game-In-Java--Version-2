package edu.indiana.p532.command;

import edu.indiana.p532.game.Ball;

public class BallChangeYDirectionCommand implements Command{

	Ball ball;

	public BallChangeYDirectionCommand(Ball ball) {
		this.ball = ball;
	}

	public void move() {
		ball.setVelocityY(-1 * ball.getVelocityY());
	}

	public void undoMove() {
		ball.setVelocityY(-1 * ball.getVelocityY());
	}
}
