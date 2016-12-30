package com.swapnil.leveleditor.util.forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swapnil.leveleditor.item.Guard;
import com.swapnil.leveleditor.util.Point;

public class GuardForm {

    private Table specMenu;
    private TextField x1Field, y1Field;
    private TextField x2Field, y2Field;

    public GuardForm(Guard guard) {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/atlas.pack"));
        Skin skin = new Skin(Gdx.files.internal("Skins/MenuSkin.json"), atlas);

        TextButton move = new TextButton("Check Movement", skin);
        move.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                guard.setMove(!guard.isMove());
            }
        });
        specMenu = new Table();

        List<String> list1 = new List<>(skin);
        list1.setItems("x1", "y1");
        list1.setColor(0, 1, 0, 0.3f);

        List<String> list2 = new List<>(skin);
        list2.setItems("x2", "y2");
        list2.setColor(1, 0, 0, 0.3f);


        char[] accepted = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
        TextField.TextFieldFilter textFieldFilter = (textField, c) -> {
            for (char a : accepted)
                if (a == c) return true;

            return false;
        };

        x1Field = new TextField("", skin);
        x2Field = new TextField("", skin);
        y1Field = new TextField("", skin);
        y2Field = new TextField("", skin);

        x1Field.setTextFieldFilter(textFieldFilter);
        x2Field.setTextFieldFilter(textFieldFilter);
        y1Field.setTextFieldFilter(textFieldFilter);
        y2Field.setTextFieldFilter(textFieldFilter);

        x1Field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list2.setSelectedIndex(0);
                try {
                    float x1Parsed = Float.parseFloat(x1Field.getText());
                }catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        });
        y1Field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list1.setSelectedIndex(1);
                try {
                    float y1Parsed = Float.parseFloat(y1Field.getText());
                } catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        });
        x2Field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list2.setSelectedIndex(0);
                try {
                    float x2Parsed = Float.parseFloat(x2Field.getText());
                }catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        });
        y2Field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list2.setSelectedIndex(1);
                try {
                    float y2Parsed = Float.parseFloat(y2Field.getText());
                }catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        });

        Table textFieldTable1 = new Table();
        textFieldTable1.add(x1Field).row();
        textFieldTable1.add(y1Field).row();
        textFieldTable1.pack();

        Table textFieldTable2 = new Table();
        textFieldTable2.add(x2Field).row();
        textFieldTable2.add(y2Field).row();
        textFieldTable2.pack();

        specMenu.add(list1);
        specMenu.add(textFieldTable1).row();
        specMenu.add(list2);
        specMenu.add(textFieldTable2).row();
        specMenu.add(move).colspan(4);
        specMenu.pack();
    }

    public void setDefaults(Point p1, Point p2) {
        x1Field.setText(Integer.toString((int) p1.getX()));
        y1Field.setText(Integer.toString((int) p1.getY()));
        x2Field.setText(Integer.toString((int) p2.getX()));
        y2Field.setText(Integer.toString((int) p2.getY()));
    }

    public Table getForm() {
        return specMenu;
    }

}
