package com.swapnil.leveleditor;

import com.badlogic.gdx.Game;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.screens.PlayScreen;

/**
 * Created by swapnilverma on 01/07/16.
 */
public class SwipeCross extends Game {
    @Override
    public void create() {
        setScreen(new LevelEditor("LevelLayout.xml",this));
    }
}
