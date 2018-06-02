package com.fourmen.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.box2D.Walls;

public class Box2DRender extends ScreenAdapter {
    SpriteBatch batch;
    World world;
    Body body;
    Walls leftWall,rightWall, topWall,bottomWall;
    Box2DPlayer player;
    private float BOUND_WIDTH = 3000;
    private float BOUND_HEIGHT = BOUND_WIDTH * (3f/5f);


    public void show() {

        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        leftWall = new Walls(world,0,0,BOUND_WIDTH * .086f,BOUND_HEIGHT);
        bottomWall = new Walls(world,0,0, BOUND_WIDTH,BOUND_HEIGHT * .1533333333f);
        topWall = new Walls(world,0,BOUND_HEIGHT - (.14F * BOUND_HEIGHT),
                BOUND_WIDTH, BOUND_HEIGHT * .14f);
        rightWall = new Walls(world,BOUND_WIDTH - (BOUND_WIDTH * .088f),
                0,BOUND_WIDTH * .088f,BOUND_HEIGHT);
        player = new Box2DPlayer(world);

    }


    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(leftWall.getWallSprite(), leftWall.getWallSprite().getX(), leftWall.getWallSprite().getY(),leftWall.getWidth(),
                leftWall.getHeight());
        batch.draw(bottomWall.getWallSprite(), bottomWall.getWallSprite().getX(), bottomWall.getWallSprite().getY(),bottomWall.getWidth(),
                bottomWall.getHeight());
        batch.draw(topWall.getWallSprite(), topWall.getWallSprite().getX(), topWall.getWallSprite().getY(),topWall.getWidth(),
                topWall.getHeight());
        batch.draw(rightWall.getWallSprite(), rightWall.getWallSprite().getX(), rightWall.getWallSprite().getY(),rightWall.getWidth(),
                rightWall.getHeight());
        batch.draw(player.currentFrame, player.getX(), player.getY());
        batch.end();
    }


    public void dispose() {
        leftWall.dispose();
        world.dispose();
        batch.dispose();
    }
}
