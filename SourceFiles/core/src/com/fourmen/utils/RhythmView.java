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
    private Texture whenButton,clicked,perfectButton,goodButton,missButton;
    private Vector2 centerWhen;
    //private File
    private ArrayList<Beat> beatList;
    private float[] mapValues;
    private float beatHeight, beatWidth;
    private boolean drawClick;
    private int clickTime,i;
    private float xDistance;
    private float velocity;
    private Music beatJams;
    private float timeBeforeSpawn;
    private boolean musicStarted;
    private double timeBetweenNotes;
    private double songLength;
    int index = 0;
    private double timeToFillMap;
    private double remainder;
    private double bpm, offset;
    int score;

    public RhythmView(SpriteBatch spriteBatch, Music beatJams, double songLength,double bpm,
                      double offset){
        int score = -1;
        //bpm = 135;
        timeBetweenNotes = (60.0)/ bpm;
        this.songLength = songLength;
        this.bpm = bpm;
        i = 0;
        this.offset = offset;
        //offset = .35;
        this.batch = spriteBatch;
        createWithBPM();
        bar = new Texture("Images/Beat/rhythmBar.png");
        y = Gdx.graphics.getHeight() - 115;
        whenButton = new Texture("Images/Beat/hitarea.png");
        perfectButton = new Texture("Images/Beat/perfect.png");
        goodButton = new Texture("Images/Beat/good.png");
        missButton = new Texture("Images/Beat/miss.png");
        clicked = goodButton;
        beatHeight = 150;
        beatWidth = 150;
        drawClick = false;
        clickTime = 0;
        //beatJams = Gdx.audio.newMusic(Gdx.files.internal("Music/song2.mp3"));
        this.beatJams = beatJams;

        xDistance = Gdx.graphics.getWidth() + (.5f * beatWidth) -        //the first beatSize here is the size of the beat transitioning across the map (replace later)
                100 - (.5f * beatWidth);
        centerWhen = new Vector2(100 + (beatWidth/2f), y + (.5f * bar.getHeight()));
        velocity = 350f / 60f;
        timeBeforeSpawn = (velocity * 60f) / xDistance;
        remainder = timeBetweenNotes - (timeBeforeSpawn % timeBetweenNotes);
        timeToFillMap = timeBeforeSpawn + remainder + offset;
        beatList = new ArrayList<Beat>();
    }
    public void update(float delta){
        for(Beat b : beatList){
            b.update(delta);
        }
        for(int j = 0; j < beatList.size(); j++){
            beatList.get(j).update(delta);
            if(beatList.get(j).getXPosition() < -Beat.beatWidth){
                beatList.remove(j);
            }
        }
        if (clickTime == 7){
            drawClick = false;
            clickTime = 0;
        }
        //if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
          //      Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
            //    Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
              //  Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !drawClick){
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !drawClick){
            drawClick = true;
            if(beatList.size() > 0) {
                if (centerWhen.dst(beatList.get(0).getXPosition(), centerWhen.y) < 300) {
                    float distance = centerWhen.dst(beatList.remove(0).getXPosition(), centerWhen.y);
                    if (distance > 200) {
                        clicked = missButton;
                        score = -1;
                    } else if (distance > 100) {
                        clicked = goodButton;
                        score = 1;
                    } else {
                        clicked = perfectButton;
                        score = 2;
                    }
                }
            }
        }
        if(i < mapValues.length && beatJams.getPosition() >= mapValues[i] + timeBeforeSpawn){
            beatList.add(new Beat(velocity, Gdx.graphics.getWidth(),y + (.5f * bar.getHeight())));
            i++;
        }


    }

    public int getScore(){
        return score;
    }
    public void draw(){
        batch.draw(bar,0,y,Gdx.graphics.getWidth(),bar.getHeight());
        if (drawClick){
            batch.draw(clicked,100,y + (.5f * bar.getHeight()) - (.5f * beatHeight),
                    beatHeight,beatWidth);
            clickTime++;
        }
        else {
            batch.draw(whenButton, 100, y + (.5f * bar.getHeight()) - (.5f * beatHeight),
                    beatHeight, beatWidth);
        }
        for(Beat b : beatList){
            b.draw(batch);
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
    public void startMusic(){
        beatJams.play();
        musicStarted = true;
    }
    public void createWithBPM(){
        mapValues = new float[(int)(songLength/timeBetweenNotes)];
        System.out.println(mapValues.length + "");
        while(index < mapValues.length){
            mapValues[index] = ((float)(timeToFillMap));
            timeToFillMap += timeBetweenNotes;
            index++;
        }
        for(float f : mapValues){
            System.out.println(f + "");
        }
    }
    public float getSongPosition(){
        return beatJams.getPosition();
    }
    public float getSongLength(){
        return (float)songLength;
    }
    public Music getBeatJams(){
        return beatJams;
    }
}