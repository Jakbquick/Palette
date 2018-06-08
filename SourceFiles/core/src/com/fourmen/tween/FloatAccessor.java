package com.fourmen.tween;

import aurelienribon.tweenengine.TweenAccessor;

public class FloatAccessor implements TweenAccessor<Float> {
    public static final int VALUE = 0;
    @Override
    public int getValues(Float target, int tweenType, float[] returnValues) {
        switch(tweenType){
            case 0:
                returnValues[0] = target.floatValue();
                return 1;
                default:
                    assert false;
                    return  -1;
        }
    }

    @Override
    public void setValues(Float target, int tweenType, float[] newValues) {
        switch (tweenType){
            case VALUE:
                target = new Float(newValues[0]);
            default:
                assert false;
        }
    }
}
