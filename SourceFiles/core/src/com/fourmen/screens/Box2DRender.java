package com.fourmen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.box2D.Walls;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.utils.BodyEditorLoader;
import com.fourmen.utils.CameraStyles;


public class Box2DRender extends ScreenAdapter {
    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;


    SpriteBatch batch;
    World world;
    Walls walls;

    Box2DPlayer player;
    private float BOUND_WIDTH = 3000;
    private float BOUND_HEIGHT = BOUND_WIDTH * (3f/5f);

    Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;
    private Viewport viewport;
    private float timeSinceStart;


    public Box2DRender(int width, int height){
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }

    public void show() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        player = new Box2DPlayer(world);
        walls = new Walls(world,0,0);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        //viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport = new ExtendViewport(camera.viewportWidth / 2f, camera.viewportHeight / 2f, camera);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        timeSinceStart = 0;
    }


    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        camera.update();
        timeSinceStart += delta;
        update(delta);
        clearScreen();
        debugRenderer.render(world, camera.combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        camera.update();
        debugRenderer.render(world,camera.combined);
        cameraUpdate();
        player.act();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    private void clearScreen() {
        // Gdx.gl.glClearColor(com.badlogic.gdx.graphics.Color.BLACK.r, com.badlogic.gdx.graphics.Color.BLACK.g,
        //com.badlogic.gdx.graphics.Color.BLACK.b, Color.BLACK.a);

        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public void update(float delta) {
        //flower.drawDebug(shapeRenderer);

        // we should move this camera stuff to a new method
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;

        CameraStyles.boundary(camera, startX, startY, BOUND_WIDTH - (2 * startX),
                BOUND_HEIGHT - (2 * startY));

        player.updateTimers(delta);
    }


    public void drawDebug() {

    }

    public void dispose() {
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
        walls.dispose();
        world.destroyBody(walls.getBody());
    }

    private void cameraUpdate() {
        CameraStyles.lockOnTarget(camera, player);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}

