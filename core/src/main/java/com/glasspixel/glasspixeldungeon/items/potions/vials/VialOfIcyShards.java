package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Shards;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;

public class VialOfIcyShards extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_CHILLINGAIR;
        hitsEnemy = true;
        defaultAction = AC_THROW;
    }

    @Override
    public void apply(Hero hero) {
        identify();
        if (hero.buff(Shards.class) == null) {
            Buff.prolong(hero, Shards.class, Shards.DURATION);
        }

    }

    @Override
    public void singleSplash(Char ch) {
        if (Dungeon.level.heroFOV[ch.pos]) {
            identify();
        }
        if (ch.buff(Shards.class) == null) {
            Buff.prolong(ch, Shards.class, Shards.DURATION);
        }

    }
}
