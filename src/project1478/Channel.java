package project1478;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
	private boolean collisionOccuredC;
	private boolean collisionOccuredA;
	private DataSource input1;
	private DataSource input2;
	
	private Node nodeA;
	private Node nodeC;
	
	private int NodeADIFS;
	private int NodeCDIFS;
	private int NodeAFrame;
	private int NodeCFrame;
	private int NodeARTS;
	private int NodeCRTS;
	private int AFramesSent;
	private int CFramesSent;
	
	private String projectPart;

	
	public Channel(int lamA, int lamC, String projectPart){
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
		AFramesSent = 0;
		CFramesSent = 0;
		NodeARTS = 0;
		NodeCRTS = 0;
		collisionOccuredC = false;
		collisionOccuredA = false;
		nodeA.setNewBackoff(collisions);
		nodeC.setNewBackoff(collisions);
		
		this.projectPart = projectPart;
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
		if(projectPart.equals("A1")) processA1();
		else if(projectPart.equals("A2")) processA2();
		else if(projectPart.equals("B1")) processB1();
		else if(projectPart.equals("B2")) processB2();
		
		currSlotIndex++;
	}
	
	//Does the processing for Arrangement A, implementation 1
	private void processA1(){
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
					AFramesSent++;
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
					nodeC.changeBusyStatus(true);
					NodeCFrame = frameSlotLength;
					slotChannel[currSlotIndex].updateSlot("NodeCTx");
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
					CFramesSent++;
				}
			}
		}
	}
	
	//Does the processing for Arrangement A, implementation 2
	private void processA2(){
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
						NodeAFrame = frameSlotLength + 5;
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
					AFramesSent++;
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
					nodeC.changeBusyStatus(true);
					NodeCFrame = frameSlotLength + 5;
					slotChannel[currSlotIndex].updateSlot("NodeCTx");
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
					CFramesSent++;
				}
			}
		}
	}
	
	//Does the processing for Arrangement B, Implementation 1
	private void processB1(){
		if(collisionOccuredC) collisionOccuredA = true;
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
				nodeA.changeBusyStatus(true);
				NodeAFrame = frameSlotLength;
				slotChannel[currSlotIndex].updateSlot("NodeATx");
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
				NodeADIFS = 2;
				if(!collisionOccuredA){
					AFramesSent++;
				}
				collisionOccuredA = false;
			}
		}
		
		
		if(!nodeC.isBusy() && nodeC.getFramesInQueue() > 0){
			if(NodeCDIFS > 0){
				NodeCDIFS--;
				if(!nodeA.isBusy()){
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
			}
			else if(NodeCDIFS == 0 && nodeC.getBackOff() > 0){
				nodeC.decrementBackOff();
				if(!nodeA.isBusy()){
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
			}
			else if(NodeCDIFS == 0 && nodeC.getBackOff() == 0){
				nodeC.changeBusyStatus(true);
				NodeCFrame = frameSlotLength;
				if(nodeA.isBusy()){
					collisionOccuredC = true;
				}
				else{
					slotChannel[currSlotIndex].updateSlot("NodeCTx");
				}
			}
		}
		else if(nodeC.isBusy()){
			if(NodeCFrame > 0 && !nodeA.isBusy()){
				NodeCFrame--;
				slotChannel[currSlotIndex].updateSlot("NodeCTx");
			}
			else if(NodeCFrame > 0 && nodeA.isBusy()){
				NodeCFrame--;
				collisionOccuredC = true;
			}
			else if(NodeCFrame == 0){
				nodeC.changeBusyStatus(false);
				slotChannel[currSlotIndex].updateSlot("Idle");
				nodeC.setNewBackoff(collisions);
				NodeCDIFS = 2;
				if(!collisionOccuredC){
					CFramesSent++;
				}
				else{
					collisions++;
				}
				collisionOccuredC = false;
			}
		}
		
	}
	
	//Does the processing for Arrangement B, implementation 2
	private void processB2(){
		if(!nodeC.isBusy() && !nodeC.RTS()){
			if(!nodeA.isBusy() && nodeA.getFramesInQueue() > 0){
				if(NodeADIFS > 0){
					NodeADIFS--;
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeADIFS == 0 && nodeA.getBackOff() > 0){
					nodeA.decrementBackOff();
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeADIFS == 0 && nodeA.getBackOff() == 0 && !nodeA.RTS()){
					if(nodeC.getBackOff() == 0){
						collisions++;
						slotChannel[currSlotIndex].updateSlot("Collision");
						nodeA.setNewBackoff(collisions);
						nodeC.setNewBackoff(collisions);
						NodeADIFS = 2;
						NodeCDIFS = 2;
					} 
					else {
						nodeA.setRTS(true);
						NodeARTS = 2;
					}
				}
				else if(NodeADIFS == 0 && nodeA.getBackOff() == 0 && nodeA.RTS()){
					if(NodeARTS > 0){
						NodeARTS--;
						slotChannel[currSlotIndex].updateSlot("Idle");
					}
					else if(NodeARTS == 0){
						nodeA.setRTS(false);
						nodeA.changeBusyStatus(true);
						NodeAFrame = frameSlotLength + 3;
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
					AFramesSent++;
				}
			}
		}
		if(!nodeA.isBusy() && !nodeA.RTS() && !slotChannel[currSlotIndex].getSlotContent().equals("Collision")){
			if(!nodeC.isBusy() && nodeC.getFramesInQueue() > 0){
				if(NodeCDIFS > 0){
					NodeCDIFS--;
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeCDIFS == 0 && nodeC.getBackOff() > 0){
					nodeC.decrementBackOff();
					slotChannel[currSlotIndex].updateSlot("Idle");
				}
				else if(NodeCDIFS == 0 && nodeC.getBackOff() == 0 && !nodeC.RTS()){
					nodeC.setRTS(true);
					NodeCRTS = 2;
				}
				else if(NodeCDIFS == 0 && nodeC.getBackOff() == 0 && nodeC.RTS()){
					if(NodeCRTS > 0){
						NodeCRTS--;
						slotChannel[currSlotIndex].updateSlot("Idle");
					}
					else if(NodeCRTS == 0){
						nodeC.setRTS(false);
						nodeC.changeBusyStatus(true);
						NodeCFrame = frameSlotLength + 3;
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
					CFramesSent++;
				}
			}
		}
	}
	public void outputResults(String filename) throws IOException{
		int totalASlots = 0, totalCSlots = 0;
	    
	    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    
		
		for(int i = 0; i < NUM_SLOTS; i++){
			
			if(slotChannel[i].getSlotContent().equals("NodeATx")){
				totalASlots++;
			}
			if(slotChannel[i].getSlotContent().equals("NodeCTx")) {
				totalCSlots++;
			}
			
			
		}
		writer.write("Collisions: " + collisions + "\n");
		writer.write("Throughput: " + (((AFramesSent + CFramesSent)*1.5*8)/10) + " Kbps\n");
	    writer.write("Fairness Index: " + (totalASlots * 1.0)/(totalCSlots * 1.0) + "\n");
	    writer.close();
		
	}
	
	public static void main(String[] args){
	
		
		Channel network = new Channel(50, 50, "A1");
		runSimulation(network, "A1_50");
		network = new Channel(100, 100, "A1");
		runSimulation(network, "A1_100");
		network = new Channel(200, 200, "A1");
		runSimulation(network, "A1_200");
		network = new Channel(300, 300, "A1");
		runSimulation(network, "A1_300");
		
		network = new Channel(50, 50, "A2");
		runSimulation(network, "A2_50");
		network = new Channel(100, 100, "A2");
		runSimulation(network, "A2_100");
		network = new Channel(200, 200, "A2");
		runSimulation(network, "A2_200");
		network = new Channel(300, 300, "A2");
		runSimulation(network, "A2_300");

		network = new Channel(50, 50, "B1");
		runSimulation(network, "B1_50");
		network = new Channel(100, 100, "B1");
		runSimulation(network, "B1_100");
		network = new Channel(200, 200, "B1");
		runSimulation(network, "B1_200");
		network = new Channel(300, 300, "B1");
		runSimulation(network, "B1_300");
	
		network = new Channel(50, 50, "B2");
		runSimulation(network, "B2_50");
		network = new Channel(100, 100, "B2");
		runSimulation(network, "B2_100");
		network = new Channel(200, 200, "B2");
		runSimulation(network, "B2_200");
		network = new Channel(300, 300, "B2");
		runSimulation(network, "B2_300");
		
	}
	public static void runSimulation(Channel network, String filename){
		for (int i = 0; i < NUM_SLOTS; i++){
			network.processSlot();		
		}
		try {
			network.outputResults(filename + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

