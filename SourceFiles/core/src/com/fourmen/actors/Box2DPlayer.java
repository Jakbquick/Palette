package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.fourmen.box2D.SlashAttack;
import com.fourmen.utils.Animator;

import java.awt.*;

public class Box2DPlayer extends Entity{
    //instance variables
    private final static float PLAYER_SIZE = 1f;
    private final static float BOX2D_SCALE = .5f;
    private final static float PLAYER_WIDTH = 163 * PLAYER_SIZE; //163    40
    private final static float PLAYER_HEIGHT = 251 * PLAYER_SIZE;
    private final static int MAX_SPEED = 700;
    private final static float DASH_SPEED = 1000;
    private final static float ACCELERATION_CONSTANT = 1f;
    private final static float DECELERATION_CONSTANT = 1f;
    private final static float DASH_COOLDOWN = 2f;
    private final static float DASH_DURATION = .25f;
    private final static float ATTACK_COOLDOWN = 2f;
    private final static float ATTACK_DURATION = .76f;
    private final static float DASH_END_LAG = .15f;
    private final static float STANDING_COOLDOWN = .25f;
    private enum PlayerState {
        STANDING, MOVING, DASHING, ATTACKING
    }

    private Vector2 direction;
    private Vector2 attackDirection;
    private Vector2 dashDirection;
    private Vector2 dashPosition;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;
    private Vector2 tempSpeed;

    private float acceleration;
    private float deceleration;

    private Body body;
    private Body attackHitbox;

    public int fixtureCollisions;

    private PlayerState playerState;

    private PlayerBounds playerBounds;

    private SlashAttack slash;

    private float dashDurationTimer = 0;
    private float attackDurationTimer = 0;
    private float dashCooldownTimer = 0;
    private float attackCooldownTimer = 0;
    private float standingCooldownTimer = 0;
    private float stateTime = 0;

    private Timer timer;

    public TextureRegion currentFrame;
    private Animation<TextureRegion> moving;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> dash;
    private Animation<TextureRegion> dashEnd;
    private Animation<TextureRegion> empty;

    private int lastDirectionfaced;
    private int LEFT = 0;
    private int RIGHT = 1;

    //constructors
    public Box2DPlayer(World world, PlayerBounds myPlayerBounds) {
        super();
        lastDirectionfaced = LEFT;
        direction = new Vector2(0, 0);
        attackDirection = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        dashPosition = new Vector2(0, 0);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        tempSpeed = new Vector2(0,0);

        acceleration = ACCELERATION_CONSTANT * MAX_SPEED;
        deceleration = DECELERATION_CONSTANT * MAX_SPEED;

        fixtureCollisions = 0;

        playerState = PlayerState.STANDING;

        playerBounds = myPlayerBounds;

        slash = new SlashAttack(world, position, PLAYER_SIZE);

        timer = new Timer();

        moving = new Animation<TextureRegion>(.25f, Animator.setUpSpriteSheet("Images/spritemovesheet.png", 1, 4));
        idle = new Animation<TextureRegion>(.25f, Animator.setUpSpriteSheet("Images/spriteidlesheet.png", 1, 5));
        dash = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/spritedashsheet.png", 1, 5));
        dashEnd = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/spritedashendsheet.png", 1, 5));
        empty = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/emptyframe.png", 1, 1));
        currentFrame = moving.getKeyFrame(stateTime, true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        /*
        PolygonShape square = new PolygonShape();
        square.setAsBox(PLAYER_WIDTH * BOX2D_SCALE, PLAYER_HEIGHT * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;

        body.createFixture(fixtureDef);

        square.dispose();
        */

        PolygonShape bodySquare = new PolygonShape();
        bodySquare.setAsBox(40 * PLAYER_SIZE * BOX2D_SCALE, 251 * PLAYER_SIZE * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodySquare;

        body.createFixture(fixtureDef);

        bodySquare.dispose();

        PolygonShape wingsSquare = new PolygonShape();
        Vector2 wingCenter = new Vector2(1.5f * PLAYER_SIZE,12.5f * PLAYER_SIZE);
        wingsSquare.setAsBox(137 * PLAYER_SIZE * BOX2D_SCALE, 107 * PLAYER_SIZE * BOX2D_SCALE, wingCenter, 0);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = wingsSquare;

        body.createFixture(fixtureDef);

        wingsSquare.dispose();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        attackHitbox = world.createBody(bodyDef);


    }

    //methods
    public float getPlayerWidth() {
        return PLAYER_WIDTH;
    }

    public float getPlayerHeight() {
        return PLAYER_HEIGHT;
    }

    public Body getPlayerBody(){
        return body;
    }

    public float getPlayerSize() {
        return PLAYER_SIZE;
    }

    public void act() {
        //System.out.println(currentSpeed + " " + playerState);
        //System.out.println(fixtureCollisions);
        updateDirection();
        switch (playerState) {
            case STANDING:
                slash.updateAttackDirection();
                currentFrame = getFrame(idle);
                //currentFrame = idle.getKeyFrame(stateTime, true);
                move();

                if(attackCooldownTimer <= 0 && slash.checkAttack()) {
                    attackDurationTimer = 0;
                    playerState = playerState.ATTACKING;
                }

                if(!direction.isZero()) {
                    playerState = playerState.MOVING;
                }

                break;
            case MOVING:
                slash.updateAttackDirection();
                currentFrame = getFrame(moving);
                //currentFrame = moving.getKeyFrame(stateTime, true);
                move();

                if(attackCooldownTimer <= 0 && slash.checkAttack()) {
                    attackDurationTimer = 0;
                    playerState = playerState.ATTACKING;
                }

                if(dashCooldownTimer <= 0 && checkDash()) {
                    playerState = playerState.DASHING;
                }

                if(!direction.isZero()) {
                    standingCooldownTimer = STANDING_COOLDOWN;
                }

                if(standingCooldownTimer <= 0) {
                    playerState = playerState.STANDING;
                }

                break;
            case ATTACKING:
                currentFrame = slash.getFrame();
                move();

                if(attackDurationTimer >= ATTACK_DURATION) {
                    playerState = playerState.MOVING;
                }

                break;
            case DASHING:
                if(dashDurationTimer == 0) {
                    stateTime = 0;
                }

                if(dashDurationTimer <= dash.getFrameDuration() * 5) {
                    //currentFrame = getFrame(dash);
                    dash();
                    currentFrame = dash.getKeyFrame(stateTime, true);
                    //System.out.println(dash.isAnimationFinished(stateTime));
                    System.out.println("dash frame" + dash.getKeyFrameIndex(stateTime));

                    if(dash.isAnimationFinished(stateTime)) {
                        //currentFrame = getFrame(empty);
                        //currentFrame = empty.getKeyFrame(stateTime);
                    }
                }
                else if(dashDurationTimer <= DASH_DURATION) {
                    stateTime = 0;
                    System.out.println("empty frame" + dash.getKeyFrameIndex(stateTime));
                    dash();
                    currentFrame = empty.getKeyFrame(stateTime);
                }
                else if(dashDurationTimer <= DASH_DURATION + DASH_END_LAG) {
                    dash();
                    setX(dashPosition.x);
                    setY(dashPosition.y);
                    System.out.println("end frame" + dash.getKeyFrameIndex(stateTime));
                    //currentFrame = getFrame(dashEnd);
                    currentFrame = dashEnd.getKeyFrame(stateTime, false);
                }
                else {
                    currentSpeed.x = tempSpeed.x;
                    currentSpeed.y = tempSpeed.y;
                    setX(dashPosition.x);
                    setY(dashPosition.y);
                    playerState = playerState.MOVING;
                }
                break;
        }
        blockPlayerLeavingTheWorld();
        updatePosition();
    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;
            lastDirectionfaced = LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
            lastDirectionfaced= RIGHT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
        }

        direction.nor();

        targetSpeed.x = (int) (direction.x * MAX_SPEED);
        targetSpeed.y = (int) (direction.y * MAX_SPEED);

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

    private boolean checkDash() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !(direction.x == 0 && direction.y == 0)) {
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            tempSpeed.x = currentSpeed.x;
            tempSpeed.y = currentSpeed.y;

            currentSpeed = new Vector2(0,0);
            dashPosition = new Vector2(position);

            dashDurationTimer = 0;
            dashCooldownTimer = DASH_COOLDOWN;

            stateTime = 0;

            return true;
        }
        else {
            return false;
        }
    }

    private void dash() {
        currentSpeed.x = DASH_SPEED * dashDirection.x;
        currentSpeed.y = DASH_SPEED * dashDirection.y;

        dashPosition.x += currentSpeed.x * Gdx.graphics.getDeltaTime();
        dashPosition.y += currentSpeed.y * Gdx.graphics.getDeltaTime();

        //setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        //setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    public void draw(Batch batch) {
        if(playerState == playerState.ATTACKING) {
            lastDirectionfaced = slash.getLastAttack();
        }

        boolean flip = (lastDirectionfaced == RIGHT);
        //batch.draw(currentFrame, getX() - 141.5f * PLAYER_SIZE, getY() - 146 * PLAYER_SIZE);
        batch.draw(currentFrame, flip ? getX() - (16f * PLAYER_SIZE) - (141.5f * PLAYER_SIZE) + currentFrame.getRegionWidth() * PLAYER_SIZE : getX() - (141.5f * PLAYER_SIZE),
                getY() - 146 * PLAYER_SIZE, flip ? -currentFrame.getRegionWidth() * PLAYER_SIZE : currentFrame.getRegionWidth() * PLAYER_SIZE,
                currentFrame.getRegionHeight() * PLAYER_SIZE);

    }

    public TextureRegion getFrame(Animation<TextureRegion> frame) {
        if(frame.isAnimationFinished(stateTime)) {
            stateTime = 0;
        }
        return frame.getKeyFrame(stateTime, true);
    }

    public void updateTimers(float delta) {
        dashDurationTimer += delta;
        dashCooldownTimer -= delta;
        attackDurationTimer += delta;
        attackCooldownTimer -= delta;
        standingCooldownTimer -= delta;
        stateTime += delta;
        slash.update(delta);
    }

    public void updatePosition() {
        body.setTransform(position, 0);
        slash.updatePosition(position);
    }

    public void updateCollisions(int collisions) {
        fixtureCollisions += collisions;
    }

    public void drawDebug(Box2DDebugRenderer debug) {

    }

    private void blockPlayerLeavingTheWorld() {
        setPosition(MathUtils.clamp(getX(),playerBounds.getW1()+ (.5f* PLAYER_WIDTH),playerBounds.getWidth()+ playerBounds.getW1() - PLAYER_WIDTH + (.5f * PLAYER_WIDTH)),
                MathUtils.clamp(getY(), playerBounds.getH2() + (.5f *PLAYER_HEIGHT), playerBounds.getHeight() + playerBounds.getH2() - PLAYER_HEIGHT + (.5f *PLAYER_HEIGHT)));
    }



}
