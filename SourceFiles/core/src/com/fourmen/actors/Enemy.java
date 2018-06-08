package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.fourmen.actors.Entity;
import com.fourmen.actors.Player;

import javax.swing.text.Position;
import javax.xml.stream.Location;

public class Enemy extends Entity {
    //constants
    private static final double ACCELERATION_CONSTANT = .7;
    private static final double DECELERATION_CONSTANT = .3;
    private static final float ENEMY_SIZE = .50f;
    private final static float enemyWidth = 300 * ENEMY_SIZE; //163    40
    private final static float enemyHeight = 300 * ENEMY_SIZE; //251
    private Player player = new Player();

    private enum PlayerState {
        MOVING, CHARGING
    }

    //instance variables
    public Rectangle rectangle;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;       // how fast the player is currently going in x and y
    private int maxSpeed;               // the max speed a player can move
    private double acceleration;
    private double deceleration;
    private Vector2 direction;          // contains a x direction and y direction from -1 to 1
    private Vector2 chargeDirection;
    private double chargeSpeed;
    private double chargeDuration;
    private double chargeDurationTimer;
    private double chargeCooldownTimer;
    private PlayerState enemyState;

    //constructors
    public Enemy() {
        super(0, null, 0, 0, null);
        rectangle = new Rectangle(getX(), getY(), enemyWidth, enemyHeight);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        maxSpeed = 400;
        acceleration = ACCELERATION_CONSTANT * maxSpeed;
        deceleration = DECELERATION_CONSTANT * maxSpeed;
        direction = new Vector2(0, 0);
        chargeDirection = new Vector2(0, 0);
        chargeSpeed = 1000;
        chargeDuration = .4;
        chargeDurationTimer = 0;
        chargeCooldownTimer = 0;
        enemyState = enemyState.MOVING;

    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;

        if(distanceBetween(player, getX(), getY()) > 500) {
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

    //methods
    public void act() {
        updateDirection();
        switch (enemyState) {
            case MOVING:
                move();
                if(chargeCooldownTimer <= 0)
                    checkCharge();
                //if(standingCooldownTimer <= 0)
                //playerState = playerState.STANDING;
                break;
            case CHARGING:
                charge();
                if(chargeDurationTimer <= 0) {
                    enemyState = enemyState.MOVING;
                }
                break;
        }
    }

    private void checkCharge() {
        if(distanceBetween(player, getX(), getY()) > 200 && distanceBetween(player, getX(), getY()) < 500) {
                enemyState = enemyState.CHARGING;
                chargeDirection.x = direction.x;
                chargeDirection.y = direction.y;

                currentSpeed = new Vector2(0, 0);
                chargeDurationTimer = chargeDuration;
                //chargeCooldownTimer = chargeCooldown;
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

    private void charge() {
        currentSpeed.x = (float) (chargeSpeed * chargeDirection.x);
        currentSpeed.y = (float) (chargeSpeed * chargeDirection.y);

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    private double distanceBetween(Player player1, float xVal, float yVal){
        return Math.sqrt(Math.pow((xVal - player1.getX()), 2) + Math.pow((yVal - player1.getY()), 2));
    }

    public float getEnemyWidth(){
        return enemyWidth;
    }
    public float getEnemyHeight(){
        return enemyHeight;
    }

    public void getPlayer(Player playerr) {
        player = playerr;
    }

    private void updateRectangle() {
        rectangle.setX(position.x);
        rectangle.setY(position.y);

    }

    public void update(float delta) {
        chargeDurationTimer -= delta;
        chargeCooldownTimer -= delta;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        updateRectangle();
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

}