package Filips_Tests;

import CombatMap.MapData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MapDataTest {

    @Test
    public void testMapLoadsFromJsonAndContainsBossNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/Resources/maps/map01.json");
        MapData mapData = mapper.readValue(file, MapData.class);

        assertNotNull(mapData, "MapData should not be null");
        assertNotNull(mapData.getNodes(), "Nodes should not be null");
        assertFalse(mapData.getNodes().isEmpty(), "Map should contain nodes");

        boolean hasBoss = mapData.getNodes().stream()
                .anyMatch(node -> "boss".equalsIgnoreCase(node.getType()));

        assertTrue(hasBoss, "Map should contain at least one boss node");
    }
}
