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
import com.glasspixel.glasspixeldungeon.actors.buffs.Invisibility;
import com.glasspixel.glasspixeldungeon.actors.hero.Belongings;
import com.glasspixel.glasspixeldungeon.actors.hero.Hero;
import com.glasspixel.glasspixeldungeon.items.armor.Armor;
import com.glasspixel.glasspixeldungeon.items.bags.Bag;
import com.glasspixel.glasspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.glasspixel.glasspixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.glasspixel.glasspixeldungeon.items.stones.InventoryStone;
import com.glasspixel.glasspixeldungeon.items.weapon.Weapon;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.scenes.PixelScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.ui.RedButton;
import com.glasspixel.glasspixeldungeon.ui.RenderedTextBlock;
import com.glasspixel.glasspixeldungeon.ui.Window;
import com.glasspixel.glasspixeldungeon.windows.IconTitle;
import com.glasspixel.glasspixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TinkerKit extends Item {
	
	{
		image = ItemSpriteSheet.TINKER_KIT;
	}

	public static final String AC_USE	= "USE";

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private String inventoryTitle(){
		return Messages.get(this, "inv_title");
	}

	protected void activate(int cell) {
		GameScene.selectItem( itemSelector );
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_USE );
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_USE)){
			curItem = detach( hero.belongings.backpack );
			activate(curUser.pos);
		}
	}


	protected boolean usableOnItem(Item item) {
		return ScrollOfEnchantment.enchantable(item);
	}


	protected void onItemSelected(Item item) {
		
		GameScene.show(new WndAugment( item));
		
	}

	protected void useAnimation() {
		curUser.spend( 1f );
		curUser.busy();
		curUser.sprite.operate(curUser.pos);

		Sample.INSTANCE.play( Assets.Sounds.READ );
		Invisibility.dispel();
	}
	
	public void apply( Weapon weapon, Weapon.Augment augment ) {
		
		weapon.augment = augment;
		useAnimation();
		ScrollOfUpgrade.upgrade(curUser);
		
	}
	
	public void apply( Armor armor, Armor.Augment augment ) {
		
		armor.augment = augment;
		useAnimation();
		ScrollOfUpgrade.upgrade(curUser);
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}

	@Override
	public int energyVal() {
		return 4 * quantity;
	}

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return inventoryTitle();
		}

		@Override
		public boolean itemSelectable(Item item) {
			return usableOnItem(item);
		}

		@Override
		public void onSelect( Item item ) {

			//it would be better to eliminate the curItem static variable.
			if (!(curItem instanceof TinkerKit)){
				return;
			}

			if (item != null) {

				((TinkerKit)curItem).onItemSelected( item );

			} else{
				curItem.collect( curUser.belongings.backpack );
			}
		}
	};
	
	public class WndAugment extends Window {
		
		private static final int WIDTH			= 120;
		private static final int MARGIN 		= 2;
		private static final int BUTTON_WIDTH	= WIDTH - MARGIN * 2;
		private static final int BUTTON_HEIGHT	= 20;
		
		public WndAugment( final Item toAugment ) {
			super();
			
			IconTitle titlebar = new IconTitle( toAugment );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock tfMesage = PixelScene.renderTextBlock( Messages.get(this, "choice"), 8 );
			tfMesage.maxWidth(WIDTH - MARGIN * 2);
			tfMesage.setPos(MARGIN, titlebar.bottom() + MARGIN);
			add( tfMesage );
			
			float pos = tfMesage.top() + tfMesage.height();
			
			if (toAugment instanceof Weapon){
				for (final Weapon.Augment aug : Weapon.Augment.values()){
					if (((Weapon) toAugment).augment != aug){
						RedButton btnSpeed = new RedButton( Messages.get(this, aug.name()) ) {
							@Override
							protected void onClick() {
								hide();
								TinkerKit.this.apply( (Weapon)toAugment, aug );
							}
						};
						btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
						add( btnSpeed );
						
						pos = btnSpeed.bottom();
					}
				}
				
			} else if (toAugment instanceof Armor){
				for (final Armor.Augment aug : Armor.Augment.values()){
					if (((Armor) toAugment).augment != aug){
						RedButton btnSpeed = new RedButton( Messages.get(this, aug.name()) ) {
							@Override
							protected void onClick() {
								hide();
								TinkerKit.this.apply( (Armor) toAugment, aug );
							}
						};
						btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
						add( btnSpeed );
						
						pos = btnSpeed.bottom();
					}
				}
			}
			
			RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
				@Override
				protected void onClick() {
					hide();
					TinkerKit.this.collect();
				}
			};
			btnCancel.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
			add( btnCancel );
			
			resize( WIDTH, (int)btnCancel.bottom() + MARGIN );
		}
		
		@Override
		public void onBackPressed() {
			TinkerKit.this.collect();
			super.onBackPressed();
		}
	}
}
