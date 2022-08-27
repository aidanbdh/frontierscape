package src;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Random;
import java.util.Stack;

import src.helpers.Action;
import src.settings.AgentSettings;


public class Agent {

    public Image sprite;
    public Tile tile;
    public Tile[][] tiles;
    public int x;
    public int y;
    private Stack<Action> history = new Stack<>(); 
    public Boolean updated = false;
    public int vision;
    public int[][] resources; // [value, metabolism]
    private Action[] queue = new Action[10];
    public boolean active = true;
    // Randomizer
    private Random rand = new Random();

    public Agent(int tileSize, Tile[][] boardTiles, AgentSettings settings) {
        // Randomize vision from settings
        vision = rand.nextInt(settings.vision[1] - settings.vision[0]) + settings.vision[0];
        // Initialize resources
        resources = new int[settings.resources.length][2];
        // Randomize starting resources and metabolism
        for (int i = 0; i < settings.resources.length; i++) {
            int[] values = {rand.nextInt(settings.resources[i][1] - settings.resources[i][0]) + settings.resources[i][0], rand.nextInt(settings.metabolism[i][1] - settings.metabolism[i][0]) + settings.metabolism[i][0]};
            resources[i] = values;
        }
        // Save the reference to the board state
        tiles = boardTiles;
        // Create the sprite for the agent
        ImageIcon icon = new ImageIcon("src/images/agent.png");
        sprite = icon.getImage();//.getScaledInstance(tileSize, tileSize, java.awt.Image.SCALE_SMOOTH);
    }

    public void upkeep() {
        // Check each resource
        for (int i = 0; i < resources.length; i++) {
            // Harvest some of that resource
            resources[i][0] += tile.interact(i);
            // Use up some resource
            resources[i][0] -= resources[i][1];
            // Check for death
            if (resources[i][0] < 0) {
                this.remove();
            }
        }  
    };
    
    public void action() {
        // Queue a random movement event for testing
        int ydirection = rand.nextInt(3);
        int xdirection = rand.nextInt(3);
        queue[0] = new Action("move", ydirection - 1, xdirection - 1);
    };

    public void execute() {
        // Run events
        for (Action action : queue) {
            if (action == null) {
                break;
            }
            if (action.type == "move") {
                // Check for the edge of the board
                if(y + action.value1 < 0 || y + action.value1 >= tiles.length) {
                    action.value1 = 0;
                }
                if(x + action.value2 < 0 || x + action.value2 >= tiles[0].length) {
                    action.value2 = 0;
                }
                // Check for no moven or collision
                if(!(action.value1 == 0 && action.value2 == 0) && tiles[y + action.value1][x + action.value2].agent == null) {
                    // Move if not empty
                    tiles[y + action.value1][x + action.value2].agent = this;
                    tiles[y][x].agent = null;
                    y += action.value1;
                    x += action.value2;
                }
            }
            // Save the action in the history
            history.push(action);
        }
    };

    // Returns the agent to its last known position
    public void undo() {
        queue[0] = history.pop();
    };

    public void remove() {
        tiles[y][x].agent = null;
        active = false;
    }
}
