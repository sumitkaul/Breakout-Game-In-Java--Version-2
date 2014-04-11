package edu.indiana.p532.game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import edu.indiana.p532.command.BallChangeXDirectionCommand;
import edu.indiana.p532.command.BallChangeYDirectionCommand;
import edu.indiana.p532.command.BallMoveCommand;
import edu.indiana.p532.command.BrickRemoveCommand;
import edu.indiana.p532.command.CommandImpl;
import edu.indiana.p532.command.PaddleMoveLeftCommand;
import edu.indiana.p532.command.PaddleMoveRightCommand;
import edu.indiana.p532.command.UpdateScoreCommand;
import edu.indiana.p532.command.UpdateTimeCommand;

/**
 * This is the class where the actual logic of the game is implemented. The
 * control of the class is given to the key listener and action listener to
 * interact with the game interface.
 * 
 * @param start
 *            object for the class to implement start button
 * @param pause
 *            object for the class to implement pause/restart button
 */

public class ControlClass extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int TIMER_DELAY = Constants.TIMER_DELAY;
	private int time = 0;
	/**
	 * @param args
	 */
	private Button start;
	private Button pause;
	private Button undo;
	private Button replay;

	private Board board;
	private List<Brick> bricksList;
	private Ball ball;
	private Paddle paddle;
	private Score score;
	private ScoreBoard scorePanel;
	private Constants constant;
	private TimePanel timePanel;
	private TimerHandler daemonThread;
	private ButtonHandler actionHandler;
	private Timer daemon;
	private Timer undoer;
	private Timer player;
	private List<CommandImpl> commandList;
	private List<CommandImpl> replayCommandList;
	private ArrayList<Observer> observers;

	public ControlClass(ScoreBoard scorePanel, Board board,
			List<Brick> bricksList, Ball ball, Paddle paddle,
			TimePanel timePanel, Constants constant) {
		this.board = board;
		this.bricksList = bricksList;
		this.ball = ball;
		this.paddle = paddle;
		this.scorePanel = scorePanel;
		this.constant = constant;
		this.timePanel = timePanel;
		this.score = new Score(0);
		observers = new ArrayList<Observer>();

		daemonThread = new TimerHandler();

		actionHandler = new ButtonHandler();

		board.addKeyListener(new PaddleMovementListener());
		board.setFocusable(true);

		daemon = new Timer(TIMER_DELAY, daemonThread);
		undoer = new Timer(TIMER_DELAY, new StateRecorder());
		player = new Timer(TIMER_DELAY, new Player());
		commandList = new ArrayList<CommandImpl>();
		replayCommandList = new ArrayList<CommandImpl>();

		start = new Button("START/RESTART");
		pause = new Button("PAUSE/RESUME");
		undo = new Button("UNDO");
		replay = new Button("REPLAY");

		start.addMouseListener(actionHandler);
		pause.addMouseListener(actionHandler);
		undo.addMouseListener(actionHandler);
		replay.addMouseListener(actionHandler);

		this.setLayout(new GridLayout(4, 1));
		this.add(start);
		this.add(pause);
		this.add(undo);
		this.add(replay);

	}

	public void registerObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(i);
		}
	}

	public void notifyObservers() {
		for (int i = 0; i < observers.size(); i++) {
			Observer observer = observers.get(i);
			observer.update();
		}
	}

	/**
	 * This class is use to listen to the buttons on the applet interface
	 * 
	 * @param brickCount
	 *            this variable contains the number of bricks in the array
	 * @param score
	 *            this variable contains the score of the player
	 */
	class ButtonHandler implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == start) {

				if (player.isRunning()) {
					player.stop();

				}
				daemon.start();

				resetAll();
				board.requestFocus(true);
				board.draw();
				commandList = new ArrayList<CommandImpl>();

			} else if (e.getSource() == pause) {
				if (daemon.isRunning()) {
					daemon.stop();
					board.draw();
					board.requestFocus(true);

				} else {
					board.requestFocus(true);
					board.draw();
					daemon.start();
				}

			} else if (e.getSource() == replay) {
				if (daemon.isRunning()) {
					daemon.stop();
					undoer.stop();

				}

				resetAll();
				replayCommandList.clear();
				replayCommandList.addAll(commandList);
				player.start();
			}

		}

		private void resetAll() {
			timePanel.timeReset();
			score.setScore(0);
			scorePanel.setScore("" + score.getScore());
			for (Brick brick : bricksList) {
				brick.setHit(false);
			}

			ball.setX(constant.getBall_xvalue());
			ball.setVelocityX(constant.getBall_xvelocity());
			ball.setVelocityY(constant.getBall_yvelocity());
			ball.setY(constant.getBall_yvalue());
			paddle.setX(constant.getPaddle_xvalue());
			paddle.setY(constant.getPaddle_yvalue());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getSource() == undo) {
				if (daemon.isRunning()) {
					daemon.stop();
				}
				undoer.start();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == undo) {
				if (daemon.isRunning()) {
					daemon.stop();
				}
				if (undoer.isRunning()) {
					undoer.stop();
				}
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// Do Nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// Do Nothing
		}
	}

	/**
	 * This class implements serializable object and substitutes the threading
	 * logic. The board is repainted and the collision is checked at every run.
	 */

	class TimerHandler implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			// time is increased once timer begins
			time++;

			if (time % 100 == 0) {
				CommandImpl command = new CommandImpl();
				UpdateTimeCommand updatetime = new UpdateTimeCommand(timePanel);
				command.setCommand(updatetime);
				command.move();
				commandList.add(command);
			}

			checkCollision();
			notifyObservers();
			CommandImpl command = new CommandImpl();
			BallMoveCommand ballMoveCmd = new BallMoveCommand(ball);
			command.setCommand(ballMoveCmd);
			command.move();
			commandList.add(command);
			board.draw();
		}
	}

	class StateRecorder implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (!commandList.isEmpty()) {
				CommandImpl command = commandList.get(commandList.size() - 1);
				command.undoMove();
				commandList.remove(command);
				notifyObservers();
				scorePanel.setScore(score.getScore() + "");
				board.requestFocus(true);
				board.draw();
			} else {
				undoer.stop();
			}
		}
	}

	class Player implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (!replayCommandList.isEmpty()) {
				CommandImpl command = replayCommandList.get(0);
				command.move();
				replayCommandList.remove(command);
				notifyObservers();
				scorePanel.setScore(score.getScore() + "");
				board.requestFocus(true);
				board.draw();
			} else {
				player.stop();
			}
		}
	}

	/**
	 * This class listens to the keyboard input and takes care of the paddle
	 * movement
	 */
	public class PaddleMovementListener implements KeyListener {

		public void keyPressed(KeyEvent event) {
			CommandImpl command = new CommandImpl();
			switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:

				PaddleMoveLeftCommand paddleMoveLeftCmd = new PaddleMoveLeftCommand(
						paddle);
				command.setCommand(paddleMoveLeftCmd);
				command.move();
				commandList.add(command);
				break;

			case KeyEvent.VK_RIGHT:

				PaddleMoveRightCommand paddleMoveRightCmd = new PaddleMoveRightCommand(
						paddle);
				command.setCommand(paddleMoveRightCmd);
				command.move();
				commandList.add(command);
				break;

			}

		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

	}

	/**
	 * This function play sounds on different events in the game
	 */
	private void playSound(String fileName) {
		AudioClip clip = Applet.newAudioClip(getClass().getClassLoader()
				.getResource(fileName));
		clip.play();
	}

	/**
	 * This function implements the logic to detect the collision between
	 * objects during the play of the game
	 */
	private void checkCollision() {

		if (daemon.isRunning()) {
			// Check the collision between left and right walls

			if (ball.getX() <= Constants.BOARD_OFFSET
					|| (ball.getX() + Constants.BALL_SIZE) > (Constants.BOARD_LENGTH)) {
				CommandImpl command = new CommandImpl();
				BallChangeXDirectionCommand ballMoveCmd = new BallChangeXDirectionCommand(
						ball);
				command.setCommand(ballMoveCmd);
				command.move();
				commandList.add(command);
			}

			// Checking for collision with the top wall

			if (ball.getY() <= Constants.BOARD_OFFSET) {
				CommandImpl command = new CommandImpl();
				BallChangeYDirectionCommand ballMoveCmd = new BallChangeYDirectionCommand(
						ball);
				command.setCommand(ballMoveCmd);
				command.move();
				commandList.add(command);
			}

			// Game over logic

			if (ball.getY() > Constants.WINDOW_WIDTH) {
				playSound("edu/indiana/p532/sound/smb_gameover.wav");
				daemon.stop();
				JOptionPane.showMessageDialog(null,
						"Game Over\nScore:" + score.getScore());

			}

			// Check to see if ball touches the paddle

			if (((ball.getX() + Constants.BALL_SIZE) >= paddle.getX())
					&& (ball.getX() <= paddle.getX() + Constants.PADDLE_WIDTH)) {
				if (((ball.getY() + Constants.BALL_SIZE) >= paddle.getY())
						&& ((ball.getY() + Constants.BALL_SIZE) <= (paddle
								.getY() + Constants.PADDLE_WIDTH))) {
					CommandImpl command = new CommandImpl();
					BallChangeYDirectionCommand ballMoveCmd = new BallChangeYDirectionCommand(
							ball);
					command.setCommand(ballMoveCmd);
					command.move();
					commandList.add(command);

					playSound("edu/indiana/p532/sound/bounce.au");
				}
			}

			// Check for a collision with bricks

			boolean winnerFlag = true;
			for (Brick brick : bricksList) {
				if (!brick.isHit()) {
					if ((ball.getX() <= (brick.getX() + Constants.BRICK_LENGTH))
							&& (ball.getX() + Constants.BALL_SIZE) >= brick
									.getX()) {

						if ((ball.getY() <= (brick.getY() + Constants.BRICK_WIDTH))
								&& ((ball.getY() + Constants.BALL_SIZE) >= brick
										.getY())) {

							CommandImpl command = new CommandImpl();
							UpdateScoreCommand scoreUpdateCmd = new UpdateScoreCommand(
									score);
							command.setCommand(scoreUpdateCmd);
							command.move();
							commandList.add(command);
							scorePanel.setScore("" + score.getScore());
							playSound("edu/indiana/p532/sound/Brick.au");

							// Check for lateral collision with bricks
							if (((ball.getX() + Constants.BALL_SIZE - ball
									.getVelocityX()) <= brick.getX())
									|| ((ball.getX() - ball.getVelocityX()) >= (brick
											.getX() + Constants.BRICK_LENGTH))) {
								command = new CommandImpl();
								BallChangeXDirectionCommand ballMoveCmd = new BallChangeXDirectionCommand(
										ball);
								command.setCommand(ballMoveCmd);
								command.move();
								commandList.add(command);
							} else {
								command = new CommandImpl();
								BallChangeYDirectionCommand ballMoveCmd = new BallChangeYDirectionCommand(
										ball);
								command.setCommand(ballMoveCmd);
								command.move();
								commandList.add(command);

							}
							command = new CommandImpl();
							BrickRemoveCommand brickRemoveCmd = new BrickRemoveCommand(
									bricksList, brick);
							command.setCommand(brickRemoveCmd);
							command.move();
							commandList.add(command);
						}
					}
				}
				winnerFlag = winnerFlag && brick.isHit();
			}
			if (winnerFlag) {
				playSound("edu/indiana/p532/sound/applause.wav");
				daemon.stop();
				JOptionPane.showMessageDialog(null, "Congratulations\nScore:"
						+ score.getScore());

			}

		}
	}
}
