package code.monsters.special;

import code.monsters.Gnat;
import code.monsters.IncenseSkittler;
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
                gnats[i] = new Gnat(-150 + (10 * x), -200 + (10 * y));
                i++;
            }
        }
        gnats[26] = new IncenseSkittler(100, 100);
        return gnats;
    }
}
