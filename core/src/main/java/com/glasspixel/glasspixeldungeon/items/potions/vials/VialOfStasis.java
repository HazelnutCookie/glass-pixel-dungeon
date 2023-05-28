package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Stasis;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class VialOfStasis extends Vial {
    {
        hitsEnemy = true;
        icon = ItemSpriteSheet.Icons.VIAL_STASIS;
    }

    public static void stasis(Char ch) {
            Buff.affect(ch, Stasis.class, 5);
            if (ch == Dungeon.hero) {
                GLog.p(Messages.get(VialOfStasis.class, "stasis"));
            }
            //if thrown on a char, play the shatter sfx
            if (Dungeon.level.heroFOV[ch.pos] && (ch.pos != Dungeon.hero.pos)) {
                Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            }
        }

    @Override
    public void apply(Hero hero) {
        identify();
        stasis(hero);
    }

    @Override
    protected void singleSplash(Char ch) {
        identify();
        stasis(ch);
    }
}
