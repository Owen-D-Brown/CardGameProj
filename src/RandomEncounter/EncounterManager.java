package RandomEncounter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Manages the list of available encounters and handles triggering them.
 */
public class EncounterManager {

    private final List<EncounterData> allEncounters = new ArrayList<>();
    private final Random random = new Random();

    public EncounterManager() {
        // In the future, load from JSON
        // For now, this is where hardcoded encounters or test cases can go
    }

    /**
     * Adds a single encounter manually (useful for testing or editor tools).
     */
    public void addEncounter(EncounterData encounter) {
        allEncounters.add(encounter);
    }

    /**
     * Returns a random encounter from the current pool.
     */
    public static EncounterData loadRandomEncounter() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new FileInputStream("src/Resources/random_encounters.json");
            List<EncounterData> encounters = mapper.readValue(is, new TypeReference<List<EncounterData>>() {});
            if (encounters.isEmpty()) return null;

            Random random = new Random();
            return encounters.get(random.nextInt(encounters.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads encounters from JSON later — to be implemented.
     */
    public void loadEncountersFromJSON(String jsonFilePath) throws IOException {
        // Placeholder: this will be expanded to read from a JSON file
    }

    /**
     * Clears all stored encounters (can be helpful during dev or testing).
     */
    public void clearEncounters() {
        allEncounters.clear();
    }

    /**
     * Optional: get all loaded encounters (for debugging or UI).
     */
    public List<EncounterData> getAllEncounters() {
        return Collections.unmodifiableList(allEncounters);
    }
}