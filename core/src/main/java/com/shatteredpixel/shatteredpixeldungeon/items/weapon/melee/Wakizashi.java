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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Wakizashi extends MeleeWeapon {

    {
        image = ItemSpriteSheet.WAKIZASHI;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.3f;

        tier = 4;
        DLY = 1/3f; //3x speed
        ACC = 1.32f; //+32% boost to accuracy
    }

    @Override
    public int min(int lvl) {
        return 1 +  //1 base, down from 4
                lvl; //same level scaling
    }

    @Override
    public int max(int lvl) {
        return  Math.round(1.25f*(tier+1)) +     //6 base, down from 25
                lvl*Math.round(0.4f*(tier+1));  //+2 per level, down from +5
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        damage += (int) Math.ceil(defender.drRoll()/3f);
        LethalCombo lethalCombo;
        if ((lethalCombo = attacker.buff(LethalCombo.class)) != null){
            damage += lethalCombo.hits;
            lethalCombo.hits++;
            Sample.INSTANCE.play(hitSound, 1, (float) (Random.Float(0.87f, 1.15f) * Math.pow(1.1f, lethalCombo.hits) * hitSoundPitch));
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        //1 turn less as using the ability is instant
        LethalCombo lethalCombo;
        int hits = 0;
        if ((lethalCombo = hero.buff(LethalCombo.class)) != null){
            hits = lethalCombo.hits/2;
        }
        Buff.affect(hero, LethalCombo.class, 2+buffedLvl()/2f).hits = hits;
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", Messages.decimalFormat("#.#", 3+buffedLvl()/2f));
        } else {
            return Messages.get(this, "typical_ability_desc", 3);
        }
    }

    public static class LethalCombo extends FlavourBuff {

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        public int hits;

        @Override
        public int icon() {
            return BuffIndicator.DUEL_LETHAL;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (3 - visualcooldown()) / 3);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(), hits);
        }

        private static final String HIT_COUNT = "hit_count";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(HIT_COUNT, hits);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            hits = bundle.getInt(HIT_COUNT);
        }
    }

}
