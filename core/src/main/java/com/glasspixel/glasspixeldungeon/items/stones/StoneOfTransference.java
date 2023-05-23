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

package com.glasspixel.glasspixeldungeon.items.stones;

import com.glasspixel.glasspixeldungeon.actors.hero.Belongings;
import com.glasspixel.glasspixeldungeon.items.Item;
import com.glasspixel.glasspixeldungeon.items.armor.Armor;
import com.glasspixel.glasspixeldungeon.items.bags.Bag;
import com.glasspixel.glasspixeldungeon.items.rings.Ring;
import com.glasspixel.glasspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.glasspixel.glasspixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.glasspixel.glasspixeldungeon.items.wands.Wand;
import com.glasspixel.glasspixeldungeon.items.weapon.Weapon;
import com.glasspixel.glasspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.glasspixel.glasspixeldungeon.items.weapon.missiles.MissileWeapon;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.scenes.PixelScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.glasspixel.glasspixeldungeon.ui.RedButton;
import com.glasspixel.glasspixeldungeon.ui.RenderedTextBlock;
import com.glasspixel.glasspixeldungeon.ui.Window;
import com.glasspixel.glasspixeldungeon.utils.GLog;
import com.glasspixel.glasspixeldungeon.windows.IconTitle;
import com.glasspixel.glasspixeldungeon.windows.WndBag;

public class StoneOfTransference extends InventoryStone {

	{
		preferredBag = Belongings.Backpack.class;
		image = ItemSpriteSheet.STONE_AUGMENTATION;
	}
	String firstType;
	Item firstItem;
	protected String itemType(Item item) {

		if (item instanceof Armor) {
			return "Armor";
		} else if (item instanceof Wand) {
			return "Wand";
		} else if (item instanceof MeleeWeapon) {
			return "MeleeWeapon";
		} else if (item instanceof MissileWeapon) {
			return "MissileWeapon";
		} else if (item instanceof Ring) {
			return "Ring";
		}

		return null;
	}

	private String reuseTitle(){
		return Messages.get(this, "reuse_title");
	}

	@Override
	public boolean usableOnItem(Item item) {
		return ((item.isUpgradable()) && (item.trueLevel()) >= 2) && (item.isIdentified());
	}

	protected WndBag.ItemSelector reuseSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return reuseTitle();
		}

		@Override
		public Class<? extends Bag> preferredBag() {

			return preferredBag;

		}

		@Override
		public boolean itemSelectable(Item item) {

			return (itemType(item) == firstType) && ((item.isUpgradable())) && (item.isIdentified());

		}

		@Override
		public void onSelect(Item item) {
			if (item != null) {
				firstItem.level--;
				item.upgrade();}
			else {
				curItem.collect( curUser.belongings.backpack );
			}
		}
	};

	@Override
	protected void onItemSelected(final Item item) {
		firstType = itemType(item);
		firstItem = item;
		{
			GameScene.selectItem( reuseSelector );
		}
	}

	@Override
	public int value() {
		return 30 * quantity;
	}

	@Override
	public int energyVal() {
		return 4 * quantity;
	}
}
