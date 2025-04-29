package clueGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Board Panel:
 * Represents the Clue game GUI for the main board, including the grid of cells and the players. The class itself extends JPanel because it is only one section of the frame and so it is 
 * fitting to add everything to a panel
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class BoardPanel extends JPanel {
	private Board theInstance = Board.getInstance();
	private static Board board;

	private BoardCell clickedCell;
	private Player currentPlayer;
	private boolean isHumanTurn;
	private Set<BoardCell> targetCells;
	private List<Player> players;
	protected boolean playerMoved;
	private Timer animationTimer;
	private int playerPixelX;
	private int playerPixelY;
	private int destPixelX;
	private int destPixelY;

	private int numRows;
	private int numCols;

	private int panelWidth;
	private int panelHeight;

	private int mouseX;
	private int mouseY;
	private int clickedRow;
	private int clickedCol;

	private int cellWidth;
	private int cellHeight;

	private int xOffset;
	private int yOffset;
	private int padding;

	private int currentPlayerIndex = 0;
	protected boolean humanTurnFinished = false;
	private GameControlPanel controlPanel;

	private int roll;
	private AccusationOrSuggestion suggestion;
	private int speed;
	private Map<Player, AnimationState> playerAnimations = new HashMap<>();
	private KnownCardsPanel knownCardsPanel;

	// Main constructor. Connects to gameControlPanel and  adds the JPanel and MouseListener
	public BoardPanel(GameControlPanel controlPanel, KnownCardsPanel knownCardsPanel) {
		this.controlPanel = controlPanel;
		this.knownCardsPanel = knownCardsPanel;
		players = theInstance.getPlayers();
		currentPlayer = players.get(0); // Human always first

		// Update knownCards Panel with user's cards
		if(currentPlayer instanceof HumanPlayer) {
			for(Card card : currentPlayer.getHand()) {
				if(card.getType() == CardType.PERSON) {
					knownCardsPanel.updatePanels(knownCardsPanel.personPanel, card, currentPlayer);

				}else if(card.getType() == CardType.ROOM) {
					knownCardsPanel.updatePanels(knownCardsPanel.roomPanel, card, currentPlayer);

				}else {
					knownCardsPanel.updatePanels(knownCardsPanel.weaponPanel, card, currentPlayer);

				}
			}
		}

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		add(panel);
		addMouseListener(new mouseListener());
	}

	// nextTurn runs all the logic for turn events, such as highlighting target cells, calling the calcTargets function and handling Human and Computer player logic
	public void nextTurn() {
		players = theInstance.getPlayers();
		currentPlayer = players.get(currentPlayerIndex);
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // increment for next turn

		controlPanel.setGuess("", Color.WHITE);
		controlPanel.setGuessResult("", Color.WHITE);

		// Get a roll using random 
		roll = new Random().nextInt(6) + 1;
		playerMoved = false;

		// refresh highlighted cells/clean board
		for (BoardCell[] row : theInstance.getGrid()) {
			for (BoardCell cell : row) {
				cell.setHighlight(false);
			}
		}

		// Set current player and roll in the control panel
		controlPanel.setTurn(currentPlayer, roll);

		BoardCell startCell = theInstance.getCell(currentPlayer.getRow(), currentPlayer.getColumn());

		theInstance.calcTargets(startCell, roll, currentPlayer);
		targetCells = theInstance.getTargets();

		// Human player logic
		if (currentPlayer instanceof HumanPlayer) {
			for (BoardCell cell : targetCells) {
				cell.setHighlight(true);
			}
			humanTurnFinished = false; // need to reset this to stop user from clicking next 
			repaint();
		} else {
			// Computer player logic
			ComputerPlayer cpu = (ComputerPlayer) currentPlayer;

			// Check to see if they can make accusation
			if(cpu.getFlag()) {
				AccusationOrSuggestion accusation = cpu.createAccusation();
				// Process accusation
				Solution solution = theInstance.getSolution();
				if(theInstance.checkAccusation(accusation, solution)) {
					JOptionPane.showMessageDialog(null, "CPU has Won. \n Game Over ", "Message", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("The correct solution was \n Room: " + solution.getRoom().getCardName() + " \n Person: " + solution.getPerson().getCardName() + "\n Weapon: " + solution.getWeapon().getCardName());
					System.exit(0);
				}else{
					JOptionPane.showMessageDialog(null, "CPU made a wrong accusation. \n Game Over","Message", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("The correct solution was \n Room: " + solution.getRoom().getCardName() + " \n Person: " + solution.getPerson().getCardName() + "\n Weapon: " + solution.getWeapon().getCardName());
					System.out.println("The CPU(" + cpu.getName() + ") guessed: \n Room: " + accusation.getRoom().getCardName() + " \n Person: " + accusation.getPerson().getCardName() + "\n Weapon: " + accusation.getWeapon().getCardName());

					Set<Card> cpuHand = cpu.getHand();
					List<Card> tempList = new ArrayList<>(cpuHand);
					Card card1 = tempList.get(0);
					Card card2 = tempList.get(1);
					Card card3 = tempList.get(2);


					System.out.println("" + cpu.getName() + "'s hand looks like this: \n Room: " + card1.getCardName() + " \n Person: " + card2.getCardName() + "\n Weapon: " + card3.getCardName());
					System.exit(0);
				}
			}
			BoardCell target = cpu.selectTarget(targetCells, theInstance);
			cpu.setMovedBySuggestion(false); // after cpu chooses on their own 
			playerAnimation(currentPlayer, target, 2);
			currentPlayer.movePlayer(target);
			repaint();

			// When CPU enters a room
			if (theInstance.getCell(currentPlayer.getRow(), currentPlayer.getColumn()).isRoomCenter()) { // check if room center
				AccusationOrSuggestion suggestion = cpu.createSuggestion(theInstance); // make a new suggestion

				// update control Panel
				controlPanel.setGuess( 
						suggestion.getPerson().getCardName() + ", " + suggestion.getRoom().getCardName() + ", " + suggestion.getWeapon().getCardName(),
						currentPlayer.getColor()
						);



				// Handle the suggestion by checking if anyone can disprove it
				Card disprovingCard = theInstance.handleSuggestion(suggestion, (ArrayList<Player>) theInstance.getPlayers());

				// Move suggested player to suggested room.
				moveSuggestedPlayer(suggestion);

				// Process disprove card
				if (disprovingCard != null) {
					Player disprover = theInstance.getDisprovingPlayer(); // USE the saved player!
					controlPanel.setGuessResult(disprover.getName() + " disproved the suggestion.", disprover.getColor());
					cpu.addSeenCard(disprovingCard);
				} else {
					controlPanel.setGuessResult("No new clue was found", Color.LIGHT_GRAY);

					// Before setting flag check to make sure that cpu doesn't have one of the cards
					Set<Card> cpuHand = cpu.getHand();
					List<Card> handList = new ArrayList<>(cpuHand);
					
					for(Card c : handList) {
						if(c.equals(suggestion.getPerson()) || c.equals(suggestion.getRoom()) || c.equals(suggestion.getWeapon()) ) {
							return; // come back if cpu has any of the cards in the suggestion
						}
					}
					cpu.setFlag(true, suggestion);

				}
			}
		}
	}

	// Helper method to summon player to room if they are suggested/accused
	private void moveSuggestedPlayer(AccusationOrSuggestion suggestion) {
		Player suggestedPlayer = null;
		for (Player player : players) {
			if (player.getName().equals(suggestion.getPerson().getCardName())) {
				suggestedPlayer = player;
				break;
			}
		}

		if (suggestedPlayer != null) {
			// get room center cell
			Room targetRoom = theInstance.getRoomByName(suggestion.getRoom().getCardName());
			BoardCell roomCenter = targetRoom.getCenterCell();

			// Animate the suggested player AFTER CPU animation finishes
			final Player playerToMove = suggestedPlayer;
			final BoardCell destinationCell = roomCenter;

			animateAfterPrevious(() -> {
				playerAnimation(playerToMove, destinationCell, 2);
				playerToMove.movePlayer(destinationCell);
			});
		}
		if(currentPlayer.equals(suggestedPlayer)) {
			suggestedPlayer.setMovedBySuggestion(false);
			return;
		}
		suggestedPlayer.setMovedBySuggestion(true);

	}

	private Player findPlayerWithCard(Card card) {
		for (Player p : players) {
			if (p.getHand().contains(card)) {
				return p;
			}
		}
		return null; // Should not happen unless logic error
	}

	// Draws the board by invoking draw method in boardCells along with players and doors
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		computeLayoutMetrics(g);
		drawBoardCells(g);
		drawPlayers(g);
		drawDoorsAndLabels(g);
	}

	private void computeLayoutMetrics(Graphics g) {
		numRows = theInstance.getNumRows();
		numCols = theInstance.getNumColumns();
		padding = 10;
		panelWidth = getWidth() - 2 * padding;
		panelHeight = getHeight() - 2 * padding;
		cellWidth = panelWidth / numCols;
		cellHeight = panelHeight / numRows;
	}

	// Helper method to generally draw the board cells correctly
	private void drawBoardCells(Graphics g) {
		BoardCell[][] grid = theInstance.getGrid();
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				BoardCell cell = grid[row][col];
				cell.draw(g, cellWidth, cellHeight, col * cellWidth + padding, row * cellHeight + padding);
			}
		}
	}

	// Helper method to ensure players are properly drawn
	private void drawPlayers(Graphics g) {
		List<Player> players = theInstance.getPlayers();
		for (Player player : players) {
			g.setColor(player.getColor());

			int drawX, drawY;
			if (playerAnimations.containsKey(player) && playerAnimations.get(player).isAnimating) {
				AnimationState state = playerAnimations.get(player);
				drawX = state.pixelX + padding + 4;
				drawY = state.pixelY + padding + 1;
			} else {
				drawX = player.getColumn() * cellWidth + padding + 4;
				drawY = player.getRow() * cellHeight + padding + 1;
			}

			g.fillOval(drawX, drawY, cellWidth - 8, cellHeight - 2);
			g.drawOval(drawX, drawY, cellWidth - 8, cellHeight - 2);
		}
	}

	// Helper method for drawing doors and labels
	private void drawDoorsAndLabels(Graphics g) {
		BoardCell[][] grid = theInstance.getGrid();
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				BoardCell cell = grid[row][col];
				if (cell.isDoorway) {
					drawDoorWay(g, cell.getDoorDirection(), row, col, cellWidth, cellHeight);
				}
				if (cell.isLabel()) {
					drawRoomLabel(g, cell.getRoom(), row, col);
				}
			}
		}
	}

	// Helper method to draw room labels
	private void drawRoomLabel(Graphics g, String label, int row, int col) {
		String[] words = label.split(" ");
		Font font = new Font("SansSerif", Font.BOLD, 11);
		g.setFont(font);
		g.setColor(Color.blue);
		FontMetrics metrics = g.getFontMetrics(font);
		int totalHeight = words.length * metrics.getHeight();

		int cellX = col * cellWidth + padding;
		int cellY = row * cellHeight + padding;
		int drawY = cellY + (cellHeight - totalHeight) / 2 + metrics.getAscent();

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			int textWidth = metrics.stringWidth(word);
			int drawX = cellX + (cellWidth - textWidth) / 2;
			g.drawString(word, drawX, drawY + i * metrics.getHeight());
		}
	}

	// Helper method for paint Component to be able to draw the doorways
	private void drawDoorWay(Graphics g, DoorDirection direction, int row, int col, int width, int height) {
		int targetRow = row;
		int targetCol = col;

		// Ensure the doorway is being drawn in the cell that the doorway points to not the cell with door indicator
		switch(direction) {
		case DOWN -> targetRow++;
		case UP -> targetRow--;
		case LEFT -> targetCol--;
		case RIGHT -> targetCol++;
		default -> throw new IllegalStateException("Unexpected door direction");
		}

		if (targetRow >= 0 && targetRow < numRows && targetCol >= 0 && targetCol < numCols) { // Make sure cell is in bounds
			int newRow = targetRow * cellHeight + padding; // Adjust for board GUI
			int newCol = targetCol * cellWidth + padding;
			g.setColor(Color.BLUE);

			// Draw rectangle based on door direction 
			switch(direction) {
			case DOWN -> g.fillRect(newCol, newRow, width, 5);
			case UP -> g.fillRect(newCol, newRow + height - 5, width ,5);
			case LEFT -> g.fillRect(newCol + width - 5, newRow, 5, height);
			case RIGHT -> g.fillRect(newCol, newRow, 5, height);
			default -> throw new IllegalStateException("Unexpected door direction");
			}
		}
	}

	// mouseListener allows for user to click on boardPanel and handles logic of what happens when a click is detected
	private class mouseListener extends MouseAdapter{ // use MouseAdapter so need for other 4 empty methods of mouselistener

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!(currentPlayer instanceof HumanPlayer)) return; // No need for mouse listener if not the user's turn
			if (playerMoved) return; // Make it so user can only pick a cell to move to once

			// Adjust to account for the padding we added around the panel
			mouseX = e.getX() - padding;
			mouseY = e.getY() - padding;

			// Convert to know where on the panel
			clickedCol = mouseX / cellWidth;
			clickedRow = mouseY / cellHeight;

			clickedCell = theInstance.getCell(clickedRow, clickedCol); // Use for calc targets function

			if (clickedCell == null || !targetCells.contains(clickedCell)) {
				JOptionPane.showMessageDialog(null, "That is not a target", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Move the player 
			for (BoardCell[] gridrow : theInstance.getGrid()) {
				for (BoardCell cell : gridrow) {
					cell.setHighlight(false); // Remove highlight once cell selected
				}
			}


			// move the player and start animation
			playerAnimation(currentPlayer, clickedCell, 2);
			currentPlayer.movePlayer(clickedCell);
			currentPlayer.setMovedBySuggestion(false);
			clickedCell.setOccupied(true);

			// open suggestion dialog screen
			if(clickedCell.isRoomCenter()) {
				createSuggestionPane(clickedCell.getRoomObj());
			}

			if(currentPlayer instanceof HumanPlayer) {
				humanTurnFinished = true;
			}
		}
	}

	// Helper method to animate player movement using Timer and action Listener
	private void playerAnimation(Player player, BoardCell destCell, int speed) {
		// Initialize animation state if needed
		if (!playerAnimations.containsKey(player)) {
			playerAnimations.put(player, new AnimationState(
					player.getColumn() * cellWidth,
					player.getRow() * cellHeight
					));
		}

		AnimationState state = playerAnimations.get(player);
		state.pixelX = player.getColumn() * cellWidth;
		state.pixelY = player.getRow() * cellHeight;
		state.isAnimating = true;

		int destPixelX = destCell.getColumn() * cellWidth;
		int destPixelY = destCell.getRow() * cellHeight;

		// before moving player we need to make the cell the player is in appear unoccupied
		int prevRow = player.getRow();
		int prevCol = player.getColumn();
		BoardCell prevCell = theInstance.getCell(prevRow, prevCol);
		prevCell.setOccupied(false);

		Timer animationTimer = new Timer(10, e -> {
			boolean moved = false;

			if (state.pixelX < destPixelX) {
				state.pixelX += speed;
				if (state.pixelX > destPixelX) state.pixelX = destPixelX;
				moved = true;
			} else if (state.pixelX > destPixelX) {
				state.pixelX -= speed;
				if (state.pixelX < destPixelX) state.pixelX = destPixelX;
				moved = true;
			}

			if (state.pixelY < destPixelY) {
				state.pixelY += speed;
				if (state.pixelY > destPixelY) state.pixelY = destPixelY;
				moved = true;
			} else if (state.pixelY > destPixelY) {
				state.pixelY -= speed;
				if (state.pixelY < destPixelY) state.pixelY = destPixelY;
				moved = true;
			}

			repaint();

			if (!moved) {
				((Timer)e.getSource()).stop();
				state.isAnimating = false;
				player.movePlayer(destCell);
			}
		});

		animationTimer.start();
	}
	// Create the suggestion dialog screen
	private void createSuggestionPane(Room room) {
		String[] suspects = { "Harry Potter", "Hermione Granger", "Ron Weasley", "Draco Malfoy", "Luna Lovegood", "Neville Longbottom" };
		String[] weapons = { "Wand", "Potion", "Sword", "Broomstick", "Book", "Goblet" };

		JLabel roomBox = new JLabel(room.getName());
		JComboBox<String> personBox = new JComboBox<>(suspects);
		JComboBox<String> weaponBox = new JComboBox<>(weapons);

		Card roomCard = null;
		Card weapon = null;
		Card person = null;

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5));  // vertical stacking
		panel.add(new JLabel("Current Room:"));
		panel.add(roomBox);
		panel.add(new JLabel("Select Suspect:"));
		panel.add(personBox);
		panel.add(new JLabel("Select Weapon:"));
		panel.add(weaponBox);

		int result = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Make a Suggestion",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
				);

		if (result == JOptionPane.OK_OPTION) {

			String selectedPerson = (String) personBox.getSelectedItem();
			String selectedWeapon = (String) weaponBox.getSelectedItem();

			roomCard = new Card(room.getName(),CardType.ROOM);
			weapon = new Card(selectedWeapon, CardType.WEAPON);
			person = new Card(selectedPerson, CardType.PERSON);

		}else {
			return;
		}

		suggestion = new AccusationOrSuggestion(currentPlayer, person, weapon, roomCard);
		Card disprovingCard = theInstance.handleSuggestion(suggestion, (ArrayList) players);

		// update control Panel
		controlPanel.setGuess( 
				suggestion.getPerson().getCardName() + ", " + suggestion.getRoom().getCardName() + ", " + suggestion.getWeapon().getCardName(),
				currentPlayer.getColor()
				);

		// Move suggested player to suggested room.
		moveSuggestedPlayer(suggestion);

		// Process disprove card
		if (disprovingCard != null) {
			Player disprover = theInstance.getDisprovingPlayer(); // USE the saved player!
			controlPanel.setGuessResult(disprover.getName() + " disproved the suggestion.", disprover.getColor());

			if(disprovingCard.getType().equals(CardType.PERSON)) {
				knownCardsPanel.updatePanels(knownCardsPanel.seenPersonPanel, disprovingCard, disprover);

			}else if(disprovingCard.getType().equals(CardType.ROOM)) {
				knownCardsPanel.updatePanels(knownCardsPanel.seenRoomPanel, disprovingCard, disprover);

			}else {
				knownCardsPanel.updatePanels(knownCardsPanel.seenWeaponPanel, disprovingCard, disprover);
			}
		} else {
			controlPanel.setGuessResult("No new clue was found", Color.LIGHT_GRAY);
		}



	}



	// Helper to delay animation until current animation finishes
	private void animateAfterPrevious(Runnable nextAnimation) {
		Timer waitTimer = new Timer(100, null);
		waitTimer.addActionListener(e -> {
			if (animationTimer == null || !animationTimer.isRunning()) {
				waitTimer.stop();
				// Add a small buffer delay (e.g., 200ms) for visual clarity
				Timer bufferTimer = new Timer(200, ev -> {
					nextAnimation.run();
					((Timer)ev.getSource()).stop();
				});
				bufferTimer.setRepeats(false); // Run only once
				bufferTimer.start();
			}
		});
		waitTimer.start();
	}

	// inner class to track animation state ** needed for animating several players 
	private class AnimationState {
		int pixelX;
		int pixelY;
		boolean isAnimating;

		public AnimationState(int x, int y) {
			this.pixelX = x;
			this.pixelY = y;
			this.isAnimating = false;
		}
	}

	// Must make an instance for testing 
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}




	public static void main(String[] args) {
		setUp();
		JFrame frame = new JFrame();
		GameControlPanel controlPanel = new GameControlPanel();
		KnownCardsPanel knownCardsPanel = new KnownCardsPanel();
		BoardPanel panel = new BoardPanel(controlPanel, knownCardsPanel);

		frame.setContentPane(panel);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}

