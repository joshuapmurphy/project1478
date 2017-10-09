package project1478;
//The channel is made up of several slots. Since we simulate for 10 seconds, this is equivalent to 10s/20us = 500,000 slots.
//This will also house the main method for the simulation since all of the processing should happen here anyway.
public class Channel {
	public static final int NUM_SLOTS = 500000;
	private Slot[] slotChannel;
	private int currSlotIndex;
	private int frameSlotLength;
	private int lamdaA;
	private int lamdaC;
	private int collisions;
	private DataSource input1;
	private DataSource input2;
	
	private Node nodeA;
	private Node nodeC;
	
	private int NodeADIFS;
	private int NodeCDIFS;
	private int NodeAFrame;
	private int NodeCFrame;

	
	public Channel(int lamA, int lamC){
		slotChannel = new Slot[NUM_SLOTS];
		currSlotIndex = 0;
		collisions = 0;
		lamdaA = lamA;
		lamdaC = lamC;
		input1 = new DataSource(lamdaA);
		input2 = new DataSource(lamdaC);
		nodeA = new Node();
		nodeC = new Node();
		for(int i = 0; i < NUM_SLOTS; i++){
			slotChannel[i] = new Slot();
		}
		
		frameSlotLength = 103; //100 for frame + 1 for SIFS + 2 for ACK
		NodeADIFS = 2;
		NodeCDIFS = 2;
		NodeAFrame = 0;
		NodeCFrame = 0;
		
		nodeA.setNewBackoff(collisions);
		nodeC.setNewBackoff(collisions);
	}
	
	public void processSlot() {
		/*Here is the main bread and butter of the program. This function is the main part
		 * of the program that gets called 500,000 times, once for each slot.
		 * I'm guessing it will be a big if/else block that does different things
		 * based off of what it was doing previously
		 */
		if(input1.getSlotNumbers().get(0) == currSlotIndex){
			nodeA.addFrameToQueue();
			input1.getSlotNumbers().remove(0);
		}
		if(input2.getSlotNumbers().get(0) == currSlotIndex){
			nodeC.addFrameToQueue();
			input2.getSlotNumbers().remove(0);
		}
		if(!nodeC.isBusy()){
			if(!nodeA.isBusy() && nodeA.getFramesInQueue() > 0){
				if(NodeADIFS > 0){
					NodeADIFS--;
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeADIFS == 0 && nodeA.getBackOff() > 0){
					nodeA.decrementBackOff();
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeADIFS == 0 && nodeA.getBackOff() == 0){
					if(nodeC.getBackOff() == 0){
						collisions++;
						slotChannel[currSlotIndex].updateSlot("Collision");
						nodeA.setNewBackoff(collisions);
						nodeC.setNewBackoff(collisions);
						NodeADIFS = 2;
						NodeCDIFS = 2;
					} 
					else {
						nodeA.changeBusyStatus(true);
						NodeAFrame = frameSlotLength;
						slotChannel[currSlotIndex].updateSlot("NodeATx");
					}
				}
			}
			else if(nodeA.isBusy()){
				if(NodeAFrame > 0){
					NodeAFrame--;
					slotChannel[currSlotIndex].updateSlot("NodeATx");
				}
				else if(NodeAFrame == 0){
					nodeA.changeBusyStatus(false);
					slotChannel[currSlotIndex].updateSlot("Idle");
					nodeA.setNewBackoff(collisions);
					nodeC.setNewBackoff(collisions);
					NodeADIFS = 2;
					NodeCDIFS = 2;
				}
			}
		}
		if(!nodeA.isBusy() && !slotChannel[currSlotIndex].getSlotContent().equals("Collision")){
			if(!nodeC.isBusy() && nodeC.getFramesInQueue() > 0){
				if(NodeCDIFS > 0){
					NodeCDIFS--;
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeCDIFS == 0 && nodeC.getBackOff() > 0){
					nodeC.decrementBackOff();
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeCDIFS == 0 && nodeC.getBackOff() == 0){
					if(nodeA.getBackOff() == 0){
						System.out.println("Collision");
					} 
					else {
						nodeC.changeBusyStatus(true);
						NodeCFrame = frameSlotLength;
						slotChannel[currSlotIndex].updateSlot("NodeCTx");
					}
				}
			}
			else if(nodeC.isBusy()){
				if(NodeCFrame > 0){
					NodeCFrame--;
					slotChannel[currSlotIndex].updateSlot("NodeCTx");
				}
				else if(NodeCFrame == 0){
					nodeC.changeBusyStatus(false);
					slotChannel[currSlotIndex].updateSlot("Idle");
					nodeA.setNewBackoff(collisions);
					nodeC.setNewBackoff(collisions);
					NodeADIFS = 2;
					NodeCDIFS = 2;
				}
			}
		}

		currSlotIndex++;
}
	public int getCurrSlotIndex(){
		return currSlotIndex;
	}
	public void outputResults(String filename){
		for(int i = 0; i < NUM_SLOTS; i++){
			//if(slotChannel[i].getSlotContent().equals("Idle")) System.out.print(".");
			if(slotChannel[i].getSlotContent().equals("Collision")) System.out.print("C");
			//if(slotChannel[i].getSlotContent().equals("NodeATx")) System.out.print("a");
			//if(slotChannel[i].getSlotContent().equals("NodeCTx")) System.out.print("b");
			//if(i % 10000 == 0) System.out.println();
		}
	}
	
	public static void main(String[] args){
	
		String filename = args[0];
		int lamdaA = Integer.parseInt(args[1]);
		int lamdaC = Integer.parseInt(args[2]);
		Channel network = new Channel(lamdaA, lamdaC);
		
		for (int i = 0; i < NUM_SLOTS; i++){
			network.processSlot();
						
		}
		network.outputResults(filename);
	}
}

