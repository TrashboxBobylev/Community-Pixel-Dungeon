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

import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public enum Feature {

    NONE;

    public String author;
    public String description;
    boolean enabled = true;

    Feature(){}

    Feature(String author, String description){
        this.author = author;
        this.description = description;
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
