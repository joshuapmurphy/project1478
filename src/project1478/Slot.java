package project1478;
//The channel is made up of 500,000 of these and they will either be empty if nothing is send during this slot.
//Or they will be full. It will basically just store which event happened during this time slot.
public class Slot {
	private String content;
	
	public Slot(){
		content = "empty";
	}
	//Your choices for content to update with are:
	/*
	 * Idle
	 * NodeATx
	 * NodeCTx
	 * Collision
	 */
	public void updateSlot(String update){
		content = update;
	}
	public String getSlotContent(){
		return content;
	}
}
