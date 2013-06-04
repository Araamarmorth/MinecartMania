package com.afforess.minecartmania.signs.actions;

import com.afforess.minecartmania.config.Settings;
import com.afforess.minecartmania.minecarts.MMMinecart;
import com.afforess.minecartmania.signs.SignAction;
import com.afforess.minecartmania.utils.StringUtils;

public class SetGravityPercentAction extends SignAction {

	protected double percent = 0;

	public boolean execute(MMMinecart minecart) {
		minecart.setGravityPercent(percent);
		return true;
	}

	public boolean async() {
		return false;
	}

	public boolean process(String[] lines) {
		for (String line : lines) {
			if (line.toLowerCase().contains("[set grav:def") || line.toLowerCase().contains("[no fly"))  {
				percent = Settings.DefaultGravityPercent;
				return true;
			}
			else if (line.toLowerCase().contains("[set grav")) {
				String[] split = line.toLowerCase().split(":");
				if (split.length != 2) continue;
				percent = Double.parseDouble(StringUtils.getNumber(split[1]));
				return true;
			}
			else if (line.toLowerCase().contains("[fly")) {
				percent = 0;
				return true;
			}
		}
		return false;
	}


	public String getPermissionName() {
		return "setgravitypercentsign";
	}


	public String getFriendlyName() {
		return "Set Gravity Percent" + percent;
	}

}
