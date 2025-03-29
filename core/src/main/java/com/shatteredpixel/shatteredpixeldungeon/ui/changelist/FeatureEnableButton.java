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

import com.shatteredpixel.shatteredpixeldungeon.Feature;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class FeatureEnableButton extends RedButton {

    protected Feature feature;

    public FeatureEnableButton(Feature feature) {
        super(Messages.get(WndFeature.class, feature != null && feature.enabled ? "disable" : "enable"));
        setFeature(feature);
    }

    public void setFeature(Feature feature){
        if (feature != null) {
            this.feature = feature;
            updateButton();
            visible = true;
        } else {
            visible = false;
        }
    }

    private void updateButton(){
        icon(Icons.get(feature.enabled ? Icons.UNCHECKED : Icons.CHECKED));
        text(Messages.get(WndFeature.class, feature.enabled ? "disable" : "enable"));
    }

    @Override
    protected void onClick() {
        super.onClick();
        feature.enabled = !feature.enabled;
        Feature.saveFeatures();
        updateButton();
        if (parent instanceof Window)
            ((Window) parent).hide();
    }

}
