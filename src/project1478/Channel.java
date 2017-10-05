package project1478;
//The channel is made up of several slots. Since we simulate for 10 seconds, this is equivalent to 10s/20us = 500,000 slots.
//This will also house the main method for the simulation since all of the processing should happen here anyway.
public class Channel {
	public static final int NUM_SLOTS = 500000;
	private Slot[] slotChannel;
	private int currSlotIndex;
	private DataSource input;
	private Transmitter tx1;
	private Transmitter tx2;
	private Receiver rx1;
	private Receiver rx2;
	
	public Channel(){
		slotChannel = new Slot[NUM_SLOTS];
		currSlotIndex = 0;
		input = new DataSource();
		tx1 = new Transmitter();
		tx2 = new Transmitter();
		rx1 = new Receiver();
		rx2 = new Receiver();
	}
	
	public boolean processSlot(){
		/*Here is the main bread and butter of the program. This function is the main part
		 * of the program that gets called 500,000 times, once for each slot.
		 * I'm guessing it will be a big if/else block that does different things
		 * based off of what it was doing previously
		*/
		currSlotIndex++;
		return false;
	}
	public int getCurrSlotIndex(){
		return currSlotIndex;
	}
	public boolean outputResults(String filename){
		//Here we will output the results either to a text file to use in matlab
		//or just using a Java GUI.
		//Haven't decided which will be easier.
		
		return false;
	}
	
	public static void main(String[] args){
		final int numberOfSlots = 500000;
		String filename = args[0];
		Channel network = new Channel();
		
		for (int i = 0; i < numberOfSlots; i++){
			network.processSlot();
		}
		network.outputResults(filename);
	}
}

