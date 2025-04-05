package CombatMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Tracks which dungeons are unlocked and completed.
 */
public class GameProgress {

    private static final boolean[] dungeonUnlocked = { true, false, false, false };
    private static final Set<Integer> completedDungeons = new HashSet<>();

    //Check if a dungeon is unlocked and NOT completed
    public static boolean isUnlocked(int index) {
        return index >= 0 && index < dungeonUnlocked.length
                && dungeonUnlocked[index]
                && !completedDungeons.contains(index);
    }

    //Unlock the next dungeon after completing one
    public static void unlockNextDungeon(int currentIndex) {
        if (currentIndex + 1 < dungeonUnlocked.length) {
            dungeonUnlocked[currentIndex + 1] = true;
            System.out.println("Dungeon " + (currentIndex + 2) + " unlocked!");
        }
    }

    //Get number of unlocked dungeons
    public static int getUnlockedCount() {
        int count = 0;
        for (boolean b : dungeonUnlocked) {
            if (b) count++;
        }
        return count;
    }

    //Mark a dungeon as completed
    public static void markDungeonCompleted(int index) {
        completedDungeons.add(index);
        System.out.println("Dungeon " + (index + 1) + " marked as completed.");
    }

    //Check if a dungeon has already been completed
    public static boolean isDungeonCompleted(int index) {
        return completedDungeons.contains(index);
    }
}

