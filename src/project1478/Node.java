package project1478;

public class Node {
	
	private int backOff;
	private boolean busy;
	private int framesInQueue;
	
	public Node(){
		backOff = 0;
		busy = false;
		framesInQueue = 0;
	}
	public void addFrameToQueue(){
		framesInQueue++;
	}
	public int getFramesInQueue(){
		return framesInQueue;
	}
	public void removeFrameFromQueue(){
		framesInQueue--;
	}
	public void setNewBackoff(int coll){
		int CWvalue = ((int)Math.pow(2, coll)) * 4 - 1;
		backOff = (int) (Math.random() * CWvalue);
	}
	
	public int getBackOff(){
		return backOff;
	}
	public void decrementBackOff(){
		backOff--;
	}
	public boolean isBusy() {
		return busy;
	}

	public void changeBusyStatus(boolean business) {
		busy = business;
	}
	
}
