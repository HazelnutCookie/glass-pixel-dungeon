package com.glasspixel.glasspixeldungeon.actors.buffs;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.effects.Splash;
import com.glasspixel.glasspixeldungeon.ui.BuffIndicator;
import com.glasspixel.glasspixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Shards extends FlavourBuff {
    public static final float DURATION	= 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.FROST;
    }

    @Override
    public void detach() {
        iceexplode(target.pos);
        super.detach();

    }

    public void iceexplode(int pos) {
        Sample.INSTANCE.play( Assets.Sounds.SHATTER );
        PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Splash.at( i, 0x00ffff, 2);
                Char ch = Actor.findChar(i);

                if (ch != null){
                    int dmg = Random.NormalIntRange(3 + Dungeon.scalingDepth(), 5 + Dungeon.scalingDepth()*2);
                    ;
                    ch.damage(dmg, this);
                }
            }
        }
    }
}
