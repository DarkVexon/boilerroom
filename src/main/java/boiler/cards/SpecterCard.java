package boiler.cards;

import basemod.helpers.CardModifierManager;
import boiler.cardmods.IsSpecterModifier;
import boiler.util.Wiz;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static boiler.BoilerRoomMod.makeID;
import static boiler.util.Wiz.atb;
import static boiler.util.Wiz.att;

public class SpecterCard extends AbstractEasyCard implements StartupCard {
    public static String ID = makeID(SpecterCard.class.getSimpleName());

    public SpecterCard() {
        super(ID, 1, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE, CardColor.CURSE);
        exhaust = true;
    }

    private static String scream() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            return "VO_NEMESIS_2A";
        } else {
            return "VO_NEMESIS_2B";
        }
    }

    private static boolean canDisguiseAs(AbstractCard target) {
        return target.cost != -2 && !target.cardID.equals(SpecterCard.ID) && !CardModifierManager.hasModifier(target, IsSpecterModifier.ID);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upp() {
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        atb(new SFXAction(scream()));
    }

    @Override
    public boolean atBattleStartPreDraw() {
        att(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                List<AbstractCard> possibilities = AbstractDungeon.player.drawPile.group.stream().filter(SpecterCard::canDisguiseAs).collect(Collectors.toList());
                if (!possibilities.isEmpty() && AbstractDungeon.player.drawPile.contains(SpecterCard.this)) {
                    int index = AbstractDungeon.player.drawPile.group.indexOf(SpecterCard.this);
                    AbstractDungeon.player.drawPile.removeCard(SpecterCard.this);
                    AbstractCard disguise = Wiz.getRandomItem(possibilities, AbstractDungeon.cardRandomRng).makeStatEquivalentCopy();
                    CardModifierManager.addModifier(disguise, new IsSpecterModifier(SpecterCard.this));
                    if (index > 0) {
                        AbstractDungeon.player.drawPile.group.add(index, disguise);
                    } else {
                        AbstractDungeon.player.drawPile.addToRandomSpot(disguise);
                    }
                    HashMap<AbstractCard, AbstractCard> toFrom = new HashMap<>();
                    toFrom.put(SpecterCard.this.makeStatEquivalentCopy(), disguise.makeStatEquivalentCopy());
                }
            }
        });
        return false;
    }
}
