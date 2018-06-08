package com.fourmen.box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;

public class Beam {
    //instance variables
    private final static float SPEED = 2000;

    public Vector2 position;
    private float direction;
    private float size;

    private int hitValue;

    private float stateTime;

    Animation<TextureRegion> beamShot;

    public static Body body;

    //constructors
    public Beam(World world, Vector2 playerPosition, float shotDirection, float playerSize) {
        position = new Vector2(playerPosition.x + (200 * shotDirection * playerSize), playerPosition.y);
        direction = shotDirection;
        size = playerSize;

        hitValue = 0;

        beamShot = new Animation<TextureRegion>(.02f, Animator.setUpSpriteSheet("Images/beamsheet.png", 1, 2));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(200 * size * .5f, 200 * size * .5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;

        body.createFixture(fixtureDef);

        hitbox.dispose();
    }

    //methods
    public void move() {
        position.x += SPEED * direction * Gdx.graphics.getDeltaTime();

        body.setTransform(position, 0);
    }

    public void draw(Batch batch) {
        batch.draw(beamShot.getKeyFrame(stateTime), direction > 0 ? position.x - (200f * size): position.x + (200f * size),
                position.y - 146 * size, direction < 0 ? -beamShot.getKeyFrame(stateTime).getRegionWidth() * size : beamShot.getKeyFrame(stateTime).getRegionWidth() * size,
                beamShot.getKeyFrame(stateTime).getRegionHeight() * size);
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void setHitValue(int value) {
        hitValue = value;
    }

    public int getHitValue() {
        return hitValue;
    }

    public void dispose() {

    }
}
