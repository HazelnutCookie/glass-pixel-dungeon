package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Challenges;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Healing;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.effects.CellEmitter;
import com.glasspixel.glasspixeldungeon.effects.Speck;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import static com.glasspixel.glasspixeldungeon.items.potions.PotionOfHealing.pharmacophobiaProc;


public class VialOfTriage extends Vial {

    {
        hitsEnemy = true;
        defaultAction = AC_THROW;
        icon = ItemSpriteSheet.Icons.VIAL_TRIAGE;
    }

    public void triage(Char ch, float percent) {
        if (ch == Dungeon.hero && Dungeon.isChallenged(Challenges.NO_HEALING)) {
            pharmacophobiaProc(Dungeon.hero);
        } else {
            //heals just under a third of the hero's hp
            Buff.affect(ch, Healing.class).setHeal((int) (percent * ch.HT), 1f, 0);
            if (ch == Dungeon.hero) {
                GLog.p(Messages.get(VialOfTriage.class, "heal"));
            }

            //if thrown on a tile, make separate particles and play the shatter sfx
            if (Dungeon.level.heroFOV[ch.pos] && (ch.pos != Dungeon.hero.pos)) {
                identify();
                CellEmitter.center(ch.pos).start(Speck.factory(Speck.HEALING), 0.3f, 3);
                Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            }
        }
    }

    @Override
    public void apply(Hero hero) {
        identify();
        triage(hero, 0.3f);
    }

    @Override
    protected void singleSplash(Char ch) {
        triage(ch, 1f);
    }

}
