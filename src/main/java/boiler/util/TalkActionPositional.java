package boiler.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class TalkActionPositional extends AbstractGameAction {
    private String msg;
    private boolean used;
    private float bubbleDuration;
    private float x;
    private float y;

    public TalkActionPositional(float x, float y, String text, float duration, float bubbleDuration) {
        this.used = false;// 11
        this.duration = duration;// 20

        this.msg = text;// 23
        this.actionType = ActionType.TEXT;// 24
        this.bubbleDuration = bubbleDuration;// 25

        this.x = x;
        this.y = y;
    }// 27

    public void update() {
        if (!this.used) {// 40
            AbstractDungeon.effectList.add(new SpeechBubble(this.x, this.y, this.bubbleDuration, this.msg, false));// 50
            this.used = true;// 5
        }
        this.tickDuration();// 61
    }// 62
}
