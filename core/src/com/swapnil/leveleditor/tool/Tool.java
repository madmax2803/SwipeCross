package com.swapnil.leveleditor.tool;

import com.swapnil.leveleditor.LevelEditor;


public abstract class Tool {


    protected LevelEditor levelEditor;

    public Tool(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
    }

    public abstract void onPress(int screenX, int screenY);

}
