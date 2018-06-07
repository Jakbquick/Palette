package com.fourmen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RhythmView {
    private SpriteBatch batch;
    public Music gameMusic;
    private FileHandle location;
    private String values;
    private Texture bar;
    private float x,y;
    private Viewport viewport;
    private Texture whenButton,clicked;
    private Vector2 centerWhen;
    //private File
    private Array<Beat> beatList;
    private float[] mapValues;
    private float beatSize;
    private boolean drawClick;
    private int clickTime;

    public RhythmView(SpriteBatch spriteBatch, Viewport viewport){
        this.viewport = viewport;
        this.batch = spriteBatch;
        readMapValues();
        bar = new Texture("Images/BlackBar.png");
        y = Gdx.graphics.getHeight() - 115;
        whenButton = new Texture("Images/Beat/NoClick.png");
        clicked = new Texture("Images/Beat/Click.png");
        beatSize = 75;
        drawClick = false;
        clickTime = 0;
    }
    public void update(float delta){
        if (clickTime == 3){
            drawClick = false;
            clickTime = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !drawClick){
            drawClick = true;
        }

    }
    public void draw(){
        batch.draw(bar,0,y,viewport.getScreenWidth(),bar.getHeight());
        if (drawClick){
            batch.draw(clicked,100,y + (.5f * bar.getHeight()) - (.5f * beatSize),
                    beatSize,beatSize);
            drawClick = false;
            clickTime++;
        }
        else {
            batch.draw(whenButton, 100, y + (.5f * bar.getHeight()) - (.5f * beatSize),
                    beatSize, beatSize);
        }
    }
    public void readMapValues(){
        location = Gdx.files.internal("Maps/Music1.txt");
        values = location.readString();
        String[] stringValues = values.split(" ");
        mapValues = new float[stringValues.length];
        for(int i = 0; i< stringValues.length; i++){
            mapValues[i] = (Float.valueOf(stringValues[i]));
        }

    }

}
