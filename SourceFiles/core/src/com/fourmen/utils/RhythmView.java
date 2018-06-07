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
    private Texture whenButton,clicked;
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
    private boolean musicStarted,odd;
    private double bpm;
    private double timeToFillMap;
    private double betweenNotes, songLength;
    private int index;
    private float timer;
    private float stopWatch;

    public RhythmView(SpriteBatch spriteBatch){
        stopWatch = 0f;
        odd = false;
        timeToFillMap = 0.0;
        bpm = 135;
        songLength = 164.0;
        betweenNotes = (1.0/(bpm / 60.0));
        i = 0;
        this.batch = spriteBatch;
        //readMapValues();
        createWithBPM();
        bar = new Texture("Images/BlackBar.png");
        y = Gdx.graphics.getHeight() - 115;
        whenButton = new Texture("Images/Beat/NoClick.png");
        clicked = new Texture("Images/Beat/Click.png");
        beatHeight = 75;
        beatWidth = 75;
        drawClick = false;
        clickTime = 0;
        beatJams = Gdx.audio.newMusic(Gdx.files.internal("Music/song1.mp3"));

        centerWhen = new Vector2(100 + (beatWidth/2f), y + (.5f * bar.getHeight()));
        xDistance = Gdx.graphics.getWidth() + (.5f * Beat.beatWidth) -         //the first beatSize here is the size of the beat transitioning across the map (replace later)
                centerWhen.x;
        velocity = 220f / 60f;
        System.out.println(velocity + " " + Gdx.graphics.getWidth());
        timeBeforeSpawn = xDistance / (velocity * 60f);
        System.out.println(timeBeforeSpawn + "");
        beatList = new ArrayList<Beat>();
        timer = timeBeforeSpawn;
    }
    public void update(float delta){
        if(timer <= 0 && musicStarted){
            beatJams.play();
            musicStarted = false;
        }
        else if(musicStarted){
            stopWatch+= delta;
            timer -= delta;
        }
        for(Beat b : beatList){
            b.update();
        }
        for(int j = 0; j < beatList.size(); j++){
            beatList.get(j).update();
            if(beatList.get(j).getXPosition() < -Beat.beatWidth){
                beatList.get(j).dispose();
                beatList.remove(j);
            }
        }
        if (clickTime == 7){
            drawClick = false;
            clickTime = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
                Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !drawClick){
            drawClick = true;
        }
        if(i < mapValues.length && stopWatch >= mapValues[i]){
            beatList.add(new Beat(velocity, Gdx.graphics.getWidth(),y + (.5f * bar.getHeight())));
            i++;
        }


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
        musicStarted = true;
    }
    public void createWithBPM(){
        mapValues = new float[(int)(songLength/betweenNotes) + 0];
        while(index < mapValues.length){
            if(odd && index >= 0){
                mapValues[index -0] = ((float)(timeToFillMap - timeBeforeSpawn));
                odd = !odd;
            }
            else{
                odd = ! odd;
            }
            timeToFillMap += betweenNotes;
            index++;
        }
    }
}
