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
                try {
                    levelEditor.getSelectedItem().getForm().setVisible(false);
                }catch (NullPointerException e)
                {
                    //DO NOTHING
                }
                levelEditor.setSelectedItem(temp);
                levelEditor.getCommonMenu().setDefaults(new Point(levelEditor.getSelectedItem().getX(),
                        levelEditor.getSelectedItem().getY()), levelEditor.selectedItem.getAngle());
                levelEditor.getCommonMenu().getMenu().setVisible(true);
                break;
            }
            else {
               flag = false;
            }
        }
        if(!flag) {
            try {
                levelEditor.getCommonMenu().getMenu().setVisible(false);
                levelEditor.getSelectedItem().getForm().setVisible(false);
            } catch (Exception e) {
                //DO NOTHING
            }
            levelEditor.setDrawSelectionRectangle(false);
            levelEditor.selectedItem = null;

        }
    }

}
