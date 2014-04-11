package edu.indiana.p532.command;

import edu.indiana.p532.game.Ball;

public class BallChangeXDirectionCommand implements Command{

	Ball ball;

	public BallChangeXDirectionCommand(Ball ball) {
		this.ball = ball;
	}

	public void move() {
		ball.setVelocityX(-1 * ball.getVelocityX());
	}

	public void undoMove() {
		ball.setVelocityX(-1 * ball.getVelocityX());
	}
}
