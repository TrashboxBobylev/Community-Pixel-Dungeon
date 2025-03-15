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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DefenderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.*;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class CommPD_Changes {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("All Current Additions", true, "");
        changes.hardlight(0x25CA1F);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.MAGNIFY), "Indicator for full cleared floors",
                "Idea's author: _Mintzi/@.mint17_\n\n" +
                        "_-_ Added a small icon, that indicates the current dungeon floor being fully explored (for purposes of rankings' exploration score)."
        ));

        changes.addButton( new ChangeButton(new TalentIcon(Talent.RUNIC_TRANSFERENCE), "Hold Fast/Runic Transference tier-swap",
                "Idea's author: _Raynuva/@raynuva_\n\n" +
                        "_-_ _Hold Fast_ talent has been moved from Tier 3 to Tier 2, with reducing its max blocking from 2/4/6 to 2/5.\n\n" +
                        "_-_ _Runic Transference_ talent has been moved from Tier 2 to Tier 3, with new effect for _+3_!\n\n" +
                        "_-_ On _+3_, Runic Transference makes armor and broken seal independently store glyphs (with making seal be possible to inscribe with glyph) and _have them activate at the same time_!"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_INTUITION), "Stone of Intuition notes",
                "Idea's author: _Sentient Orange Pile/@sentientorangepile_\n\n" +
                        "_-_ Wrongly guessing item's type with _Stone of Intuition_ makes the guess _be saved as a note_ in item's description."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_RUBY), "Ring of Accuracy buff",
                "Idea's author: _ImanUserIus/@imanuserius_\n\n" +
                        "_-_ _Ring of Accuracy_ provides _surprise attack damage boost_, in addition to accuracy boost.\n\n" +
                        "_-_ Each upgrade adds _1.1x_ to surprise attack damage."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SILENCE), "Silencing spell",
                "Idea's author: _emphysima gaming/@miutsuifa_\n\n" +
                        "_-_ Added _Silencing_ spell, which disables the magical abilities of targeted enemy for several turns.\n\n" +
                        "_-_ Silenced enemies cannot do magic-based attacks or use magical abilities.\n\n" +
                        "_-_ The spell can be made from Scroll of Anti-Magic."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCHEMICAL_SWORD), "Alchemical Akrafena",
                "Idea's author: _Serpens/@serpens2137_\n\n" +
                        "_-_ Added _Alchemical Akrafena_, the special T4 weapon, which can be _imbued with potions_ for on-hit effects.\n\n" +
                        "_-_ The imbue lasts several hits and will apply effect of a dart, that corresponds to potion's seed, which was imbued.\n\n" +
                        "_-_ Duelist's ability will attack enemies in 5x5 area around the player with current imbue, but costs 2 charges and uses up the weapon's imbue."
        ));

        changes.addButton( new ChangeButton(new RatKingSprite(), "Rat King's tripping curse",
                "Idea's author: _watabou's uncle Dialga/@engio_\n\n" +
                        "_-_ Awakening Rat King from his sleep will _apply a curse_ on player.\n\n" +
                        "_-_ With curse active, there is 1/2222 chance on every step for player to trip and instantly die from breaking their neck.\n\n" +
                        "_-_ This effect cannot be countered with ankhs and invulnerability effects."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_WARDING), "Wand of Warding's sentry refunding",
                "Idea's author: _Sir Ayin/@uchufoxgd_\n\n" +
                        "_-_ Removing wards and sentries by interacting with them also _refunds some of the charge spent_ on deploying that sentry.\n\n" +
                        "_-_ 0,5/1/1,5 charges is refunded for wards and 1/1,5/2 charges is refunded for sentries."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_KAUNAN), "Revealing item's upgrades on identifying",
                "Idea's author: _Yams/@xrider107_\n\n" +
                        "_-_ Identifying equipment by any means states its upgrade level and enchantments in game log's identify message."
        ));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.VERTIGO, true), "Missing attacks while being vertigoed",
                "Idea's author: _MarioDied64/@merio64_\n\n" +
                        "_-_ Being inflicted with _Vertigo_ effect causes player to frequently miss the target of throwing or wand zaps.\n\n" +
                        "_-_ Enemies, that are affected with _Vertigo_, will randomly attack the wrong target nearby instead of player, regardless of attacking in melee or at range."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TENGU_BOMB), "Checking floor in range of Tengu's bomb",
                "Idea's author: _inverse-snake/@inverse.snake_\n\n" +
                        "_-_ Checking empty floor, that is in range of Tengu's smoke bomb, will show, if this floor will be affected by smoke bomb's explosion."
        ));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.FIRE, true), "Fire and frost reactions",
                "Idea's author: _Nat/@nat9542_\n\n" +
                        "_-_ Inflicting Burning on enemies with Chilling and vice versa _will damage the enemy_ equivalent to _75% of bomb's damage_.\n\n" +
                        "_-_ This interaction can be triggered with potions and elemental wands."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_ENCHANT), "Enchanting results",
                "Idea's author: _Cilian/@cilian._ and _Zackary (Prof. Wand Hater)/@zackary4536_\n\n" +
                        "_-_ _Arcane Stylus_ and _Stone of Enchantment_ state the enchantment/glyph they applied in their game log's message."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BANDOLIER), "Inventory bags from bosses",
                "Idea's author: _ifritdiezel/@ifritdiezel_\n\n" +
                        "_-_ Instead of being bought at the shops, storage bags will drop from the bosses."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RNG_MANIPULATOR), "Luck Manipulator spell",
                "Idea's author: _NeoSlav/@neoslav_\n\n" +
                        "_-_ Added _Luck Manipulator_ spell, which allows to get luck boost for next floor _by either sacrificing money or health_.\n\n" +
                        "_-_ Player can sacrifice 50% of their current gold or 75% of their total HP.\n\n" +
                        "_-_ The amount of luck boost _will depend on amount of sacrificed resources_ and will be shown in game log after spell's usage.\n\n" +
                        "_-_ The luck boost effect affects the amount and quality of puzzle room's drops and random enemy drops.\n\n" +
                        "_-_ The spell can be made with Unstable Spell."
        ));

        changes.addButton( new ChangeButton(Icons.get(Icons.CALENDAR), "Daily challenges",
                "Idea's author: _vexxjacobs/@vexxjacobs_\n\n" +
                        "_-_ The player can select to _get some challenges_, when doing the daily run.\n\n" +
                        "_-_ The resulting challenges will depend on the seed generated for that daily."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKEN_TREASURE), "Broken Treasure Box trinket",
                "Idea's author: _goteryup/@goteryup_\n\n" +
                        "_-_ Added _Broken Treasure Box_ trinket, which increases amount of special rooms by _25%/60%/95%/130%_.\n\n" +
                        "_-_ The amount of alchemical energy required to upgrade this trinket is _25/30/35_."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GOLDEN_KEY), "No Torches and Keys as rewards",
                "Idea's author: _Luiz Felipe Sá/@luizfelipesa_\n\n" +
                        "_-_ Torches and any variety of key will _no longer be rolled as a prize_ for special puzzle rooms."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WATERSKIN), "Sipping out of Waterskin",
                "Idea's author: _The healing plant/@fuwn._\n\n" +
                        "_-_ Added ability to _sip_ out of waterskin, consuming only 1 dewdrop out of it."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BREW_FROSTFIRE), "Frostfire Brew return",
                "Idea's author: _u/SmithyLK_\n\n" +
                        "_-_ Reimplemented _Frostfire Brew_, with it being reworked to create _frost fire_ instead of normal fire and chill blobs.\n\n" +
                        "_-_ Frost fire doesn't spread to other tiles, freezes and burns heaps at the same time, and inflicts _Frostburn debuff_.\n\n" +
                        "_-_ Frostburn makes its targets be both slowed and damaged and freezes them when extinguished with water."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GEMSTONE_DUST), "Gemstone Dust",
                "Idea's authors: _Drach/u/InkDrach, u/radiantchaos18 and u/AntManMax_\n\n" +
                        "_-_ Added _Gemstone Dust_ as _ring counterpart_ to arcane resin.\n\n" +
                        "_-_ Gemstone dust can be used to _upgrade rings up to +2_, this boost is reset with normal upgrades.\n\n" +
                        "_-_ Arcane resin can also be used to upgrade rings and gemstone dust can be used to upgrade wands, but each level of boost _costs one more item than usual_."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACE_NECKLACE), "Necklace of Ice",
                "Idea's author: _Pxl Pddng/u/-pixelpudding-_\n\n" +
                        "_-_ Added _Necklace of Ice_, an ice-themed artifact that _turns cold into protection_, making player be immune to frost, not be slowed by chill and get additional armor while being chilled.\n\n" +
                        "_-_ While worn, it will slowly charge (300 turns to full), with 100% charge necklace of ice can be used to _unleash a blizzard effect_, that damages and chills enemies and ground in 5x5 area.\n\n" +
                        "_-_ Necklace of ice is upgraded by gathering experience."
        ));

        changes.addButton( new ChangeButton(Icons.get(Icons.TALENT), "Explorer mode",
                "Idea's author: _u/confusedpuppy@lemmy.dbzer0.com_\n\n" +
                        "_-_ Added _Explorer mode_, inspired by Caves of Qud, that allows to _set checkpoints_ at shop rooms, and _reset the game back at them, when dying_.\n\n" +
                        "_-_ The Explorer mode runs _cannot obtain badges_ and their runs will be at the bottom of rankings, like seeded runs."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RUNIC_BLADE), "Runic blade rework",
                "Idea's author: _u/diamocube_\n\n" +
                        "_-_ Changed Runic Blade to _never lose enchantments_ and _increase their power_ with upgrading, instead of getting more damage from them.\n\n" +
                        "_-_ Each upgrade boosts enchantments by _15%_ in multiplicative fashion."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FURY), "Fury spell",
                "Idea's author: _u/Lancelot-Gaming_\n\n" +
                        "_-_ Added _Fury_ spell, that enrages enemies by inflicting Amok and Adrenaline on them.\n\n" +
                        "_-_ The spell can be made with Scroll of Rage."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAKIZASHI), "Wakizashi",
                "Idea's author: _u/Demonetizing-YT-GUY_\n\n" +
                        "_-_ Added _Wakizashi_, the special T4 weapon, that has very low damage, but gets +33% accuracy and _attacks 3 times per turn_, with each hit ignoring _33% of enemy's armor_\n\n" +
                        "_-_ Duelist's ability is combo effect that lasts for 3 turns, during which each hit will deal 1 more damage than previous one."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROSS_BOMB), "Cross Bomb",
                "Idea's author: _tpd0618/@tpd0618_\n\n" +
                        "_-_ Added _cross bomb_, that deals 33% more damage, but _explodes in cross_ shape without corners, like Bomberman bomb.\n\n" +
                        "_-_ Each line of cross is 2 tiles long.\n\n" +
                        "_-_ The bomb can either be made with bomb and shuriken, or be found as alternative to normal bomb."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_FEAR), "Colored runestones",
                "Idea's author: _:soiled:/@tarzhel_\n\n" +
                        "_-_ Each runestone's sign is now colored like their respective scroll."
        ));

        changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.PERFECT_ASCENT.image), "Hardcore badges",
                "Idea's author: _:soiled:/@tarzhel_\n\n" +
                        "_-_ Added few new badges, intended to be additional challenge for players.\n\n" +
                        "" +
                        "_-_ _UnHappy Ending_ - rewarded, when player dies during ascension.\n" +
                        "_-_ _The Root of All Evil_ - rewarded, when at least three shopkeepers are forced to flee.\n" +
                        "_-_ _Pristine Victory_ - rewarded, when ascension run is completed with every score category being maxed out.\n" +
                        "_-_ _Champion of All Trades_ - rewarded, when player completes run with every challenge (not all at once, think of it as counterpart to badge for defeating DM-300 with all subclasses).\n" +
                        "_-_ _Syphoned Enchanter_ - rewarded, when player completes run without using scroll of upgrade."
        ));

        changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.PLANTS_ACTIVATED_2.image), "Herbalist badges",
                "Idea's author: _The healing plant/@fuwn._\n\n" +
                        "_-_ Added a badge category for planting and using plants, with a badge for 10/20/30/40/50 triggered plants in a run."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_LEAN), "Leany Elixir",
                "Idea's author: _Hrohlu/@hrohlu_\n\n" +
                        "_-_ Added _Leany Elixir_, which makes player take _3x less_ elemental damage, move _2x_ faster, dodge _50% more_ frequently and do _2x more_ physical damage.\n\n" +
                        "_-_ This elixir also makes player _vertigoed_, making them run in random directions and miss their melee and ranged attacks.\n" +
                        "_-_ This elixir can be made with Potion of Levitation and Unstable Brew (given 2 at the craft)."
        ));

        changes.addButton( new ChangeButton(new DefenderSprite(), "Failed Defender",
                "Idea's author: _Gamma/@gammalolman_\n\n" +
                        "_-_ Added _failed defender_, the rare variant of prison guard, that combines traits of prison guard, succubus and gnoll brute.\n\n" +
                        "_-_ Failed defender has 3 more armor and 2 more evasion points, than normal guard, and _can teleport to the player_, when they are out to reach.\n\n" +
                        "_-_ His drop is _enchanted armor_ at 5x drop rate of normal guard."
        ));

        changes.addButton( new ChangeButton(new TalentIcon(Talent.ROGUES_FORESIGHT), "Rogue's Foresight rework",
                "Idea's author: _QKuroire/@qkuroire_\n\n" +
                        "_-_ Reduced the chance to detect a secret from _60%/90%_ to _40%/60%_, but successful procs show the secret room entrances on the map."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_SAPPHIRE), "Ring of Tenacity buff",
                "Idea's author: _Raynuva/@raynuva_\n\n" +
                        "_-_ Changed the 0% damage reduction threshold from _100% HP_ to _80% HP_.\n\n" +
                        "_-_ Changed the 100% damage reduction threshold from _0%_ to _20% HP_."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_AMBER), "Dragon's Breath water interaction",
                "Idea's author: _ImanUserIus/@imanuserius_\n\n" +
                        "_-_ Changed _Potion of Dragon's Breath_ to _evaporate roughly 60% of water_ in effect's AoE, acting as reverse Aqua Brew."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROSSBOW), "Darts and Crossbow's augmentation",
                "Idea's author: _Lootbug/@thelootbug_\n\n" +
                        "_-_ Darts are now _affected by Crossbow's augmentation_, making them take more/less time to hit, but dealing more/less damage."
        ));

        changes = new ChangeInfo("v0.1.1", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "_-_ Desktop builds are now always \"debug\", allowing Scroll of Debug and other testing QoL to appear.\n" +
                        "_-_ Made list of idea authors in about screen layout correctly on any resolution."));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Fixed the following bugs:\n" +
                "_-_ The new glyph code crashing in certain conditions due to developer's carelessness."
        ));

        changes = new ChangeInfo("v0.2.0", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "_-_ Added 17 more ideas, sourced from Reddit, Lemmy and older #ideas channel.\n" +
                        "_-_ Turned Scroll of Debug into setting option in data tab."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKEN_TREASURE), "Broken Treasure Box trinket",
                "Changed:\n\n" +
                        "_-_ Reduced the amount of special rooms from _25%/75%/125%/175%_ to _25%/60%/95%/130%_.\n\n" +
                        "_-_ Reduced upgrade energy requirement from _25/35/45_ to _25/30/35_."
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE), "Custom notes",
                "Improved:\n\n" +
                        "_-_ - Adding a note switches journal to notes tab.\n\n" +
                        "_-_ Reworked note removing: now is done through the button at note itself with approving window.\n\n" +
                        "_-_ Improved note list refreshing."
        ));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Fixed the following bugs:\n" +
                        "_-_ Scroll of Enchantment not properly showing enchantment/glyph names after being used."
        ));

    }
}
