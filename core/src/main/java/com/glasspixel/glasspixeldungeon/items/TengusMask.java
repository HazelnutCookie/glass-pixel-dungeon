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

package com.glasspixel.glasspixeldungeon.items;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Badges;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.actors.hero.HeroSubClass;
import com.glasspixel.glasspixeldungeon.actors.hero.Talent;
import com.glasspixel.glasspixeldungeon.effects.Speck;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.glasspixel.glasspixeldungeon.windows.WndChooseSubclass;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class TengusMask extends Item {
	
	private static final String AC_WEAR	= "WEAR";
	
	{
		stackable = false;
		image = ItemSpriteSheet.MASK;

		defaultAction = AC_WEAR;

		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_WEAR );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_WEAR )) {
			
			curUser = hero;

			GameScene.show( new WndChooseSubclass( this, hero ) );
			
		}
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		Badges.validateMastery();
		return super.doPickUp( hero, pos );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	public void choose( HeroSubClass way ) {
		
		detach( curUser.belongings.backpack );
		
		curUser.spend( Actor.TICK );
		curUser.busy();
		
		curUser.subClass = way;
		Talent.initSubclassTalents(curUser);
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.Sounds.MASTERY );
		
		Emitter e = curUser.sprite.centerEmitter();
		e.pos(e.x-2, e.y-6, 4, 4);
		e.start(Speck.factory(Speck.MASK), 0.05f, 20);
		GLog.p( Messages.get(this, "used"));
		
	}
}
