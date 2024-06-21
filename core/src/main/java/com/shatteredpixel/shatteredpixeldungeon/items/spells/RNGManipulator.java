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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class RNGManipulator extends Spell {

    {
        image = ItemSpriteSheet.RNG_MANIPULATOR;

        talentChance = 1/(float) Recipe.OUT_QUANTITY;
    }

    @Override
    protected void onCast(Hero hero) {
        GameScene.show(new WndOptions(
                new ItemSprite(image),
                Messages.titleCase(name()),
                Messages.get(RNGManipulator.class, "window_desc"),
                Messages.get(RNGManipulator.class, "window_gold", Dungeon.gold / 2),
                Messages.get(RNGManipulator.class, "window_hp", (Dungeon.hero.HT * 3 / 4)),
                Messages.get(RNGManipulator.class, "window_cancel")
        ){
            @Override
            protected void onSelect(int index) {
                if (index == 0){
                    Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1.5f, 0.6f);

                    GameScene.shake(1f, 1f);

                    int goldAmount = Dungeon.gold * 3 / 4;

                    hero.sprite.showStatusWithIcon( CharSprite.NEUTRAL, Integer.toString(-Dungeon.gold / 2), FloatingText.GOLD );

                    Dungeon.gold -= Dungeon.gold / 2;

                    GLog.h(Messages.get(RNGManipulator.class, "gold_luck", goldAmount));

                    Buff.affect(Dungeon.hero, LuckBoost.class).init(goldAmount, Dungeon.depth+1);
                    BuffIndicator.refreshHero();

                    detach(Dungeon.hero.belongings.backpack);
                } else if (index == 1){
                    Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1.5f, 0.6f);

                    GameScene.shake(1f, 1f);

                    Dungeon.hero.damage(Dungeon.hero.HT / 2, new GrimTrap());

                    int goldAmount = (Dungeon.hero.HT * 3 / 4)*65;

                    GLog.h(Messages.get(RNGManipulator.class, "health_luck", goldAmount));

                    Buff.affect(Dungeon.hero, LuckBoost.class).init(goldAmount, Dungeon.depth+1);
                    BuffIndicator.refreshHero();

                    detach(Dungeon.hero.belongings.backpack);
                }
            }

            @Override
            protected boolean enabled(int index) {
                if (Dungeon.hero.HP <= Dungeon.hero.HT * 3 / 4 && index == 1)
                    return false;
                return super.enabled(index);
            }
        });
    }

    @Override
    public int value() {
        return (int)(60 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int)(12 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    public static class LuckBoost extends Buff {
        public int luckBoost;
        public int luckDepth;

        @Override
        public int icon() {
            return BuffIndicator.LIGHT;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFFD400);
        }

        public void init(int boost, int depth){
            luckBoost = boost;
            luckDepth = depth;
        }

        @Override
        public String desc() {
            if (Dungeon.depth != luckDepth)
                return Messages.get(this, "desc_nowork");
            else
                return Messages.get(this, "desc", luckBoost);
        }

        private static final String BOOST = "luckBoost";
        private static final String DEPTH = "luckDepth";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BOOST, luckBoost);
            bundle.put(DEPTH, luckDepth);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            luckBoost = bundle.getInt(BOOST);
            luckDepth = bundle.getInt(DEPTH);
        }

        @Override
        public float iconFadePercent() {
            if (Dungeon.depth != luckDepth)
                return 1;
            else
                return 0;
        }

        @Override
        public String iconTextDisplay() {
            return String.valueOf(luckDepth);
        }

        public static int luckBoost(){
            if (Dungeon.hero == null)
                return 0;

            LuckBoost boost = Dungeon.hero.buff(LuckBoost.class);

            if (boost != null && boost.luckDepth == Dungeon.depth )
                return boost.luckBoost;
            else {
                return 0;
            }
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 1;

        {
            inputs =  new Class[]{UnstableSpell.class};
            inQuantity = new int[]{1};

            cost = 18;

            output = RNGManipulator.class;
            outQuantity = OUT_QUANTITY;
        }

    }

}
