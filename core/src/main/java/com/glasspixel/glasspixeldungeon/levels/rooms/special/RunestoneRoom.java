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

package com.glasspixel.glasspixeldungeon.levels.rooms.special;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.items.Generator;
import com.glasspixel.glasspixeldungeon.items.Item;
import com.glasspixel.glasspixeldungeon.items.keys.IronKey;
import com.glasspixel.glasspixeldungeon.items.stones.Runestone;
import com.glasspixel.glasspixeldungeon.levels.Level;
import com.glasspixel.glasspixeldungeon.levels.Terrain;
import com.glasspixel.glasspixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class RunestoneRoom extends SpecialRoom {
	
	@Override
	public int minWidth() { return 6; }
	
	@Override
	public int minHeight() {
		return 6;
	}
	
	@Override
	public void paint( Level level) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.CHASM );
		
		Painter.drawInside( level, this, entrance(), 2, Terrain.EMPTY_SP);
		Painter.fill( level, this, 2, Terrain.EMPTY );
		
		int n = Random.NormalIntRange(2, 3);
		int dropPos;
		for (int i = 0; i < n; i++) {
			do {
				dropPos = level.pointToCell(random());
			} while (level.map[dropPos] != Terrain.EMPTY || level.heaps.get( dropPos ) != null);
			level.drop(prize(level), dropPos);
		}
		
		entrance().set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );
	}
	
	private static Item prize( Level level ) {
		
		Item prize = level.findPrizeItem( Runestone.class );
		if (prize == null)
			prize = Generator.random( Generator.Category.STONE );
		
		return prize;
	}
}
