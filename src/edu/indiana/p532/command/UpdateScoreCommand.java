package edu.indiana.p532.command;

import edu.indiana.p532.game.Score;


public class UpdateScoreCommand implements Command{

	Score score;

	public UpdateScoreCommand(Score score) {
		this.score = score;
	}

	public void move() {
		// Add score
		score.setScore(score.getScore() + 10);
	}

	public void undoMove() {
		// Subtract the score
		score.setScore(score.getScore() - 10);
	}
}
