package com.fourmen.Actors;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    //instance variables
    public int health;
    public Vector2 position;

    //constructors
    public Entity() {
        health = 100;
        position = new Vector2(600, 300);
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
