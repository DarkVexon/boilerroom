package code.monsters.special;

import code.monsters.Gnat;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

public class GnatMonsterGroup extends MonsterGroup {
    public GnatMonsterGroup() {
        super(getGnats());
    }

    public static AbstractMonster[] getGnats() {
        AbstractMonster[] gnats = new AbstractMonster[50];
        int i = 0;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                gnats[i] = new Gnat(-675 + (100 * x) + MathUtils.random(-30, 30), 50 + (110 * y) + MathUtils.random(-33, 33));
                i++;
            }
        }
        return gnats;
    }
}
