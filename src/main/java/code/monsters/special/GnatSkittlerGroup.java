package code.monsters.special;

import code.monsters.Gnat;
import code.monsters.IncenseSkittler;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

public class GnatSkittlerGroup extends MonsterGroup {
    public GnatSkittlerGroup() {
        super(getGnats());
    }

    public static AbstractMonster[] getGnats() {
        AbstractMonster[] gnats = new AbstractMonster[26];
        int i = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                gnats[i] = new Gnat(-650 + (150 * x) + MathUtils.random(-30, 30), 10 + (125 * y) + MathUtils.random(-33, 33));
                i++;
            }
        }
        gnats[25] = new IncenseSkittler(300, 0);
        return gnats;
    }
}
