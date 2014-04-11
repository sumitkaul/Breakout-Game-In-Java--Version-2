package edu.indiana.p532.command;

import edu.indiana.p532.game.Constants;
import edu.indiana.p532.game.Paddle;

public class PaddleMoveRightCommand implements Command{

	Paddle paddle;

	public PaddleMoveRightCommand(Paddle paddle) {
		this.paddle = paddle;
	}

	public void move() {
		if (paddle.getX() + Constants.PADDLE_WIDTH < Constants.BOARD_LENGTH) {
			paddle.setX(paddle.getX() + Constants.PADDLE_PRECISION);
		}
	}

	public void undoMove() {
		paddle.setX(paddle.getX() - Constants.PADDLE_PRECISION);
	}
}
