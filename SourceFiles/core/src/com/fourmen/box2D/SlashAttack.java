package com.fourmen.box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.utils.Animator;

public class SlashAttack {
    //instance variables
    public Body body;
    public Body beam;

    public Vector2 direction;
    private Vector2 position;
    private float playerSize;

    private int hitValue;

    public float stateTime = 0;

    public Animation<TextureRegion> attackSide;
    public Animation<TextureRegion> attackSideStart;
    public Animation<TextureRegion> attackSideMid;
    public Animation<TextureRegion> attackSideEnd;
    public Animation<TextureRegion> slashSide;

    private Vector2 lastAttackDirection;

    //constructors
    public SlashAttack(World world, Vector2 bodyPosition, float size) {
        direction = new Vector2(0, 0);
        position = new Vector2(bodyPosition);
        lastAttackDirection = new Vector2(0, 0);
        playerSize = size;
        position = new Vector2(bodyPosition);

        hitValue = -1;

        attackSide = new Animation<TextureRegion>(0.015f, Animator.setUpSpriteSheet("Images/attacksheet.png", 1, 38));
        attackSideStart = new Animation<TextureRegion>(0.015f, Animator.setUpSpriteSheet("Images/attackstartupsheet.png", 1, 13));
        attackSideMid = new Animation<TextureRegion>(0.015f, Animator.setUpSpriteSheet("Images/attackmidsheet.png", 1, 16));
        attackSideEnd = new Animation<TextureRegion>(0.015f, Animator.setUpSpriteSheet("Images/attackendsheet.png", 1, 9));
        slashSide = new Animation<TextureRegion>(0.1f, Animator.setUpSpriteSheet("Images/slashsheet.png", 1, 7));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(bodyPosition));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(bodyPosition));
        bodyDef.fixedRotation = true;

        beam = world.createBody(bodyDef);
    }

    //methods
    public void act() {

    }

    public void updateAttackDirection() {
        //Vector2 tempDir = new Vector2(direction.x, direction.y);

        direction.x = 0;
        direction.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //direction.y = 0;
            //direction.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //direction.y = 0;
            //direction.y -= 1;
        }

        direction.nor();

        if(!direction.isZero()) {
            lastAttackDirection = new Vector2(direction);
        }
    }

    public boolean checkAttack() {
        updateAttackDirection();
        if(!direction.isZero() && Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && hitValue > -1) {
            stateTime = 0;
            return true;
        }
        else {
            return false;
        }
    }
    public int getLastAttack() {
        if(lastAttackDirection.x < 0) {
            return 1;
        }
        else if(lastAttackDirection.x > 0) {
            return 0;
        }
        else {
            return 2;
        }
    }

    public void createHitbox() {
        PolygonShape hitbox = new PolygonShape();
        Vector2 center = new Vector2(lastAttackDirection.x * 75f * playerSize,9f * playerSize);
        hitbox.setAsBox(150 * playerSize * .5f, 40 * playerSize * .5f, center, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitbox;

        body.createFixture(fixtureDef);

        hitbox.dispose();
    }

    public void destroyHitbox(Body myBody) {
        int fixtureCount = myBody.getFixtureList().size;
        for(int i=0;i<fixtureCount;i++){
            myBody.destroyFixture(myBody.getFixtureList().get(0));
        }
    }

    public void updatePosition(Vector2 bodyPosition) {
        position = new Vector2(bodyPosition);
        body.setTransform(position, 0);
    }

    public void createBeam() {
        PolygonShape beamHitbox = new PolygonShape();
        Vector2 center = new Vector2(lastAttackDirection.x * 75f * playerSize,9f * playerSize);
        beamHitbox.setAsBox(150 * playerSize * .5f, 40 * playerSize * .5f, center, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = beamHitbox;

        body.createFixture(fixtureDef);

        beamHitbox.dispose();
    }

    public void setStateTime(float newStateTime) {
        stateTime = newStateTime;
    }

    public TextureRegion getSlashFrame() {
        return slashSide.getKeyFrame(stateTime, false);
    }

    public TextureRegion getAttackFrame() {
        return attackSide.getKeyFrame(stateTime, true);
    }

    public TextureRegion getAttackStartFrame() {
        return attackSideStart.getKeyFrame(stateTime, true);
    }

    public TextureRegion getAttackMidFrame() {
        return attackSideMid.getKeyFrame(stateTime, true);
    }

    public TextureRegion getAttackEndFrame() {
        return attackSideEnd.getKeyFrame(stateTime, true);
    }

    public void getHitValue(int value) {
        hitValue = value;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void dispose() {

    }

}
