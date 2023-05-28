package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Dexterity;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;

public class VialOfDexterity extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_DEXTERITY;
    }
    @Override
    public void apply(Hero hero) {
        identify();
        Buff.affect(Dungeon.hero, Dexterity.class, Dexterity.DURATION);
    }
}
