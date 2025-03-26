package CombatMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Loads map data from a JSON file and converts it into Java objects.
 */
public class MapLoader {
    /**
     * Loads a map from a JSON file.
     * @param filePath The path to the JSON file relative to the project root.
     * @return A MapData object containing the parsed map.
     */
    public static MapData loadMap(String filePath) {
        try {
            // Use the correct absolute path to load the JSON file
            File file = new File("src/" + filePath);

            // Check if the file exists
            if (!file.exists()) {
                throw new IOException("File not found: " + file.getAbsolutePath());
            }

            // Create ObjectMapper and parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, MapData.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }
}