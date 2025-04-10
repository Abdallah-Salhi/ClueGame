package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

public class ComputerAITest {
    private static Board board;
    private static ComputerPlayer cpu;

    private static Card wand, sword, goblet, harry, draco, luna, roomOfRequirement, chamberSecrets;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();

        cpu = new ComputerPlayer("CPU", java.awt.Color.BLUE, 24, 7);
        wand = new Card("Wand", CardType.WEAPON);
        sword = new Card("Sword", CardType.WEAPON);
        goblet = new Card("Goblet", CardType.WEAPON);
        harry = new Card("Harry Potter", CardType.PERSON);
        draco = new Card("Draco Malfoy", CardType.PERSON);
        luna = new Card("Luna Lovegood", CardType.PERSON);
        roomOfRequirement = new Card("Room of Requirement", CardType.ROOM);
        chamberSecrets = new Card("Chamber of Secrets", CardType.ROOM);
    }

    @Test
    public void suggestionWithSingleUnseenCard() {
        ComputerPlayer computer = new ComputerPlayer("Draco", Color.GREEN, 24, 7); // Assume this is Room of Requirement
        Board board = Board.getInstance();

        Card sword = null;
        Card wand = null;
        Card potion = null;
        Card goblet = null;
        Card broomstick = null;
        Card book = null;

        // Locate exact instances from the board's deck
        for (Card c : board.getDeck()) {
            switch (c.getCardName()) {
                case "Sword" -> sword = c;
                case "Wand" -> wand = c;
                case "Potion" -> potion = c;
                case "Goblet" -> goblet = c;
                case "Broomstick" -> broomstick = c;
                case "Book" -> book = c;
            }
        }

        // Mark all other weapons as seen except Sword
        computer.addSeenCard(wand);
        computer.addSeenCard(potion);
        computer.addSeenCard(goblet);
        computer.addSeenCard(broomstick);
        computer.addSeenCard(book);

        // Only Sword is left unseen
        Solution suggestion = computer.createSuggestion(board);
        assertEquals(sword, suggestion.getWeapon());
    }

    @Test
    public void suggestionRandomAmongUnseenCards() {
        Set<String> seenWeapons = new HashSet<>();
        Set<String> seenPeople = new HashSet<>();

        for (int i = 0; i < 100; i++) {
        	cpu = new ComputerPlayer("CPU", java.awt.Color.BLUE, 24, 7);

            cpu.addSeenCard(wand); // Only sword and goblet unseen
            cpu.addSeenCard(draco); // Only harry and luna unseen

            Solution suggestion = cpu.createSuggestion(board);
            seenWeapons.add(suggestion.getWeapon().getCardName());
            seenPeople.add(suggestion.getPerson().getCardName());
        }

        assertTrue(seenWeapons.contains("Sword"));
        assertTrue(seenWeapons.contains("Goblet"));

        assertTrue(seenPeople.contains("Harry Potter"));
        assertTrue(seenPeople.contains("Luna Lovegood"));
    }

    @Test
    public void selectTargetRandom_NoRooms() {
        ComputerPlayer computer = new ComputerPlayer("Ron", Color.ORANGE, 5, 5);
        Board board = Board.getInstance();

        BoardCell cell1 = board.getCell(5, 6); // Assume walkway
        BoardCell cell2 = board.getCell(5, 4); // Assume walkway
        BoardCell cell3 = board.getCell(6, 5); // Assume walkway

        Set<BoardCell> targets = Set.of(cell1, cell2, cell3);
        Set<BoardCell> seenResults = new HashSet<>();

        // Run multiple times to observe randomness
        for (int i = 0; i < 100; i++) {
            BoardCell selected = computer.selectTarget(targets, board);
            seenResults.add(selected);
        }

        // Ensure more than one unique cell was chosen over 100 trials
        assertTrue(seenResults.size() > 1, "Expected random selection across multiple targets");
    }

    @Test
    public void selectsUnseenRoomOverWalkway() {
        Set<BoardCell> targets = new HashSet<>();
        BoardCell chamberCell = board.getCell(15, 6); // center of Chamber of Secrets
        targets.add(board.getCell(6, 2)); // walkway
        targets.add(chamberCell);

        // Chamber is unseen
        cpu.getSeenCards().clear();

        BoardCell choice = cpu.selectTarget(targets, board);
        assertEquals(chamberCell, choice);
    }

    @Test
    public void seenRoomRandomLikeWalkways() {
        Set<BoardCell> targets = new HashSet<>();
        BoardCell chamberCell = board.getCell(15, 6);
        targets.add(board.getCell(6, 2));
        targets.add(chamberCell);

        cpu.getSeenCards().clear();
        cpu.addSeenCard(new Card("Chamber of Secrets", CardType.ROOM));

        Set<BoardCell> chosen = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            chosen.add(cpu.selectTarget(targets, board));
        }

        assertTrue(chosen.contains(chamberCell));
        assertTrue(chosen.contains(board.getCell(6, 2)));
    }
}
