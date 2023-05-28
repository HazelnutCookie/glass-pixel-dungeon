package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Levitation;
import com.glasspixel.glasspixeldungeon.effects.Pushing;
import com.glasspixel.glasspixeldungeon.effects.Splash;
import com.glasspixel.glasspixeldungeon.effects.Tornado;
import com.glasspixel.glasspixeldungeon.mechanics.Ballistica;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.BArray;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class VialOfVortexes extends Vial {
    {
        icon = ItemSpriteSheet.Icons.VIAL_VORTEX;
        defaultAction = AC_THROW;
    }

    private static final int DISTANCE = 4;
    protected int target = -1;

    @Override
    public void shatter(int cell) {
        if (Dungeon.level.heroFOV[cell]) {
            identify();
            splash(cell);
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Splash.at(cell, 0xffffff, 4 );
            Game.scene().addToFront(new Tornado(cell, 0xffffff));
        }
        ArrayList<Char> affected = new ArrayList<>();
        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), DISTANCE);
        for (int i = 0; i < Dungeon.level.length(); i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }
        Collections.shuffle(Arrays.asList(affected));
        for (Char ch : affected) {
            suck(ch.pos, cell, ch);
        }
    }

    private void suck(int target, int pos, Char character) {
        if (!character.properties().contains(Char.Property.IMMOVABLE)) {
            {
                Ballistica vortex = new Ballistica(pos, target, (Ballistica.STOP_TARGET | Ballistica.STOP_SOLID));
                if (vortex.collisionPos != character.pos) {}
                else {
                    Buff.affect(character, Levitation.class, 1);
                    int newPos = -1;
                    for (int i : vortex.subPath(1, vortex.dist)) {
                        if (!Dungeon.level.solid[i] && Actor.findChar(i) == null) {
                            newPos = i;
                            break;
                        }
                    }
                    if (newPos != -1) {
                        final int newPosFinal = newPos;
                        this.target = newPos;

                        if (character.sprite.visible) {
                            Actor.addDelayed(new Pushing(character, character.pos, newPosFinal), -1);
                            pullIn(character, newPosFinal);

                        }
                        else {
                            pullIn(character, newPosFinal);
                        }
                    }
                }


            }
        }
    }

    private void pullIn(Char character, int pullPos) {
        character.pos = pullPos;
        character.sprite.place(pullPos);
        Dungeon.level.occupyCell(character);
        if (character == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }
}
