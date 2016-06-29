package com.swapnil.leveleditor.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.swapnil.leveleditor.LevelEditor;
import com.swapnil.leveleditor.item.Item;

public class DeleteTool extends Tool  {

    public DeleteTool(LevelEditor levelEditor) {
        super(levelEditor);
    }

    @Override
    public void onPress(int screenX, int screenY) {
        Array<Item> itemArray = levelEditor.getItemList();
        Item temp;
        for (int i = 0; i < itemArray.size; i++) {
            temp = itemArray.get(i);
            if (temp.contains(screenX, Gdx.graphics.getHeight() -  screenY)) {
                temp.getActor().remove();
                itemArray.removeIndex(i);
                break;
            }
        }
    }
}
