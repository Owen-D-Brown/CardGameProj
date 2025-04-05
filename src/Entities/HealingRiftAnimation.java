package Entities;

import java.awt.*;
import java.io.IOException;

public class HealingRiftAnimation extends InPlaceAnimation {
    public HealingRiftAnimation() throws IOException {
        super("../Resources/HealingRift/sprites.png", 72, 72, 8, 1, 7);
        setSize(w*3, h*3);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMoving) {

            g.drawImage(sprites[aniIndex], x, y, w*3, h*3, null);
        }
    }
}
