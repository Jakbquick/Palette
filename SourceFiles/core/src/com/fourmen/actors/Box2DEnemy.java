package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Box2DEnemy extends Entity {
    //constants
    private final static float BOX2D_SCALE = .5f;
    private static final double ACCELERATION_CONSTANT = 0.2;
    private static final double DECELERATION_CONSTANT = 0.2;
    private final static float ENEMY_WIDTH = 30;
    private final static float ENEMY_HEIGHT = 30;
    private enum PlayerState {
        MOVING, DASHING
    }

    //instance variables
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
    private Box2DPlayer player;

    private Body body;

    //constructors
    public Box2DEnemy(World world, Box2DPlayer myPlayer) {
        super();
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
        player = myPlayer;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape square = new PolygonShape();
        square.setAsBox(ENEMY_WIDTH * BOX2D_SCALE, ENEMY_HEIGHT * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;

        body.createFixture(fixtureDef);

        square.dispose();

    }

    //methods
    public void act() {
        updateDirection();
        switch (enemyState) {
            case MOVING:
                //move();
                if(dashCooldownTimer <= 0)
                    checkDash();
                //if(standingCooldownTimer <= 0)
                //playerState = playerState.STANDING;
                break;
            case DASHING:
                //dash();
                if(dashDurationTimer <= 0) {
                    enemyState = enemyState.MOVING;
                }
                break;
        }
        updatePosition();
    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;
        if(distanceBetween(player, getX(), getY()) > 800) {
            if (player.getX() > getX())
                direction.x += 1;
            else if (player.getX() < getX())
                direction.x -= 1;

            if (player.getY() > getY())
                direction.y += 1;
            else if (player.getY() < getY())
                direction.y -= 1;
        }
        else {
            int directionFactor = MathUtils.random(7);
            if(directionFactor == 0) {
                direction.x -= 1;
            }
            else if(directionFactor == 1) {
                direction.x -= 1;
                direction.y += 1;
            }
            else if(directionFactor == 2) {
                direction.y += 1;
            }
            else if(directionFactor == 3) {
                direction.x += 1;
                direction.y += 1;
            }
            else if(directionFactor == 4) {
                direction.x += 1;
            }
            else if(directionFactor == 5) {
                direction.x += 1;
                direction.y -= 1;
            }
            else if(directionFactor == 6) {
                direction.y -= 1;
            }
            else if(directionFactor == 7) {
                direction.x -= 1;
                direction.y -= 1;
            }
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

    private double distanceBetween(Box2DPlayer player1, float xVal, float yVal){
        return Math.sqrt(Math.pow((xVal - player1.getX()), 2) + Math.pow((yVal - player1.getY()), 2));
    }

    public float getEnemyWidth(){
        return ENEMY_WIDTH;
    }

    public float getEnemyHeight(){
        return ENEMY_HEIGHT;
    }

    public Body getPlayerBody(){
        return body;
    }

    public void updatePosition() {
        body.setTransform(position, 0);
    }

    public void updateTimers(float delta) {
        dashDurationTimer -= delta;
        dashCooldownTimer -= delta;
    }
}