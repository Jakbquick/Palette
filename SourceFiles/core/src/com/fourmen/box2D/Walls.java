package com.fourmen.box2D;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;
import com.fourmen.utils.BodyEditorLoader;


public class Walls {
    private Body body;
    private float x,y;
    private Vector2 position;
    BodyEditorLoader bodyEditorLoader;
    private FixtureDef fixtureDef;
    private Animation<TextureRegion> floor;
    private TextureRegion state;
    private float timeSinceStart;
    private float scale;
    public Walls(World world, float x, float y,float scale){
        timeSinceStart = 0;
        this.scale = scale;
        this.x = x;
        this.y = y;
        position = new Vector2(x,y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);
        body = world.createBody(bodyDef);

        bodyEditorLoader = new BodyEditorLoader(Gdx.files.internal("Box2d/Walls"));
        fixtureDef = new FixtureDef();
        bodyEditorLoader.attachFixture(body,"Walls",fixtureDef, scale);

        floor = new Animation<TextureRegion>(0.25f, Animator.setUpSpriteSheet("Images/FloorSprites.png", 1, 26));

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

    public void draw(SpriteBatch batch){
        batch.draw(state, 0,0,scale,scale * (3f/5f));
    }
    public void updateAnimations(float delta){
        timeSinceStart += delta;
        state = floor.getKeyFrame(timeSinceStart, true);
    }
}


