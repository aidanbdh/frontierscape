package src;

// This class holds settings related to the board state.
public class BoardSettings {
    // These settings are not modifiable after initialization
    private int agents; // Number of agents initially on the board
    private int width; // Number of tiles wide the board is
    private int height; // Number of tiles high the board is
    // These settings can change anytime
    public boolean autoEpoch; // Flag for if the board should auto advance epochs
    public int interval; // Time in ms between epochs if autoEpoch is on

    public BoardSettings(int agentsInit, int widthInit, int heightInit, boolean autoEpochInit, int intervalInit) {
        agents = agentsInit;
        width = widthInit;
        height = heightInit;
        autoEpoch = autoEpochInit;
        interval = intervalInit;
    }

    public int getAgents() {
        return agents;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
