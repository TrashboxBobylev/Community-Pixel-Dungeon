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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Feature;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfAccuracy extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_ACCURACY;
		buffClass = Accuracy.class;
	}
	
	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, soloBuffedBonus()) - 1f)));
			if (Feature.ACCURACY_BUFF.enabled){
				info += Messages.get(this, "sneak_stats", Messages.decimalFormat("#.##", 100f * (Math.pow(1.1f, soloBuffedBonus()) - 1f)));
			}
			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
				if (Feature.ACCURACY_BUFF.enabled){
					info += Messages.get(this, "combined_sneak_stats", Messages.decimalFormat("#.##", 100f * (Math.pow(1.1f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
				}
			}
			return info;
		} else {
			String info = Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 30f));
			if (Feature.ACCURACY_BUFF.enabled){
				info += Messages.get(this, "typical_sneak_stats", Messages.decimalFormat("#.##", 10f));
			}
			return info;
		}
	}

	public String upgradeStat1(int level){
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, level+1)-1f)) + "%";
	}

	public String upgradeStat2(int level){
		if (!Feature.ACCURACY_BUFF.enabled)
			return null;
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Messages.decimalFormat("#.##", 100f * (Math.pow(1.1f, level+1)-1f)) + "%";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Accuracy();
	}
	
	public static float accuracyMultiplier( Char target ){
		return (float)Math.pow(1.3f, getBuffedBonus(target, Accuracy.class));
	}

	public static float sneakAttackMultiplier( Char target ){
		if (!Feature.ACCURACY_BUFF.enabled)
			return 1.0f;
		return (float)Math.pow(1.1f, getBuffedBonus(target, Accuracy.class));
	}
	
	public class Accuracy extends RingBuff {
	}
}
