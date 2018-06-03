package com.fourmen.box2D;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;


public class Walls {
    private Body body;
    private float width, height,x,y;
    private Fixture wallFixture;
    public Walls(World world, float x, float y, float width, float height){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + (.5f + width), y + (.5f * height));
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        wallFixture = body.createFixture(fixtureDef);
        shape.dispose();
    }
    public void dispose(){

    }
    //code left here so I could look at it later for objects that actually move

    //public void updateWall(){
        //wallSprite.setPosition(body.getPosition().x, body.getPosition().y);
    //}
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public void drawDebug(ShapeRenderer shapeRenderer){

    }
    public Body getBody(){
        return body;
    }
}


