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

package com.glasspixel.glasspixeldungeon;

import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.scenes.PixelScene;
import com.glasspixel.glasspixeldungeon.scenes.TitleScene;
import com.glasspixel.glasspixeldungeon.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PlatformSupport;

public class GlassPixelDungeon extends Game {

	public static final int v0_1_0 = 1;
	
	public GlassPixelDungeon(PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//pre-v1.3.0
		com.watabou.utils.Bundle.addAlias(
				com.glasspixel.glasspixeldungeon.actors.buffs.Bleeding.class,
				"com.shatteredpixel.glasspixeldungeon.levels.features.Chasm$FallBleed" );
		com.watabou.utils.Bundle.addAlias(
				com.glasspixel.glasspixeldungeon.plants.Mageroyal.class,
				"com.shatteredpixel.glasspixeldungeon.plants.Dreamfoil" );
		com.watabou.utils.Bundle.addAlias(
				com.glasspixel.glasspixeldungeon.plants.Mageroyal.Seed.class,
				"com.shatteredpixel.glasspixeldungeon.plants.Dreamfoil$Seed" );

		com.watabou.utils.Bundle.addAlias(
				com.glasspixel.glasspixeldungeon.items.weapon.curses.Dazzling.class,
				"com.shatteredpixel.glasspixeldungeon.items.weapon.curses.Exhausting" );
		com.watabou.utils.Bundle.addAlias(
				com.glasspixel.glasspixeldungeon.items.weapon.curses.Explosive.class,
				"com.shatteredpixel.glasspixeldungeon.items.weapon.curses.Fragile" );
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		SPDAction.loadBindings();
		
		Music.INSTANCE.enable( SPDSettings.music() );
		Music.INSTANCE.volume( SPDSettings.musicVol()*SPDSettings.musicVol()/100f );
		Sample.INSTANCE.enable( SPDSettings.soundFx() );
		Sample.INSTANCE.volume( SPDSettings.SFXVol()*SPDSettings.SFXVol()/100f );

		Sample.INSTANCE.load( Assets.Sounds.all );
		
	}

	@Override
	public void finish() {
		if (!DeviceCompat.isiOS()) {
			super.finish();
		} else {
			//can't exit on iOS (Apple guidelines), so just go to title screen
			switchScene(TitleScene.class);
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}
	
	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}
	
	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}