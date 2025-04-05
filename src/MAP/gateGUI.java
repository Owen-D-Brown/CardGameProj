package MAP;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class gateGUI extends JPanel {
    private BufferedImage backgroundImage;
//
    public gateGUI() {
        setLayout(null); // absolute positioning
        setPreferredSize(new Dimension(1000, 666));

        // background image loading
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Resources/gate/Bk.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Failed to load background image: " + e.getMessage());
        }

        // Image paths, array of 4
        String[] imagePaths = {
                "/Resources/gate/Buttoin.png",
                "/Resources/gate/Buttoin.png",
                "/Resources/gate/Buttoin.png",
                "/Resources/gate/Buttoin.png"
        };

        int buttonWidth = 125;
        int buttonHeight = 125;
        int spacing = 25;
        int totalWidth = (buttonWidth * 4) + (spacing * 3);
        int startX = (1000 - totalWidth) / 2;
        int y = (1195 - buttonHeight) / 2;

        for (int i = 0; i < 4; i++) {
            try {
                BufferedImage img = ImageIO.read(getClass().getResourceAsStream(imagePaths[i]));
                ImagePanel button = new ImagePanel(img);
                button.setBounds(startX + i * (buttonWidth + spacing), y, buttonWidth, buttonHeight);

                int buttonIndex = i; // Need a final or effectively final variable for inner class

                // making each button clickable
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //clickable cursor for iamge/button
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        System.out.println("Button " + (buttonIndex + 1) + " clicked!");
                        // action for the buttons here
                        switch (buttonIndex) {
                            case 0:
                                System.out.println("Action for Button 1");
                                break;
                            case 1:
                                System.out.println("Action for Button 2");
                                break;
                            case 2:
                                System.out.println("Action for Button 3");
                                break;
                            case 3:
                                System.out.println("Action for Button 4");
                                break;
                        }
                    }
                });

                add(button);
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Failed to load image at: " + imagePaths[i]);
            }
        }

        // Fading text
        FadingText fadingText = new FadingText("Enter A Gate");
        fadingText.setBounds(0, 40, 1000, 100); // setting width here, so it looks good
        add(fadingText);
        fadingText.startFadeIn();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // background silhouette for buttons, just a background for the actual buttons
        Graphics2D g2d = (Graphics2D) g.create();
        int silhouetteHeight = 70;
        int silhouetteWidth = 1000;
        int silhouetteX = (getWidth() - silhouetteWidth) / 2;
        int silhouetteY = (180 - silhouetteHeight) / 2;

        g2d.setColor(new Color(0, 0, 0, 150)); // transparent black
        g2d.fillRoundRect(silhouetteX, silhouetteY, silhouetteWidth, silhouetteHeight, 25, 25);
        g2d.dispose();

    }


    private static class ImagePanel extends JPanel {
        private final BufferedImage icon;
        private BufferedImage frame;

        public ImagePanel(BufferedImage icon) {
            this.icon = icon;
            setOpaque(false);

            try {
                frame = ImageIO.read(getClass().getResourceAsStream("/Resources/gate/wall.png"));
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Failed : " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // draw the frame
            if (frame != null) {
                g2d.drawImage(frame, 0, 0, getWidth(), getHeight(), this);
            }

            // draw the icon inside with padding
            if (icon != null) {
                int padding = 20;
                int iconWidth = getWidth() - 2 * padding;
                int iconHeight = getHeight() - 2 * padding;
                g2d.drawImage(icon, padding, padding, iconWidth, iconHeight, this);
            }

            g2d.dispose();
        }
    }

    // Faded text custom
    private class FadingText extends JComponent {
        private final String text; //storing text
        private float alpha = 0f;
        private Timer fadeTimer;

        public FadingText(String text) {
            this.text = text;
            setOpaque(false);
        }

        public void startFadeIn() { //check if the timer has started
            if (fadeTimer != null && fadeTimer.isRunning()) return;

            fadeTimer = new Timer(30, e -> { //timer ticks every 30 miliseconds
                alpha += 0.02f; //each tick increases alpha, * 0.2 and opcaity goes to 0 - 1
                if (alpha >= 1f) {
                    alpha = 1f;
                    ((Timer) e.getSource()).stop();
                }
                repaint();
            });

            fadeTimer.start(); //start the anjimation
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();



            //Font size here and styling
            g2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 48));
            //transparecy of what is being drawn
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); //new drawing goes over the exisiting content
            g2.setColor(new Color(135, 206, 250));

            //centering text
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() / 2) + fm.getAscent() / 2;

            //draw the String with (alpha opcaity applied
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gate Choice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new gateGUI());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

