package edu.indiana.p532.game;

import java.util.List;


public class State {

	private Ball ballstate;
	private List<Brick> bricksListState;
	private Paddle paddleState;
	private int scoreState;
	//private List<State> statesList;

	public State(Ball ballstate, List<Brick> bricksListState, Paddle paddleState,
			int scoreState) {
		this.ballstate = ballstate;
		this.bricksListState = bricksListState;
		this.paddleState = paddleState;
		this.scoreState = scoreState;
	}

	public Ball getBallstate() {
		return ballstate;
	}

	public void setBallstate(Ball ballstate) {
		this.ballstate = ballstate;
	}

	public List<Brick> getBricksListState() {
		return bricksListState;
	}

	public void setBricksListState(List<Brick> bricksListState) {
		this.bricksListState = bricksListState;
	}

	public Paddle getPaddleState() {
		return paddleState;
	}

	public void setPaddleState(Paddle paddleState) {
		this.paddleState = paddleState;
	}

	public int getScoreState() {
		return scoreState;
	}

	public void setScoreState(int scoreState) {
		this.scoreState = scoreState;
	}

/*	public List<State> getStatesList() {
		return statesList;
	}

	public void setStatesList(List<State> statesList) {
		this.statesList = statesList;
	}

	public void addState(State state) {
		this.statesList.add(state);
	}

	public void removeState(State state) {
		this.statesList.remove(state);
	}*/


}

