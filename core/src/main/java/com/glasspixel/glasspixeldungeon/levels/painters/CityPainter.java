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

package com.glasspixel.glasspixeldungeon.levels.painters;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.levels.Level;
import com.glasspixel.glasspixeldungeon.levels.Terrain;
import com.glasspixel.glasspixeldungeon.levels.rooms.Room;
import com.glasspixel.glasspixeldungeon.tiles.DungeonTileSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CityPainter extends RegularPainter {
	
	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {
		
		int[] map = level.map;
		int w = level.width();
		int l = level.length();
		
		for (int i=0; i < l - w; i++) {
			
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
				
			} else if (map[i] == Terrain.WALL
					&& !DungeonTileSheet.wallStitcheable(map[i + w])
					&& Random.Int( 21 - Dungeon.depth ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
	}
}
