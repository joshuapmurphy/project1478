package project1478;

import java.util.ArrayList;

//This class will generate the times for frames to arrive at the transmitters for transmission.
public class DataSource {
	//may need some more instance variables here
	private ArrayList<Integer> slotNumbers;
	
	//This constructor should do all of the calculations for times that frames arrive right at the start of the program
	//In main(), we will transfer each item from this list into the transmitter queues at the appropriate times.
	public DataSource(int lamdaValue){
		//Generate the times for frames to show up and convert those to slot numbers and add them to the arraylist.
		slotNumbers = new ArrayList<Integer>();
		//This while loop generates a set of exponentially distributed numbers
		ArrayList<Double> exponentialNumbers = new ArrayList<Double>();
		double total = 0.0;
		while(total < 12.0){
			double value = -1 * (1/((double)lamdaValue)) * Math.log(1 - Math.random());
			exponentialNumbers.add(value);
			total += value;
		}
		
		//This for loop converts the exponential numbers from seconds to slots
		//Then it adds a slot number to the slotNumbers list using the exponentially distributed numbers of slots
		//as gaps between the slot numbers that it adds to the list.
		int currSlot = 0;
		for(int i = 0; i < exponentialNumbers.size(); i++){
			int value = (int)(exponentialNumbers.get(i) / .00002);
			currSlot += value;
			if(currSlot >= 510000) break;
			slotNumbers.add(currSlot);
		}
	}
	public ArrayList<Integer> getSlotNumbers(){
		return slotNumbers;
	}
}
