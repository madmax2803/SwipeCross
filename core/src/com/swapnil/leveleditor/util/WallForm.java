package com.swapnil.leveleditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swapnil.leveleditor.item.Wall;

public class WallForm {

    private Table specMenu;
    private TextField heightField;
    private TextField widthField;

    public  WallForm(Wall wall) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        specMenu = new Table();

        List<String> list = new List<>(skin);
        list.setItems("Width", "Height");

        char[] accepted = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
        TextField.TextFieldFilter textFieldFilter = (textField, c) -> {
            for (char a : accepted)
                if (a == c) return true;

            return false;
        };

        heightField = new TextField("", skin);
        widthField = new TextField("", skin);

        heightField.setTextFieldFilter(textFieldFilter);
        widthField.setTextFieldFilter(textFieldFilter);

        widthField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(0);
                float widthParsed = 0;
                try {
                    widthParsed = Float.parseFloat(widthField.getText());
                    wall.setWidth(widthParsed);
                } catch (NumberFormatException e) {
                    //DO NOTHING
                }

            }
        });
        heightField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(1);
                try {
                    float heightParsed = Float.parseFloat(heightField.getText());
                    wall.setHeight(heightParsed);
                } catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        });

        Table textFieldTable = new Table();
        textFieldTable.add(widthField).row();
        textFieldTable.add(heightField).row();
        textFieldTable.pack();

        specMenu.add(list);
        specMenu.add(textFieldTable);
        specMenu.pack();
    }

    public void setDefaults(float width, float height) {
        widthField.setText(Integer.toString((int)width));
        heightField.setText(Integer.toString((int)height));
    }

    public Table getForm() {
        return specMenu;
    }
}
