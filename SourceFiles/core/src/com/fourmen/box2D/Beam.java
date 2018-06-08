package com.fourmen.box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.actors.PlayerBounds;
import com.fourmen.utils.Animator;

public class Beam {
    //instance variables
    private final static float SPEED = 2000;
    private final static float BEAM_DURATION = 1f;

    public Vector2 position;
    private float direction;
    private float size;

    private int hitValue;

    private boolean hitEnemy;
    private boolean ending;

    private float durationTimer = 0;
    private float stateTime = 0;

    private Animation<TextureRegion> beamShot;
    private Animation<TextureRegion> rainbowBeamShot;
    private Animation<TextureRegion> beamShotEnd;
    private Animation<TextureRegion> rainbowBeamShotEnd;
    private TextureRegion currentFrame;

    public Body body;

    //constructors
    public Beam(World world, Vector2 playerPosition, float shotDirection, float playerSize, int hitValue) {
        position = new Vector2(playerPosition.x + (200 * shotDirection * playerSize), playerPosition.y);
        direction = shotDirection;
        size = playerSize;

        this.hitValue = hitValue;

        hitEnemy = false;

        beamShot = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/beamsheet.png", 1, 3));
        rainbowBeamShot = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/rainbowbeamsheet.png", 1, 3));
        beamShotEnd = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/beamendsheet.png", 1, 5));
        rainbowBeamShotEnd = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/rainbowendsheet.png", 1, 5));
        currentFrame = beamShot.getKeyFrame(stateTime);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(100 * size * .5f, 200 * size * .5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;

        body.createFixture(fixtureDef);
        body.setAwake(false);

        hitbox.dispose();

        body.setUserData(this);
    }

    //methods
    public void move() {
        position.x += SPEED * direction * Gdx.graphics.getDeltaTime();
        body.setTransform(position, 0);
    }

    public void draw(Batch batch) {
        if (hitValue == 2) {
            if (hitEnemy || durationTimer >= BEAM_DURATION) {
                currentFrame = rainbowBeamShotEnd.getKeyFrame(stateTime);
            }
            else {
                currentFrame = rainbowBeamShot.getKeyFrame(stateTime);
            }
        }
        else {
            if (hitEnemy || durationTimer >= BEAM_DURATION) {
                currentFrame = beamShotEnd.getKeyFrame(stateTime);
            }
            else {
                currentFrame = beamShot.getKeyFrame(stateTime);
            }
        }


        batch.draw(currentFrame, direction > 0 ? position.x - (200f * size): position.x + (200f * size),
                position.y - 146 * size, direction < 0 ? -currentFrame.getRegionWidth() * size : currentFrame.getRegionWidth() * size,
                currentFrame.getRegionHeight() * size);
    }

    public void update(float delta) {
        System.out.println(durationTimer);
        durationTimer += delta;
        stateTime += delta;
    }

    public boolean checkRemoveFromWorld() {
        if (durationTimer >= BEAM_DURATION) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setHitValue(int value) {
        hitValue = value;
    }

    public void setHitEnemy(boolean hitEnemy) {
        this.hitEnemy = hitEnemy;
    }

    public int getHitValue() {
        return hitValue;
    }

    public void dispose() {

    }
}
