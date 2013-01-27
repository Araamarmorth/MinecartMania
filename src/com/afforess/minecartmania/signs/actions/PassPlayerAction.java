package com.afforess.minecartmania.signs.actions;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmania.signs.MMSign;
import com.afforess.minecartmania.signs.SignAction;

public class PassPlayerAction extends SignAction{
	
	
	public boolean execute(MinecartManiaMinecart minecart) {
		return false;
	}

	
	public boolean async() {
		return false;
	}

	
	public boolean process(String[] lines) {
		for (String line : lines) {
			if (line.toLowerCase().contains("[pass player")) {
				return true;
			}
		}
		return false;
	}

	
	public String getPermissionName() {
		return "passplayersign";
	}

	
	public String getFriendlyName() {
		return "Pass Player Sign";
	}

}