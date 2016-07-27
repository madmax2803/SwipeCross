package com.swapnil.leveleditor.tool;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.swapnil.leveleditor.screens.LevelEditor;
import com.swapnil.leveleditor.item.*;

public class ItemTool extends Tool {

    private ItemType itemType;

    public ItemTool(LevelEditor levelEditor,  ItemType itemType) {
        super(levelEditor);
        this.itemType = itemType;
    }

    @Override
    public void onPress(int screenX, int screenY) {
        Item item = createItem(itemType);
        item.setX(screenX);
        item.setY(screenY);
        super.levelEditor.addToStage(item);
    }

    private Item createItem(ItemType itemType) {
        switch (itemType) {
            case PLAYER:
                Player player = new Player();
                player.setSprite(new Sprite(new Texture("unitTexture/Player.png")));
                return player;
            case DESTINATION:
                Destination destination = new Destination();
                destination.setSprite(new Sprite(new Texture("unitTexture/Destination.png")));
                return destination;
            case WALL:
                Wall wall = new Wall();
                wall.setSprite(new Sprite(new Texture("unitTexture/Wall.png")));
                return wall;
            case CAMERA:
                Camera camera = new Camera();
                camera.setSprite(new Sprite(new Texture("unitTexture/Camera.png")));
                return camera;
            case GUARD:
                Guard guard = new Guard();
                guard.setSprite(new Sprite(new Texture("unitTexture/GuardEditor1.png")), new Sprite(new Texture("unitTexture/GuardEditor2.png")));
                return guard;
            case LASER:
                Laser laser = new Laser();
                laser.setSprite(new Sprite(new Texture("unitTexture/Laser.png")));
                return laser;

        }
        return null;
    }


}
