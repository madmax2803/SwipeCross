package com.swapnil.leveleditor;

import com.badlogic.gdx.Game;
import com.swapnil.leveleditor.screens.GameOverScreen;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.screens.PlayScreen;

public class SwipeCross extends Game {
	@Override
	public void create() {
		GameData gameData = new GameData("LevelLayoutTemp.xml", this);
		setScreen(new PlayScreen(gameData));
	}
}
