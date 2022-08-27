package src;
import javax.swing.JPanel;

import src.settings.BoardSettings;
import src.settings.TileSettings;

import java.util.Random;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

public class Board extends JPanel {
    // Settings modifiable in startup
    public BoardSettings settings;
    // Arrays of tiles and agents
    public Tile[][] tiles;
    public Agent[] agents;
    // Number of epochs so far
    private int epochs = 0;
    // Size of tiles to display
    private int tileSize;
    // Randomizer
    private Random rand = new Random();
    // Timer
    private Timer timer = new Timer();

    public Board(int size) {
        // Default board settings
        settings = new BoardSettings(100, 50, 50, true, 750);
        // Default tile settings
        int[][] initials = {{0, 10}};
        TileSettings tileSettings = new TileSettings(initials);
        // Save the size of each tile
        tileSize = size;
        // Initialize the tiles array
        tiles = new Tile[settings.getHeight()][settings.getWidth()];
        // Add new tiles to each location
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = new Tile(tileSettings);
            }
        }
        System.out.print("Initialized Tiles\n");

        // Initialize the agents array
        agents = new Agent[settings.getAgents()];
        // Put agents on the board
        for (int i = 0; i < agents.length; i++) {
            // Create new agent at i
            agents[i] = new Agent(tileSize, tiles);
            // Randomize agent locations
            int location = rand.nextInt(tiles.length * tiles[0].length);
            boolean placed = false;
            while (!placed) {
                // Get the tile at the location
                Tile tile = tiles[(int)Math.floor(location/settings.getWidth())][location % settings.getHeight()];
                // Check if the tile has an agent already
                if(tile.agent != null) {
                    // Get a new random location if the current one is full
                    location = rand.nextInt(tiles.length * tiles[0].length);
                } else {
                    // Associate the agent with the tile it is on
                    tile.agent = agents[i];
                    agents[i].tile = tile;
                    agents[i].y = (int)Math.floor(location/settings.getWidth());
                    agents[i].x = location % settings.getHeight();
                    placed = true;
                }
            }
        }
        System.out.print("Initialized Agents\n");
        // Set the schedule if applicable
        if (settings.autoEpoch) {
            schedule(settings.interval);
            System.out.print("Auto epoch enabled");
        }
    }

    // Checks if a tile has no agent
    public boolean isEmpty(int y, int x) {
        return tiles[y][x].agent == null;
    }

    // This function upkeeps each tile and agent
    public void next() {
        // Run upkeep on each tile and agent
        for (Tile[] tiles : tiles) {
            for (Tile tile: tiles) {
                // Perform the tile's upkeep
                tile.upkeep();
                // Perform the agent's upkeep
                if (tile.agent != null) {
                    tile.agent.upkeep();
                }
            }
        }
        // Increment epoch
        epochs++;
        // Run action on each agent
        for (Agent agent : agents) {
            agent.action();
        }
        // Run execute on each agent
        for (Agent agent : agents) {
            agent.execute();
        }
        // Repaint the board
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Change graphics to 2D graphics
        Graphics2D graphics2D = (Graphics2D) graphics;        
        // Render each agent in its proper location
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                // Check if a tile has an agent
                if (tiles[y][x].agent != null) {
                    // Draw each agent's sprite
                    graphics2D.drawImage(agents[0].sprite, x * tileSize, y * tileSize, tileSize, tileSize, null);
                }
            }
        }

        System.out.print("Rendering complete\n");
    }

    // This sets a timer after which to call next
    public void schedule(int ms) {
        // Create the task to call next
        TimerTask timerNext = new TimerTask() {
            @Override
            public void run() {
                next();
            }
        };
        // Schedule the timer
        timer.scheduleAtFixedRate(timerNext, ms, ms);
    }

    // This stops the timer loop set by schedule
    public void unschedule() {
        timer.cancel();
    }

}