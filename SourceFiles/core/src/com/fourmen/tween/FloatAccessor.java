package com.fourmen.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FloatAccessor implements TweenAccessor<Float> {

    public static final int ALPHA = 0;

    @Override
    public int getValues(Float target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Float target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target = newValues[0];
                break;
            default:
                assert false;
        }
    }
}

