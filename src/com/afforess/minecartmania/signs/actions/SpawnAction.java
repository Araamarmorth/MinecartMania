package com.afforess.minecartmania.signs.actions;


import java.util.HashMap;

import org.bukkit.Location;

import com.afforess.minecartmania.entity.Item;
import com.afforess.minecartmania.entity.MinecartManiaWorld;
import com.afforess.minecartmania.minecarts.MMMinecart;
import com.afforess.minecartmania.signs.SignAction;


public class SpawnAction extends SignAction {
	private HashMap<Location, Long> lastSpawn = new HashMap<Location, Long>();

	Item spawnType = Item.MINECART;

	@Override
	public boolean execute(MMMinecart minecart) {

		Long lastSpawn = this.lastSpawn.get(loc); //cooldown on spawner
		if (lastSpawn == null || (Math.abs(System.currentTimeMillis() - lastSpawn) > 1000)) {

			Location spawn = loc.clone().add(0, 1, 0);
			
			if(!com.afforess.minecartmania.utils.MinecartUtils.isTrack(spawn.getBlock())) return false;
				
			final MMMinecart newminecart = MinecartManiaWorld.spawnMinecart(spawn, spawnType, null);

			this.lastSpawn.put(loc, System.currentTimeMillis());

			return true;

		}
		return false;
	}


	@Override
	public boolean async() {
		return false;
	}

	@Override
	public boolean process(String[] lines) {
		for (String line : lines) {
			if (line.toLowerCase().contains("[spawn")) {
				if(line.toLowerCase().contains("east") ||line.toLowerCase().contains("west") ||line.toLowerCase().contains("north") ||line.toLowerCase().contains("south") ) return false;
				
				this.executeAcceptsNull = true;

				if(	line.toLowerCase().contains("[spawn:stor")){
					spawnType = Item.STORAGE_MINECART;
				}
				else if(	line.toLowerCase().contains("[spawn:pow")){
					spawnType = Item.POWERED_MINECART;
				}
				else if(	line.toLowerCase().contains("[spawn:hop")){
					spawnType = Item.MINECART_HOPPER;
				}
				else if(	line.toLowerCase().contains("[spawn:tnt")){
					spawnType = Item.MINECART_TNT;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String getPermissionName() {
		return "spawnsign";
	}

	@Override
	public String getFriendlyName() {
		return "Spawn";
	}

}
