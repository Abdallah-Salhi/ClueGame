package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private Set<Card> seen = new HashSet<>();

    public ComputerPlayer(String name, java.awt.Color color, int row, int col) {
        super(name, color, row, col);
    }

    public void addSeenCard(Card card) {
        seen.add(card);
    }

    public Set<Card> getSeenCards() {
        return seen;
    }

    public Solution createSuggestion(Board board) {
        Room currentRoom = board.getRoom(board.getCell(row, column));
        Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);

        List<Card> unseenPeople = new ArrayList<>();
        List<Card> unseenWeapons = new ArrayList<>();

        for (Card c : board.getDeck()) {
            if (!seen.contains(c) && !hand.contains(c)) {
                if (c.getType() == CardType.PERSON) unseenPeople.add(c);
                else if (c.getType() == CardType.WEAPON) unseenWeapons.add(c);
            }
        }

        Random rand = new Random();
        Card personCard = unseenPeople.get(rand.nextInt(unseenPeople.size()));
        Card weaponCard = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));

        return new Solution(personCard, weaponCard, roomCard);
    }

    public BoardCell selectTarget(Set<BoardCell> targets, Board board) {
        List<BoardCell> unseenRooms = new ArrayList<>();

        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                Room room = board.getRoom(cell);
                Card roomCard = new Card(room.getName(), CardType.ROOM);
                if (!seen.contains(roomCard)) {
                    unseenRooms.add(cell);
                }
            }
        }

        Random rand = new Random();
        if (!unseenRooms.isEmpty()) {
            return unseenRooms.get(rand.nextInt(unseenRooms.size()));
        }

        List<BoardCell> allTargets = new ArrayList<>(targets);
        return allTargets.get(rand.nextInt(allTargets.size()));
    }
}