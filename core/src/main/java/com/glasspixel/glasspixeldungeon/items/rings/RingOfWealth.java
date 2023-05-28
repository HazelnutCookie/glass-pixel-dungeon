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

package com.glasspixel.glasspixeldungeon.items.rings;

import com.glasspixel.glasspixeldungeon.Challenges;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.effects.Flare;
import com.glasspixel.glasspixeldungeon.items.Generator;
import com.glasspixel.glasspixeldungeon.items.Gold;
import com.glasspixel.glasspixeldungeon.items.Honeypot;
import com.glasspixel.glasspixeldungeon.items.Item;
import com.glasspixel.glasspixeldungeon.items.armor.Armor;
import com.glasspixel.glasspixeldungeon.items.bombs.Bomb;
import com.glasspixel.glasspixeldungeon.items.potions.AlchemicalCatalyst;
import com.glasspixel.glasspixeldungeon.items.potions.PotionOfExperience;
import com.glasspixel.glasspixeldungeon.items.potions.exotic.ExoticPotion;
import com.glasspixel.glasspixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.glasspixel.glasspixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.glasspixel.glasspixeldungeon.items.spells.ArcaneCatalyst;
import com.glasspixel.glasspixeldungeon.items.stones.StoneOfEnchantment;
import com.glasspixel.glasspixeldungeon.items.weapon.Weapon;
import com.glasspixel.glasspixeldungeon.messages.Messages;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class RingOfWealth extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_WEALTH;
	}

	private float triesToDrop = Float.MIN_VALUE;
	private int dropsToRare = Integer.MIN_VALUE;
	
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", Messages.decimalFormat("#.##", 100f * (Math.pow(1.20f, soloBuffedBonus()) - 1f)));
		} else {
			return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 20f));
		}
	}

	private static final String TRIES_TO_DROP = "tries_to_drop";
	private static final String DROPS_TO_RARE = "drops_to_rare";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TRIES_TO_DROP, triesToDrop);
		bundle.put(DROPS_TO_RARE, dropsToRare);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		triesToDrop = bundle.getFloat(TRIES_TO_DROP);
		dropsToRare = bundle.getInt(DROPS_TO_RARE);
	}

	@Override
	protected RingBuff buff( ) {
		return new Wealth();
	}
	
	public static float dropChanceMultiplier( Char target ){
		return (float)Math.pow(1.20, getBuffedBonus(target, Wealth.class));
	}
	
	public static ArrayList<Item> tryForBonusDrop(Char target, int tries ){
		int bonus = getBuffedBonus(target, Wealth.class);

		if (bonus <= 0) return null;
		
		HashSet<Wealth> buffs = target.buffs(Wealth.class);
		float triesToDrop = Float.MIN_VALUE;
		int dropsToEquip = Integer.MIN_VALUE;
		
		//find the largest count (if they aren't synced yet)
		for (Wealth w : buffs){
			if (w.triesToDrop() > triesToDrop){
				triesToDrop = w.triesToDrop();
				dropsToEquip = w.dropsToRare();
			}
		}

		//reset (if needed), decrement, and store counts
		if (triesToDrop == Float.MIN_VALUE) {
			triesToDrop = Random.NormalIntRange(0, 20);
			dropsToEquip = Random.NormalIntRange(5, 10);
		}

		//now handle reward logic
		ArrayList<Item> drops = new ArrayList<>();

		triesToDrop -= tries;
		while ( triesToDrop <= 0 ){
			if (dropsToEquip <= 0){
				int equipBonus = 0;

				//A second ring of wealth can be at most +1 when calculating wealth bonus for equips
				//This is to prevent using an upgraded wealth to farm another upgraded wealth and
				//using the two to get substantially more upgrade value than intended
				for (Wealth w : target.buffs(Wealth.class)){
					if (w.buffedLvl() > equipBonus){
						equipBonus = w.buffedLvl() + Math.min(equipBonus, 2);
					} else {
						equipBonus += Math.min(w.buffedLvl(), 2);
					}
				}

				Item i;
				do {
					i = genEquipmentDrop(equipBonus - 1);
				} while (Challenges.isItemBlocked(i));
				drops.add(i);
				dropsToEquip = Random.NormalIntRange(5, 10);
			} else {
				Item i;
				do {
					i = genConsumableDrop(bonus - 1);
				} while (Challenges.isItemBlocked(i));
				drops.add(i);
				dropsToEquip--;
			}
			triesToDrop += Random.NormalIntRange(0, 20);
		}

		//store values back into rings
		for (Wealth w : buffs){
			w.triesToDrop(triesToDrop);
			w.dropsToRare(dropsToEquip);
		}
		
		return drops;
	}

	//used for visuals
	// 1/2/3 used for low/mid/high tier consumables
	// 3 used for +0-1 equips, 4 used for +2 or higher equips
	private static int latestDropTier = 0;

	public static void showFlareForBonusDrop( Visual vis ){
		if (vis == null || vis.parent == null) return;
		switch (latestDropTier){
			default:
				break; //do nothing
			case 1:
				new Flare(6, 20).color(0x00FF00, true).show(vis, 3f);
				break;
			case 2:
				new Flare(6, 24).color(0x00AAFF, true).show(vis, 3.33f);
				break;
			case 3:
				new Flare(6, 28).color(0xAA00FF, true).show(vis, 3.67f);
				break;
			case 4:
				new Flare(6, 32).color(0xFFAA00, true).show(vis, 4f);
				break;
		}
		latestDropTier = 0;
	}
	
	public static Item genConsumableDrop(int level) {
		float roll = Random.Float();
		//60% chance - 4% per level. Starting from +15: 0%
		if (roll < (0.6f - 0.04f * level)) {
			latestDropTier = 1;
			return genLowValueConsumable();
		//30% chance + 2% per level. Starting from +15: 60%-2%*(lvl-15)
		} else if (roll < (0.9f - 0.02f * level)) {
			latestDropTier = 2;
			return genMidValueConsumable();
		//10% chance + 2% per level. Starting from +15: 40%+2%*(lvl-15)
		} else {
			latestDropTier = 3;
			return genHighValueConsumable();
		}
	}

	private static Item genLowValueConsumable(){
		switch (Random.Int(4)){
			case 0: default:
				Item i = new Gold().random();
				return i.quantity(i.quantity()/2);
			case 1:
				return Generator.randomUsingDefaults(Generator.Category.STONE);
			case 2:
				return Generator.randomUsingDefaults(Generator.Category.POTION);
			case 3:
				return Generator.randomUsingDefaults(Generator.Category.SCROLL);
		}
	}

	private static Item genMidValueConsumable(){
		switch (Random.Int(6)){
			case 0: default:
				Item i = genLowValueConsumable();
				return i.quantity(i.quantity()*2);
			case 1:
				i = Generator.randomUsingDefaults(Generator.Category.POTION);
				return Reflection.newInstance(ExoticPotion.regToExo.get(i.getClass()));
			case 2:
				i = Generator.randomUsingDefaults(Generator.Category.SCROLL);
				return Reflection.newInstance(ExoticScroll.regToExo.get(i.getClass()));
			case 3:
				return Random.Int(2) == 0 ? new ArcaneCatalyst() : new AlchemicalCatalyst();
			case 4:
				return new Bomb();
			case 5:
				return new Honeypot();
		}
	}

	private static Item genHighValueConsumable(){
		switch (Random.Int(4)){
			case 0: default:
				Item i = genMidValueConsumable();
				if (i instanceof Bomb){
					return new Bomb.DoubleBomb();
				} else {
					return i.quantity(i.quantity()*2);
				}
			case 1:
				return new StoneOfEnchantment();
			case 2:
				return new PotionOfExperience();
			case 3:
				return new ScrollOfTransmutation();
		}
	}

	private static Item genEquipmentDrop( int level ){
		Item result;
		//each upgrade increases depth used for calculating drops by 1
		int floorset = (Dungeon.depth + level)/5;
		switch (Random.Int(5)){
			default: case 0: case 1:
				Weapon w = Generator.randomWeapon(floorset);
				if (!w.hasGoodEnchant() && Random.Int(10) < level)      w.enchant();
				else if (w.hasCurseEnchant())                           w.enchant(null);
				result = w;
				break;
			case 2:
				Armor a = Generator.randomArmor(floorset);
				if (!a.hasGoodGlyph() && Random.Int(10) < level)        a.inscribe();
				else if (a.hasCurseGlyph())                             a.inscribe(null);
				result = a;
				break;
			case 3:
				result = Generator.random(Generator.Category.RING);
				break;
			case 4:
				result = Generator.random(Generator.Category.ARTIFACT);
				break;
		}
		//minimum level is 1/2/3/4/5/6 when ring level is 1/3/5/7/9/11
		if (result.isUpgradable()){
			int minLevel = (level+1)/2;
			if (result.level() < minLevel){
				result.level(minLevel);
			}
		}
		result.cursed = false;
		result.cursedKnown = true;
		if (result.level() >= 2) {
			latestDropTier = 4;
		} else {
			latestDropTier = 3;
		}
		return result;
	}

	public class Wealth extends RingBuff {
		
		private void triesToDrop( float val ){
			triesToDrop = val;
		}
		
		private float triesToDrop(){
			return triesToDrop;
		}

		private void dropsToRare( int val ) {
			dropsToRare = val;
		}

		private int dropsToRare(){
			return dropsToRare;
		}
		
	}
}