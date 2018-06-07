package com.fourmen.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fourmen.utils.TextureFadeImage;

public class TextureAccessor implements TweenAccessor<TextureRegion> {

    public static final int ALPHA = 0;

    @Override
    public int getValues(TextureRegion target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                //returnValues[0] = target.getOpacity();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(TextureRegion target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                //target.setOpacity(newValues[0]);
                break;
            default:
                assert false;
        }
    }
}
