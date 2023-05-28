package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Amok;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.MeldShadows;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class VialOfIllusions extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_ILLUSIONS;
        hitsEnemy = true;
    }

    @Override
    public void singleSplash(Char ch) {
        if (Dungeon.level.heroFOV[ch.pos] && (ch.pos != Dungeon.hero.pos)) {
            identify();
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);}
        if (ch == Dungeon.hero) {}
        else {
            Buff.affect(ch, Amok.class, 5);
        }}


    @Override
    public void apply(Hero hero) {
        identify();
        Buff.affect(Dungeon.hero, MeldShadows.class);
        GLog.i( Messages.get(this, "invisible") );
        Sample.INSTANCE.play( Assets.Sounds.MELD );
    }
}
