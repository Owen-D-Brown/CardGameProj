package RandomEncounter;

import java.util.List;

/**
 * Holds the data for a single random encounter, including
 * image, description text, and the possible player choices.
 */
public class EncounterData {

    private String id; // Unique identifier for the encounter
    private String imagePath; // Path to the image to show
    private String description; // Main description text
    private List<EncounterChoice> choices; // List of possible actions

    public EncounterData() {
        // Default constructor needed for JSON deserialization
    }

    public EncounterData(String id, String imagePath, String description, List<EncounterChoice> choices) {
        this.id = id;
        this.imagePath = imagePath;
        this.description = description;
        this.choices = choices;
    }

    // === Getters ===
    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public List<EncounterChoice> getChoices() {
        return choices;
    }

    // === Setters ===
    public void setId(String id) {
        this.id = id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChoices(List<EncounterChoice> choices) {
        this.choices = choices;
    }
}
