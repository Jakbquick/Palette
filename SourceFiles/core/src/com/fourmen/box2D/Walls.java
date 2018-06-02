package com.fourmen.box2D;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;


public class Walls {
    private Texture img;
    private Sprite wallSprite;
    private Body body;
    public Walls(World world){
        img = new Texture("Images/Logo.jpg");
        wallSprite = new Sprite(img);
        wallSprite.setPosition(0, 0);


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(wallSprite.getX(), wallSprite.getY());
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(wallSprite.getWidth()/2, wallSprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }
    public Sprite getWallSprite(){
        return wallSprite;
    }
    public void dispose(){
        img.dispose();
    }
    //code left here so I could look at it later for objects that actually move

    //public void updateWall(){
        //wallSprite.setPosition(body.getPosition().x, body.getPosition().y);
    //}
}


