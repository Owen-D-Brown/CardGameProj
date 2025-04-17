package Filips_Tests;

import RandomEncounter.EncounterChoice;
import RandomEncounter.EncounterData;
import RandomEncounter.EncounterManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncounterManagerTest {

    @Test
    public void testLoadRandomEncounterReturnsValidData() {
        EncounterData encounter = EncounterManager.loadRandomEncounter();

        assertNotNull(encounter, "Encounter should not be null");

        assertNotNull(encounter.getDescription(), "Encounter description should not be null");
        assertFalse(encounter.getChoices().isEmpty(), "Encounter should have at least one choice");
    }
    @Test
    public void testEncounterChoiceGoldEffect() {
        EncounterChoice choice = new EncounterChoice("Take gold", "You get gold", "gold", 50);
        assertEquals("gold", choice.getEffectType());
        assertEquals(50, choice.getEffectValue());
    }
}
