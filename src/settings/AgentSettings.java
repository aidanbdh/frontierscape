package src.settings;

public class AgentSettings {
    public int[] vision;
    public int[][] resources;
    public int[][] metabolism;
    
    public AgentSettings(int[] v, int[][] r, int[][] m) {
        vision = v;
        resources = r;
        metabolism = m;
    }
}
