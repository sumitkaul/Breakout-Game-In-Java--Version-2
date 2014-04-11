package edu.indiana.p532.game;

import java.util.Random;

/**
 * This class contains all the static variables that are used throughout the
 * game
 */

public class Constants {

	public static final int WINDOW_LENGTH = 500;
	public static final int WINDOW_WIDTH = 600;
	public static final int PADDLE_WIDTH = 100;
	public static final int PADDLE_LENGTH = 4;
	public static final int BRICK_WIDTH = 20;
	public static final int BRICK_LENGTH = 60;
	public static final int PADDLE_OFFSET = 10;
	public static final int BALL_SIZE = 20;
	public static final int PADDLE_PRECISION = 30;
	public static final int BOARD_LENGTH = 360;
	public static final int BOARD_WIDTH = 550;
	public static final int BOARD_OFFSET = 0;
	public static final int BRICK_COUNT = 10;
	public static final int TIMER_DELAY = 10;

	int paddle_xvalue;
	int paddle_yvalue;
	int ball_xvalue;
	int ball_yvalue;
	int ball_xvelocity;
	int ball_yvelocity;

	public int generateNumber(int lowerbound, int upperbound) {
		Random random = new Random();
		return random.nextInt(upperbound - lowerbound + 1) + lowerbound;
	}

	public void generateRandomStartingPositions() {
		setPaddle_xvalue(generateNumber(1, 280));
		setPaddle_yvalue(550);
		setBall_xvalue(generateNumber(70, 185));
		setBall_yvalue(generateNumber(70, 400));
		int direction = generateNumber(1, 4);
		switch (direction) {
		case 1:
			setBall_xvelocity(3);
			setBall_yvelocity(3);
			break;
		case 2:
			setBall_xvelocity(3);
			setBall_yvelocity(-3);
			break;
		case 3:
			setBall_xvelocity(-3);
			setBall_yvelocity(3);
			break;
		case 4:
			setBall_xvelocity(-3);
			setBall_yvelocity(-3);
			break;
		default:
			setBall_xvelocity(3);
			setBall_yvelocity(-3);
		}
	}

	public int getPaddle_xvalue() {
		return paddle_xvalue;
	}

	public void setPaddle_xvalue(int paddle_xvalue) {
		this.paddle_xvalue = paddle_xvalue;
	}

	public int getPaddle_yvalue() {
		return paddle_yvalue;
	}

	public void setPaddle_yvalue(int paddle_yvalue) {
		this.paddle_yvalue = paddle_yvalue;
	}

	public int getBall_xvalue() {
		return ball_xvalue;
	}

	public void setBall_xvalue(int ball_xvalue) {
		this.ball_xvalue = ball_xvalue;
	}

	public int getBall_yvalue() {
		return ball_yvalue;
	}

	public void setBall_yvalue(int ball_yvalue) {
		this.ball_yvalue = ball_yvalue;
	}

	public int getBall_xvelocity() {
		return ball_xvelocity;
	}

	public void setBall_xvelocity(int ball_xvelocity) {
		this.ball_xvelocity = ball_xvelocity;
	}

	public int getBall_yvelocity() {
		return ball_yvelocity;
	}

	public void setBall_yvelocity(int ball_yvelocity) {
		this.ball_yvelocity = ball_yvelocity;
	}
}
