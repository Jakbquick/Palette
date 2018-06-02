package com.fourmen.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.box2D.Walls;

public class Box2DRender extends ScreenAdapter {
    SpriteBatch batch;
    World world;
    Body body;
    Walls leftWall,rightWall, topWall,bottomWall;
    private float BOUND_WIDTH = 3000;
    private float BOUND_HEIGHT = BOUND_WIDTH * (3f/5f);


    public void show() {

        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        leftWall = new Walls(world);

    }


    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(leftWall.getWallSprite(), leftWall.getWallSprite().getX(), leftWall.getWallSprite().getY());
        batch.end();
    }


    public void dispose() {
        leftWall.dispose();
        world.dispose();
    }
}
