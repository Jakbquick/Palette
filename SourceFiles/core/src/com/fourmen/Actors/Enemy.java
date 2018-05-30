package com.fourmen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import javax.swing.text.Position;

public class Enemy extends Entity {
    //constants
    private static final double ACCELERATION_CONSTANT = 0.2;
    private static final double DECELERATION_CONSTANT = 0.2;
    private final static float enemyWidth = 30;
    private final static float enemyHeight = 30;
    private float lastDirection = 0;
    private float lastLastDirection = 3;
    private enum PlayerState {
        MOVING, DASHING
    }

    //instance variables
    public Rectangle rectangle;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;       // how fast the player is currently going in x and y
    private int maxSpeed;               // the max speed a player can move
    private double acceleration;
    private double deceleration;
    private Vector2 direction;          // contains a x direction and y direction from -1 to 1
    private Vector2 dashDirection;
    private double dashSpeed;
    private double dashCooldown;
    private double dashDuration;
    private double dashDurationTimer;
    private double dashCooldownTimer;
    private PlayerState enemyState;

    //constructors
    public Enemy() {
        super();
        rectangle = new Rectangle(getX(), getY(), enemyWidth, enemyHeight);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        maxSpeed = 500;
        acceleration = ACCELERATION_CONSTANT * maxSpeed;
        deceleration = DECELERATION_CONSTANT * maxSpeed;
        direction = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        dashSpeed = 1000;
        dashCooldown = 0;
        dashDuration = .1;
        dashDurationTimer = 0;
        dashCooldownTimer = 0;
        enemyState = enemyState.MOVING;

    }

    //methods
    public void act() {
        updateDirection();
        switch (enemyState) {
            case MOVING:

                move();
                if(dashCooldownTimer <= 0)
                    checkDash();
                break;
            case DASHING:
                dash();
                if(dashDurationTimer <= 0) {
                    enemyState = enemyState.MOVING;
                }
                break;
        }
    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;

        int directionFactor = MathUtils.random(7);
        if (directionFactor == lastDirection) {
            if(directionFactor == lastLastDirection) {
                if(directionFactor < 7)
                    directionFactor++;
                else
                    directionFactor = 0;
            }
            else if(directionFactor < 7)
                directionFactor++;
            else
                directionFactor = 0;
        }

        lastLastDirection = lastDirection;

        if(directionFactor == 0) {
            direction.x -= 1;
            lastDirection = 0;
        }
        else if(directionFactor == 1) {
            direction.x -= 1;
            direction.y += 1;
            lastDirection = 1;
        }
        else if(directionFactor == 2) {
            direction.y += 1;
            lastDirection = 2;
        }
        else if(directionFactor == 3) {
            direction.x += 1;
            direction.y += 1;
            lastDirection = 3;
        }
        else if(directionFactor == 4) {
            direction.x += 1;
            lastDirection = 4;
        }
        else if(directionFactor == 5) {
            direction.x += 1;
            direction.y -= 1;
            lastDirection = 5;
        }
        else if(directionFactor == 6) {
            direction.y -= 1;
            lastDirection = 6;
        }
        else if(directionFactor == 7) {
            direction.x -= 1;
            direction.y -= 1;
            lastDirection = 7;
        }

        direction.nor();

        targetSpeed.x = (int) (direction.x * maxSpeed);
        targetSpeed.y = (int) (direction.y * maxSpeed);

    }

    private void checkDash() {

        int dashChance  = MathUtils.random(4);

        if(dashChance == 3) {
            enemyState = enemyState.DASHING;
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            currentSpeed = new Vector2(0,0);
            dashDurationTimer = dashDuration;
            dashCooldownTimer = dashCooldown;
        }
    }

    private void move() {
        Vector2 curDir = new Vector2(Math.signum(targetSpeed.x - currentSpeed.x), Math.signum(targetSpeed.y - currentSpeed.y));

        if(targetSpeed.x == 0) {                                            // checks want to be stopped on the x axis
            currentSpeed.x += deceleration * curDir.x;                      // decelerates the player's x movement speed
        }
        else {
            currentSpeed.x += acceleration * curDir.x;                      // accelerates the player's x movement speed
        }

        if(targetSpeed.y == 0) {                                            // checks want to be stopped on the y axis
            currentSpeed.y += deceleration * curDir.y;                      // decelerates the player's x movement speed
        }
        else {
            currentSpeed.y += acceleration * curDir.y;                      // accelerates the player's x movement speed
        }

        if(Math.signum(targetSpeed.x - currentSpeed.x) != curDir.x) {       // checks if the player passes the x targetSpeed
            currentSpeed.x = targetSpeed.x;
        }
        if(Math.signum(targetSpeed.y - currentSpeed.y) != curDir.y) {       // checks if the player passes the y targetSpeed
            currentSpeed.y = targetSpeed.y;
        }

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());        // changes the x position of the entity
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());        // changes the y position of the entity
    }

    private void dash() {
        currentSpeed.x = (float) (dashSpeed * dashDirection.x);
        currentSpeed.y = (float) (dashSpeed * dashDirection.y);

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    public float getEnemyWidth(){
        return enemyWidth;
    }
    public float getEnemyHeight(){
        return enemyHeight;
    }

    private void updateRectangle() {
        rectangle.setX(position.x);
        rectangle.setY(position.y);

    }

    public void update(float delta) {
        dashDurationTimer -= delta;
        dashCooldownTimer -= delta;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        updateRectangle();
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }



}