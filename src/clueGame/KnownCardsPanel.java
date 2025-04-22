package clueGame;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/*
 * Known Cards Panel:
 * Represents the Clue game GUI for known cards and seen cards. The class itself extends JPanel because it is only one section of the frame and so it is 
 * fitting to add everything to a panel
 *
 * Authors/Contributors:
 * Abdallah Salhi
 * Montgomery Hughes
 */
public class KnownCardsPanel extends JPanel {

	private static JTextField noneTextfield;
	private static JTextField newTextField;

	private JPanel personPanel;
	private JPanel roomPanel;
	private JPanel weaponPanel;

	private JPanel seenPersonPanel;
	private JPanel seenRoomPanel;
	private JPanel seenWeaponPanel;


	// Main Constructor calls all the methods to create inner panels and adds them to the main panel
	public KnownCardsPanel() {
		setLayout(new BorderLayout());

		// Create a layout with 3 rows
		JPanel mainPanel = new JPanel(new GridLayout(3,1));
		mainPanel.setBorder(new TitledBorder (new EtchedBorder(),"Known Cards"));

		// create 1st panel for people cards
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new BoxLayout(peoplePanel, BoxLayout.Y_AXIS));
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(),"People"));

		//create in Hand and seen panels. Must be separate to add to them dynamically
		personPanel = createInHandPersonPanel();
		seenPersonPanel = createSeenPersonPanel();

		peoplePanel.add(personPanel);
		peoplePanel.add(Box.createVerticalStrut(10));
		peoplePanel.add(seenPersonPanel);

		// create 2nd panel for room cards
		JPanel roomsPanel = new JPanel();
		roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS));
		roomsPanel.setBorder(new TitledBorder (new EtchedBorder(),"Rooms"));

		roomPanel = createInHandRoomPanel();
		seenRoomPanel = createSeenRoomPanel();

		roomsPanel.add(roomPanel);
		roomsPanel.add(Box.createVerticalStrut(10));
		roomsPanel.add(seenRoomPanel);

		// create 3rd panel for weapon cards
		JPanel weaponsPanel = new JPanel();
		weaponsPanel.setLayout(new BoxLayout(weaponsPanel, BoxLayout.Y_AXIS));

		weaponsPanel.setBorder(new TitledBorder (new EtchedBorder(),"Weapons"));

		weaponPanel = createInHandWeaponPanel();
		seenWeaponPanel = createSeenWeaponPanel();

		weaponsPanel.add(weaponPanel);
		weaponsPanel.add(Box.createVerticalStrut(10));
		weaponsPanel.add(seenWeaponPanel);

		mainPanel.add(peoplePanel);
		mainPanel.add(roomsPanel);
		mainPanel.add(weaponsPanel);

		add(mainPanel); //add to gameControlPanel
	}

	// Panel for people cards in hand
	private JPanel createInHandPersonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("In Hand:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		panel.add(noneTextfield);

		return panel;
	}

	// Panel for seen people cards
	private JPanel createSeenPersonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Seen:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(noneTextfield);


		return panel;
	}

	// Panel for room cards in hand
	private JPanel createInHandRoomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("In Hand:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		panel.add(noneTextfield);

		return panel;
	}

	// Panel for seen room cards
	private JPanel createSeenRoomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Seen:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(noneTextfield);


		return panel;
	}

	// Panel for weapon cards in hand
	private JPanel createInHandWeaponPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("In Hand:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		panel.add(noneTextfield);

		return panel;
	}

	// Panel for seen weapon cards
	private JPanel createSeenWeaponPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(new JLabel("Seen:"));

		noneTextfield = new JTextField();
		noneTextfield.setText("None");
		noneTextfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(noneTextfield);


		return panel;
	}
	// Add cards to panels. Be able to get name of card and color of player
	public void updatePanels(JPanel panel, Card card, Player player) {
		newTextField = new JTextField();
		
		Component components[] = panel.getComponents();
		for(Component c : components) {	
			if(c instanceof JTextField) {
				String text = ((JTextField) c).getText();
				if(text.equals("None")) {
					panel.remove(c);
				}
			}
		}
		
		panel.add(newTextField);
		newTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		newTextField.setHorizontalAlignment(JTextField.CENTER);
		newTextField.setText(card.getCardName());
		newTextField.setBackground(player.color);

		panel.revalidate();
		panel.repaint();


	}

	public static void main(String[] args) {
		KnownCardsPanel panel = new KnownCardsPanel();
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(180, 750);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible

		// test filling in the data
		Player randomPlayer = new HumanPlayer("Harry Potter", Color.YELLOW, 10, 10);
		Player randomPlayer1 = new HumanPlayer("Luna Lovegood", Color.CYAN, 10, 10);
		Player randomPlayer2 = new HumanPlayer("Neville Longbottom", Color.MAGENTA, 10, 10);

		Card card = new Card("Draco Malfoy",CardType.PERSON);
		Card card1 = new Card("Ron Weasley",CardType.PERSON);
		Card card2 = new Card("Hermione Granger",CardType.PERSON);

		
		panel.updatePanels(panel.personPanel,card ,randomPlayer);
		panel.updatePanels(panel.seenPersonPanel, card1, randomPlayer1);
		panel.updatePanels(panel.seenPersonPanel, card2, randomPlayer2);
		
		

		
	}

}
