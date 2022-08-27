package src;

import java.util.Random;

public class Resource {
    Random rand = new Random();
    public String name;
    public int cap;
    public int value;

    public Resource(String label, int min, int max) {
        name = label;
        value = rand.nextInt(max - min) + min;
        cap = value;
    }

    public void upkeep() {
        value = cap;
    };

    public int inspect() {
        return value;
    };

    // Harvest max if no specified value
    public int harvest() {
        return harvest(-1);
    }

    public int harvest(int amount) {
        // Check for complete harvest
        if (amount == -1) {
            amount = value;
        }
        // Reduce the amount available 
        value -= amount;
        // Check for attempted overharvesting
        if (value < 0) {
            amount += value; // value is negative
            value = 0;
        }
        // Return the amount harvested
        return amount;
    }

}
