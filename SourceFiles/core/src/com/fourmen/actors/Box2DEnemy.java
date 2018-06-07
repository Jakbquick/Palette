package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;


public class Box2DEnemy extends Entity {
    //constants
    private final static float ENEMY_SIZE = 1f;
    private final static float BOX2D_SCALE = .5f;
    private final static float ENEMY_WIDTH = 30 * ENEMY_SIZE;
    private final static float ENEMY_HEIGHT = 30 * ENEMY_SIZE;
    private static final float ACCELERATION_CONSTANT = 1f;
    private static final float DECELERATION_CONSTANT = 1f;
    private final int MAX_SPEED = 500;               // the max speed a player can move
    private final float DASH_SPEED = 1000;
    private final float CHARGE_SPEED = 3000;
    private final static float DASH_COOLDOWN = .6f;
    private final static float DASH_DURATION = .1f;
    private enum PlayerState {
        MOVING, DASHING, CHARGING, ENDCHARGE;
    }

    //instance variables

    private Vector2 direction;          // contains a x direction and y direction from -1 to 1
    private Vector2 dashDirection;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;       // how fast the player is currently going in x and y
    private Vector2 chargePos;
    private float acceleration;
    private float deceleration;
    private float prevX = 0;
    private float prevY = 0;

    private double dashDurationTimer = 0;
    private double dashCooldownTimer = 0;
    private float stateTime = 0;

    public TextureRegion currentFrame;
    private Animation<TextureRegion> moving;
    private Animation<TextureRegion> dash;
    private Animation<TextureRegion> charge;

    private int lastDirectionfaced;
    private int LEFT = 0;
    private int RIGHT = 1;

    private PlayerState enemyState;
    private Box2DPlayer player;
    private Body body;
    private boolean towardsPlayer;

    //constructors
    public Box2DEnemy(World world, Box2DPlayer myPlayer) {
        super();
        lastDirectionfaced = LEFT;
        direction = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        chargePos = new Vector2(0,0);
        acceleration = ACCELERATION_CONSTANT * MAX_SPEED;
        deceleration = DECELERATION_CONSTANT * MAX_SPEED;
        towardsPlayer = false;
        enemyState = enemyState.MOVING;
        player = myPlayer;

        moving = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/TempBoss.png", 1, 4));
        dash = new Animation<TextureRegion>(0.08f, Animator.setUpSpriteSheet("Images/TempBoss.png", 1, 5));
        charge = new Animation<TextureRegion>(0.08f, Animator.setUpSpriteSheet("Images/TempBoss.png", 1, 5));
        currentFrame = moving.getKeyFrame(stateTime, true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape bodySquare = new PolygonShape();
        bodySquare.setAsBox(50 * ENEMY_SIZE * BOX2D_SCALE, 50 * ENEMY_SIZE * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodySquare;

        body.createFixture(fixtureDef);

        bodySquare.dispose();
    }

    //methods
    public void act() {
        int dirChance = MathUtils.random(10);
        if(dirChance == 1) {
        updateDirection();
        switch (enemyState) {
            case MOVING:
                currentFrame = getFrame(moving);
                move();
                if (dashCooldownTimer <= 0)
                    checkDash();
                checkCharge();
                break;
            case DASHING:
                currentFrame = getFrame(dash);
                dash();
                if (dashDurationTimer <= 0) {
                    enemyState = enemyState.MOVING;
                }
                break;
            case ENDCHARGE:
                currentFrame = getFrame(dash);
                endcharge();
                if (dashDurationTimer <= -0.3) {
                    enemyState = enemyState.MOVING;
                }
                break;
            case CHARGING:
                currentFrame = getFrame(charge);
                charge();
                enemyState = enemyState.ENDCHARGE;
        }
        }
        else{
            if(direction.x >= 0)
                direction.x++;
            else if(direction.x < 0)
                direction.x--;
            if(direction.y >= 0)
                direction.y++;
            else if(direction.y < 0)
                direction.y--;
            move();
        }
        updatePosition();
    }

    private void dirTowardsPlayer() {
        Vector2 playerDir = new Vector2(player.getX(), player.getY());
        direction = playerDir.sub(direction);
    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;
        if(distanceBetween(player, getX(), getY()) > 600) {
            if (player.getX() > getX()) {
                direction.x += 1;
                lastDirectionfaced = RIGHT;
            }
            else if (player.getX() < getX()) {
                direction.x -= 1;
                lastDirectionfaced = LEFT;
            }
            if (player.getY() > getY())
                direction.y += 1;
            else if (player.getY() < getY())
                direction.y -= 1;

            towardsPlayer = true;
        }
        else {
            direction.setToRandomDirection();
            if(direction.x >= 0)
                direction.x++;
            else if(direction.x < 0)
                direction.x--;
            if(direction.y >= 0)
                direction.y++;
            else if(direction.y < 0)
                direction.y--;
            towardsPlayer = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.H)) {
            position.set(0,0);
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

    private void checkDash() {

        int dashChance  = MathUtils.random(4);

        if(dashChance == 3) {
            enemyState = enemyState.DASHING;
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            currentSpeed = new Vector2(0,0);
            dashDurationTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
        }
    }

    private void checkCharge() {

        int chargeChance  = MathUtils.random(0);

        if(distanceBetween(player, getX(), getY()) <= 500 && distanceBetween(player, getX(), getY()) >= 400 && chargeChance == 0) {
            enemyState = enemyState.CHARGING;
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            currentSpeed = new Vector2(0,0);
            dashDurationTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
        }
    }

    private void dash() {
        currentSpeed.x = DASH_SPEED * dashDirection.x;
        currentSpeed.y = DASH_SPEED * dashDirection.y;

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    private void charge() {
        prevX = getX();
        prevY = getY();
        double disBetween = distanceBetween(player, getX(), getY());
        setPosition(player.getX(), player.getY());
    }

    private void endcharge() {
        currentSpeed.x = CHARGE_SPEED * dashDirection.x;
        currentSpeed.y = CHARGE_SPEED * dashDirection.y;

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    public void lookAt(Vector2 target) {

        float angle = (float) Math.atan2(target.y - this.position.y, target.x
                - this.position.x);
        angle = (float) (angle * (180 / Math.PI));

        direction.setAngle(angle);
    }

    private double distanceBetween(Box2DPlayer player1, float xVal, float yVal){
        return Math.sqrt(Math.pow((xVal - player1.getX()), 2) + Math.pow((yVal - player1.getY()), 2));
    }

    public void draw(Batch batch) {
        boolean flip = (lastDirectionfaced == RIGHT);
        batch.draw(currentFrame, flip ? getX() - (16f * ENEMY_SIZE) - (141.5f * ENEMY_SIZE)+currentFrame.getRegionWidth()* ENEMY_SIZE : getX() - (141.5f * ENEMY_SIZE),
                getY() - 146 * ENEMY_SIZE, flip ? -currentFrame.getRegionWidth()* ENEMY_SIZE: currentFrame.getRegionWidth()* ENEMY_SIZE,
                currentFrame.getRegionHeight()* ENEMY_SIZE);
    }

    public TextureRegion getFrame(Animation<TextureRegion> frame) {
        if(frame.isAnimationFinished(stateTime)) {
            stateTime = 0;
        }
        return frame.getKeyFrame(stateTime, true);
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