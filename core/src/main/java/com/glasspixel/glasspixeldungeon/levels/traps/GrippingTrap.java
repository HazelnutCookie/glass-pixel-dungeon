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

package com.glasspixel.glasspixeldungeon.levels.traps;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Bleeding;
import com.glasspixel.glasspixeldungeon.actors.buffs.Buff;
import com.glasspixel.glasspixeldungeon.actors.buffs.Cripple;
import com.glasspixel.glasspixeldungeon.effects.Wound;

public class GrippingTrap extends Trap {

	{
		color = GREY;
		shape = DOTS;

		disarmedByActivation = false;
		avoidsHallways = true;
	}

	@Override
	public void activate() {

		Char c = Actor.findChar( pos );

		if (c != null && !c.flying) {
			int damage = Math.max( 0,  (2 + Dungeon.depth/2) - c.drRoll()/2 );
			Buff.affect( c, Bleeding.class ).set( damage );
			Buff.prolong( c, Cripple.class, Cripple.DURATION);
			Wound.hit( c );
		} else {
			Wound.hit( pos );
		}

	}
}
