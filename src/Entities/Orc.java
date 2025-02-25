package Entities;

public class Orc extends Enemy {

    private boolean isEnraged = false; // Tracks if the Orc has entered rage mode

    public Orc() {
        super(30, 10, 5, 2, 1); // (HP, Attack, Defense, Agility, Speed)
    }

    @Override
    public String getEnemyType() {
        return "Orc";
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage); // Take damage normally

        // If health drops to 14 or less and rage hasn't triggered yet
        if (currentHealth <= 14 && !isEnraged) {
            isEnraged = true; // Prevents multiple rage triggers
            attackPower += 20; // Boost attack power by 20
            System.out.println("Orc enters a berserk rage! Attack power increased to " + attackPower);
        }
    }
}
