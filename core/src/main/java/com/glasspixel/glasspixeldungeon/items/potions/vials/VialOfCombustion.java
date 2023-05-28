package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Combusting;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class VialOfCombustion extends Vial {

    {
        icon = ItemSpriteSheet.Icons.VIAL_COMBUSTION;
        hitsEnemy = true;
        defaultAction = AC_THROW;
    }

    public void ignite(Char ch) {
            if (Dungeon.level.heroFOV[ch.pos]) {
            identify();}

            Buff.affect(ch, Combusting.class).reignite( ch, 12f);

            //if thrown on an enemy, play the shatter sfx
            if (Dungeon.level.heroFOV[ch.pos] && (ch.pos != Dungeon.hero.pos)) {
                Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            }
        }

    @Override
    public void apply(Hero hero) {
        ignite(hero);
    }

    @Override
    protected void singleSplash(Char ch) {
        ignite(ch);
    }

}

