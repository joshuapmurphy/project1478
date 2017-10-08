package project1478;

public class Node {
	
	//We can delete  vvv this right?  JOSH MURTLE TURTLE
	//private int collisions;
	private int backOff;
	private boolean busy;
	
	public int getBackOff(){
		return backOff;
	}
	
	public boolean checkBusy() {
		return busy;
	}
	
	public void updateBackOff(boolean inTransmission) {
		if (!inTransmission) {
			if (backOff == 0) {
				//JOSH MURTLE TURTLE update with new backoff value
			}
			else {
				--backOff;
			}
		}
		else {
			//give new back off value
		}
	}
	
	public void changeBusyStatus() {
		//JOSH MURTLE TURTLE is this ok syntax
		busy = !busy;
	}
	
}
