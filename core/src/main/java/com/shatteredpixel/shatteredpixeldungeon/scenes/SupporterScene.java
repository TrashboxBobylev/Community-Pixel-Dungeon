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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

public class SupporterScene extends PixelScene {

	private static final int BTN_HEIGHT = 22;
	private static final int GAP = 2;

	@Override
	public void create() {
		super.create();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		int elementWidth = PixelScene.landscape() ? 202 : 120;

		Archs archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add(btnExit);

		IconTitle title = new IconTitle(Icons.GOLD.get(), Messages.get(this, "title"));
		title.setSize(200, 0);
		title.setPos(
				(w - title.reqWidth()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		SupporterMessage msg = new SupporterMessage();
		msg.setSize(elementWidth, 0);
		add(msg);

		StyledButton link1 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "discord_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://discord.gg/KBfMN8X";
				ShatteredPixelDungeon.platform.openURI(link);
			}
		};
		link1.icon(Icons.get(Icons.COMMUNITY));
		link1.textColor(Window.WHITE);
		link1.setSize(elementWidth, BTN_HEIGHT);
		add(link1);

		StyledButton link2 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "lemmy_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://lemmy.world/c/pixeldungeon";
				ShatteredPixelDungeon.platform.openURI(link);
			}
		};
		link2.icon(Icons.get(Icons.COMMUNITY));
		link2.textColor(Window.WHITE);
		link2.setSize(elementWidth, BTN_HEIGHT);
		add(link2);

		StyledButton link3 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "reddit_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://reddit.com/r/PixelDungeon";
				ShatteredPixelDungeon.platform.openURI(link);
			}
		};
		link3.icon(Icons.get(Icons.COMMUNITY));
		link3.textColor(Window.WHITE);
		link3.setSize(elementWidth, BTN_HEIGHT);
		add(link3);

		StyledButton link4 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "mods_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://pixeldungeon.fandom.com/wiki/Category:Mods";
				ShatteredPixelDungeon.platform.openURI(link);
			}
		};
		link4.icon(Icons.get(Icons.NEWS));
		link4.textColor(Window.WHITE);
		link4.setSize(elementWidth, BTN_HEIGHT);
		add(link4);

		float elementHeight = msg.height() + BTN_HEIGHT*4 + GAP;

		float top = 16 + (h - 16 - elementHeight)/2f;
		float left = (w-elementWidth)/2f;

		msg.setPos(left, top);
		align(msg);

		link1.setPos(left, msg.bottom()+GAP);
		align(link1);
		link2.setPos(left, link1.bottom());
		align(link2);
		link3.setPos(left, link2.bottom());
		align(link3);
		link4.setPos(left, link3.bottom());
		align(link4);

	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TitleScene.class );
	}

	private static class SupporterMessage extends Component {

		NinePatch bg;
		RenderedTextBlock text;
		Image icon;

		@Override
		protected void createChildren() {
			bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
			add(bg);

			String message = Messages.get(SupporterScene.class, "intro");
			message += "\n\n- TrashboxBobylev";

			text = PixelScene.renderTextBlock(message, 6);
			add(text);

			icon = Icons.get(Icons.BOBYLEV);
			add(icon);

		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			text.maxWidth((int)width - bg.marginHor());
			text.setPos(x + bg.marginLeft(), y + bg.marginTop() + 1);

			icon.y = text.bottom() - icon.height() + 4;
			icon.x = x + 65;

			height = (text.bottom() + 3) - y;

			height += bg.marginBottom();

			bg.size(width, height);

		}

	}

}
