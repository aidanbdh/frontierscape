package src;

import java.awt.Image;
import javax.swing.ImageIcon;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import src.helpers.Action;
import src.settings.AgentSettings;


public class Agent {

    public Image sprite;
    public Tile[][] tiles;
    public int x;
    public int y;
    private Stack<Action> history = new Stack<>(); 
    public Boolean updated = false;
    public int vision;
    public int[][] resources; // [value, metabolism]
    private Queue<Action> queue = new LinkedList<>();
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
        Tile tile = tiles[y][x];
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
        Tile tile = tiles[y][x];
        // Look for tiles with more food up to vision away
        int[] coords = {y, x};
        // Get the current tile's data
        int[] currentTile = tile.inspect();
        int current = 0;
        // Get the best resource from the current tile
        for (int val : currentTile) {
            if (val > current) {
                current = val;
            }
        }
        // Initialize max
        int max = current;
        // Check surrounding tiles
        for (int i = y - vision; i <= y + vision; i++) {
            // Check for out of bounds
            if (i < 0) {
                i = 0;
            } else if (i >= tiles.length) {
                break;
            }
            for (int j = x - vision; j <= x + vision; j++) {
                // Check for out of bounds
                if (j < 0) {
                    j = 0;
                } else if (j >= tiles[0].length) {
                    break;
                }
                //if (x == y)
                    System.out.print(i + "," + j + "\n" + max + "\n" + current + '\n');
                // Check if occupied
                if (tiles[i][j].agent != null) {
                    continue;
                }
                // Inspect all resources on the tile
                int[] data = tiles[i][j].inspect();
                // Check the values vs max
                for (int val : data) {
                    if (val > max) {
                        max = val;
                        coords[0] = i;
                        coords[1] = j;
                    }
                }
            }
        }
        // Check for movement
        if (max > current) {
            int[] direction = {0, 0};
            // Move towards the tile
            if (coords[0] < y) {
                direction[0] = -1;
            } else if (coords[0] > y) {
                direction[0] = 1;
            }
            if (coords[1] < x) {
                direction[1] = -1;
            } else if (coords[1] > x) {
                direction[1] = 1;
            }
            // Add the action to the queue 
            System.out.print("Max: " + max + "\nCurent: " + current + '\n');
            System.out.print("Moving: " + direction[0] + "," + direction[1] + "\nTowards: " + coords[0] + "," + coords[1] + "\nFrom: " + y + "," + x + '\n');
            queue.add(new Action("move", direction[0], direction[1]));
        }
    };

    public void execute() {
        // Run events
        for (int i = 0; i < queue.size(); i++) {
            // Remove the action to be executed from the queue
            Action action = queue.remove();
            // Handle move actions
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
        queue.add(history.pop());
        execute();
    };

    public void remove() {
        tiles[y][x].agent = null;
        active = false;
    }
}
