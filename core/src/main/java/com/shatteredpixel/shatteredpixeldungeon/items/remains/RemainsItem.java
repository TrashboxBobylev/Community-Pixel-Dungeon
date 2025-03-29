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

package com.shatteredpixel.shatteredpixeldungeon.items.remains;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;

import java.util.ArrayList;

public abstract class RemainsItem extends Item {

	{
		bones = false;

		defaultAction = AC_USE;
	}

	public static final String AC_USE =  "USE";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_USE)){
			hero.sprite.operate(hero.pos);

			Catalog.countUse(getClass());
			doEffect(hero);

			hero.spendAndNext(Actor.TICK);
			detach(hero.belongings.backpack);
		}
	}

	protected abstract void doEffect(Hero hero);

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public int value() {
		return 50;
	}

	public static RemainsItem get(HeroClass cls){
		switch (cls){
			case WARRIOR: default:
				return new SealShard();
			case MAGE:
				return new BrokenStaff();
			case ROGUE:
				return new CloakScrap();
			case HUNTRESS:
				return new BowFragment();
			case DUELIST:
				return new BrokenHilt();
			case CLERIC:
				return new TornPage();
		}
	}

	public static class SealRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{SealShard.class};
			inQuantity = new int[]{1};

			cost = 15;

			output = BrokenSeal.class;
			outQuantity = 1;
		}

	}

	public static class StaffRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{BrokenStaff.class};
			inQuantity = new int[]{1};

			cost = 25;

			output = MagesStaff.class;
			outQuantity = 1;
		}

	}

	public static class CloakRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{CloakScrap.class};
			inQuantity = new int[]{1};

			cost = 45;

			output = CloakOfShadows.class;
			outQuantity = 1;
		}

	}

	public static class BowRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{BowFragment.class};
			inQuantity = new int[]{1};

			cost = 45;

			output = SpiritBow.class;
			outQuantity = 1;
		}

	}

	public static class TomeRecipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{TornPage.class};
			inQuantity = new int[]{1};

			cost = 25;

			output = HolyTome.class;
			outQuantity = 1;
		}

	}

}
