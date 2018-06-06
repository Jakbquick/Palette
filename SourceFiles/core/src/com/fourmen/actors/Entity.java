package com.fourmen.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Entity extends Actor {
    //instance variables
    public int health;
    public Vector2 position;

    //constructors
    public Entity() {
        health = 100;
        position = new Vector2(400, 400);
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

}
