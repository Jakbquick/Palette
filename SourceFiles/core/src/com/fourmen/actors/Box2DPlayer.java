package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;

import java.awt.*;

public class Box2DPlayer extends Entity{
    //instance variables
    private final static float PLAYER_SIZE = 1f;
    private final static float BOX2D_SCALE = 1/3f;
    private final static float PLAYER_WIDTH = 163 * PLAYER_SIZE; //163    40
    private final static float PLAYER_HEIGHT = 251 * PLAYER_SIZE;
    private final static int MAX_SPEED = 700;
    private final static float ACCELERATION_CONSTANT = .6f;
    private final static float DECELERATION_CONSTANT = .3f;

    private Vector2 direction;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;
    private float acceleration;
    private float deceleration;
    private Body body;
    private float stateTime = 0;

    public TextureRegion currentFrame;
    private Animation<TextureRegion> moving;

    //constructors
    public Box2DPlayer(World world) {
        super();

        direction = new Vector2(0, 0);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);

        acceleration = ACCELERATION_CONSTANT * MAX_SPEED;
        deceleration = DECELERATION_CONSTANT * MAX_SPEED;

        moving = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/spritemovesheet.png", 1, 4));
        currentFrame = moving.getKeyFrame(stateTime, true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);

        PolygonShape square = new PolygonShape();
        square.setAsBox(PLAYER_WIDTH * BOX2D_SCALE, PLAYER_HEIGHT * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;

        Fixture fixure = body.createFixture(fixtureDef);

        square.dispose();
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

    public void act() {
        updateDirection();
        move();
        currentFrame = moving.getKeyFrame(stateTime, true);
        updatePosition();
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

    public void draw(Batch batch) {
        batch.draw(currentFrame, getX() - 141.5f * PLAYER_SIZE, getY() - 146 * PLAYER_SIZE);

    }

    public void updateTimers(float delta) {
        stateTime += delta;
    }

    public void updatePosition() {
        body.setTransform(position, 0);
    }

    public void drawDebug(Box2DDebugRenderer debug) {

    }



}
