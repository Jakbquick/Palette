package com.fourmen.box2D;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.BodyEditorLoader;


public class Walls {
    private Body body;
    private float x,y;
    BodyEditorLoader bodyEditorLoader;
    private FixtureDef fixtureDef;
    public Walls(World world, float x, float y){
        this.x = x;
        this.y = y;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x , y);
        body = world.createBody(bodyDef);

        bodyEditorLoader = new BodyEditorLoader("Box2d");
        fixtureDef = new FixtureDef();
        bodyEditorLoader.attachFixture(body,"Walls",fixtureDef,28.04907975f);



    }
    public void dispose(){

    }
    //code left here so I could look at it later for objects that actually move

    //public void updateWall(){
        //wallSprite.setPosition(body.getPosition().x, body.getPosition().y);
    //}

    public Body getBody(){
        return body;
    }
}


