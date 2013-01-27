package com.afforess.minecartmania.signs.actions;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmania.config.Settings;
import com.afforess.minecartmania.signs.MMSign;
import com.afforess.minecartmania.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class SetMaxSpeedAction extends SignAction {
	
	protected int percent = -1;
	
	
	public boolean execute(MinecartManiaMinecart minecart) {
		minecart.setMaxSpeed(0.4D * percent / 100);
		return true;
	}

	
	public boolean async() {
		return true;
	}

	
	public boolean process(String[] lines) {
		
		for (String line : lines) {
			if (line.toLowerCase().contains("[max speed")) {
				String[] split = line.split(":");
				if (split.length != 2) continue;
				double percent = Double.parseDouble(StringUtils.getNumber(split[1]));
				percent = Math.min(percent, Settings.getMaximumMinecartSpeedPercent());
				this.percent = (int)percent;
				break;
			}
		}
		
		return this.percent > 0;
	}

	
	public String getPermissionName() {
		return "maxspeedsign";
	}

	
	public String getFriendlyName() {
		return "Max Speed Sign";
	}

}