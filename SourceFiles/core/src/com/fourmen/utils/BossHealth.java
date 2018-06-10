package com.fourmen.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fourmen.tween.FloatAccessor;

public class BossHealth {
    private SpriteBatch batch;
    private Float previous, current;
    private Texture enemyBar,enemyInside;
    TweenManager tweenManager = new TweenManager();
    private float x;
    private float y;

    public BossHealth(SpriteBatch batch, float health){
        this.batch = batch;
        previous = health;
        enemyBar = new Texture("Images/Healthbars/bossbar.png");
        enemyInside = new Texture("Images/Healthbars/bossInner.png");
        x = 22;
        y = Gdx.graphics.getHeight() - 240;
        Tween.registerAccessor(Float.class, new FloatAccessor());
    }
    /**
     * this method allows the bar to tween to whatever the Entity's health is updated
     * to
     * @param current current health of the entity
     */
    public void update(float current,float delta){
        this.current = current;
        /*
        if(current == previous){

        }
        else{
            Tween.set(previous,0).target(previous).start(tweenManager);
            Tween.to(previous,0,.2f).target(current).start(tweenManager);
        }
        */
        previous = current;
        tweenManager.update(delta);
    }
    public void draw(){
        batch.draw(enemyInside,58f + x,25f + y,(previous * (.287f)),20f);
        batch.draw(enemyBar, x,y,enemyBar.getWidth(),enemyBar.getHeight());
    }

    public void dispose(){
        enemyBar.dispose();
        enemyInside.dispose();
    }
}
