package Entities;

import java.io.IOException;

public class GoblinAttackAnimation extends InPlaceAnimation {
    public GoblinAttackAnimation() throws IOException {
        super("/Resources/greenSlash.png", 52, 32, 5, 1, 23);
    }
}
