/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Community Pixel Dungeon
 * Copyright (C) 2024-2025 Trashbox Bobylev and Pixel Dungeon's community
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AngryThornSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Random;

public class AngryThorn extends Mob {

    {
        spriteClass = AngryThornSprite.class;

        baseSpeed = 1.5f;

        loot = Generator.Category.SEED;
        lootChance = 1f;

        // so they don't furrow stuff
        flying = true;
    }

    public AngryThorn(){
        super();

        HP = HT = 10 + Dungeon.scalingDepth() * 5;
        defenseSkill = Dungeon.scalingDepth();

        EXP = Math.round((Dungeon.scalingDepth() / 5f)*2);
        maxLvl = 4 + Dungeon.scalingDepth();
    }

    public static boolean isGrass(int pos){
        return Dungeon.level.map[pos] == Terrain.GRASS || Dungeon.level.map[pos] == Terrain.HIGH_GRASS || Dungeon.level.map[pos] == Terrain.FURROWED_GRASS;
    }

    @Override
    protected void onAdd() {
        if (firstAdded && !isGrass(pos)){
            do {
                pos = Dungeon.level.randomRespawnCell(this);
            } while (pos == -1 || !isGrass(pos));
        }
        super.onAdd();
    }

    @Override
    public boolean[] modifyPassable(boolean[] passable) {
        for (int i = 0; i < Dungeon.level.length(); i++){
            passable[i] = isGrass(i);
        }
        return passable;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (src instanceof Burning) {
            dmg += HT / 3;
        }

        super.damage(dmg, src);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Bleeding b = Buff.affect(enemy, Bleeding.class);
        b.announced = false;
        b.set(Math.round(damage*.6f));
        enemy.sprite.showStatus(CharSprite.WARNING, Messages.titleCase(b.name()) + " " + (int)b.level());
        return super.attackProc(enemy, 0);
    }

    @Override
    public int distance(Char other) {
        return super.distance(other)/2;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(Dungeon.scalingDepth() / 2, Dungeon.scalingDepth() * 3 / 2);
    }

    @Override
    public int attackSkill( Char target ) {
        return 10 + Dungeon.scalingDepth()*2;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, Dungeon.scalingDepth());
    }
}
