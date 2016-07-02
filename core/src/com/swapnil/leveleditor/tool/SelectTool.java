package com.swapnil.leveleditor.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.item.Item;
import com.swapnil.leveleditor.util.Point;

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
                levelEditor.getCommonMenu().getMenu().setVisible(true);
                levelEditor.getCommonMenu().setDefaults(new Point(levelEditor.getSelectedItem().getX(),
                        levelEditor.getSelectedItem().getY()), levelEditor.selectedItem.getAngle());
                break;
            }
            else {
               flag = false;
            }
        }
        if(!flag) {
            levelEditor.selectedItem = null;
            levelEditor.getCommonMenu().getMenu().setVisible(false);
            levelEditor.setDrawSelectionRectangle(false);
        }
    }

}
