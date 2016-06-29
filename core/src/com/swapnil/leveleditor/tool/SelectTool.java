package com.swapnil.leveleditor.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.swapnil.leveleditor.LevelEditor;
import com.swapnil.leveleditor.item.Item;

public class SelectTool extends Tool {

    public SelectTool(LevelEditor levelEditor) {
        super(levelEditor);
    }

    @Override
    public void onPress(int screenX, int screenY) {
        Array<Item> itemArray = levelEditor.getItemList();
        Item temp;
        boolean flag = true;
        for (int i = 0; i < itemArray.size; i++) {
            temp = itemArray.get(i);
            if (temp.contains(screenX, Gdx.graphics.getHeight() -  screenY)) {
                flag = true;
                levelEditor.setSelectedItem(temp);
                break;
            }
            else {
               flag = false;
            }
        }
        if(!flag) {
            levelEditor.selectedItem = null;
            levelEditor.setDrawSelectionRectangle(false);
        }
    }

}
