package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.fourmen.utils.Animator;
//import sun.awt.image.GifImageDecoder;

import javax.swing.text.Position;
import javax.xml.stream.Location;

public class Player extends com.fourmen.actors.Entity {
    //constants
    private static final double ACCELERATION_CONSTANT = .6;
    private static final double DECELERATION_CONSTANT = .3;
    private static final float PLAYER_SIZE = .5f;
    private final static float playerWidth = 163 * PLAYER_SIZE; //163    40
    private final static float playerHeight = 251 * PLAYER_SIZE;
    private enum PlayerState {
        STANDING, MOVING, DASHING
    }

    //instance variables
    public Rectangle rectangle;
    public Circle attackRange;
    public Circle attack;
    public Rectangle body;
    public Rectangle wings;
    private Color rectangleColor;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;       // how fast the player is currently going in x and y
    private Vector2 tempSpeed;
    private int maxSpeed;               // the max speed a player can move
    private double acceleration;
    private double deceleration;
    private Vector2 direction;          // contains a x direction and y direction from -1 to 1
    private Vector2 attackDirection;
    private Vector2 dashDirection;
    private double dashSpeed;
    private double dashCooldown;
    private double dashDuration;
    private double standingCooldown;
    private double dashDurationTimer;
    private double dashCooldownTimer;
    private double standingCooldownTimer;
    private float stateTime;
    private PlayerState playerState;

    private TextureRegion currentFrame;
    private Animation<TextureRegion> moving;
    private Animation<TextureRegion> idle;

    //constructors
    public Player() {
        super();
        rectangle = new Rectangle(getX(), getY(), playerWidth, playerHeight);
        attackRange = new Circle(getX(), getY(), playerHeight / 1.5f);
        attack = new Circle(getX(), getY(), 25);
        body = new Rectangle(getX() + 63 * PLAYER_SIZE, getY(), 40 * PLAYER_SIZE, 251 * PLAYER_SIZE);
        wings = new Rectangle(getX() + 16, getY() + 85, 137 * PLAYER_SIZE,109 * PLAYER_SIZE);
        rectangleColor = new Color();
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        tempSpeed = new Vector2(0, 0);
        maxSpeed = 700;
        acceleration = ACCELERATION_CONSTANT * maxSpeed;
        deceleration = DECELERATION_CONSTANT * maxSpeed;
        direction = new Vector2(0, 0);
        attackDirection = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        dashSpeed = 2800;
        dashCooldown = .6;
        dashDuration = .1;
        standingCooldown = .25;
        dashDurationTimer = 0;
        dashCooldownTimer = 0;
        standingCooldownTimer = 0;
        stateTime = 0;
        playerState = playerState.MOVING;

        moving = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/spritemovesheet.png", 1, 4));
        idle = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/spriteidlesheet.png", 1, 5));
        currentFrame = idle.getKeyFrame(stateTime, true);

    }

    //methods
    public void act() {
        //System.out.println(currentSpeed + " " + playerState);
        updateDirection();
        updateAttackDirection();
        switch (playerState) {
            case STANDING:
                rectangleColor = new Color(Color.BLUE);
                currentFrame = idle.getKeyFrame(stateTime, true);
                move();
                if(!direction.isZero())
                    playerState = playerState.MOVING;
                break;
            case MOVING:
                rectangleColor = new Color(Color.GREEN);
                currentFrame = moving.getKeyFrame(stateTime, true);
                move();
                if(dashCooldownTimer <= 0)
                    checkDash();
                if(!direction.isZero())
                    standingCooldownTimer = standingCooldown;
                if(standingCooldownTimer <= 0)
                    playerState = playerState.STANDING;
                break;
            case DASHING:
                rectangleColor = new Color(Color.RED);
                dash();
                if(dashDurationTimer <= 0) {
                    currentSpeed.x = tempSpeed.x;
                    currentSpeed.y = tempSpeed.y;
                    playerState = playerState.MOVING;
                }
                break;
        }
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

    private void updateAttackDirection() {
        Vector2 tempDir = new Vector2(attackDirection.x, attackDirection.y);

        attackDirection.x = 0;
        attackDirection.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            attackDirection.x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            attackDirection.x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            attackDirection.y = 0;
            attackDirection.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            attackDirection.y = 0;
            attackDirection.y -= 1;
        }

        attackDirection.nor();

        if(attackDirection.isZero()) {
            attackDirection = tempDir;
        }


    }

    private void checkDash() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !(direction.x == 0 && direction.y == 0)) {
            playerState = playerState.DASHING;
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            tempSpeed.x = currentSpeed.x;
            tempSpeed.y = currentSpeed.y;
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

    public float getPlayerWidth() {
        return playerWidth;
    }
    public float getPlayerHeight() {
        return playerHeight;
    }

    private void updateRectangle() {
        rectangle.setX(position.x);
        rectangle.setY(position.y);
        attackRange.x = position.x + playerWidth / 2;
        attackRange.y = position.y + playerHeight / 2;
        attack.x = position.x + playerWidth / 2 + attackRange.radius * attackDirection.x;
        attack.y = position.y + playerHeight / 2 + attackRange.radius * attackDirection.y;
        body.setX(position.x + 63 * PLAYER_SIZE);
        body.setY(position.y);
        wings.setX(position.x + 16 * PLAYER_SIZE);
        wings.setY(position.y + 85 * PLAYER_SIZE);

    }

    public void update(float delta) {
        dashDurationTimer -= delta;
        dashCooldownTimer -= delta;
        standingCooldownTimer -= delta;
        stateTime += delta;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        updateRectangle();
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height,
                rectangleColor, rectangleColor, rectangleColor, rectangleColor);
        shapeRenderer.circle(attackRange.x, attackRange.y, attackRange.radius);
        shapeRenderer.circle(attack.x, attack.y, attack.radius);
        shapeRenderer.rect(body.x, body.y, body.width, body.height,
                rectangleColor, rectangleColor, rectangleColor, rectangleColor);
        shapeRenderer.rect(wings.x, wings.y, wings.width, wings.height,
                rectangleColor, rectangleColor, rectangleColor, rectangleColor);
    }

    public void updateAnimations(SpriteBatch batch) {
        batch.draw(currentFrame, position.x - 60 * PLAYER_SIZE, position.y - 20 * PLAYER_SIZE, 300 * PLAYER_SIZE, 300 * PLAYER_SIZE);
    }

}
