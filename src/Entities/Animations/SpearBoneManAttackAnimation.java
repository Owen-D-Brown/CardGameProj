package Entities.Animations;

import java.io.IOException;

public class SpearBoneManAttackAnimation extends InPlaceAnimation {
    public SpearBoneManAttackAnimation() throws IOException {
        // You can pass a 1x1 transparent PNG or something harmless
        super("/Resources/greenSlash.png", 1, 1, 1, 1, 0);
    }
}