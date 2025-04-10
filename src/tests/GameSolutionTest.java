package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;


class GameSolutionTest {

	private static Board board;
	
	private static Card potterCard;
	private static Card malfoyCard;
	private static Card grangerCard;
	private static Card weasleyCard;


	private static Card wandCard;
	private static Card potionCard;
	private static Card swordCard;
	private static Card bookCard;


	private static Card slytherinCard;
	private static Card greatHallCard;
	private static Card chamberOfSecCard;
	private static Card hufflepuffCard;

	
	private static Solution solution;
	private static Suggestion suggestion;
	private static ArrayList<Player> players;

	private static HumanPlayer player1;
	private static ComputerPlayer player2;
	private static ComputerPlayer player3;
	
	private static ComputerPlayer randomPlayer;


	

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance(); // Assuming singleton
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt"); // Set your actual file names
		board.initialize(); // Loads rooms, players, weapons, cards

		potterCard = new Card("HarryPotter", CardType.PERSON);
		malfoyCard = new Card("Draco Malfoy", CardType.PERSON);
		grangerCard = new Card("Hermione Granger", CardType.PERSON);
		weasleyCard = new Card("Ron Weasley", CardType.PERSON);
		
		
		wandCard = new Card("Wand", CardType.WEAPON);
		potionCard = new Card("Potion", CardType.WEAPON);
		swordCard = new Card("Potion", CardType.WEAPON);
		bookCard = new Card("Book", CardType.WEAPON);


		slytherinCard = new Card("Slytherin", CardType.ROOM);
		greatHallCard = new Card("Great Hall", CardType.ROOM);
		chamberOfSecCard = new Card("Chamber of Secrets", CardType.ROOM);
		hufflepuffCard = new Card("Hufflepuff", CardType.ROOM);

		
		solution = new Solution(potterCard, wandCard, slytherinCard);

		player1 = new HumanPlayer("Harry Potter", Color.BLUE , 10 , 25);
		player2 = new ComputerPlayer("Draco Malfoy", Color.RED , 15 , 20);
		player3 = new ComputerPlayer("Ron Weasley", Color.BLACK , 18 , 18);
		
		randomPlayer = new ComputerPlayer("Random", Color.cyan, 5,5);
		
		players = new ArrayList<Player>();
		players.add(randomPlayer);
		players.add(player1);
		players.add(player2);
		players.add(player3);

		


	}

	@Test
	void checkAccusationTest() {
		HashSet<Card> accusation = new HashSet<>();
		
		//solution that is correct
		accusation.add(potterCard);
		accusation.add(wandCard);
		accusation.add(slytherinCard);
		
		boolean check = board.checkAccusation(accusation, solution);
		assertTrue(check);
		accusation.clear();
		
		//solution that has wrong person
		accusation.add(malfoyCard);
		accusation.add(wandCard);
		accusation.add(slytherinCard);
		
		check = board.checkAccusation(accusation, solution);
		assertFalse(check);
		accusation.clear();
		
		//solution that has wrong weapon
		accusation.add(potterCard);
		accusation.add(potionCard);
		accusation.add(slytherinCard);
		
		check = board.checkAccusation(accusation, solution);
		assertFalse(check);
		accusation.clear();
		
		//solution that has wrong room
		accusation.add(potterCard);
		accusation.add(wandCard);
		accusation.add(greatHallCard);
		
		check = board.checkAccusation(accusation, solution);
		assertFalse(check);
		accusation.clear();


	}

	@Test
	void disproveSuggestionTest() {
		suggestion = new Suggestion(randomPlayer, malfoyCard, wandCard, greatHallCard);
		
		ArrayList<Card> validList = new ArrayList<>();
		validList.add(malfoyCard);
		validList.add(wandCard);
		validList.add(greatHallCard);
		
		
		//if player has only 1 matching card
		player1.resetHand();
		player1.giveCard(greatHallCard);
		player1.giveCard(potterCard); //test if wrong card is returned
		
		Card disproveCard1 = player1.disproveSuggestion(suggestion); //run function
		assertEquals(disproveCard1,greatHallCard);
		assertNotEquals(disproveCard1,potterCard);
		player1.resetHand();
		
		//if player has more than one matching card 
		player1.giveCard(greatHallCard);
		player1.giveCard(potterCard); //test if wrong card is returned
		player1.giveCard(wandCard);
		player1.giveCard(malfoyCard);
		
		for(int i = 0; i < 3; i++) { // test several times to ensure random functionality is working 
			
			Card disproveCard2 = player1.disproveSuggestion(suggestion);			
			assertTrue(validList.contains(disproveCard2)); //return true if returned card is expected and will fail if any other card is returned
			
		}
	
		player1.resetHand();
		
		// return null if player has no matching cards
		player1.giveCard(potterCard);
		player1.giveCard(potionCard);
		player1.giveCard(slytherinCard);
		
		Card disproveCard3 = player1.disproveSuggestion(suggestion);			
		assertNull(disproveCard3);
		player1.resetHand();


		
	}

	@Test
	void handleSuggestionTest() {
		
		//board.getPlayers();
		
		player1.giveCard(malfoyCard);
		player1.giveCard(swordCard);
		player1.giveCard(chamberOfSecCard);
		
		player2.giveCard(grangerCard);
		player2.giveCard(wandCard);
		player2.giveCard(slytherinCard);
		
		player3.giveCard(potterCard);
		player3.giveCard(potionCard);
		player3.giveCard(greatHallCard);

		// Suggestion no one can disprove returns null
		suggestion = new Suggestion(randomPlayer, weasleyCard,  bookCard, hufflepuffCard);
		Card card = board.handleSuggestion(suggestion, players);
		assertNull(card);
		
		
		// Suggestion only suggesting player can disprove returns null
		suggestion = new Suggestion(player2, weasleyCard, wandCard, hufflepuffCard);
		card = board.handleSuggestion(suggestion, players);
		assertNull(card);
		
		
		// Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
		suggestion = new Suggestion(randomPlayer, weasleyCard, swordCard, hufflepuffCard);
		card = board.handleSuggestion(suggestion, players);
		assertNotNull(card);
		assertEquals(card,swordCard);
		
		// Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
		suggestion = new Suggestion(randomPlayer, potterCard, bookCard, slytherinCard); //player2 and player 3 can disprove but player 2 should be next in list
		card = board.handleSuggestion(suggestion, players);
		assertNotNull(card);
		assertEquals(card,slytherinCard);
		

	}

}
