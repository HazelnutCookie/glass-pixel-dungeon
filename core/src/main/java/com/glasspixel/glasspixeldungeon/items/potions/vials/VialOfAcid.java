package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Cripple;
import com.glasspixel.glasspixeldungeon.effects.Splash;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VialOfAcid extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_ACID;
    }

    @Override
    public void shatter(int cell) {

        if (Dungeon.level.heroFOV[cell]) {
            splash(cell);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if (!Dungeon.level.solid[cell+offset]) {
                Splash.at(cell+offset, 0x00ff00, 1);
                Char ch = Actor.findChar(cell+offset);

                if (ch != null) {
                    Buff.affect(ch, Cripple.class, 2);
                    int damage = Random.NormalIntRange(2 + Dungeon.scalingDepth(), 6 + Dungeon.scalingDepth());
                    ch.damage(damage, this);
                }
            }
        }
    }
}
