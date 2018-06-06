package com.fourmen.box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.utils.Animator;

public class SlashAttack {
    //instance variables
    private Body body;

    public Vector2 direction;
    private Vector2 position;
    private float attackRadius;

    private float stateTime = 0;

    private Animation<TextureRegion> attackSide;

    private Vector2 lastAttackDirection;

    //constructors
    public SlashAttack(World world, Vector2 bodyPosition, float size) {
        direction = new Vector2(0, 0);
        lastAttackDirection = new Vector2(0, 0);
        attackRadius = 150 * size;
        position = new Vector2(bodyPosition);

        attackSide = new Animation<TextureRegion>(0.02f, Animator.setUpSpriteSheet("Images/attacksheet.png", 1, 38));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(bodyPosition));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        CircleShape range = new CircleShape();
        range.setRadius(80 * size);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = range;

        body.createFixture(fixtureDef);

        range.dispose();
    }

    //methods
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
            direction.y = 0;
            direction.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = 0;
            direction.y -= 1;
        }

        direction.nor();

        if(!direction.isZero()) {
            lastAttackDirection = new Vector2(direction);
        }
    }

    public boolean checkAttack() {
        if(!direction.isZero()) {
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

    public void updatePosition(Vector2 bodyPosition) {
        position = new Vector2(bodyPosition);
        //body.setTransform(position.x + attackRadius * direction.x, position.y + attackRadius * direction.y, 0);
    }

    public TextureRegion getFrame() {
        return attackSide.getKeyFrame(stateTime, true);
    }

    public void update(float delta) {
        stateTime += delta;
    }

}
