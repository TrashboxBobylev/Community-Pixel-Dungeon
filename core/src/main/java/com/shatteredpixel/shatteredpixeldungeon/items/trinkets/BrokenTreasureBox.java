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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BrokenTreasureBox extends Trinket {

    {
        image = ItemSpriteSheet.BROKEN_TREASURE;
    }

    @Override
    protected int upgradeEnergyCost() {
        //5 -> 25(30) -> 35(65) -> 45(110)
        return 25+10*level();
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        if (extraSpecialRoomChance(buffedLvl()) > 1f)
            desc += "\n\n" + Messages.get(this, "desc_extra", (int)(100*(extraSpecialRoomChance(buffedLvl())-1f)));
        else
            desc += "\n\n" + Messages.get(this, "desc_chance", (int)(100*extraSpecialRoomChance(buffedLvl())));
        desc += "\n\n" + Messages.get(this, "desc_upgrade");

        return desc;
    }

    public static float extraSpecialRoomChance(){
        return extraSpecialRoomChance(trinketLevel(BrokenTreasureBox.class));
    }

    public static float extraSpecialRoomChance( int level ){
        if (level == -1){
            return 0f;
        } else {
            return 0.25f + 0.50f*level;
        }
    }
}
