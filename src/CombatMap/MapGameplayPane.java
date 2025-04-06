package CombatMap;

import GUI.RootContainer;
import MainPackage.Game;
import RandomEncounter.EncounterData;
import RandomEncounter.EncounterManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGameplayPane extends JPanel {
    private MapData mapData;
    private RootContainer rootContainer;
    private Map<String, Image> nodeIcons;
    private final int iconSize = 60;
    private MapNode hoveredNode = null;

    private BufferedImage[] circleFrames;
    private int currentFrame = 0;
    private Timer circleTimer;
    private MapNode selectedNode = null;
    private Runnable postAnimationAction;
    private float fadeAlpha = 1f;
    private Timer fadeTimer;


    public MapGameplayPane(RootContainer rootContainer) {
        setOpaque(false);
        this.rootContainer = rootContainer;
        this.nodeIcons = new HashMap<>();

        nodeIcons.put("combat", new ImageIcon("src/Resources/Icons/combat.png").getImage());
        nodeIcons.put("boss", new ImageIcon("src/Resources/Icons/boss.png").getImage());
        nodeIcons.put("default", new ImageIcon("src/Resources/Icons/default.png").getImage());
        nodeIcons.put("defeated", new ImageIcon("src/Resources/Icons/defeated.png").getImage());
        nodeIcons.put("encounter", new ImageIcon("src/Resources/Icons/encounter.png").getImage());
        nodeIcons.put("encounter_defeated", new ImageIcon("src/Resources/Icons/encounter_defeated.png").getImage());

        try {
            BufferedImage spriteSheet = ImageIO.read(new File("src/Resources/MiscAnimations/circleanimation.png"));
            circleFrames = new BufferedImage[14];
            for (int i = 0; i < 14; i++) {
                circleFrames[i] = spriteSheet.getSubimage(i * 60, 0, 60, 60);
            }
        } catch (IOException e) {
            System.err.println("⚠ Failed to load circle animation.");
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    handleNodeClick(e.getPoint());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoveredNode(e.getPoint());
            }
        });
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
        this.mapData.randomizeNodePositions(200);

        if (getParent() != null) {
            setBounds(getParent().getBounds());
        }

        repaint();
        fadeIn(null);

    }

    private void handleNodeClick(Point clickPoint) throws IOException {
        if (mapData == null || mapData.getNodes() == null) return;

        List<MapNode> availableNodes = mapData.getAvailableNodes();

        for (MapNode node : availableNodes) {
            if (node.isDefeated()) continue;

            int iconX = node.getX() - (iconSize / 2);
            int iconY = node.y - (iconSize / 2);
            Rectangle bounds = new Rectangle(iconX, iconY, iconSize, iconSize);

            if (bounds.contains(clickPoint)) {
                selectedNode = node;
                playNodeSelectAnimation(node, () -> performNodeAction(node));
                return;
            }
        }
    }

    private void playNodeSelectAnimation(MapNode node, Runnable onFinish) {
        currentFrame = 0;
        postAnimationAction = onFinish;

        if (circleTimer != null && circleTimer.isRunning()) {
            circleTimer.stop();
        }

        circleTimer = new Timer(75, e -> {
            currentFrame++;
            if (currentFrame >= circleFrames.length) {
                ((Timer) e.getSource()).stop();
                if (postAnimationAction != null) {
                    fadeOut(postAnimationAction);
                }
            }
            repaint();
        });
        circleTimer.start();
    }

    private void performNodeAction(MapNode node) {
        try {
            Game.currentNodeType = node.getType(); // Save node type globally
            if ("encounter".equals(node.type)) {
                EncounterData encounter = EncounterManager.loadRandomEncounter();
                if (encounter != null) {
                    ImageIcon background = new ImageIcon("src/Resources/RandomEvents/encounterbg.png");
                    Game.gui.encounterPanel.setBackgroundImage(background);
                    Game.gui.encounterPanel.displayEncounter(encounter);
                    Game.gui.showScreen(Game.gui.encounterPanel);
                    node.setDefeated(true);
                    repaint();
                    setVisible(false);
                } else {
                    System.err.println("⚠ No valid encounter loaded.");
                }
                return;
            }

            if (node.getCombatID() == -1) {
                Game.currentNodeType = "combat";
                MainPackage.NorthPanel encounter = rootContainer.startRandomFight(Game.randomCombatMaxWeight, Game.randomCombatMinWeight);
                if (encounter == null) {
                    System.err.println("⚠ Could not start randomized combat.");
                    return;
                }
                rootContainer.gameScreen.newFight(encounter);
            } else {
                Game.currentNodeType = "combat";
                rootContainer.gameScreen.newFight(rootContainer.startFight(node.getCombatID()));
            }

            rootContainer.showScreen(rootContainer.gameScreen);
            Game.gui.gameScreen.glassPane.setVisible(true);
            Game.gui.gameScreen.cardLayout.show(Game.gui.gameScreen.centerContainer, "main");
            Game.unslotAllCards();
            rootContainer.gameScreen.center.revalidate();
            rootContainer.gameScreen.center.repaint();

            node.setDefeated(true);

            if ("boss".equals(node.type)) {
                Game.currentNodeType = "boss";
                String mapId = mapData.getMapID();
                try {
                    int index = Integer.parseInt(mapId.replaceAll("[^0-9]", "")) - 1;
                    GameProgress.markDungeonCompleted(index);
                    GameProgress.unlockNextDungeon(index);
                } catch (Exception ex) {
                    System.err.println("Failed to parse map index from mapID: " + mapId);
                }
            }

            repaint();
            setVisible(false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateHoveredNode(Point mousePosition) {
        MapNode previousHovered = hoveredNode;
        hoveredNode = null;

        for (MapNode node : mapData.getAvailableNodes()) {
            int iconX = node.getX() - (iconSize / 2);
            int iconY = node.y - (iconSize / 2);
            Rectangle bounds = new Rectangle(iconX, iconY, iconSize, iconSize);

            if (bounds.contains(mousePosition)) {
                hoveredNode = node;
                break;
            }
        }

        if (hoveredNode != previousHovered) {
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapData == null || mapData.getNodes() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        float[] dashPattern = {10, 10};
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0));
        g2d.setColor(Color.BLACK);

        for (MapNode node : mapData.getNodes()) {
            if (!node.isDefeated()) continue;

            for (int connectedId : node.connections) {
                MapNode connectedNode = mapData.getNodeById(connectedId);
                if (connectedNode == null) continue;
                g2d.drawLine(node.getX(), node.y, connectedNode.getX(), connectedNode.y);
            }
        }

        for (MapNode node : mapData.getNodes()) {
            Image icon;
            if (node.isDefeated()) {
                icon = "encounter".equals(node.type) ?
                        nodeIcons.getOrDefault("encounter_defeated", nodeIcons.get("defeated")) :
                        nodeIcons.get("defeated");
            } else if (mapData.getAvailableNodes().contains(node)) {
                icon = nodeIcons.getOrDefault(node.type, nodeIcons.get("default"));
            } else {
                icon = toGrayscale(nodeIcons.getOrDefault(node.type, nodeIcons.get("default")));
            }

            int drawSize = (node == hoveredNode) ? (int) (iconSize * 1.25) : iconSize;
            int iconX = node.getX() - (drawSize / 2);
            int iconY = node.y - (drawSize / 2);

            g2d.drawImage(icon, iconX, iconY, drawSize, drawSize, this);
        }

        // Draw animation
        if (selectedNode != null && currentFrame < circleFrames.length) {
            BufferedImage frame = circleFrames[currentFrame];
            int size = (int)(iconSize * 1.5);
            int x = selectedNode.getX() - size / 2;
            int y = selectedNode.y - size / 2;
            g2d.drawImage(frame, x, y, size, size, this);
        }

        // Fade overlay
        if (fadeAlpha > 0f) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

    }

    public void fadeIn(Runnable afterFade) {
        fadeAlpha = 1f;
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

        fadeTimer = new Timer(30, e -> {
            fadeAlpha -= 0.05f;
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                ((Timer) e.getSource()).stop();
                if (afterFade != null) afterFade.run();
            }
            repaint();
        });
        fadeTimer.start();
    }

    public void fadeOut(Runnable afterFade) {
        fadeAlpha = 0f;
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

        fadeTimer = new Timer(30, e -> {
            fadeAlpha += 0.05f;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                ((Timer) e.getSource()).stop();
                if (afterFade != null) afterFade.run();
            }
            repaint();
        });
        fadeTimer.start();
    }


    private Image toGrayscale(Image originalImage) {
        if (originalImage == null) return null;

        BufferedImage colored = new BufferedImage(
                originalImage.getWidth(null),
                originalImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = colored.createGraphics();
        g2.drawImage(originalImage, 0, 0, null);
        g2.dispose();

        for (int y = 0; y < colored.getHeight(); y++) {
            for (int x = 0; x < colored.getWidth(); x++) {
                int rgba = colored.getRGB(x, y);
                Color col = new Color(rgba, true);
                int gray = (int) (0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue());
                Color grayColor = new Color(gray, gray, gray, col.getAlpha());
                colored.setRGB(x, y, grayColor.getRGB());
            }
        }

        return colored;
    }
}