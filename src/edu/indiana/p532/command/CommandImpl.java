package edu.indiana.p532.command;

public class CommandImpl {

	private Command command;

	public void setCommand(Command command) {
		this.command = command;
	}

	public void move() {
		command.move();
	}

	public void undoMove() {
		command.undoMove();
	}

}
