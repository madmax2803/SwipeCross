package com.swapnil.leveleditor;

import com.badlogic.gdx.Game;
import com.swapnil.leveleditor.screens.GameOverScreen;

public class SwipeCross extends Game {
	@Override
	public void create() {
		GameData gameData = new GameData("LevelLayout.xml", this);
		setScreen(new GameOverScreen(gameData, true));
	}
}
