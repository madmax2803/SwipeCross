package com.swapnil.leveleditor.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.item.Item;

import java.io.IOException;
import java.io.StringWriter;

public class SaveListener extends ChangeListener {

    private LevelEditor levelEditor;

    public SaveListener(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Array<Item> itemArray = levelEditor.getItemList();
        Item item;
        StringWriter stringWriter = new StringWriter();
        XmlWriter xmlWriter = new XmlWriter(stringWriter);
        try {
            xmlWriter = xmlWriter.element("Root");
        }catch(Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i<itemArray.size; i++) {
            item = itemArray.get(i);
            item.writeToXml(xmlWriter);
        }
        try {
            xmlWriter.pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        levelEditor.getHandle().writeString(stringWriter.toString(), false);
        levelEditor.getMenu().setVisible(false);
    }

}
