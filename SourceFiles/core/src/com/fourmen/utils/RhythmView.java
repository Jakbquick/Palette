package com.fourmen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import chn.util.*;

public class RhythmView {
    private SpriteBatch batch;
    private Rectangle musicBar;
    public Music gameMusic;
    private FileInput inFile;

    private ArrayList<Float> mapValues;

    public RhythmView(SpriteBatch spriteBatch){
        this.batch = spriteBatch;
        createMapValues();
    }
    public void draw(){

    }
    public void createMapValues(){
        inFile = new FileInput("Maps/Music1.txt");
        while(inFile.hasMoreTokens()){
            mapValues.add(Float.valueOf((float)inFile.readDouble()));
        }
        inFile.close();
    }

}
