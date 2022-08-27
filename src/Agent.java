package src;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Agent {

    public Image sprite;
    public Tile tile;
    public Tile[][] tiles;
    public int x;
    public int y;
    private int[][] history;
    public Boolean updated = false;

    public Agent(int tileSize, Tile[][] boardTiles) {
        // Save the reference to the board state
        tiles = boardTiles;
        // Create the sprite for the agent
        ImageIcon icon = new ImageIcon("src/images/agent.png");
        sprite = icon.getImage();//.getScaledInstance(tileSize, tileSize, java.awt.Image.SCALE_SMOOTH);
    }

    public void upkeep() {};
    
    public void action() {};

    public void execute() {};

    // Returns the agent to its last known position
    public void undo() {};
}
