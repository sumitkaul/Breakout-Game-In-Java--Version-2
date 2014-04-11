package edu.indiana.p532.game;

public interface Observable {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers();
}
