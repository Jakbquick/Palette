package com.fourmen.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenu implements Screen {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private Texture buttonExit, playButton, background;
    private BitmapFont font;


    public MainMenu() {
    }


    @Override
    public void show() {
        stage = new Stage();



        skin = new Skin(Gdx.files.internal("ui/skinexport.json"));

        table = new Table(skin);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("Images/Titlescreen.png"))));
        table.setFillParent(true);
        //table.setDebug(true);

        stage.addActor(table);
        table.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        ImageButton playbutt = new ImageButton(skin);

        playbutt.setPosition(280, 300);


        playbutt.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(1500,(int)(1500 * (double)(9.0/16.0))));

                event.stop();

            }
        });





        table.debug();
        stage.addActor(table);
        stage.addActor(playbutt);
        Gdx.input.setInputProcessor(stage);
        table.setDebug(false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

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


    }
}
