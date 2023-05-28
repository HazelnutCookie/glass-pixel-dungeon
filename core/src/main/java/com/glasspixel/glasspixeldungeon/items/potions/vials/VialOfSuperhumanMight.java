package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.ui.BuffIndicator;
import com.glasspixel.glasspixeldungeon.utils.GLog;

public class VialOfSuperhumanMight extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_SUPERMIGHT;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        Buff.affect(hero, StrBoost.class).reset();
        GLog.p(Messages.get(this, "msg"));
    }

    public static class StrBoost extends Buff {
        {
            type = buffType.POSITIVE;
        }

        private int left;

        public void reset() {
            left = 1;
        }

        @Override
        public int icon() {
            return BuffIndicator.SUPER_MIGHT;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", left);
        }

        public void onLevelUp() {
            left--;
            if (left <= 0) {
                detach();
            }
        }
    }
}
