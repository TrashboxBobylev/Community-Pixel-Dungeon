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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WindSpiritSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class WindSpirit extends Mob{

	{
		spriteClass = WindSpiritSprite.class;

		flying = true;

		viewDistance = 5;

		loot = StoneOfBlink.class;
		lootChance = 0.5f;

		properties.add(Property.INORGANIC);
	}

	public WindSpirit(){
		super();

		HP = HT = 5 + Dungeon.scalingDepth();
		defenseSkill = 10 + Dungeon.scalingDepth()*2;

		EXP = Math.round((Dungeon.scalingDepth() / 5f)*2);
		maxLvl = 4 + Dungeon.scalingDepth();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 0, Dungeon.scalingDepth()/2 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 5 + Dungeon.scalingDepth()/2;
	}

	@Override
	public boolean canAttack( Char enemy ) {
		return buff(Silencing.Effect.class) == null && (super.canAttack(enemy)
				|| new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos);
	}

	protected boolean doAttack(Char enemy ) {

		if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
			sprite.zap( enemy.pos );
			return false;
		} else {
			zap();
			return true;
		}
	}

	//used so resistances can differentiate between melee and magical attacks
	public static class LightBeam {}

	private void zap() {
		spend( 1f );

		Invisibility.dispel(this);
		Char enemy = this.enemy;
		if (hit( this, enemy, true )) {

			int dmg = Random.NormalIntRange( 0, Dungeon.scalingDepth()/2 );
			enemy.damage( dmg, new LightBeam() );
			// generic knockback code
			Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
			trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
			WandOfBlastWave.throwChar(enemy,
					trajectory,
					1,
					false,
					true,
					this);

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Badges.validateDeathFromEnemyMagic();
				Dungeon.fail( this );
				GLog.n( Messages.get(this, "beam_kill") );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}
}
