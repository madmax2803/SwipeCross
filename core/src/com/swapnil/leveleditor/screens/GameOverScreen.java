package com.swapnil.leveleditor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swapnil.leveleditor.GameData;
import com.swapnil.leveleditor.SwipeCross;

/**
 * Created by Home on 29-11-2016.
 */
public class GameOverScreen implements Screen{

    private Stage stage = new Stage();

    public GameOverScreen(GameData gameData, boolean gameWin) {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);
        TextButton playAgain = new TextButton("Play Again", skin);
        TextField result = new TextField("", skin);

        playAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameData.getGame().setScreen(new PlayScreen(gameData));
            }
        });

        if(gameWin)
            result.setText("You Won!!");
        else
            result.setText("You Lost!!");
        result.setPosition(Gdx.graphics.getWidth()/2 - result.getWidth()/2, Gdx.graphics.getHeight()/2);
        playAgain.setPosition(Gdx.graphics.getWidth()/2 - playAgain.getWidth()/2, Gdx.graphics.getHeight()/4);

        stage.addActor(result);
        stage.addActor(playAgain);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        stage.act();
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
        stage.dispose();
    }
}
