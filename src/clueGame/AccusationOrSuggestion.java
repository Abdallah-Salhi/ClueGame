package clueGame;

/*
* Accusation:
* Represents accusation and suggestion objects to store information when an accusation occurs. Necessary for modularization and for ease in access.
* 
* Authors/Contributors:
* Abdallah Salhi
* Montgomery Hughes
*/
public class AccusationOrSuggestion {

	private Player accuser;
	private Card person;
	private Card weapon;
	private Card room;
	
	// Main constructor, which sets information about the accusation and who made it
	public AccusationOrSuggestion(Player accuser, Card person, Card weapon, Card room) {
		this.accuser = accuser;
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	// Getters and setters 
	
	public Player getSuggestor() {
		return accuser;
	}
	
	public Card getPerson() {
		return person;
	}
	
	public Card getWeapon() {
		return weapon;
	}
	public Card getRoom() {
		return room;
	}
	
}
	