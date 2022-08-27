package src;

import src.settings.TileSettings;

public class Tile {
    // Reference to what agent is on the tile
    public Agent agent = null;
    // Resources on the tile
    private Resource[] resources;
    
    public Tile(TileSettings settings) {
        // Initialize resource array
        resources = new Resource[settings.resources.length];
        // Save each resource
        for (int i = 0; i < settings.resources.length; i++) {
            resources[i] = new Resource(settings.resources[i], settings.initials[i][0], settings.initials[i][1]);
        }
        
    };

    public void upkeep() {
        // Upkeep each resource
        for (Resource resource : resources) {
            resource.upkeep();
        };
    };

    public int inspect(String resource) {
        // Find the correct index of resource to return
        for(int i = 0; i < resources.length; i++) {
            if (resources[i].name == resource) {
                return inspect(i);
            }
        }
        // This should never happen
        return -1;
    }

    // Inspect a resource based on index
    public int inspect(int i) {
        return resources[i].inspect();
    }

    // Inspect all resources
    public int[] inspect() {
        // Array of resource data to return
        int[] data = new int[resources.length];
        // Inspect all resources
        for(int i = 0; i < resources.length; i++) {
            data[i] = inspect(i);
        }
        // Return the inspected resources
        return data;
    }

    public int interact(int resource) {
        return interact(resource, -1);
    }

    public int interact(int resource, int amount) {
        // Check for bad index or no resource available
        if (resource >= resources.length || resources[resource] == null)
            return 0;
        // Harvest specified resources
        return resources[resource].harvest(amount);
    }
    
}
