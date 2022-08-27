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
        // Find the correct resource to return
        for(int i = 0; i < resources.length; i++) {
            if (resources[i].name == resource) {
                return resources[i].inspect();
            }
        }
        // This should never happen
        return -1;
    }

    public int interact(String resource, int amount) {
        // Harvest specified resources
        for(int i = 0; i < resources.length; i++) {
            if (resources[i].name == resource) {
                return resources[i].harvest();
            }
        }
        // Return 0 if none of that resource available
        return 0;
    }
    
}
