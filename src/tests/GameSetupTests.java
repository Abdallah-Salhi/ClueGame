package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Player;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

public class GameSetupTests {

	private static Board board;
	
    @BeforeAll
    public static void setUp() {
        board = Board.getInstance(); // Assuming singleton
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt"); // Set your actual file names
        board.initialize(); // Loads rooms, players, weapons, cards
    }

    
    // Check that the correct number of players are loaded and types are correct
    @Test
    public void testPlayerLoading() {
        List<Player> players = board.getPlayers();
        assertEquals(6, players.size()); // 1 human, 5 computer

        int humanCount = 0, computerCount = 0;
        for (Player p : players) {
            if (p instanceof HumanPlayer) humanCount++;
            else if (p instanceof ComputerPlayer) computerCount++;
        }

        assertEquals(1, humanCount);
        assertEquals(5, computerCount);
    }
    
    // Deck contains the right number and types of cards
    @Test
    public void testDeckIntegrity() {
        Set<Card> deck = board.getDeck();
        assertEquals(21, deck.size()); // 6 people + 6 weapons + 9 rooms

        int people = 0, weapons = 0, rooms = 0;
        for (Card c : deck) {
            switch (c.getType()) {
                case PERSON -> people++;
                case WEAPON -> weapons++;
                case ROOM -> rooms++;
            }
        }

        assertEquals(6, people);
        assertEquals(6, weapons);
        assertEquals(9, rooms);
    }
    

    // Solution has one card of each type
    @Test
    public void testSolutionValid() {
        Solution sol = board.getSolution();
        assertNotNull(sol.getRoom());
        assertNotNull(sol.getPerson());
        assertNotNull(sol.getWeapon());

        assertEquals(CardType.ROOM, sol.getRoom().getType());
        assertEquals(CardType.PERSON, sol.getPerson().getType());
        assertEquals(CardType.WEAPON, sol.getWeapon().getType());
    }
    
    // Cards are dealt evenly and uniquely among players
    @Test
    public void testCardDealing() {
        List<Player> players = board.getPlayers();
        Set<Card> allCards = new HashSet<>();
        int totalCards = 0;

        for (Player p : players) {
            totalCards += p.getHand().size();
            for (Card c : p.getHand()) {
                assertFalse("Duplicate card found in hands", allCards.contains(c));
                allCards.add(c);
            }
        }

        int expectedCardsDealt = board.getDeck().size() - 3; // 3 cards in solution
        assertEquals(expectedCardsDealt, totalCards);

        int avg = expectedCardsDealt / players.size();
        for (Player p : players) {
            assertTrue(p.getHand().size() >= avg && p.getHand().size() <= avg + 1);
        }
    }
}
