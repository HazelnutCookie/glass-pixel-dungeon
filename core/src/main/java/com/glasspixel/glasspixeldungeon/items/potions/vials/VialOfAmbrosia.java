package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.actors.buffs.Bless;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.effects.Flare;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;

public class VialOfAmbrosia extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_AMBROSIA;
    }
    @Override
    public void apply(Hero hero) {
        identify();
        Buff.prolong(hero, Bless.class, Bless.DURATION);
        new Flare( 6, 32 ).color(0xFFFF00, true).show( hero.sprite, 2f );
    }
}
