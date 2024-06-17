package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class Silencing extends TargetedSpell {
    {
        image = ItemSpriteSheet.SILENCE;

        usesTargeting = true;

        talentChance = 1/(float) Recipe.OUT_QUANTITY;
    }

    @Override
    protected void affectTarget(Ballistica bolt, Hero hero) {
        final Char ch = Actor.findChar(bolt.collisionPos);

        if (ch != null && !(ch instanceof Hero)) {
            if (!Char.hasProp(ch, Char.Property.BOSS) && !Char.hasProp(ch, Char.Property.MINIBOSS)) {
                Buff.affect(ch, Effect.class, Effect.DURATION);
            } else {
                Buff.affect(ch, Effect.class, Effect.DURATION/3f);
            }
            Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            ch.sprite.burst(0xFF50ED07, 5);
        } else {
            GLog.w( Messages.get(this, "no_target") );
        }
    }

    @Override
    public int value() {
        return (int)(60 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int)(12 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    public static class Effect extends FlavourBuff {
        public static float DURATION = 6f;

        {
            announced = true;
        }

        public int icon() { return BuffIndicator.TIME; }
        public void tintIcon(Image icon) { icon.hardlight(0.31f, 0.93f, 0.03f); }
        public float iconFadePercent() { return Math.max(0, visualcooldown() / Effect.DURATION); }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 3;

        {
            inputs =  new Class[]{ScrollOfAntiMagic.class};
            inQuantity = new int[]{1};

            cost = 10;

            output = Silencing.class;
            outQuantity = OUT_QUANTITY;
        }

    }
}
