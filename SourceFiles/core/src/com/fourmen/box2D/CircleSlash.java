package com.fourmen.box2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.actors.Box2DPlayer;

public class CircleSlash {
    //instance variables
    private Body body;

    private Vector2 direction;
    private float attackRadius;


    //constructors
    public CircleSlash(World world, Vector2 position, float size) {
        direction = new Vector2(0, 0);
        attackRadius = 150 * size;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
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
    public void act() {
        updateAttackDirection();
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
            direction.y = 0;
            direction.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = 0;
            direction.y -= 1;
        }

        direction.nor();

        if(direction.isZero()) {
            //direction = tempDir;
        }
    }

    public void updatePosition(Vector2 position) {
        body.setTransform(position.x + attackRadius * direction.x, position.y + attackRadius * direction.y, 0);
    }


}
