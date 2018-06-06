package com.fourmen.tween;


import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fourmen.utils.TextureFadeImage;

public class TextureOpAccessor implements TweenAccessor<TextureFadeImage> {

    public static final int ALPHA = 0;

    @Override
    public int getValues(TextureFadeImage target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getOpacity();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(TextureFadeImage target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.setOpacity(newValues[0]);
                break;
            default:
                assert false;
        }
    }
}

