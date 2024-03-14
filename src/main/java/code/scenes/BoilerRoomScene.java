package code.scenes;

import code.util.TexLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

public class BoilerRoomScene extends AbstractScene {
    private static Texture tex = TexLoader.getTexture("img/scene/boiler");
    private static Texture campfireBg = TexLoader.getTexture("img/scene/camp");
    private static ArrayList<BoilerRoomShape> shapes = new ArrayList<>();

    public BoilerRoomScene() {
        super("");

    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.draw(tex, 0, 0);
        for (BoilerRoomShape s : shapes) {
            s.render(sb);
        }
    }

    @Override
    public void renderCombatRoomFg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
    }

    @Override
    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(campfireBg, 0, 0);
    }

    @Override
    public void randomizeScene() {
        shapes.clear();
        for (int i = 0; i < MathUtils.random(3, 8); i++) {
            shapes.add(new BoilerRoomShape());
        }
    }
}
