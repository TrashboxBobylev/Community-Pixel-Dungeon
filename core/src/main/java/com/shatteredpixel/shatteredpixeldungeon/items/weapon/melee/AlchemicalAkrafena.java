/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * Community Pixel Dungeon
 * Copyright (C) 2024-2024 Trashbox Bobylev and Pixel Dungeon's community
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
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColoredWound;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlchemicalAkrafena extends MeleeWeapon {

    private static final String AC_INFUSE = "INFUSE";

    {
        image = ItemSpriteSheet.ALCHEMICAL_SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.25f;

        //mostly to be able to see what dart is in akrafena right now
        defaultAction = AC_INFUSE;

        tier = 4;
    }

    public Class effectSeed;
    public int effectDuration = 0;

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //20 base, down from 25
                lvl*(tier);   //+4 per level, down from +5
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_INFUSE );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_INFUSE)) {

            curUser = hero;
            GameScene.selectItem( itemSelector );

        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (effectSeed != null && effectDuration > 0){
            TippedDart effect = TippedDart.getTipped((Plant.Seed) Reflection.newInstance(effectSeed), 100);
            damage = effect.proc(attacker, defender, damage);
            if (--effectDuration <= 0){
                effectSeed = null;
                effectDuration = 0;
                GLog.w( Messages.get(AlchemicalAkrafena.class, "duration_out"));
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public int effectDuration(){
        int duration = 4 + buffedLvl() / 2;
        if (effectSeed == Rotberry.Seed.class)
            duration /= 3;
        return duration;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        if (effectSeed != null){
            return glowings.get(effectSeed);
        }
        return super.glowing();
    }

    @Override
    public String statsInfo() {
        String info = super.statsInfo();
        if (effectSeed != null){
            TippedDart effect = TippedDart.getTipped((Plant.Seed) Reflection.newInstance(effectSeed), 100);
            info = Messages.get(AlchemicalAkrafena.class, "current_effect", effect.name(), effect.desc()) +
                "\n" + Messages.get(AlchemicalAkrafena.class, "duration_left", effectDuration, effectDuration());
        }
        return info;
    }

    private static final String EFFECT_SEED = "effect_seed";
    private static final String EFFECT_DURATION = "effect_duration";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(EFFECT_SEED, effectSeed);
        bundle.put(EFFECT_DURATION, effectDuration);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        effectSeed = bundle.getClass(EFFECT_SEED);
        effectDuration = bundle.getInt(EFFECT_DURATION);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return super.baseChargeUse(hero, target)
                *(effectSeed == Rotberry.Seed.class ? 4 : 2);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (effectSeed == null){
            GLog.w(Messages.get(this, "ability_no_imbue"));
            return;
        }

        beforeAbilityUsed(hero, null);
        hero.busy();
        Invisibility.dispel();
        hero.sprite.operate(hero.pos, () -> {
            hero.sprite.idle();
            hero.sprite.zap(hero.pos);
            PathFinder.buildDistanceMap( hero.pos, BArray.not( Dungeon.level.solid, null ), 2 );
            Sample.INSTANCE.play(hitSound, 1.5f, hitSoundPitch / 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE && i != hero.pos) {
                    Char ch = Char.findChar(i);
                    if (ch != null && ch.alignment == Char.Alignment.ENEMY){
                        ColoredWound.hit(ch, SandalsOfNature.seedColors.get(effectSeed));
                        TippedDart effect = TippedDart.getTipped((Plant.Seed) Reflection.newInstance(effectSeed), 100);
                        ch.damage(Math.round(damageRoll(hero) * (0.4f + 0.075f * buffedLvl()) + effect.proc(hero, ch, effect.damageRoll(hero))), hero);
                        if (!ch.isAlive()){
                            onAbilityKill(hero, ch);
                        }
                    } else {
                        ColoredWound.hit(i, SandalsOfNature.seedColors.get(effectSeed));
                    }
                }
            }
            hero.spendAndNext(hero.attackDelay());
            effectSeed = null;
            effectDuration = 0;
            afterAbilityUsed(hero);
        });
    }

    @Override
    public String abilityInfo() {
        float dmgModifier = levelKnown ? 0.4f + 0.075f * buffedLvl() : 0.4f;
        if (levelKnown){
            return Messages.get(this, "ability_desc", Math.round(augment.damageFactor(min())*dmgModifier), Math.round(augment.damageFactor(max())*dmgModifier));
        } else {
            return Messages.get(this, "typical_ability_desc", Math.round(min(0)*dmgModifier), Math.round(max(0)*dmgModifier));
        }
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(AlchemicalAkrafena.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return PotionBandolier.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Potion && !(item instanceof ExoticPotion);
        }

        @Override
        public void onSelect(Item item) {
            if (item instanceof Potion){
                if (!((Potion) item).isKnown()){
                    GLog.w( Messages.get(AlchemicalAkrafena.class, "identify"));
                    return;
                }

                for (Map.Entry<Class<?extends Plant.Seed>, Class<?extends Potion>> entry : Potion.SeedToPotion.types.entrySet()) {
                    if (Objects.equals(item.getClass(), entry.getValue())) {
                        effectSeed = entry.getKey();
                        break;
                    }
                }

                effectDuration = effectDuration();

                item.detach(curUser.belongings.backpack);

                curUser.sprite.operate( curUser.pos );
                Sample.INSTANCE.play( Assets.Sounds.DRINK );
                Enchanting.show(curUser, AlchemicalAkrafena.this);
                GLog.i( Messages.get(AlchemicalAkrafena.class, "absorb_potion", item.name()) );
                updateQuickslot();
                curUser.busy();
                curUser.spend( Actor.TICK );
            }
        }
    };

    protected static final HashMap<Class<? extends Plant.Seed>, ItemSprite.Glowing> glowings = new HashMap<>();

    static {
        for (Class<? extends Plant.Seed> seedClass: SandalsOfNature.seedColors.keySet()){
            glowings.put(seedClass, new ItemSprite.Glowing(SandalsOfNature.seedColors.get(seedClass)));
        }
    }

}
