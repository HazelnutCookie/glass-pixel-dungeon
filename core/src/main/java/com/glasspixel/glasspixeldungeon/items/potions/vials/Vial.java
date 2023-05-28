package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.Actor;
import com.glasspixel.glasspixeldungeon.actors.Char;
import com.glasspixel.glasspixeldungeon.actors.buffs.Invisibility;
import com.glasspixel.glasspixeldungeon.items.Item;
import com.glasspixel.glasspixeldungeon.items.Recipe;
import com.glasspixel.glasspixeldungeon.items.potions.*;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class Vial extends Potion {

    @Override
    public float drinkTime(){
        return 0.5f;
    }
    //this tag is used for vials that splash on a single enemy, as vanilla potions only ever shatter into an AOE effect
    protected boolean hitsEnemy = false;
    public void anonymize(){
        if (!isKnown()) image = ItemSpriteSheet.VIAL_HOLDER;
        anonymous = true;
    }

    @Override
    protected void onThrow(int cell) {
        Char ch = Actor.findChar(cell);
        if (Dungeon.level.pit[cell]) {
            super.onThrow(cell);
        } else if ((hitsEnemy) && ch != null) {
            singleSplash(ch);
            Invisibility.dispel();
        } else {
            Dungeon.level.pressCell(cell);
            shatter(cell);
        }
    }
    protected void singleSplash(Char ch) {
    };

    public static final HashMap<Class<?extends Vial>, Class<?extends Potion>> exoToReg = new HashMap<>();
    public static final HashMap<Class<?extends Potion>, Class<?extends Vial>> regToExo = new HashMap<>();
    static{
        exoToReg.put(VialOfTriage.class, PotionOfHealing.class);
        regToExo.put(PotionOfHealing.class, VialOfTriage.class);

        regToExo.put(PotionOfToxicGas.class, VialOfAcid.class);
        exoToReg.put(VialOfAcid.class, PotionOfToxicGas.class);

        regToExo.put(PotionOfStrength.class, VialOfSuperhumanMight.class);
        exoToReg.put(VialOfSuperhumanMight.class, PotionOfStrength.class);

        regToExo.put(PotionOfFrost.class, VialOfIcyShards.class);
        exoToReg.put(VialOfIcyShards.class, PotionOfFrost.class);

        regToExo.put(PotionOfHaste.class, VialOfDexterity.class);
        exoToReg.put(VialOfDexterity.class, PotionOfHaste.class);

        regToExo.put(PotionOfLiquidFlame.class, VialOfCombustion.class);
        exoToReg.put(VialOfCombustion.class, PotionOfLiquidFlame.class);

        regToExo.put(PotionOfInvisibility.class, VialOfIllusions.class);
        exoToReg.put(VialOfIllusions.class, PotionOfInvisibility.class);

        regToExo.put(PotionOfMindVision.class, VialOfStrangeVisions.class);
        exoToReg.put(VialOfStrangeVisions.class, PotionOfMindVision.class);

        regToExo.put(PotionOfLevitation.class, VialOfVortexes.class);
        exoToReg.put(VialOfVortexes.class, PotionOfLevitation.class);

        regToExo.put(PotionOfExperience.class, VialOfAmbrosia.class);
        exoToReg.put(VialOfAmbrosia.class, PotionOfExperience.class);

        regToExo.put(PotionOfPurity.class, VialOfStasis.class);
        exoToReg.put(VialOfStasis.class, PotionOfPurity.class);

        regToExo.put(PotionOfParalyticGas.class, VialOfWebbing.class);
        exoToReg.put(VialOfWebbing.class, PotionOfParalyticGas.class);
    }

    @Override
    public boolean isKnown() {
        return anonymous || (handler != null && handler.isKnown( exoToReg.get(this.getClass()) ));
    }

    @Override
    public void setKnown() {
        if (!isKnown()) {
            handler.know(exoToReg.get(this.getClass()));
            updateQuickslot();
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (handler != null && handler.contains(exoToReg.get(this.getClass()))) {
            image = handler.image(exoToReg.get(this.getClass())) + 64;
            color = handler.label(exoToReg.get(this.getClass()));
        }
    }

    public static class PotionToVial extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() == 1 && regToExo.containsKey(ingredients.get(0).getClass())){
                return true;
            }

            return false;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 0;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            for (Item i : ingredients){
                i.quantity(i.quantity()-1);
            }

            return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass())).quantity(2);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass())).quantity(2);
        }
    }

    @Override
    //Half the gold of its none-exotic equivalent
    public int value() {
        return Math.round(Reflection.newInstance(exoToReg.get(getClass())).value()/2) * quantity;
    }

    @Override
    //Half the energy of its none-exotic equivalent
    public int energyVal() {
        return Math.round(Reflection.newInstance(exoToReg.get(getClass())).energyVal()/2) * quantity;
    }
}

