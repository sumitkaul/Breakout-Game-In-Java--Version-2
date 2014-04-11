package edu.indiana.p532.command;

import edu.indiana.p532.game.Ball;

public class BallMoveCommand implements Command{

	Ball ball;

	public BallMoveCommand(Ball ball) {
		this.ball = ball;
	}

	public void move() {
		ball.setX(ball.getX() + ball.getVelocityX());
		ball.setY(ball.getY() + ball.getVelocityY());
	}

	public void undoMove() {
		ball.setX(ball.getX() - ball.getVelocityX());
		ball.setY(ball.getY() - ball.getVelocityY());
	}
}
