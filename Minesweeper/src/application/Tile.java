package application;

public class Tile {

	private boolean isBomb;
	private int nextTo;
	
	public Tile() {
		this.isBomb = false;
		this.nextTo = 0;
	}
	
	public int getNextTo() {
		return nextTo;
	}
	
	public void setNextTo(int nextTo) {
		this.nextTo = nextTo;
	}
	
	public void incrementNextTo() {
		nextTo++;
	}
	
	public boolean getIsBomb() {
		return isBomb;
	}
	
	public void setIsBomb(boolean isBomb) {
		this.isBomb = isBomb;
	}
	
}
