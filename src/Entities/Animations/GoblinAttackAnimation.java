package Entities.Animations;

import MainPackage.Config;

import java.io.IOException;

public class GoblinAttackAnimation extends InPlaceAnimation {
    public GoblinAttackAnimation() throws IOException {
        super("/Resources/greenSlash.png", 52, 32, 5, 1, (int)(2* Config.scaleFactor));
    }
}
