package com.fourmen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fourmen.box2D.Walls;

/**
 * Created by julienvillegas on 17/01/2017.
 */
public class Box2DRender implements Screen {

    private Stage stage;
    private Game game;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public Box2DRender(Game aGame) {
        game = aGame;
        Gdx.input.setInputProcessor(stage);
        stage = new Stage(new ScreenViewport());
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);

        stage.addActor(new Walls(world,0,Gdx.graphics.getHeight()/3,Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight()/10,-30));

    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");

    }

    @Override
    public void render(float delta) {
        //jave 8

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        //debugRenderer.render(world, stage.getCamera().combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }




    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
