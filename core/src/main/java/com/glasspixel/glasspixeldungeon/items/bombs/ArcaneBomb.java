/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.glasspixel.glasspixeldungeon.items.bombs;

import com.glasspixel.glasspixeldungeon.Badges;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.blobs.Blob;
import com.glasspixel.glasspixeldungeon.actors.blobs.GooWarn;
import com.glasspixel.glasspixeldungeon.effects.CellEmitter;
import com.glasspixel.glasspixeldungeon.effects.particles.ElmoParticle;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ArcaneBomb extends Bomb.MagicalBomb {
	
	{
		image = ItemSpriteSheet.ARCANE_BOMB;
	}
	
	@Override
	protected void onThrow(int cell) {
		super.onThrow(cell);
		if (fuse != null){
			PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE)
					GameScene.add(Blob.seed(i, 3, GooWarn.class));
			}
		}
	}
	
	@Override
	public boolean explodesDestructively() {
		return false;
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
		
		ArrayList<Char> affected = new ArrayList<>();
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level.heroFOV[i]) {
					CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
				}
				Char ch = Actor.findChar(i);
				if (ch != null){
					affected.add(ch);
				}
			}
		}
		
		for (Char ch : affected){
			// 100%/83%/67% bomb damage based on distance, but pierces armor.
			int damage = Math.round(Random.NormalIntRange( Dungeon.scalingDepth()+5, 10 + Dungeon.scalingDepth() * 2 ));
			float multiplier = 1f - (.16667f*Dungeon.level.distance(cell, ch.pos));
			ch.damage(Math.round(damage*multiplier), this);
			if (ch == Dungeon.hero && !ch.isAlive()){
				Badges.validateDeathFromFriendlyMagic();
				Dungeon.fail(Bomb.class);
			}
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
