package com.fourmen.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.box2D.Walls;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fourmen.utils.CameraStyles;


public class Box2DRender extends ScreenAdapter {
    private static float WORLD_WIDTH;
    private static float WORLD_HEIGHT;


    SpriteBatch batch;
    World world;
    Body body;
    Walls leftWall,rightWall, topWall,bottomWall;
    private float BOUND_WIDTH = 3000;
    private float BOUND_HEIGHT = BOUND_WIDTH * (3f/5f);

    Box2DDebugRenderer renderer;

    private Camera camera;
    private Viewport viewport;

    private Box2DPlayer player;

    public Box2DRender(int width, int height){
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
    }

    public void show() {
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player = new Box2DPlayer(world);
        leftWall = new Walls(world,0,0,BOUND_WIDTH * .086f,BOUND_HEIGHT);
        bottomWall = new Walls(world,0,0, BOUND_WIDTH,BOUND_HEIGHT * .1533333333f);
        topWall = new Walls(world,0,BOUND_HEIGHT - (.14F * BOUND_HEIGHT),
                BOUND_WIDTH, BOUND_HEIGHT * .14f);
        rightWall = new Walls(world,BOUND_WIDTH - (BOUND_WIDTH * .088f),
                0,BOUND_WIDTH * .088f,BOUND_HEIGHT);
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        renderer.render(world, camera.combined);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Check to see if the collision is between the second sprite and the bottom of the screen
                // If so apply a random amount of upward force to both objects... just because
                if((contact.getFixtureA().getBody() == player.getPlayerBody() &&
                        contact.getFixtureB().getBody() == bottomWall.getBody())
                        ||
                        (contact.getFixtureA().getBody() == bottomWall.getBody() &&
                                contact.getFixtureB().getBody() == player.getPlayerBody())) {

                    player.getPlayerBody().applyForceToCenter(0,MathUtils.random(20,50),true);
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }


    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        clearScreen();
        cameraUpdate();
        player.act();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(com.badlogic.gdx.graphics.Color.BLACK.r, com.badlogic.gdx.graphics.Color.BLACK.g,
                com.badlogic.gdx.graphics.Color.BLACK.b, Color.BLACK.a);
        //Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }
    public void update(float delta){
        //flower.drawDebug(shapeRenderer);

        // we should move this camera stuff to a new method
        float startX = camera.viewportWidth /2;
        float startY = camera.viewportHeight /2;

        CameraStyles.boundary(camera, startX, startY, BOUND_WIDTH - (2 * startX),
                BOUND_HEIGHT - (2 * startY));

        //player.update(delta);
    }
    public void dispose() {
        leftWall.dispose();
        world.dispose();
        batch.dispose();
    }

    private void cameraUpdate(){
        CameraStyles.lockOnTarget(camera,player);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
