package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Vertigo;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.actors.mobs.Mob;
import com.glasspixel.glasspixeldungeon.effects.Splash;
import com.glasspixel.glasspixeldungeon.items.artifacts.TalismanOfForesight;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.Collections;

public class VialOfStrangeVisions extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_VISIONS;
    }
    @Override
    public void shatter(int cell) {

        if (Dungeon.level.heroFOV[cell]) {
            splash(cell);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if (!Dungeon.level.solid[cell+offset]) {
                Splash.at(cell+offset, 0xff00ff, 1);
                Char ch = Actor.findChar(cell+offset);

                if (ch != null) {
                    Buff.affect(ch, Vertigo.class, 3);
                }
            }
        }
    }

    @Override
    public void apply(Hero hero) {
        identify();
        ArrayList<Mob> guys = new ArrayList<>();
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            guys.add(mob);
        }
        Collections.sort(guys, (Mob m1, Mob m2) -> m2.HP-m1.HP);
        for (int i=0; (i<4 || i==guys.size()); i++) {
            Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, 3).charID = guys.get(i).id();
        }
        GLog.p(Messages.get(this, "msg"));
    }
}
