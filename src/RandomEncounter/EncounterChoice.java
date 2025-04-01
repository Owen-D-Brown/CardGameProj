package RandomEncounter;

/**
 * Represents a single button choice in a random encounter.
 */
public class EncounterChoice {
    private String label;        // Text to show on the button
    private String outcomeText;  // Text to show after this choice is selected
    private String effectType;   // Type of effect, e.g., "gainGold", "loseHP"
    private int effectValue;     // Value for the effect (e.g., amount of gold)

    public EncounterChoice() {
        // Default constructor for JSON parsing
    }

    public EncounterChoice(String label, String outcomeText, String effectType, int effectValue) {
        this.label = label;
        this.outcomeText = outcomeText;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }

    // === Getters ===
    public String getLabel() {
        return label;
    }

    public String getOutcomeText() {
        return outcomeText;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getEffectValue() {
        return effectValue;
    }

    // === Setters ===
    public void setLabel(String label) {
        this.label = label;
    }

    public void setOutcomeText(String outcomeText) {
        this.outcomeText = outcomeText;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public void setEffectValue(int effectValue) {
        this.effectValue = effectValue;
    }
}
