package com.fourmen.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Beat {
    private Texture beatTexture;
    private Vector2 position;
    private float velocity;
    public static float beatHeight = 75;
    public static float beatWidth = 75;
    public Beat(float velocity, float x, float y){
        this.velocity = velocity;
        beatTexture = new Texture("Images/Beat/hitbeat.png");

        position = new Vector2(x,y);
    }
    public void draw(SpriteBatch batch){
        batch.draw(beatTexture, position.x - (.5f * beatWidth),
                position.y - (.5f * beatHeight), beatWidth, beatHeight);
    }
    public void update(float delta){
        position.x -= velocity;
    }
    public float getXPosition(){
        return position.x;
    }
    public void dispose(){
        beatTexture.dispose();
    }
}
