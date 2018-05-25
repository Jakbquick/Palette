package com.fourmen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import javax.swing.text.Position;

public class Player extends Entity {
    //constants
    private static final double ACCELERATION_CONSTANT = .8;
    private static final double DECELERATION_CONSTANT = .8;
    private final static float playerWidth = 15;
    private final static float playerHeight = 20;
    private enum PlayerState {
        MOVING, DASHING
    }

    //instance variables
    private Rectangle rectangle;
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
    private PlayerState playerState;


    //constructors
    public Player() {
        super();
        rectangle = new Rectangle(getX(), getY(), playerWidth, playerHeight);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        maxSpeed = 500;
        acceleration = ACCELERATION_CONSTANT * maxSpeed;
        deceleration = DECELERATION_CONSTANT * maxSpeed;
        direction = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        dashSpeed = 2000;
        dashCooldown = .8;
        dashDuration = .1;
        dashDurationTimer = 0;
        dashCooldownTimer = 0;
        playerState = playerState.MOVING;

    }
    //methods
    public void act() {
        updateDirection();
        switch (playerState) {
            case MOVING:

                move();
                if(dashCooldownTimer <= 0)
                    checkDash();
                break;
            case DASHING:
                dash();
                if(dashDurationTimer <= 0) {
                    playerState = playerState.MOVING;
                }
                break;
        }

        updateRectangle();
    }

    private void updateDirection() {
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

    private void checkDash() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            playerState = playerState.DASHING;
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

    public float getPlayerWidth(){
        return playerWidth;
    }
    public float getPlayerHeight(){
        return playerHeight;
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
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

}
