package com.swapnil.leveleditor;

import com.badlogic.gdx.Game;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.screens.PlayScreen;

public class SwipeCross extends Game {
	@Override
	public void create() {
		setScreen(new PlayScreen("LevelLayout.xml",this));
	}
}
