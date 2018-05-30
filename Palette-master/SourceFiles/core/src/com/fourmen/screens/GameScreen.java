package com.fourmen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.Actors.Enemy;
import com.fourmen.Actors.Player;
import com.fourmen.Actors.PlayerBounds;
import com.fourmen.utils.CameraStyles;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    public static final float PPM = 32;

    private float BOUND_WIDTH = 1500;
    private float BOUND_HEIGHT = 1500;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    //private PlayerBounds playerBounds = new PlayerBounds(WORLD_WIDTH,WORLD_HEIGHT);
    private PlayerBounds playerBounds = new PlayerBounds(BOUND_WIDTH,BOUND_HEIGHT);
    private Player player;
    private Enemy enemy;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        player = new Player();
        enemy = new Enemy();

    }

    @Override
    public void render(float delta) {
        clearScreen();
        cameraUpdate();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.end();
        blockPlayerLeavingTheWorld();
        drawDebug();
        //flower.drawDebug(shapeRenderer);

        float startX = camera.viewportWidth /2;
        float startY = camera.viewportHeight /2;

        CameraStyles.boundary(camera, startX, startY, BOUND_WIDTH - (2 * startX),
                BOUND_HEIGHT - (2 * startY));



        player.update(delta);
        enemy.update(delta);

        shapeRenderer.end();
        player.act();       //add to update instead
        enemy.act();
        blockPlayerLeavingTheWorld();
        //update(delta);
    }

    /**
     * this class will be used to implement
     * @param delta
     */
    //private void update(float delta){

    //}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        playerBounds.drawDebug(shapeRenderer);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.drawDebug(shapeRenderer);
        enemy.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void cameraUpdate(){
        CameraStyles.lockOnTarget(camera,player);
    }

    private void blockPlayerLeavingTheWorld() {
        player.setPosition(MathUtils.clamp(player.getX(),playerBounds.getX(),playerBounds.getWidth()+ playerBounds.getX() - player.getPlayerWidth()), MathUtils.clamp(player.getY(), playerBounds.getX(), playerBounds.getHeight() + 4 + player.getPlayerHeight()));
         enemy.setPosition(MathUtils.clamp(enemy.getX(),playerBounds.getX(),playerBounds.getWidth()+ playerBounds.getX() - enemy.getEnemyWidth()), MathUtils.clamp(enemy.getY(), playerBounds.getX(), playerBounds.getHeight() + 4 + enemy.getEnemyHeight()));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(com.badlogic.gdx.graphics.Color.BLACK.r, com.badlogic.gdx.graphics.Color.BLACK.g,
                com.badlogic.gdx.graphics.Color.BLACK.b, Color.BLACK.a);
        //Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }
}
