package com.fourmen.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;

import java.awt.*;

public class Box2DPlayer extends Entity{
    //instance variables
    private static final float PLAYER_SIZE = 1f;
    private final static float PLAYER_WIDTH = 163 * PLAYER_SIZE; //163    40
    private final static float PLAYER_HEIGHT = 251 * PLAYER_SIZE;

    private float stateTime = 0;

    public TextureRegion currentFrame;
    private Animation<TextureRegion> moving;

    //constructors
    public Box2DPlayer(World world) {
        super();

        moving = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/spritemovesheet.png", 1, 4));
        currentFrame = moving.getKeyFrame(stateTime, true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(), getY());

        Body body = world.createBody(bodyDef);

        PolygonShape square = new PolygonShape();
        square.setAsBox(PLAYER_WIDTH, PLAYER_HEIGHT);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 1;

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

    public void act() {

    }

    public void draw(Batch batch) {

    }

}
