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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.GemstoneDust;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.RepairedRapier;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.CrossBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.FrostfireBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.LeanyElixir;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Fury;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.RNGManipulator;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Silencing;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.BrokenTreasureBox;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AlchemicalAkrafena;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Wakizashi;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DefenderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WaterStriderSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public enum Feature {

    EXPLORATION_ICON("Indicator for full cleared floors", "Mintzi/@.mint17",
            "_-_ Added a small icon, that indicates the current dungeon floor being fully explored (for purposes of rankings' exploration score).", Category.TWEAKS){
        @Override public Image icon() {return Icons.get(Icons.MAGNIFY);}
    },
    NEW_RUNIC_TRANSFERENCE("Hold Fast/Runic Transference tier-swap", "Raynuva/@raynuva",
            "_-_ _Hold Fast_ talent has been moved from Tier 3 to Tier 2, with reducing its max blocking from 2/4/6 to 2/5.\n\n" +
                    "_-_ _Runic Transference_ talent has been moved from Tier 2 to Tier 3, with new effect for _+3_!\n\n" +
                    "_-_ On _+3_, Runic Transference makes armor and broken seal independently store glyphs (with making seal be possible to inscribe with glyph) and _have them activate at the same time_!", Category.MECHANICS){
        @Override public Image icon() {return new TalentIcon(Talent.RUNIC_TRANSFERENCE);}
    },
    INTUITION_NOTES("Stone of Intuition notes", "Sentient Orange Pile/@sentientorangepile",
            "_-_ Wrongly guessing item's type with _Stone of Intuition_ makes the guess _be saved as a note_ in item's description.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.STONE_INTUITION);}
    },
    ACCURACY_BUFF("Ring of Accuracy buff", "ImanUserIus/@imanuserius",
            "_-_ _Ring of Accuracy_ provides _surprise attack damage boost_, in addition to accuracy boost.\n\n" +
                    "_-_ Each upgrade adds _1.1x_ to surprise attack damage.", Category.MECHANICS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.RING_RUBY);}
    },
    SILENCING_SPELL("Silencing spell", "emphysima gaming/@miutsuifa",
            "_-_ Added _Silencing_ spell, which disables the magical abilities of targeted enemy for several turns.\n\n" +
                    "_-_ Silenced enemies cannot do magic-based attacks or use magical abilities.\n\n" +
                    "_-_ The spell can be made from Scroll of Anti-Magic.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.SILENCE);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{Silencing.class};
        }
    },
    ALCHEMICAL_SWORD("Alchemical Akrafena", "Serpens/@serpens2137",
            "_-_ Added _Alchemical Akrafena_, the special T4 weapon, which can be _imbued with potions_ for on-hit effects.\n\n" +
                    "_-_ The imbue lasts several hits and will apply effect of a dart, that corresponds to potion's seed, which was imbued.\n\n" +
                    "_-_ Duelist's ability will attack enemies in 5x5 area around the player with current imbue, but costs 2 charges and uses up the weapon's imbue.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.ALCHEMICAL_SWORD);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{AlchemicalAkrafena.class};
        }
    },
    RAT_KING_CURSE("Rat King's tripping curse", "watabou's uncle Dialga/@engio",
            "_-_ Awakening Rat King from his sleep will _apply a curse_ on player.\n\n" +
                    "_-_ With curse active, there is 1/2222 chance on every step for player to trip and drop their weapon.\n\n" +
                    "_-_ This effect cannot be countered with ankhs and invulnerability effects.", Category.OTHER_CONTENT){
        @Override public Image icon() {return new RatKingSprite();}
    },
    WARDING_REFUNDING("Wand of Warding's sentry refunding", "Sir Ayin/@uchufoxgd",
            "_-_ Removing wards and sentries by interacting with them also _refunds some of the charge spent_ on deploying that sentry.\n\n" +
                    "_-_ 0,5/1/1,5 charges is refunded for wards and 1/1,5/2 charges is refunded for sentries.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.WAND_WARDING);}
    },
    UPGRADE_LOG_ON_IDENTIFY("Revealing item's upgrades on identifying", "Yams/@xrider107",
            "_-_ Identifying equipment by any means states its upgrade level and enchantments in game log's identify message.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.SCROLL_KAUNAN);}
    },
    VERTIGO_OVERHAUL("Missing attacks while being vertigoed", "MarioDied64/@merio64",
            "_-_ Being inflicted with _Vertigo_ effect causes player to frequently miss the target of throwing or wand zaps.\n\n" +
                    "_-_ Enemies, that are affected with _Vertigo_, will randomly attack the wrong target nearby instead of player, regardless of attacking in melee or at range.", Category.MECHANICS){
        @Override public Image icon() {return new BuffIcon(BuffIndicator.VERTIGO, true);}
    },
    TENGU_BOMB_CHECK("Checking floor in range of Tengu's bomb", "inverse-snake/@inverse.snake",
            "_-_ Checking empty floor, that is in range of Tengu's smoke bomb, will show, if this floor will be affected by smoke bomb's explosion.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.TENGU_BOMB);}
    },
    FIRE_FROST_REACTION("Fire and frost reactions", "Nat/@nat9542",
            "_-_ Inflicting Burning on enemies with Chilling and vice versa _will damage the enemy_ equivalent to _75% of bomb's damage_.\n\n" +
                    "_-_ This interaction can be triggered with potions and elemental wands.", Category.MECHANICS){
        @Override public Image icon() {return new BuffIcon(BuffIndicator.FIRE, true);}
    },
    ENCHANTING_RESULTS("Enchanting results", "Cilian/@cilian.",
            "_-_ _Arcane Stylus_ and _Stone of Enchantment_ state the enchantment/glyph they applied in their game log's message.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.STONE_ENCHANT);}
    },
    BOSS_BAGS("Inventory bags from bosses", "ifritdiezel/@ifritdiezel",
            "_-_ Instead of being bought at the shops, storage bags will drop from the bosses.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.BANDOLIER);}
    },
    RNG_MANIPULATOR("Luck Manipulator spell", "NeoSlav/@neoslav",
            "_-_ Added _Luck Manipulator_ spell, which allows to get luck boost for next floor _by either sacrificing money or health_.\n\n" +
                    "_-_ Player can sacrifice 50% of their current gold or 75% of their total HP.\n\n" +
                    "_-_ The amount of luck boost _will depend on amount of sacrificed resources_ and will be shown in game log after spell's usage.\n\n" +
                    "_-_ The luck boost effect affects the amount and quality of puzzle room's drops and random enemy drops.\n\n" +
                    "_-_ The spell can be made with Unstable Spell.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.RNG_MANIPULATOR);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{RNGManipulator.class};
        }
    },
    DAILY_CHALLENGES("Daily challenges", "vexxjacobs/@vexxjacobs",
            "_-_ The player can select to _get some challenges_, when doing the daily run.\n\n" +
                    "_-_ The resulting challenges will depend on the seed generated for that daily.", Category.TWEAKS){
        @Override public Image icon() {return Icons.get(Icons.CALENDAR);}
    },
    BROKEN_TREASURE_CHEST("Broken Treasure Box trinket", "goteryup/@goteryup",
            "_-_ Added _Broken Treasure Box_ trinket, which increases amount of special rooms by _25%/60%/95%/130%_.\n\n" +
                    "_-_ The amount of alchemical energy required to upgrade this trinket is _25/30/35_.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.BROKEN_TREASURE);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{BrokenTreasureBox.class};
        }
    },
    NO_TORCH_KEY_REWARDS("No Torches and Keys as rewards", "Luiz Felipe Sá/@luizfelipesa",
            "_-_ Torches and any variety of key will _no longer be rolled as a prize_ for special puzzle rooms.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.GOLDEN_KEY);}
    },
    WATERSKIN_SIP("Sipping out of Waterskin", "The healing plant/@fuwn.",
            "_-_ Added ability to _sip_ out of waterskin, consuming only 1 dewdrop out of it.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.WATERSKIN);}
    },
    FROSTFIRE_BREW("Frostfire Brew return", "u/SmithyLK",
            "_-_ Reimplemented _Frostfire Brew_, with it being reworked to create _frost fire_ instead of normal fire and chill blobs.\n\n" +
                    "_-_ Frost fire doesn't spread to other tiles, freezes and burns heaps at the same time, and inflicts _Frostburn debuff_.\n\n" +
                    "_-_ Frostburn makes its targets be both slowed and damaged and freezes them when extinguished with water.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.BREW_FROSTFIRE);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{FrostfireBrew.class};
        }
    },
    GEMSTONE_DUST("Gemstone Dust", "Drach/u/InkDrach, u/radiantchaos18 and u/AntManMax",
            "_-_ Added _Gemstone Dust_ as _ring counterpart_ to arcane resin.\n\n" +
                    "_-_ Gemstone dust can be used to _upgrade rings up to +2_, this boost is reset with normal upgrades.\n\n" +
                    "_-_ Arcane resin can also be used to upgrade rings and gemstone dust can be used to upgrade wands, but each level of boost _costs one more item than usual_.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.GEMSTONE_DUST);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{GemstoneDust.class};
        }
    },
    NECKLACE_OF_ICE("Necklace of Ice", "Pxl Pddng/u/-pixelpudding-",
            "_-_ Added _Necklace of Ice_, an ice-themed artifact that _turns cold into protection_, making player be immune to frost, not be slowed by chill and get additional armor while being chilled.\n\n" +
                    "_-_ While worn, it will slowly charge (300 turns to full), with 100% charge necklace of ice can be used to _unleash a blizzard effect_, that damages and chills enemies and ground in 5x5 area.\n\n" +
                    "_-_ Necklace of ice is upgraded by gathering experience.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.ARTIFACE_NECKLACE);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{GemstoneDust.class};
        }
    },
    EXPLORER_MODE("Explorer mode", "u/confusedpuppy@lemmy.dbzer0.com",
            "_-_ Added _Explorer mode_, inspired by Caves of Qud, that allows to _set checkpoints_ at shop rooms, and _reset the game back at them, when dying_.\n\n" +
                    "_-_ The Explorer mode runs _cannot obtain badges_ and their runs will be at the bottom of rankings, like seeded runs.", Category.OTHER_CONTENT){
        @Override public Image icon() {return Icons.get(Icons.TALENT);}
    },
    RUNIC_BLADE_REWORK("Runic blade rework", "u/diamocube",
            "_-_ Changed Runic Blade to _never lose enchantments_ and _increase their power_ with upgrading, instead of getting more damage from them.\n\n" +
                    "_-_ Each upgrade boosts enchantments by _15%_ in multiplicative fashion.", Category.MECHANICS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.RUNIC_BLADE);}
    },
    FURY_SPELL("Fury spell", "u/Lancelot-Gaming",
            "_-_ Added _Fury_ spell, that enrages enemies by inflicting Amok and Adrenaline on them.\n\n" +
                    "_-_ The spell can be made with Scroll of Rage.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.FURY);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{Fury.class};
        }
    },
    WAKIZASHI("Wakizashi", "_u/Demonetizing-YT-GUY_",
            "_-_ Added _Wakizashi_, the special T4 weapon, that has very low damage, but gets +33% accuracy and _attacks 3 times per turn_, with each hit ignoring _33% of enemy's armor_\n\n" +
                    "_-_ Duelist's ability is combo effect that lasts for 3 turns, during which each hit will deal 1 more damage than previous one.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.WAKIZASHI);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{Wakizashi.class};
        }
    },
    CROSS_BOMB("Cross Bomb", "tpd0618/@tpd0618",
            "_-_ Added _cross bomb_, that deals 33% more damage, but _explodes in cross_ shape without corners, like Bomberman bomb.\n\n" +
                    "_-_ Each line of cross is 2 tiles long.\n\n" +
                    "_-_ The bomb can either be made with bomb and shuriken, or be found as alternative to normal bomb.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.CROSS_BOMB);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{CrossBomb.class};
        }
    },
    COLORED_RUNESTONES("Colored runestones", ":soiled:/@tarzhel",
            "_-_ Each runestone's sign is now colored like their respective scroll.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.STONE_FEAR);}
    },
    HARDCORE_BADGES("Hardcore badges", ":soiled:/@tarzhel",
            "_-_ Added few new badges, intended to be additional challenge for players.\n\n" +
                    "" +
                    "_-_ _UnHappy Ending_ - rewarded, when player dies during ascension.\n" +
                    "_-_ _The Root of All Evil_ - rewarded, when at least three shopkeepers are forced to flee.\n" +
                    "_-_ _Pristine Victory_ - rewarded, when ascension run is completed with every score category being maxed out.\n" +
                    "_-_ _Champion of All Trades_ - rewarded, when player completes run with every challenge (not all at once, think of it as counterpart to badge for defeating DM-300 with all subclasses).\n" +
                    "_-_ _Syphoned Enchanter_ - rewarded, when player completes run without using scroll of upgrade.", Category.OTHER_CONTENT){
        @Override public Image icon() {return BadgeBanner.image( Badges.Badge.PERFECT_ASCENT.image);}

        @Override
        public Badges.Badge[] associatedBadges() {
            return new Badges.Badge[]{Badges.Badge.HAPPY_DEATH, Badges.Badge.THREE_SHOPKEEPERS_FLEE, Badges.Badge.PERFECT_ASCENT, Badges.Badge.VICTORY_EVERY_CHALLENGE, Badges.Badge.NO_UPGRADES,
            Badges.Badge.VICTORY_NO_ARMOR, Badges.Badge.VICTORY_NO_FOOD, Badges.Badge.VICTORY_DARKNESS, Badges.Badge.VICTORY_NO_HEALING, Badges.Badge.VICTORY_CHAMPION_ENEMIES, Badges.Badge.VICTORY_STRONGER_BOSSES, Badges.Badge.VICTORY_SWARM_INTELLIGENCE, Badges.Badge.VICTORY_NO_SCROLLS, Badges.Badge.VICTORY_NO_HERBALISM};
        }
    },
    HERBALIST_BADGES("Herbalist badges", "The healing plant/@fuwn.",
            "_-_ Added a badge category for planting and using plants, with a badge for 10/20/30/40/50 triggered plants in a run.", Category.OTHER_CONTENT){
        @Override public Image icon() {return BadgeBanner.image( Badges.Badge.PLANTS_ACTIVATED_2.image);}

        @Override
        public Badges.Badge[] associatedBadges() {
            return new Badges.Badge[]{Badges.Badge.PLANTS_ACTIVATED_1, Badges.Badge.PLANTS_ACTIVATED_2, Badges.Badge.PLANTS_ACTIVATED_3, Badges.Badge.PLANTS_ACTIVATED_4, Badges.Badge.PLANTS_ACTIVATED_5};
        }
    },
    LEAN("Leany Elixir", "Hrohlu/@hrohlu",
            "_-_ Added _Leany Elixir_, which makes player take _3x less_ elemental damage, move _2x_ faster, dodge _50% more_ frequently and do _2x more_ physical damage.\n\n" +
                    "_-_ This elixir also makes player _vertigoed_, making them run in random directions and miss their melee and ranged attacks.\n" +
                    "_-_ This elixir can be made with Potion of Levitation and Unstable Brew (given 2 at the craft).", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.ELIXIR_LEAN);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{LeanyElixir.class};
        }
    },
    FAILED_DEFENDER("Failed Defender", "Gamma/@gammalolman",
            "_-_ Added _failed defender_, the rare variant of prison guard, that combines traits of prison guard, succubus and gnoll brute.\n\n" +
                    "_-_ Failed defender has 3 more armor and 2 more evasion points, than normal guard, and _can teleport to the player_, when they are out to reach.\n\n" +
                    "_-_ His drop is _enchanted armor_ at 5x drop rate of normal guard.", Category.OTHER_CONTENT){
        @Override public Image icon() {return new DefenderSprite();}
    },
    FORESIGHT_REWORK("Rogue's Foresight rework", "QKuroire/@qkuroire",
            "_-_ Reduced the chance to detect a secret from _75%/100%_ to _50%/75%_, but successful procs show the secret room entrances on the map.", Category.MECHANICS){
        @Override public Image icon() {return new TalentIcon(Talent.ROGUES_FORESIGHT);}
    },
    TENACITY_BUFF("Ring of Tenacity buff", "Raynuva/@raynuva",
            "_-_ Changed the 0% damage reduction threshold from _100% HP_ to _80% HP_.\n\n" +
                    "_-_ Changed the 100% damage reduction threshold from _0%_ to _20% HP_.", Category.MECHANICS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.RING_SAPPHIRE);}
    },
    DRAGON_BREATH_EVAPORATE("Dragon's Breath water interaction", "ImanUserIus/@imanuserius",
            "_-_ Changed _Potion of Dragon's Breath_ to _evaporate roughly 60% of water_ in effect's AoE, acting as reverse Aqua Brew.", Category.MECHANICS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.EXOTIC_AMBER);}
    },
    AUGMENTED_CROSSBOW_DARTS("Darts and Crossbow's augmentation", "Lootbug/@thelootbug",
            "_-_ Darts are now _affected by Crossbow's augmentation_, making them take more/less time to hit, but dealing more/less damage.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.CROSSBOW);}
    },
    ALCHEMIST_MODE("Prime of Alchemist mode", "下水道螃蟹",
            "_-_ Added _Prime of Alchemist_ mode, that hyper-focuses on alchemical side of gameplay.\n\n" +
                    "_-_ In this mode, consumables become much more common, with traditional equipment being powerless and much rarer to find.\n\n" +
                    "_-_ The player also starts with Alchemist's Toolkit +10, letting them to do any alchemy on the go, and with all common consumables identified.\n\n" +
                    "_-_ The Prime of Alchemist mode runs _cannot obtain badges_ and their runs will be at the bottom of rankings, like seeded runs.", Category.MECHANICS){
        @Override public Image icon() {return Icons.get(Icons.ALCHEMY);}
    },
    SEAL_ENERGY_SYNERGY("Broken seal and Ring of Energy synergy", "-Rivlin- speedrunner #1/@-rivlin-",
            "_-_ Made Ring of Energy's recharge boost affect Warrior's broken seal.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.RING_TOPAZ);}
    },
    SUMMON_ELEMENTAL_BUFF("Summon Elemental buff", "MapleOak/@mapleoak-number-61",
            "_-_ Ally elementals from Summon Elemental spell are now \"intelligent\", allowing them to follow players across floors and no longer target wandering or sleeping enemies.\n\n" +
                    "_-_ Increased ally elemental's evasion by 20% and health by 33% and made them refresh on each recall.\n\n" +
                    "_-_ Decreased amount of spell crafted from 6 to 5.", Category.TWEAKS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.SUMMON_ELE);}
    },
    GHOST_HERO_ITEM_PICKUP("Dried Rose's ghost picks up items", "World First +24 Glaive/@heapheaus",
            "_-_ Made ghost ally from Dried Rose pick up items from the ground, when moving.", Category.MECHANICS){
        @Override public Image icon() {return new GhostSprite();}
    },
    SMART_TARGETING("Smart targeting", "484 Palkia/@zrp200",
            "_-_ Enhances combo, preparation, monk abilities and some of cleric spells' targeting with new rules:\n\n" +
                    "   _-_ If there is only one possible target, automatically uses the ability on them without requiring a tap\n" +
                    "   _-_ If there are multiple possible targets, highlights valid ones to be tapped\n" +
                    "   _-_ If there is only one valid target highlighted, and you click the ability button again, executes the ability on that target.\n\n" +
                    "_-_ This may not work as well with Cleric spells due to many of them being ally-focused.", Category.MECHANICS){
        @Override public Image icon() {return Icons.get(Icons.TARGET);}
    },
    REPAIRED_RAPIER("Repaired rapier", "Zackary (Prof. Wand Hater)/@zackary4536",
            "_-_ Added a trinket-like item, _Repaired Rapier_, that can be made from Duelist's remain item, Broken Hilt.\n\n" +
                    "_-_ When in inventory, Repaired Rapier allows to use weapon abilities even without being a Duelist.", Category.ITEMS){
        @Override public Image icon() {return new ItemSprite(ItemSpriteSheet.REPAIRED_RAPIER);}

        @Override
        public Class<? extends Item>[] associatedItems() {
            return new Class[]{RepairedRapier.class};
        }
    },
    LEVEL_FEELING_MOBS("Mobs exclusive to level feelings", "星月夜(MoonSaya)",
            "_-_ Added new mobs, that are exclusive to certain level feelings.\n\n" +
                    "_-_ There is currently 4 mobs:\n" +
                    "   _-_ _Wind Spirit_, the enemy for chasm floors, will attempt to push you over the edge with its attacks.\n" +
                    "   _-_ _Giant Water Strider_, the enemy for water floors, will swiftly fly across the water ponds and spew water at you.\n" +
                    "   _-_ _Angry Thorn_, the enemy for grass floors, will only attack enemies on grass with its thorns, that inflict Bleeding.\n" +
                    "   _-_ _DM-XXX_, the enemy for trap floors, will reclaim traps and use them to attack you.", Category.OTHER_CONTENT){
        @Override public Image icon() {return new WaterStriderSprite();}
    }
    ;

    public enum Category {
        ITEMS(0x99FF34), OTHER_CONTENT(0x2EE62E), MECHANICS(0xFF7F00), TWEAKS(0xD9D9D9);

        public final int color;

        Category(int color) {
            this.color = color;
        }
    }

    public final String author;
    public final String name;
    public final String description;
    public final Category category;
    public boolean enabled = true;

    Feature(String name, String author, String description, Category category){
        this.name = name;
        this.author = author;
        this.description = description;
        this.category = category;
    }

    public Image icon(){
        return new ItemSprite(ItemSpriteSheet.SOMETHING);
    }

    public Class<? extends Item>[] associatedItems(){
        return new Class[]{};
    }

    public Badges.Badge[] associatedBadges(){
        return new Badges.Badge[]{};
    }

    private static final String FEATURES_FILE = "features.dat";

    public static void saveFeatures(){
        Bundle bundle = new Bundle();

        ArrayList<String> disabledFeatures = new ArrayList<>();

        for (Feature feature: Feature.values()){
            if (!feature.enabled){
                disabledFeatures.add(feature.name());
            }
        }

        bundle.put("features", disabledFeatures.toArray(new String[0]));

        try {
            FileUtils.bundleToFile(FEATURES_FILE, bundle);
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }

    public static void loadFeatures(){
        try {
            Bundle bundle = FileUtils.bundleFromFile(FEATURES_FILE);
            ArrayList<String> disabledFeatures = new ArrayList(Arrays.asList(bundle.getStringArray("features")));
            for (String featureStr: disabledFeatures){
                Feature feature = Enum.valueOf(Feature.class, featureStr);
                feature.enabled = false;
            }
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }
}
