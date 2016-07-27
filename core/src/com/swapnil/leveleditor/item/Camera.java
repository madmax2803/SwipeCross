package com.swapnil.leveleditor.item;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.util.forms.CameraForm;

public class Camera extends Item{

    private Sprite sprite;
    private float deviationAngle;
    private float width, height;
    private CameraForm cameraForm;

    private boolean deviateUp;

    private float cameraRotationAngle = 0;
    private boolean rotate;

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
        cameraRotationAngle = 0;
    }

    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }

    public Camera() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                sprite.draw(batch);
            }
        };
        cameraForm = new CameraForm(this);
        cameraForm.getForm().setVisible(false);
        deviateUp = true;
        setRotate(true);

    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;

        setWidth(this.sprite.getWidth());
        setHeight(this.sprite.getHeight());

        cameraForm.setDefaults(getWidth(), getHeight(), getDeviationAngle());
    }

    @Override
    public Table getForm() {
        return cameraForm.getForm();
    }

    public float getDeviationAngle() {
        return deviationAngle;
    }

    public void setDeviationAngle(float deviationAngle) {
        this.deviationAngle = deviationAngle;
    }

    @Override
    public boolean contains(int screenX, int screenY) {
        sprite.setBounds(x - getWidth()/2,Gdx.graphics.getHeight() - y - getHeight()/2,
                getWidth(), getHeight());

        return sprite.getBoundingRectangle().contains(screenX, screenY);
    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {

        try {
            xmlWriter.element("Camera")
                        .element("Position")
                            .attribute("X", getX())
                            .attribute("Y", getY())
                        .pop()
                        .element("Angle")
                            .attribute("Value", getAngle())
                        .pop()
                        .element("Size")
                            .attribute("Width", getWidth())
                            .attribute("Height", getHeight())
                        .pop()
                        .element("DeviationAngle")
                            .attribute("Value", getDeviationAngle())
                        .pop()
                    .pop();
        }
        catch (Exception e) {
            System.out.println("XML WRITE FAILED");
        }
    }

    @Override
    public Item loadFromXml(XmlReader.Element element) {
        Camera camera = new Camera();
        camera.setSprite(new Sprite(new Texture("unitTexture/Camera.png")));
        camera.setX(element.getChildByName("Position").getFloat("X"));
        camera.setY(element.getChildByName("Position").getFloat("Y"));
        camera.setAngle(element.getChildByName("Angle").getFloat("Value"));
        camera.setWidth(element.getChildByName("Size").getFloat("Width"));
        camera.setHeight(element.getChildByName("Size").getFloat("Height"));
        camera.setDeviationAngle(element.getChildByName("DeviationAngle").getFloat("Value"));
        camera.cameraForm.setDefaults(camera.getWidth(), camera.getHeight(), camera.getDeviationAngle());
        return camera;

    }

    @Override
    public void createBody(World world) {

    }

    @Override
    public void updateEditor() {

        sprite.setX(getX() - getWidth()/2);
        sprite.setY(Gdx.graphics.getHeight() - getY() - getHeight()/2);

        sprite.setRotation(getAngle());
        setAngle(sprite.getRotation());
        sprite.setSize(getWidth(), getHeight());

        sprite.setOriginCenter();
        sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY() - getHeight()/2);
        actor.setOrigin(sprite.getOriginX(), sprite.getOriginY());


        actor.setX(getX() - getWidth()/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - getHeight()/2);
        actor.setWidth(getWidth());
        actor.setHeight(getHeight());
        actor.setRotation(getAngle());

        if (rotate) {
            deviate();
        }

    }

    @Override
    public void updatePlay() {

    }

    private void deviate() {
        if (deviateUp) {
            cameraRotationAngle += 0.5f;
            sprite.setRotation(getAngle() + cameraRotationAngle);
            if(Math.abs(cameraRotationAngle) == Math.abs(getDeviationAngle()))
                deviateUp = false;
        }else {
            cameraRotationAngle -= 0.5f;
            sprite.setRotation(getAngle() + cameraRotationAngle);
            if(Math.abs(cameraRotationAngle) == Math.abs(getDeviationAngle()))
                deviateUp = true;

        }
    }

}
