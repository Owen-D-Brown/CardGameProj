package CombatMap;

import java.util.HashMap;
import java.util.Map;

public class CombatMapManager {

    //Cache of loaded maps: key = filepath, value = loaded map instance
    private static final Map<String, MapData> loadedMaps = new HashMap<>();

    /**
     * Loads a map from the cache if it's been used before, otherwise loads from disk.
     */
    public static MapData getOrLoadMap(String path) {
        if (loadedMaps.containsKey(path)) {
            return loadedMaps.get(path);
        }

        MapData loaded = MapLoader.loadMap(path);
        loadedMaps.put(path, loaded);
        return loaded;
    }
}
