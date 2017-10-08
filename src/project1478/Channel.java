package project1478;
//The channel is made up of several slots. Since we simulate for 10 seconds, this is equivalent to 10s/20us = 500,000 slots.
//This will also house the main method for the simulation since all of the processing should happen here anyway.
public class Channel {
	public static final int NUM_SLOTS = 500000;
	private Slot[] slotChannel;
	private int currSlotIndex;
	private int transmitCounter;
	private boolean transmitting;
	private int collisions;
	private DataSource input1;
	private DataSource input2;
	private Transmitter tx1;
	private Transmitter tx2;
	private Receiver rx1;
	private Receiver rx2;
	
	public Node nodeA;
	public Node nodeB;
	public Node nodeC;
	public Node nodeD;
	
	public Channel(){
		slotChannel = new Slot[NUM_SLOTS];
		currSlotIndex = 0;
		collisions = 0;
		transmitCounter = 10;  //NEED TO CALCULATE ACTUAL NUMBER
		transmitting = false;
		input1 = new DataSource();
		input2 = new DataSource();
		nodeA = new Node();
		nodeB = new Node();
		nodeC = new Node();
		nodeD = new Node();
		/*tx1 = new Transmitter();
		tx2 = new Transmitter();
		rx1 = new Receiver();
		rx2 = new Receiver();*/
	}
	
	public boolean processSlot() {
		/*Here is the main bread and butter of the program. This function is the main part
		 * of the program that gets called 500,000 times, once for each slot.
		 * I'm guessing it will be a big if/else block that does different things
		 * based off of what it was doing previously
		 */
		//check to see if any nodes are transmitting

		if (!A.checkBusy() && !B.checkBusy()) {

			if (A.getBackOff() == 0 && B.getBackOff() == 0) {
				collisions = collisions + 1;
				A.updateBackOff(false);
				B.updateBackOff(false);
			}

			else if (A.getBackOff() == 0) {
				//A controls channel and starts transmitting
				//start transmitting counter to indicate busy time
				transmitCounter = 10393;
				transmitting = true;
				A.changeBusyStatus();
			}

			else if (B.getBackOff() == 0) {
				//start transmitting counter to indicate busy time
				transmitCounter = 10393; //FIX ACTUAL NUMBER
				transmitting = true;
				B.changeBusyStatus();
			}

			else {
				//do nothing
			}

		}

		else if (A.checkBusy() && (B.getBackOff() == 0)) {
			//A collision occurred
			collisions = collisions + 1;
			//reset B's backoff
			B.updateBackOff(false);

		}

		else if (B.checkBusy() && (A.getBackOff() == 0)) {
			collisions = collisions + 1;
			A.updateBackOff(false);
		}

		else {
			//a node might be busy, but there are no conflicting transmissions
			//so... do nothing
		}
		
		//if they are, wait until transmission is done (i.e. 

		//collision happens if both back offs are 0 OR back off reaches 0 during transmission

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
		
		//we need to initialize the nodes.  Can we make them global?
		//Node nodeA = new Node(/*initialize with the random numbers*/);
		//Node nodeB = new Node(/*initialize with the random numbers*/);
		//Node nodeC = new Node(/*initialize with the random numbers*/);
		//Node nodeD = new Node(/*initialize with the random numbers*/);
		
		for (int i = 0; i < numberOfSlots; i++){
			network.processSlot();
			/*
			 * 
			 * 
			 * 
			 */
			
			//Nodes either need to be global variables or we need to re think our processSlot method.
			
			//update nodes
		}
		network.outputResults(filename);
	}
}

