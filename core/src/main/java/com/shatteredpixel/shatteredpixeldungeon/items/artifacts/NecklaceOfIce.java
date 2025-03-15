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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class NecklaceOfIce extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACE_NECKLACE;

        levelCap = 10;

        charge = 0;
        partialCharge = 0;
        chargeCap = 100;

        defaultAction = AC_ACTIVATE;
    }

    public int exp = 0;

    public static final String AC_ACTIVATE = "ACTIVATE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero )
                && !cursed
                && hero.buff(MagicImmune.class) == null
                && charge == 100) {
            actions.add(AC_ACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_ACTIVATE)){
            if (!isEquipped( hero ))        GLog.i( Messages.get(Artifact.class, "need_to_equip") );
            else if (charge < 100)          GLog.i( Messages.get(this, "no_charge") );
            else if (cursed)                GLog.i( Messages.get(this, "cursed") );
            else {
                charge -= 100;

                GLog.p( Messages.get(this, "using") );

                hero.sprite.centerEmitter().burst(Speck.factory( Speck.BLIZZARD, true ), 4);
                hero.busy();
                Sample.INSTANCE.play(Assets.Sounds.BURNING, 1f, 2f);

                PathFinder.buildDistanceMap( hero.pos, BArray.not( Dungeon.level.solid, null ), 2 );

                hero.sprite.operate(hero.pos, () -> {
                    Sample.INSTANCE.play( Assets.Sounds.SHATTER, 2f, 1.5f );

                    for (int i = 0; i < PathFinder.distance.length; i++) {
                        if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                            GameScene.add(Blob.seed(i, 4, Freezing.class));
                            CellEmitter.get(i).burst(Speck.factory(Speck.BLIZZDUST, false), 8);

                            Char ch = Actor.findChar(i);
                            if (ch != null && ch.alignment == Char.Alignment.ENEMY){
                                ch.sprite.centerEmitter().burst(SnowParticle.FACTORY, 10);
                                ch.damage(Random.NormalIntRange(0, 10) + level()*5, new Chill());
                                Buff.affect(ch, Chill.class, 4f);
                            }
                        }
                    }

                    hero.sprite.idle();
                    hero.spend(1f);
                    hero.next();
                });
            }

        }
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;

        if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null){
            partialCharge += 8*amount;
            while (partialCharge >= 1){
                partialCharge--;
                charge++;
            }
            if (charge >= chargeCap){
                partialCharge = 0;
            }
            updateQuickslot();
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new necklaceRecharge();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( Dungeon.hero )){
            if (!cursed) {
                desc += "\n\n" + Messages.get(this, "desc_worn", level()*5, 5 + level()*5);
            } else
                desc += "\n\n" + Messages.get(this, "desc_cursed");
        }
        return desc;
    }

    private static final String EXP = "exp";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(EXP, exp);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        exp = bundle.getInt(EXP);
    }

    public class necklaceRecharge extends ArtifactBuff {
        @Override
        public boolean act() {

            if (charge < chargeCap
                    && !cursed
                    && target.buff(MagicImmune.class) == null
                    && Regeneration.regenOn()) {
                //300 turns to charge at full
                float chargeGain = 1 / 3f;
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;

                while (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                        GLog.p( Messages.get(NecklaceOfIce.class, "charged") );
                    }
                }
            } else if (cursed && Random.Int(20) == 0)
                Buff.affect(target, Frost.class, 2f);

            updateQuickslot();

            spend( TICK );

            return true;
        }

        public void gainExp(int amount){
            if (cursed || target.buff(MagicImmune.class) != null || amount == 0) return;

            exp += amount;

            if (exp >= 50 + 25*level() && level() < levelCap){
                upgrade();
                exp = 0;
                GLog.p( Messages.get(NecklaceOfIce.class, "levelup") );
            }
        }

        @Override
        public HashSet<Class> immunities() {
            HashSet<Class> set = super.immunities();
            if (!cursed)
                set.add(Frost.class);
            return set;
        }
    }
}
