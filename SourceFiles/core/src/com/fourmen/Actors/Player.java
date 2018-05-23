package com.fourmen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    //instance variables
    private Rectangle rectangle;
    private Vector2 targetSpeed;
    private Vector2 currentSpeed;
    private int maxSpeed;
    private double acceleration;
    private double deceleration;
    private Vector2 direction;

    //constructors
    public Player() {
        super();
        rectangle = new Rectangle(getX(), getY(), 15, 20);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        maxSpeed = 400;
        acceleration = .5 * maxSpeed;
        deceleration = .2 * maxSpeed;
        direction = new Vector2(0, 0);

    }

    //methods
    public void act() {
        updateDirection();
        move();
        updateRectangle();
    }

    public void updateDirection() {
        direction.x = 0;
        direction.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
        }

        direction.nor();

        targetSpeed.x = (int) (direction.x * maxSpeed);
        targetSpeed.y = (int) (direction.y * maxSpeed);

    }

    private void move() {
        Vector2 curDir = new Vector2(Math.signum(targetSpeed.x - currentSpeed.x), Math.signum(targetSpeed.y - currentSpeed.y));

        if(targetSpeed.x == 0) {
            currentSpeed.x += deceleration * curDir.x;
        }
        else {
            currentSpeed.x += acceleration * curDir.x;
        }

        if(targetSpeed.y == 0) {
            currentSpeed.y += deceleration * curDir.y;
        }
        else {
            currentSpeed.y += acceleration * curDir.y;
        }

        if(Math.signum(targetSpeed.x - currentSpeed.x) != curDir.x) {
            currentSpeed.x = targetSpeed.x;
        }
        if(Math.signum(targetSpeed.y - currentSpeed.y) != curDir.y) {
            currentSpeed.y = targetSpeed.y;
        }

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    private void updateRectangle() {
        rectangle.setX(position.x);
        rectangle.setY(position.y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

}
