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

package com.glasspixel.glasspixeldungeon.levels.rooms.standard;

import com.glasspixel.glasspixeldungeon.levels.Level;
import com.glasspixel.glasspixeldungeon.levels.Terrain;
import com.glasspixel.glasspixeldungeon.levels.painters.Painter;
import com.glasspixel.glasspixeldungeon.levels.rooms.Room;
import com.watabou.utils.Rect;

public class ChasmRoom extends PatchRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 2, 1};
	}

	@Override
	public void merge(Level l, Room other, Rect merge, int mergeTerrain) {
		if (mergeTerrain == Terrain.EMPTY
				&& (other instanceof ChasmRoom || other instanceof PlatformRoom)){
			super.merge(l, other, merge, Terrain.CHASM);
			Painter.set(l, connected.get(other), Terrain.EMPTY);
		} else {
			super.merge(l, other, merge, mergeTerrain);
		}
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		//fill scales from ~30% at 4x4, to ~60% at 18x18
		// normal   ~30% to ~40%
		// large    ~40% to ~50%
		// giant    ~50% to ~60%
		float fill = 0.30f + (width()*height())/1024f;

		setupPatch(level, fill, 1, true);
		cleanDiagonalEdges();

		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				if (patch[xyToPatchCoords(j, i)]) {
					int cell = i * level.width() + j;
					level.map[cell] = Terrain.CHASM;
				}
			}
		}
	}

}