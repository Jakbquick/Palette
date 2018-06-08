package com.fourmen.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Entity extends Actor {
    //instance variables
    protected final static float INV_COOLDOWN = .05f;

    public int health;
    public Vector2 position;
    private float width;
    private float height;
    protected int fixtureCollisions;
    protected boolean invincible;
    protected float invTimer;

    private PlayerBounds playerBounds;

    //constructors
    public Entity(int hp, PlayerBounds myPlayerBounds, float myWidth, float myHeight, Vector2 position) {
        health = hp;
        this.position = new Vector2(position);
        playerBounds = myPlayerBounds;
        width = myWidth;
        height = myHeight;
        fixtureCollisions = 0;
        invincible = false;
        invTimer = 0;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public Entity(int hp) {
        health = hp;
    }

    //methods
    public int getHealth() {
        return health;
    }

    public abstract void act();

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setX(float myX) {
        position.x = myX;
    }

    public void setY(float myY) {
        position.y = myY;
    }

    public Vector2 getPosition(){
        return position;
    }
    public void subractHealth(int value){
        health -= value;
    }
    protected void updateHealth() {
        if (fixtureCollisions > 0 && !invincible && invTimer <= 0) {
            health -= 10;
            invTimer = INV_COOLDOWN;
        }
    }

    public void update(float delta) {
        invTimer -= delta;
    }

    public void updateCollisions(int collisions) {
        fixtureCollisions += collisions;
    }

    protected void blockLeavingTheWorld() {
        setPosition(MathUtils.clamp(getX(),playerBounds.getW1()+ (.5f* width),playerBounds.getWidth()+ playerBounds.getW1() - width + (.5f * width)),
                MathUtils.clamp(getY(), playerBounds.getH2() + (.5f * height), playerBounds.getHeight() + playerBounds.getH2() - height + (.5f *height)));
    }

    //public abstract void dispose() ;

}
