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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DMXXXSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DMXXX extends Mob implements Callback {

	private static final float TIME_TO_ZAP	= 3f;
	
	{
		spriteClass = DMXXXSprite.class;
		
		HP = HT = 1;
		defenseSkill = 8;
		baseSpeed = 2f;
		
		loot = ReclaimTrap.class;
		lootChance = 0.1f;

		properties.add(Property.INORGANIC);
		properties.add(Property.LARGE);
	}

	@Override
	public float attackDelay() {
		return super.attackDelay()*3f;
	}

	public DMXXX(){
		super();

		EXP = Math.round((Dungeon.scalingDepth() / 5f)*2);
		maxLvl = 4 + Dungeon.scalingDepth();
	}

	private int abilityCoolDown = 0;

	private static final String ABILITY_COOLDOWN = "ability_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ABILITY_COOLDOWN, abilityCoolDown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		abilityCoolDown = bundle.getInt(ABILITY_COOLDOWN);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 8 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 4);
	}

	@Override
	public boolean canAttack( Char enemy ) {
		return super.canAttack(enemy)
				|| new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public void notice() {
		super.notice();
		if (abilityCoolDown <= 0) {
			for (int i = 0; i < fieldOfView.length; i++) {
				if (fieldOfView[i]) {
					Trap trap = Dungeon.level.traps.get(i);
					if (trap != null) {
						Dungeon.level.map[i] = Terrain.TRAP;
						Dungeon.level.setTrap(trap, i);
						Dungeon.level.discover(i);
						GameScene.discoverTile(i, Terrain.INACTIVE_TRAP);
						ScrollOfMagicMapping.discover(i);
						Sample.INSTANCE.play( Assets.Sounds.SECRET, 1.0f, 1.2f );
					}
				}
			}
			abilityCoolDown = 5;
		}
	}

	@Override
	protected boolean act() {
		abilityCoolDown--;
		return super.act();
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return true;
	}

	//used so resistances can differentiate between melee and magical attacks
	public static class LightningBolt{}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		spend( TIME_TO_ZAP );

		Invisibility.dispel(this);

		ArrayList<Trap> possibleTraps = new ArrayList<>();

		for (Point point: ((RegularLevel) Dungeon.level).room(pos).getPoints()) {
			int pos = Dungeon.level.pointToCell(point);
			Trap trap = Dungeon.level.traps.get(pos);
			if (trap != null && trap.active &&
					!(trap instanceof DisintegrationTrap || trap instanceof GrimTrap || trap instanceof GatewayTrap || trap instanceof RockfallTrap)) {
				possibleTraps.add(trap);
			}
		}
		if (possibleTraps.isEmpty()){
			die(null);
			return true;
		} else {
			// guide towards closer traps
			Trap selectedTrap = null;
			ArrayList<Trap> randomSampleList = new ArrayList<>();
			for (int i = 0; i < 5; i++)
				randomSampleList.add(Random.element(possibleTraps));
			for (Trap randomTrap: randomSampleList)
				if (selectedTrap == null ||
						Dungeon.level.distance(randomTrap.pos, enemy.pos) < Dungeon.level.distance(selectedTrap.pos, enemy.pos))
					selectedTrap = randomTrap;

			if (selectedTrap != null)
				selectedTrap.trigger();

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap(enemy.pos);
				return false;
			} else {
				return true;
			}
		}
	}
	
	@Override
	public void call() {
		next();
	}
	
}
