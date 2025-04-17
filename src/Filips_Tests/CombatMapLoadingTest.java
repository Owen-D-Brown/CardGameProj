package Filips_Tests;

import CombatMap.MapData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CombatMapLoadingTest {

    @Test
    public void testMapLoadsFromJsonSuccessfully() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/Resources/maps/map01.json");

        MapData mapData = mapper.readValue(file, MapData.class);
        assertNotNull(mapData);
        assertNotNull(mapData.getNodes());
        assertFalse(mapData.getNodes().isEmpty());
    }

    @Test
    public void testBossNodeExistsInMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/Resources/maps/map01.json");
        MapData mapData = mapper.readValue(file, MapData.class);

        boolean bossExists = mapData.getNodes().stream()
                .anyMatch(node -> "boss".equalsIgnoreCase(node.getType()));

        assertTrue(bossExists, "Map should contain a boss node.");
    }

    @Test
    public void testGetNodeByIdReturnsCorrectNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MapData mapData = mapper.readValue(new File("src/Resources/maps/map01.json"), MapData.class);

        int targetId = mapData.getNodes().get(0).id;
        assertEquals(targetId, mapData.getNodeById(targetId).id);
    }
}
