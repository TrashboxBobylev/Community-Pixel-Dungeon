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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Feature;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class CommPD_Changes {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("All Current Additions", true,
                "All changes can be toggled off and on in this screen");
        changes.hardlight(0x25CA1F);
        changeInfos.add(changes);

        for (Feature.Category featureCategory: Feature.Category.values()){
            changes = new ChangeInfo(Messages.get(Feature.class, "category_" + featureCategory.name()), false, null);
            changes.hardlight(featureCategory.color);
            changeInfos.add(changes);
            for (Feature feature: Feature.values()){
                if (feature.category == featureCategory){
                    changes.addButton(new FeatureButton(feature));
                }
            }
        }

        changes = new ChangeInfo("CommPD-2.0.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHECKED), "Feature screen overhaul",
                "_-_ All features are now _split into 4 categories_ for easier analysis: items, non-items, new mechanics and QoL/minor tweaks.\n\n" +
                        "_-_ Made all features _toggleable_, allowing for fully customizable experience!\n\n" +
                        "   _-_ To toggle a feature, press a button in its description in this very screen.\n\n" +
                        "   _-_ The disabled items and mobs will persist in already ongoing runs, but will not be accessible in any future ones."));

        changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Shattered Ports",
                "Ported all content from Shattered 3.0.2.\n\n" +
                        "_-_ Added all new content into journal, with it disappearing when the things are disabled in feature screen.\n\n" +
                        "_-_ Slightly redesigned the title to fit more with new Shattered title banner.\n\n" +
                        "_-_ Removed custom notes feature due to being made obsolete by Shattered's new journal.\n\n" +
                        "_-_ Added minimal armor to Hold Fast rework."));

        changes.addButton( new ChangeButton(new RatKingSprite(), Feature.RAT_KING_CURSE.name,
                "Nerfed the rat king curse into the ground after huge feedback.\n\n" +
                        "_-_ Now makes player drop their weapon instead of murdering them."));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "_-_ Made explorer mode accessible without having to win the game."));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Fixed the following bugs:\n" +
                        "_-_ The sword's sprite having incorrect width and height.\n" +
                        "_-_ RNG Manipulator feature removing extra upgrades on special room loot, when not interacting with the spell.\n" +
                        "_-_ Explorer Mode sticking to new runs until the game is closed.\n" +
                        "_-_ Rings getting extra upgrades on transmutation, when being imbued with Arcane Resin or Gemstone Dust."
        ));

        changes = new ChangeInfo("CommPD-1.0.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

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
