package clueGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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


	// Constructor which does 90% of the work
	public GameControlPanel() {

		// Create a layout with 2 rows
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		
		JPanel upperPanel = createUpperPanel();
		mainPanel.add(upperPanel);
		JPanel lowerPanel = new JPanel();
		lowerPanel = createLowerPanel();
		mainPanel.add(lowerPanel);

		add(mainPanel); //add to gameControlPanel

	}
	
	// Separate main panel into two large panels (upper + lower), which will include smaller inner panels
	public JPanel createUpperPanel() {

		JPanel panel = new JPanel(); //outer panel
		panel.setLayout(new GridLayout(1,4));

		// first inner panel
		JPanel turnPanel = new JPanel(); 
		turnPanel.setLayout(new GridLayout(2,1));

		JLabel turnLabel = new JLabel("Whose turn?"); 
		turnPanel.add(turnLabel);

		turnTextfield = new JTextField();
		turnPanel.add(turnTextfield);


		// second inner panel
		JPanel rollPanel = new JPanel(); 
		rollPanel.setLayout(new FlowLayout());

		JLabel rollLabel = new JLabel("Roll:");
		rollPanel.add(rollLabel);

		rollTextfield = new JTextField(10);
		rollPanel.add(rollTextfield);


		// third inner panel
		JPanel accusationPanel = new JPanel(); 
		accusationPanel.setLayout(new GridLayout(1,1));
		JButton accusationButton = new JButton("Make Accusation");
		accusationPanel.add(accusationButton);


		// fourth inner panel
		JPanel nextPanel = new JPanel(); 
		nextPanel.setLayout(new GridLayout(1,1));
		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(e -> {
		    if (boardPanel != null) {
		        boardPanel.nextTurn();  // advance the game
		    }
		});
		nextPanel.add(nextButton);

		//add panels to outer
		panel.add(turnPanel);
		panel.add(rollPanel);
		panel.add(accusationPanel);		
		panel.add(nextPanel);




		return panel;
	}
	
	// Second of larger panels (lower) which will be added to main panel.
	public JPanel createLowerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new FlowLayout());
		guessPanel.setBorder(new TitledBorder (new EtchedBorder(),"Guess"));
		guessTextfield = new JTextField(25);
		guessPanel.add(guessTextfield);
		
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setLayout(new FlowLayout());
		guessResultPanel.setBorder(new TitledBorder (new EtchedBorder(),"Guess Result"));
		guessResultTextfield = new JTextField(25);
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
		turnTextfield.setText(player.name);
		turnTextfield.setBackground(player.color); // Makes whole textField the same color of player
		rollTextfield.setText(String.valueOf(roll));
	}
	

	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", Color.orange , 0, 0), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
}
