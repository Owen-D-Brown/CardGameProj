package Entities;

import MainPackage.Config;

import java.io.IOException;

public class SpearBoneManAttackAnimation extends InPlaceAnimation {
    public SpearBoneManAttackAnimation() throws IOException {
        super("/Resources/greenSlash.png", 52, 32, 5, 1, (int)(50* Config.scaleFactor));
    }
}