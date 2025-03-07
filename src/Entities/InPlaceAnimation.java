package Entities;

import java.io.IOException;

public class InPlaceAnimation extends Animation {

    public InPlaceAnimation(String filePath, int w, int h, int col, int row, int aniSpeed) throws IOException {
        super(filePath, w, h, col, row, aniSpeed);
    }

    public void placeAnimation(int x, int y)  {
        this.x = x;
        this.y = y;
    }
}
