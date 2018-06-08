package com.fourmen.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fourmen.tween.FloatAccessor;

public class PlayerHealth {
    private SpriteBatch batch;
    private Float previous, current;
    private Texture playerBar,playerInside;
    TweenManager tweenManager = new TweenManager();
    private float x = 25;
    private float y = Gdx.graphics.getHeight() - 180;

    public PlayerHealth(SpriteBatch batch, float health){
        this.batch = batch;
        previous = health;
        playerBar = new Texture("Images/Healthbars/playerbar.png");
        playerInside = new Texture("Images/Healthbars/playerInside.png");
        Tween.registerAccessor(Float.class, new FloatAccessor());
    }
    /**
     * this method allows the bar to tween to whatever the Entity's health is updated
     * to
     * @param current current health of the entity
     */
    public void update(float current,float delta){
        this.current = current;
        if(current == previous){

        }
        else{
            Tween.set(previous,0).target(previous).start(tweenManager);
            Tween.to(previous,0,.2f).target(current).start(tweenManager);
        }
        tweenManager.update(delta);
    }
    public void draw(){
        batch.draw(playerInside,50 + x,18 + y,(previous * 2.88f),20);
        batch.draw(playerBar, x,y,playerBar.getWidth(),playerBar.getHeight());
    }

    public void dispose(){
        playerBar.dispose();
    }
}
