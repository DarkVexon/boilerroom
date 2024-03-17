package code.scenes;

import code.util.TexLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

public class BoilerRoomScene extends AbstractScene {
    private static Texture tex = TexLoader.getTexture("boilerResources/images/scene/boiler.png");
    private static Texture campfireBg = TexLoader.getTexture("boilerResources/images/scene/camp.png");
    private static ArrayList<BoilerRoomShape> shapes = new ArrayList<>();

    public BoilerRoomScene() {
        super("beyondScene/scene.atlas");
        this.ambianceName = "AMBIANCE_BEYOND";
        this.fadeInAmbiance();
    }

    @Override
    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.draw(tex, 0, 0);
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

    }
}
