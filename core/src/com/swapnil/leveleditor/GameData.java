package com.swapnil.leveleditor;

/**
 * Created by Home on 30-11-2016.
 */
public class GameData {

    private String levelFile;
    private SwipeCross game;

    public GameData(String levelFile, SwipeCross game) {
        this.levelFile = levelFile;
        this.game = game;
    }

    public String getLevelFile() {
        return levelFile;
    }

    public SwipeCross getGame() {
        return game;
    }
}
