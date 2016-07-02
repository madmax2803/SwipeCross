package com.swapnil.leveleditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swapnil.leveleditor.screens.LevelEditor;

public class CommonMenu {

    private TextField xField;
    private TextField yField;
    private TextField angleField;

    private final Table menu;

    public CommonMenu(LevelEditor levelEditor) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        xField = new TextField("", skin);
        yField = new TextField("", skin);
        angleField = new TextField("", skin);

        menu = new Table();

        List<String> list = new List<>(skin);
        list.setItems("X", "Y", "Angle");
        list.setSelectedIndex(0);

        char[] accepted = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
        TextField.TextFieldFilter textFieldFilter = (textField, c) -> {
            for (char a : accepted)
                if (a == c ) return true;

            return false;
        };

        xField.setTextFieldFilter(textFieldFilter);
        yField.setTextFieldFilter(textFieldFilter);
        angleField.setTextFieldFilter(textFieldFilter);

        xField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(0);
                String xString = xField.getText();
                int xParsed;
                try {
                    xParsed = Integer.parseInt(xString);
                    levelEditor.getSelectedItem().setX(xParsed);
                } catch (NumberFormatException e) {
                    //ignore to keep as existing
                }

            }
        });

        yField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(1);
                String yString = yField.getText();
                int yParsed;
                try {
                    yParsed = Integer.parseInt(yString);
                    levelEditor.getSelectedItem().setY(yParsed);
                } catch (NumberFormatException e) {
                    //ignore to keep as existing
                }

            }
        });

        angleField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(2);
                String angleString = angleField.getText();
                float angleParsed;
                try {
                    angleParsed = Float.parseFloat(angleString);
                    levelEditor.getSelectedItem().setAngle(angleParsed);
                }catch (NumberFormatException e){
                    //DO NOTHING
                }

            }
        });


        Table textFieldTable = new Table();

        textFieldTable.add(xField).row();
        textFieldTable.add(yField).row();
        textFieldTable.add(angleField).row();


        menu.add(list);
        menu.add(textFieldTable);
        menu.pack();
        menu.setBounds(Gdx.graphics.getWidth() - menu.getWidth(),
                Gdx.graphics.getHeight() - menu.getHeight(),
                menu.getWidth(), menu.getHeight());

        menu.setPosition(Gdx.graphics.getWidth() - menu.getWidth(),
                Gdx.graphics.getHeight() - menu.getHeight());
    }

    public Table getMenu() {
        return menu;
    }

    public void setDefaults(Point point, float angle) {
        xField.setText(Integer.toString((int)point.getX()));
        yField.setText(Integer.toString((int)point.getY()));
        angleField.setText(Float.toString(angle));
    }
}
