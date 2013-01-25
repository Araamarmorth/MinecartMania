package com.afforess.minecartmania.stations;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.entity.AbstractItem;
import com.afforess.minecartmaniacore.entity.Item;
import com.afforess.minecartmaniacore.entity.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.entity.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.ItemUtils;

public enum StationCondition implements Condition{
	Default {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return str.equals("D") || str.toLowerCase().contains("default");
		}
	},
	Empty {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.isStandardMinecart() && minecart.getPassenger() == null && str.toLowerCase().contains("empty");
		}
		
	},
	Player {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.hasPlayerPassenger() && str.toLowerCase().contains("player");
		}
		
	},
	Mob {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() != null && !minecart.hasPlayerPassenger() && str.toLowerCase().contains("mob");
		}
	},
	Pig {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Pig && str.toLowerCase().contains("pig");
		}
	},
	Chicken {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Chicken && str.toLowerCase().contains("chicken");
		}
	},
	Cow {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Cow && str.toLowerCase().contains("cow");
		}
	},
	Sheep {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Sheep && str.toLowerCase().contains("sheep");
		}
	},
	Creeper {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Creeper && str.toLowerCase().contains("creeper");
		}
	},
	Skeleton {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Skeleton && str.toLowerCase().contains("skeleton");
		}
	},
	Zombie {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.getPassenger() instanceof org.bukkit.entity.Zombie && str.toLowerCase().contains("zombie");
		}
	},
	StationCommand {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.hasPlayerPassenger() && SignCommands.processStationCommand(minecart, str);
		}
	},
	PlayerName {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.hasPlayerPassenger() && str.equalsIgnoreCase(minecart.getPlayerPassenger().getName());
		}
	},
	ContainsItem {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			if (minecart.hasPlayerPassenger() && minecart.getPlayerPassenger().getItemInHand() != null) {
				Item itemInHand = Item.getItem(minecart.getPlayerPassenger().getItemInHand().getTypeId(), minecart.getPlayerPassenger().getItemInHand().getDurability());
				AbstractItem[] signData = ItemUtils.getItemStringToMaterial(str);
				for (AbstractItem item : signData) {
					if (item != null && item.equals(itemInHand)) {
						return true;
					}
				}		
			}
			else if (minecart.isStorageMinecart()) {
				AbstractItem[] signData = ItemUtils.getItemStringToMaterial(str);
				for (AbstractItem item : signData) {
					if (item != null && (((MinecartManiaStorageCart)minecart).amount(item.type()) > (item.isInfinite() ? 0 : item.getAmount()))) {
						return true;
					}
				}	
			}
			return false;
		}
	},
	Cargo {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.isStorageMinecart() && str.toLowerCase().contains("cargo") && ((MinecartManiaStorageCart)minecart).isEmpty();
		}
	},
	Storage {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.isStorageMinecart() && str.toLowerCase().contains("storage");
		}
	},
	Powered {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return minecart.isPoweredMinecart() && str.toLowerCase().contains("powered");
		}
	},
	West {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return (str.equals("W") || str.toLowerCase().contains("west")) && !str.contains("-") && minecart.getDirection() == CompassDirection.WEST;
		}
	},
	East {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return (str.equals("E") || str.toLowerCase().contains("east")) && !str.contains("-") && minecart.getDirection() == CompassDirection.EAST;
		}
	},
	North {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return (str.equals("N") || str.toLowerCase().contains("north")) && !str.contains("-") && minecart.getDirection() == CompassDirection.NORTH;
		}
	},
	South {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return (str.equals("S") || str.toLowerCase().contains("south")) && !str.contains("-") && minecart.getDirection() == CompassDirection.SOUTH;
		}
	},
	Redstone {
		
		public boolean result(MinecartManiaMinecart minecart, String str) {
			return str.toLowerCase().contains("redstone") && (minecart.isPoweredBeneath() ||
					MinecartManiaWorld.isBlockIndirectlyPowered(minecart.getWorld(), minecart.getX(), minecart.getY() - 2, minecart.getZ()));
		}
	}
	
}
