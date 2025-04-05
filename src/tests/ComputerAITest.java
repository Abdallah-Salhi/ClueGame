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
    public void testCreateSuggestion_OnlyOneUnseenPersonOrWeapon() {
        cpu.giveCard(wand);
        cpu.giveCard(draco);
        cpu.addSeenCard(wand);
        cpu.addSeenCard(goblet);
        cpu.addSeenCard(draco);
        cpu.addSeenCard(luna);

        Solution suggestion = cpu.createSuggestion(board);

        assertEquals("Room of Requirement", suggestion.getRoom().getCardName());
        assertEquals("Sword", suggestion.getWeapon().getCardName());
        assertEquals("Harry Potter", suggestion.getPerson().getCardName());
    }

    @Test
    public void testCreateSuggestion_WeaponAndPersonRandom() {
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
    public void testSelectTarget_NoRoom_ChoosesRandom() {
        Set<BoardCell> targets = new HashSet<>();
        targets.add(board.getCell(6, 2)); // walkway
        targets.add(board.getCell(6, 3)); // walkway
        targets.add(board.getCell(6, 4)); // walkway

        Set<BoardCell> chosenCells = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            BoardCell chosen = cpu.selectTarget(targets, board);
            chosenCells.add(chosen);
        }

        assertTrue(chosenCells.contains(board.getCell(6, 2)));
        assertTrue(chosenCells.contains(board.getCell(6, 3)));
        assertTrue(chosenCells.contains(board.getCell(6, 4)));
    }

    @Test
    public void testSelectTarget_UnseenRoomPreferred() {
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
    public void testSelectTarget_SeenRoomEqualChance() {
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
