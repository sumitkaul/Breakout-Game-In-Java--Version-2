package edu.indiana.p532.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TimePanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JTextField timeField;
	private JLabel timeLabel;

	private int timeDisplay;
	private int min = 0;
	private int sec = 0;

	ControlClass controlClass;

	public TimePanel() {
		timeField = new JTextField(5);
		timeField.setEditable(false);
		timeField.setFont(new Font("Times New Roman", Font.BOLD, 18));
		timeField.setText("00:00");
		timeField.setHorizontalAlignment(JTextField.CENTER);

		JPanel timePanel = new JPanel();
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setText("Clock: ");
		timePanel.add(timeLabel);
		timePanel.add(timeField);
		timePanel.setBackground(Color.BLACK);

		this.setLayout(new GridLayout(1, 1));
		this.add(timePanel);

	}

	public void timeRegister(ControlClass controlClass) {
		this.controlClass = controlClass;
		controlClass.registerObserver(this);
	}

	public void setTimeIncrease() {
		this.timeDisplay++;
	}

	public void setTimeDecrease() {
		this.timeDisplay--;
	}

	public void timeReset() {
		this.timeDisplay = 0;
	}

	public void update() {

		min = this.timeDisplay / 60;
		sec = this.timeDisplay % 60;
		timeField.setText(min + ":" + sec);
	}

}
