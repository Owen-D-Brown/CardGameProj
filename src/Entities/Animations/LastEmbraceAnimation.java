package Entities.Animations;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LastEmbraceAnimation extends InPlaceAnimation {
    public LastEmbraceAnimation() throws IOException {
        super("/Resources/LastEmbraceAnimation/", 547, 483, 1, 1, 3);
    }

    @Override
    protected BufferedImage[] importSprites(String pathName, int cols, int rows, int spriteWidth, int spriteHeight) throws IOException {
        BufferedImage[] temp = new BufferedImage[27];

        for(int i = 1; i<=27; i++) {
            String path = "/Resources/LastEmbraceAnimation/"+ (i-1)+".png";
            System.out.println(path);
            temp[i-1] = ImageIO.read(getClass().getResourceAsStream(path));

        }
        return temp;
    }
}
