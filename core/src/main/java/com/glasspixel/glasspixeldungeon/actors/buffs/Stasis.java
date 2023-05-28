package com.glasspixel.glasspixeldungeon.actors.buffs;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.sprites.CharSprite;
import com.glasspixel.glasspixeldungeon.ui.BuffIndicator;

public class Stasis extends FlavourBuff{
    private static final float DURATION = 5f;

    {
        actPriority = BUFF_PRIO-3; //low priority, towards the end of a turn
        announced = true;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo(target)) {

            Invisibility.dispel();

            target.paralysed++;

            if (target == Dungeon.hero) {
                target.invisible++;
            }

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.INVISIBLE);
        else target.sprite.remove(CharSprite.State.INVISIBLE);
    }

    @Override
    public int icon() {
        return BuffIndicator.TIME;
    }

    @Override
    public boolean act() {
        super.detach();
        if (target.paralysed > 0)
            target.paralysed--;
        if (target.invisible > 0)
            target.invisible--;
        return true;
    }
}
