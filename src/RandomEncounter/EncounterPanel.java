package RandomEncounter;

import MainPackage.Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EncounterPanel extends JPanel {

    private JLabel encounterImage;
    private JTextArea descriptionText;
    private JPanel choicesPanel;
    private EncounterData currentEncounter;
    private Image backgroundImage; // New field for background image

    public EncounterPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 1000));
        setBackground(Color.BLACK); // Fallback

        // Encounter image
        encounterImage = new JLabel();
        encounterImage.setBounds(50, 150, 400, 400);
        add(encounterImage);

        // Description text
        descriptionText = new JTextArea();
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setOpaque(false);
        descriptionText.setForeground(Color.WHITE);
        descriptionText.setFont(new Font("Serif", Font.PLAIN, 18));
        descriptionText.setBounds(500, 150, 450, 400);
        add(descriptionText);

        // Choices panel
        choicesPanel = new JPanel();
        choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
        choicesPanel.setOpaque(false);
        choicesPanel.setBounds(250, 600, 500, 400);
        add(choicesPanel);
    }

    //New method to assign the background
    public void setBackgroundImage(ImageIcon icon) {
        this.backgroundImage = icon.getImage();
        repaint();
    }

    //Override to draw background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void displayEncounter(EncounterData encounter) {
        this.currentEncounter = encounter;

        // Set image
        if (encounter.getImagePath() != null) {
            ImageIcon icon = new ImageIcon(encounter.getImagePath());
            encounterImage.setIcon(icon);
        } else {
            encounterImage.setIcon(null);
        }

        // Set description
        descriptionText.setText(encounter.getDescription());

        // Setup choices
        updateChoices(encounter.getChoices());
    }

    public void setDescription(String text) {
        descriptionText.setText(text);
    }

    public void updateChoices(List<EncounterChoice> choices) {
        choicesPanel.removeAll();

        for (EncounterChoice choice : choices) {
            JButton button = new JButton(choice.getLabel());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(400, 40));

            button.addActionListener(e -> {
                // Update description
                setDescription(choice.getOutcomeText());

                // Process effect
                String effect = choice.getEffectType();
                int value = choice.getEffectValue();

                switch (effect) {
                    case "gold":
                        Game.player.giveGold(value); // increase gold count
                        break;
                    case "removegold":
                        Game.player.removeGold(value); // reduce gold count
                        break;
                    case "heal":
                        Game.player.heal(value); // this would increase current HP
                        break;
                    case "maxHP":
                        Game.player.increaseMaxHP(value); // add to max HP
                        break;
                    case "chanceGoldOrPenalty":
                        boolean success = Game.player.tryChestEncounter(value, 30); // value = HP penalty, 30 = gold reward
                        if (success) {
                            setDescription("You pry open the chest and find a small pile of gold! (+30 Gold)");
                            updateChoices(List.of());
                            appendButton("Continue", () -> Game.gui.showScreen(Game.gui.mapScreen));
                        } else {
                            setDescription("A trap! The chest blasts you with a curse. (-" + value + " Max HP)");
                            updateChoices(List.of(
                                    new EncounterChoice("Try again", "", "chanceGoldOrPenalty", value),
                                    new EncounterChoice("Leave it", "You step back, defeated.", "none", 0)
                            ));
                        }
                        return; // prevent showing the Continue button below
                }

                // Show Continue button
                updateChoices(List.of());
                appendButton("Continue", () -> Game.gui.showScreen(Game.gui.mapScreen));
            });


            choicesPanel.add(Box.createVerticalStrut(10));
            choicesPanel.add(button);
        }

        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    public void appendButton(String label, Runnable action) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(400, 40));
        btn.addActionListener(e -> {
            action.run();
            Game.gui.showScreen(Game.gui.mapScreen); // Return to map
        });

        choicesPanel.add(Box.createVerticalStrut(10));
        choicesPanel.add(btn);
        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    public EncounterData getCurrentEncounter() {
        return currentEncounter;
    }
}
