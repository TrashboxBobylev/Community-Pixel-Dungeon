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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WaterStriderSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WaterStrider extends Mob {

	{
		spriteClass = WaterStriderSprite.class;

		loot = AquaBrew.class;
		lootChance = 0.5f;

		HUNTING = new Hunting();
	}

	public WaterStrider() {
		super();

		HP = HT = 5 + Dungeon.scalingDepth() * 5;
		defenseSkill = 5 + Dungeon.scalingDepth();

		EXP = Math.round((Dungeon.scalingDepth() / 5f)*2);
		maxLvl = 4 + Dungeon.scalingDepth();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.scalingDepth() / 2, Dungeon.scalingDepth());
	}

	@Override
	public int attackSkill(Char target) {
		return 10 + Dungeon.scalingDepth();
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, Dungeon.scalingDepth()/3);
	}

	private int webCoolDown = 0;
	private int lastEnemyPos = -1;

	private static final String WEB_COOLDOWN = "web_cooldown";
	private static final String LAST_ENEMY_POS = "last_enemy_pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WEB_COOLDOWN, webCoolDown);
		bundle.put(LAST_ENEMY_POS, lastEnemyPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		webCoolDown = bundle.getInt( WEB_COOLDOWN );
		lastEnemyPos = bundle.getInt( LAST_ENEMY_POS );
	}
	
	@Override
	protected boolean act() {
		webCoolDown--;

		AiState lastState = state;
		boolean result = super.act();

		//We only want to update target position once per turn, so if switched from wandering, wait for a moment
		//Also want to avoid updating when we visually shot a web this turn (don't want to change the position)
		if (!(lastState == WANDERING && state == HUNTING)) {
			if (!shotWebVisually){
				if (enemy != null && enemySeen) {
					lastEnemyPos = enemy.pos;
				} else {
					lastEnemyPos = Dungeon.hero.pos;
				}
			}
			shotWebVisually = false;
		}
		
		return result;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage += enemy.drRoll() / 2;
		damage = super.attackProc( enemy, damage );

		return damage;
	}
	
	private boolean shotWebVisually = false;

	public int webPos(){

		Char enemy = this.enemy;
		if (enemy == null) return -1;

		//don't web a non-moving enemy that we're going to attack
		if (state != FLEEING && enemy.pos == lastEnemyPos && canAttack(enemy)){
			return -1;
		}
		
		Ballistica b = new Ballistica( enemy.pos, pos, Ballistica.WONT_STOP );
		
		int collisionIndex = 0;
		for (int i = 0; i < b.path.size(); i++){
			if (b.path.get(i) == enemy.pos){
				collisionIndex = i;
				break;
			}
		}

		//in case target is at the edge of the map and there are no more cells in the path
		if (b.path.size() <= collisionIndex+1){
			return -1;
		}

		int webPos = b.path.get( collisionIndex+1 );

		//ensure we aren't shooting the web through walls
		int projectilePos = new Ballistica( pos, webPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID).collisionPos;
		
		if (webPos != enemy.pos && projectilePos == webPos && Dungeon.level.passable[webPos]){
			return webPos;
		} else {
			return -1;
		}
		
	}
	
	public void shootWeb(){
		int webPos = webPos();
		if (webPos != -1){
			Ballistica beam = new Ballistica(pos, webPos, Ballistica.PROJECTILE);
			Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);

			for (int pos: beam.subPath(1, beam.dist)){
				Splash.at( DungeonTilemap.tileCenterToWorld( pos ), -PointF.PI/2, PointF.PI/4, 0x5bc1e3, 33, 0.01f);
				Dungeon.level.setCellToWater(true, pos);
				if (fire != null){
					fire.clear(pos);
				}
				Char ch = Actor.findChar(pos);
				//does the equivalent of a bomb's damage against fiery enemies.
				if (Char.hasProp(ch, Char.Property.FIERY)){
					int dmg = Random.NormalIntRange(5 + Dungeon.scalingDepth(), 10 + Dungeon.scalingDepth()*2);
					dmg *= 0.67f;
					if (!ch.isImmune(GeyserTrap.class)){
						ch.damage(dmg, this);
					}
					if (ch.isAlive()) {
						if (ch.buff(Burning.class) != null) {
							ch.buff(Burning.class).detach();
						}
					}
				}
			}
			
			webCoolDown = 15;

			if (Dungeon.level.heroFOV[enemy.pos]){
				Dungeon.hero.interrupt();
			}
		}
		next();
	}

	@Override
	public float speed() {
		return super.speed()*(Dungeon.level.water[pos] ? 2f : 0.5f);
	}

	private class Hunting extends Mob.Hunting {

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV && webCoolDown <= 0 && lastEnemyPos != -1){
				if (webPos() != -1){
					if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
						sprite.zap( webPos() );
						shotWebVisually = true;
						return false;
					} else {
						shootWeb();
						return true;
					}
				}
			}

			return super.act(enemyInFOV, justAlerted);
		}
	}
}
