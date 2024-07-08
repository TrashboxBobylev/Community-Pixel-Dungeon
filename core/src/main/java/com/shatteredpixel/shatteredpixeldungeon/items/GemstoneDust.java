/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Community Pixel Dungeon
 * Copyright (C) 2024 Trashbox Bobylev and Pixel Dungeon's community
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class GemstoneDust extends Item {

    {
        image = ItemSpriteSheet.GEMSTONE_DUST;

        stackable = true;

        defaultAction = AC_APPLY;

        bones = true;
    }

    private static final String AC_APPLY = "APPLY";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_APPLY );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_APPLY)) {

            curUser = hero;
            GameScene.selectItem( itemSelector );

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30*quantity();
    }

    public static void apply(Item boostItem, int usedBoost, Ring boostedRing){
        if (usedBoost < boostItem.quantity()){
            boostItem.quantity(boostItem.quantity()-usedBoost);
        } else {
            boostItem.detachAll(Dungeon.hero.belongings.backpack);
        }

        boostedRing.dustBonus++;
        Item.updateQuickslot();

        curUser.sprite.operate(curUser.pos);
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );

        curUser.spendAndNext(Actor.TICK);
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(GemstoneDust.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return (item instanceof Wand || item instanceof Ring) && item.isIdentified();
        }

        @Override
        public void onSelect( Item item ) {
            if (item instanceof Ring) {
                Ring r = (Ring)item;

                if (r.level() >= 2){
                    GLog.w(Messages.get(GemstoneDust.class, "level_too_high_ring"));
                    return;
                }

                int dustToUse = r.level()+1;

                if (quantity() < dustToUse){
                    GLog.w(Messages.get(GemstoneDust.class, "not_enough"));

                } else {

                    GemstoneDust.apply(GemstoneDust.this, dustToUse, r);
                    GLog.p(Messages.get(GemstoneDust.class, "apply_ring"));
                }
            } else if (item instanceof Wand) {
                Wand w = (Wand)item;

                if (w.level() >= 3){
                    GLog.w(Messages.get(GemstoneDust.class, "level_too_high_wand"));
                    return;
                }

                int resinToUse = w.level()+1+1;

                if (quantity() < resinToUse){
                    GLog.w(Messages.get(GemstoneDust.class, "not_enough"));

                } else {

                    ArcaneResin.apply(GemstoneDust.this, resinToUse, w);
                    GLog.p(Messages.get(GemstoneDust.class, "apply_wand"));
                }
            }
        }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1
                    && ingredients.get(0) instanceof Ring
                    && ingredients.get(0).isIdentified()
                    && !ingredients.get(0).cursed;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 5;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = sampleOutput(ingredients);

            ingredients.get(0).quantity(0);

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            Ring w = (Ring)ingredients.get(0);
            int level = w.level() - w.dustBonus;

            Item output = new GemstoneDust().quantity(2*(level+1));

            return output;
        }
    }

}
