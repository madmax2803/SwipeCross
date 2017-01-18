package com.swapnil.leveleditor.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.swapnil.leveleditor.util.Point;

public class Guard extends Item {

    private Point selectedEdge;

    private final float width = new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png")).getWidth();
    private final float height = new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png")).getHeight();

    private Array<Sprite> sprites;

    private Sprite spritePlay = new Sprite(new Texture(Gdx.files.internal("unitTexture/Guard.png")));

    private Array<Point> points;

    private boolean move;
    private boolean isPlaying;

    private Body body;

    private int waypoint = 0, adder = 1;

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public Guard() {
        this.actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
//                if(isPlaying) {
                    spritePlay.draw(batch);
//                }else {
                    for (Sprite sprite : sprites) {
                        sprite.draw(batch);
                    }
//                }
            }
        };

        points = new Array<>();
        sprites = new Array<>();

        selectedEdge = new Point();

        spritePlay.setOrigin(spritePlay.getX(), spritePlay.getY() + spritePlay.getHeight()/2);

        isPlaying = true;
    }

    @Override
    public Table getForm() {
        return null;
    }

    @Override
    public boolean contains(int screenX, int screenY) {

        for(int i=0;i<points.size;i++) {
            sprites.get(i).setBounds(points.get(i).getX() - sprites.get(i).getWidth()/2,
                    points.get(i).getY() - sprites.get(i).getHeight()/2,
                    sprites.get(i).getWidth(), sprites.get(i).getHeight());

            if(sprites.get(i).getBoundingRectangle().contains(screenX, screenY)) {
                selectedEdge = points.get(i);
                return true;
            }
        }
        return false;

    }

    @Override
    public void writeToXml(XmlWriter xmlWriter) {

        try {
            xmlWriter.element("Guard")
                    .element("Angle")
                    .attribute("Value", getAngle())
                    .pop();
            for(int i=0;i<points.size;i++) {
                xmlWriter.element("Point" + i)
                        .attribute("X", points.get(i).getX())
                        .attribute("Y", points.get(i).getY())
                        .pop();
            }
            xmlWriter.pop();
        } catch (Exception e) {
            System.out.println("XML WRITE FAILED");
        }
    }

    @Override
    public Item loadFromXml(XmlReader.Element element) {

        Guard guard = new Guard();
        guard.setAngle(element.getChildByName("Angle").getFloat("Value"));
        for(int i = 0;i<element.getChildCount() - 1;i++) {
            guard.points.add(new Point(element.getChildByName("Point" + i).getFloat("X"),
                    element.getChildByName("Point" + i).getFloat("Y")));
            guard.sprites.add(new Sprite(new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png"))));
        }


        guard.setX(guard.points.first().getX());
        guard.setY(guard.points.first().getY());

        guard.spritePlay.setPosition(guard.points.first().getX(),guard.points.first().getY()
        );
//        guard.spritePlay.setPosition(guard.points.first().getX() - spritePlay.getWidth()/2, guard.points.first().getY() - spritePlay.getHeight()/2);

        guard.spritePlay.setOrigin(spritePlay.getX(), spritePlay.getY() + spritePlay.getHeight()/2);
        return guard;

    }

    @Override
    public void createBody(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x/PIXELS_TO_METRES,Gdx.graphics.getHeight()/PIXELS_TO_METRES -  y/PIXELS_TO_METRES);
        bodyDef.angle = getAngle() * MathUtils.degRad;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{spritePlay);

//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = circle;
//        fixtureDef.restitution = 0.5f;
//        fixtureDef.density = 0.1f;
//        body.createFixture(fixtureDef);
//
//        circle.dispose();

    }

    @Override
    public void updateEditor() {

        int i;

        selectedEdge.setX(getX());
        selectedEdge.setY(Gdx.graphics.getHeight() - getY());

        for(i = 0;i<points.size-1;i++) {
            sprites.get(i).setPosition(points.get(i).getX() - width/2, (points.get(i).getY() - sprites.get(i).getHeight() / 2));

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
            shapeRenderer.line(points.get(i).getX(), points.get(i).getY(), 0,
                    points.get(i+1).getX(),points.get(i+1).getY(), 0);
            shapeRenderer.box(spritePlay.getX(), spritePlay.getY(), 0, spritePlay.getWidth(), spritePlay.getHeight(), 0);
            shapeRenderer.end();
            shapeRenderer.dispose();

        }

        try {
            sprites.get(i).setPosition(points.get(i).getX() - sprites.get(i).getWidth() / 2,
                     (points.get(i).getY() - sprites.get(i).getHeight() / 2));
        }catch (Exception e) {
            //DO NOTHING
        }

        actor.setWidth(width);
        actor.setHeight(height);
        actor.setX(getX() - width/2);
        actor.setY(Gdx.graphics.getHeight() - getY() - height/2);
    }

    @Override
    public void updatePlay() {

        int speed = 250;
        Vector2 velocity = new Vector2();
        float x = spritePlay.getX(), y = spritePlay.getY();
//        float x = 0, y = 0;

        try {
            x = points.get(waypoint - adder).getX();
            y = points.get(waypoint - adder).getY();
        } catch (Exception e) {
            x = spritePlay.getX();
            y = spritePlay.getY();
        }

        for(int i = 0;i<points.size-1;i++) {
            sprites.get(i).setPosition(points.get(i).getX() - width/2, (points.get(i).getY() - sprites.get(i).getHeight() / 2));

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
            shapeRenderer.line(points.get(i).getX(), points.get(i).getY(), 0,
                    points.get(i+1).getX(), points.get(i+1).getY(), 0);
            shapeRenderer.box(spritePlay.getX() + spritePlay.getOriginX(), spritePlay.getY() + spritePlay.getOriginY(), 0, spritePlay.getWidth(), spritePlay.getHeight(), 0);
            shapeRenderer.end();
            shapeRenderer.dispose();

        }

        float angle = (float) Math.atan2(points.get(waypoint).getY() - y, points.get(waypoint).getX() - x);

        velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);




        spritePlay.setX(spritePlay.getX() + velocity.x * Gdx.graphics.getDeltaTime());
        spritePlay.setY(spritePlay.getY() + velocity.y * Gdx.graphics.getDeltaTime());


        spritePlay.setRotation(angle * MathUtils.radiansToDegrees);

        if(isWayPointReached()) {
            if(waypoint == points.size-1) {
                adder = -1;
            } else if(waypoint==0) {
                adder = 1;
            }
            waypoint +=adder;

        }
    }

    private boolean isWayPointReached() {
        int tolerance = 8;
        Vector2 vector2 = new Vector2(points.get(waypoint).getX(), points.get(waypoint).getY());
        return vector2.dst(new Vector2(spritePlay.getX(),spritePlay.getY()))< tolerance;
    }

    @Override
    public void beginContactWith(Item itemB) {
        if(itemB.getClass() == Player.class) {
            System.out.println("Player Guard Collision");

        }
    }

    public void addPoint(int screenX, int screenY)  {
        points.add(new Point(screenX,Gdx.graphics.getHeight() - screenY));
        sprites.add(new Sprite(new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png"))));
    }

    public void createNewPoint() {
        points.add(new Point(getX(), getY()));
        sprites.add(new Sprite(new Texture(Gdx.files.internal("unitTexture/GuardEditor1.png"))));

    }
}
