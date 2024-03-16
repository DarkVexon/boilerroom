package code.scenes;

import code.util.TexLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

public class BoilerRoomShape {
    private BoilerRoomShapeType type;
    private float sizeMult;
    private float rotation;
    private float x;
    private float y;

    private static Texture circleTex = TexLoader.getTexture("boilerResources/images/scene/circle.png");
    private static Texture squareTex = TexLoader.getTexture("boilerResources/images/scene/square.png");
    private static Texture triangleTex = TexLoader.getTexture("boilerResources/images/scene/triangle.png");

    public BoilerRoomShape() {
        int val = MathUtils.random(0, 2);
        switch (val) {
            case 0:
                type = BoilerRoomShapeType.CIRCLE;
                break;
            case 1:
                type = BoilerRoomShapeType.SQUARE;
                break;
            case 2:
                type = BoilerRoomShapeType.TRIANGLE;
                break;
        }
        sizeMult = MathUtils.random(0.5F, 1.25F);
        rotation = MathUtils.random(0, 360);
        x = MathUtils.random(0, Settings.WIDTH);
        y = MathUtils.random(0, Settings.HEIGHT);
    }

    public void render(SpriteBatch sb) {
        switch (this.type) {
            case CIRCLE:
                sb.draw(circleTex, x, y);
                break;
            case SQUARE:
                sb.draw(squareTex, x, y);
                break;
            case TRIANGLE:
                sb.draw(triangleTex, x, y);
                break;
        }
    }

    enum BoilerRoomShapeType {
        CIRCLE, SQUARE, TRIANGLE
    }
}
