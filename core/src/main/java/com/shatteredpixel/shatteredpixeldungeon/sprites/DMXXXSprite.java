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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DMXXX;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class DMXXXSprite extends MobSprite {

	public DMXXXSprite() {
		super();
		
		texture( Assets.Sprites.DMXXX );
		
		TextureFilm frames = new TextureFilm( texture, 16, 14 );
		
		idle = new Animation( 1, true );
		idle.frames( frames, 0, 1 );

		run = new Animation( 12, true );
		run.frames( frames, 6, 7, 8, 9 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2, 3, 4, 0 );

		zap = new Animation( 8, false );
		zap.frames( frames, 5, 5, 1 );

		die = new Animation( 12, false );
		die.frames( frames, 10, 11, 12, 13, 14, 15 );
		
		play( idle );
	}
	
	public void zap( int pos ) {

		//shoot lightning from eye, not sprite center.
		PointF origin = center();
		if (flipHorizontal){
			origin.y -= 6*scale.y;
			origin.x -= 1*scale.x;
		} else {
			origin.y -= 8*scale.y;
			origin.x += 1*scale.x;
		}
        parent.add(new Lightning(origin, origin, (DMXXX) ch));
        Sample.INSTANCE.play( Assets.Sounds.LIGHTNING, 1.0f, 1.25f );
		
		super.zap( ch.pos );
		flash();
	}

	@Override
	public void die() {
		emitter().burst( SmokeParticle.FACTORY, 10 );
		Sample.INSTANCE.play( Assets.Sounds.BLAST );
		super.die();
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public int blood() {
		return 0xFFFFFF88;
	}
}
