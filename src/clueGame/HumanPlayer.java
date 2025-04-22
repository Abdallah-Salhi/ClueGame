package clueGame;

/*
* Human Player:
* Represents the human players that are involved in the game, which are children of the player class. Necessary for modularization of classes and 
* ease in human player logic. 
* 
* Authors/Contributors:
* Abdallah Salhi
* Montgomery Hughes
*/

public class HumanPlayer extends Player {
    public HumanPlayer(String name, java.awt.Color color, int row, int col) {
        super(name, color, row, col);
    }
}