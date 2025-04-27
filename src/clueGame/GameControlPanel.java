package clueGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/*
 * Game Control Panel:
 * Represents the Clue game GUI for the control panel, including the next and accusation button along with turn information. The class itself extends JPanel because it is only one section of the frame and so it is 
 * fitting to add everything to a panel
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class GameControlPanel extends JPanel {
	private JTextField turnTextfield;
	private JTextField rollTextfield;
	private JTextField guessTextfield;
	private JTextField guessResultTextfield;
	private BoardPanel boardPanel;
	protected Player currentPlayer;
	private Board theInstance = Board.getInstance();
	private AccusationOrSuggestion accusation;
	private Solution accusationAttempt;


	// Constructor which does 90% of the work
	public GameControlPanel() {
		

		// Create a layout with 2 rows
		JPanel mainPanel = new JPanel(new GridLayout(2,1));

		JPanel upperPanel = createUpperPanel();
		JPanel lowerPanel = createLowerPanel();

		mainPanel.add(upperPanel);
		mainPanel.add(lowerPanel);

		add(mainPanel); // Add to gameControlPanel

	}

	// Separate main panel into two large panels (upper + lower), which will include smaller inner panels
	public JPanel createUpperPanel() {

		// Outer panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));

		// First inner panel
		JPanel turnPanel = new JPanel(new GridLayout(2,1)); 
		JLabel turnLabel = new JLabel("Whose turn?"); 

		turnTextfield = new JTextField();

		turnPanel.add(turnLabel);
		turnPanel.add(turnTextfield);


		// Second inner panel
		JPanel rollPanel = new JPanel(new FlowLayout()); 
		JLabel rollLabel = new JLabel("Roll:");

		rollTextfield = new JTextField(10);

		rollPanel.add(rollLabel);
		rollPanel.add(rollTextfield);


		// Third inner panel
		JPanel accusationPanel = new JPanel(new GridLayout(1,1)); 
		JButton accusationButton = new JButton("Make Accusation");


		// Fourth inner panel
		JPanel nextPanel = new JPanel(new GridLayout(1,1)); 
		JButton nextButton = new JButton("Next");

		nextButton.addActionListener(e -> {
			if (boardPanel != null) {
				if(boardPanel.humanTurnFinished) { // only 
					boardPanel.nextTurn();  // Advance the game
				}else {
					JOptionPane.showMessageDialog(null, "You must finish your turn before moving on!", "Message", JOptionPane.ERROR_MESSAGE);	
				}
			}
		});

		accusationButton.addActionListener(e->{
			if (!(currentPlayer instanceof HumanPlayer)) { // No need for mouse listener if not the user's turn
				JOptionPane.showMessageDialog(null, "It is not your turn!", "Message", JOptionPane.ERROR_MESSAGE);	
			}else if(boardPanel.humanTurnFinished) {
				JOptionPane.showMessageDialog(null, "You can no longer make an Accusation!", "Message", JOptionPane.ERROR_MESSAGE);	
			}else {
				createAccusationPane();

				// proccess Accusation using check Accusation function in Board
				Solution solution = theInstance.getSolution();

				if(theInstance.checkAccusation(accusation, solution)) {
					JOptionPane.showMessageDialog(null, "That is correct! You have Won!", "Message", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}else{
					JOptionPane.showMessageDialog(null, "That was NOT the correct solution. You have Lost. \n The correct solution was \n Room: " + solution.getRoom().getCardName() + " \n Person: " + solution.getPerson().getCardName() + "\n Weapon: " + solution.getWeapon().getCardName(), "Message", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}



		});

		accusationPanel.add(accusationButton);
		nextPanel.add(nextButton);

		// Add panels to outer
		panel.add(turnPanel);
		panel.add(rollPanel);
		panel.add(accusationPanel);		
		panel.add(nextPanel);

		return panel;
	}

	// Create the options for making an accusation
	private void createAccusationPane() {
		String[] rooms = {"Great Hall","Gryffindor Dormitory","Slytherin Dormitory","Hufflepuff Dormitory","Ravenclaw Dormitory","Chamber of Secrets","Transfiguration Classroom","Headmaster's Office", "Room of Requirement" };
		String[] suspects = { "Harry Potter", "Hermione Granger", "Ron Weasley", "Draco Malfoy", "Luna Lovegood", "Neville Longbottom" };
		String[] weapons = { "Wand", "Potion", "Sword", "Broomstick", "Book", "Goblet" };

		JComboBox<String> roomBox = new JComboBox<>(rooms);
		JComboBox<String> personBox = new JComboBox<>(suspects);
		JComboBox<String> weaponBox = new JComboBox<>(weapons);

		Card room = null;
		Card weapon = null;
		Card person = null;

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5));  // vertical stacking
		panel.add(new JLabel("Select Room:"));
		panel.add(roomBox);
		panel.add(new JLabel("Select Suspect:"));
		panel.add(personBox);
		panel.add(new JLabel("Select Weapon:"));
		panel.add(weaponBox);

		int result = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Make an Accusation",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
				);

		if (result == JOptionPane.OK_OPTION) {
			String selectedRoom = (String) roomBox.getSelectedItem();
			String selectedPerson = (String) personBox.getSelectedItem();
			String selectedWeapon = (String) weaponBox.getSelectedItem();

			room = new Card(selectedRoom, CardType.ROOM);
			weapon = new Card(selectedWeapon, CardType.WEAPON);
			person = new Card(selectedPerson, CardType.PERSON);

		}

		accusation = new AccusationOrSuggestion(currentPlayer, person, weapon, room);

	
}

// Second of larger panels (lower) which will be added to main panel.
public JPanel createLowerPanel() {
	JPanel panel = new JPanel(new GridLayout(1,2));	

	// Guess text field
	JPanel guessPanel = new JPanel(new FlowLayout());
	guessPanel.setBorder(new TitledBorder (new EtchedBorder(),"Guess"));
	guessTextfield = new JTextField(25);

	// GuessResult text field
	JPanel guessResultPanel = new JPanel(new FlowLayout());
	guessResultPanel.setBorder(new TitledBorder (new EtchedBorder(),"Guess Result"));
	guessResultTextfield = new JTextField(25);

	guessPanel.add(guessTextfield);
	guessResultPanel.add(guessResultTextfield);
	panel.add(guessPanel);
	panel.add(guessResultPanel);

	return panel;

}

// Setter for Guess string
public void setGuess(String guess) {
	guessTextfield.setText(guess);
}

// Setter for Guess result/response string
public void setGuessResult(String guessResult) {
	guessResultTextfield.setText(guessResult);
}

// Setter for roll string
public void setRoll(String roll) {
	rollTextfield.setText(roll);
}

// Setter to connect GameControlPanel to BoardPanel
public void setBoardPanel(BoardPanel boardPanel) {
	this.boardPanel = boardPanel;
}

// Setter for whose turn it is. Needed for player name and roll int
public void setTurn(Player player, int roll) {
	currentPlayer = player;
	turnTextfield.setText(player.name);
	turnTextfield.setBackground(player.color); // Makes whole textField the same color of player
	rollTextfield.setText(String.valueOf(roll));
}

// Getter for whose turn it is
public Player getTurn() {
	return currentPlayer;
}

public static void main(String[] args) {
	GameControlPanel panel = new GameControlPanel();
	JFrame frame = new JFrame();  // Create the frame 
	frame.setContentPane(panel); // Put the panel in the frame
	frame.setSize(750, 180);  // Size the frame
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow it to close
	frame.setVisible(true); // Make it visible

	// Test filling in the data
	panel.setTurn(new ComputerPlayer( "Col. Mustard", Color.orange , 0, 0), 5);
	panel.setGuess( "I have no guess!");
	panel.setGuessResult( "So you have nothing?");
}
}
