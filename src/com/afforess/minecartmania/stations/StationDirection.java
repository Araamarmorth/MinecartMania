package com.afforess.minecartmania.stations;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public enum StationDirection implements Direction {
	Straight {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("STR") || str.toLowerCase().contains("straight"))
				return minecart.getDirection();
			return CompassDirection.NO_DIRECTION;
		}
	},
	North {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("N") || str.toLowerCase().contains("north"))
				return CompassDirection.NORTH;
			return CompassDirection.NO_DIRECTION;
		}
	},
	East {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("E") || str.toLowerCase().contains("east"))
				return CompassDirection.EAST;
			return CompassDirection.NO_DIRECTION;
		}
	},
	South {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("S") || str.toLowerCase().contains("south"))
				return CompassDirection.SOUTH;
			return CompassDirection.NO_DIRECTION;
		}
	},
	West {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("W") || str.toLowerCase().contains("west"))
				return CompassDirection.WEST;
			return CompassDirection.NO_DIRECTION;
		}
	},
	Left {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("L") || str.toLowerCase().contains("left"))
				return DirectionUtils.getLeftDirection(minecart.getDirection());
			return CompassDirection.NO_DIRECTION;
		}
	},
	Right {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("R") || str.toLowerCase().contains("right"))
				return DirectionUtils.getRightDirection(minecart.getDirection());
			return CompassDirection.NO_DIRECTION;
		}
	},
	Destroy {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("D") || str.toLowerCase().contains("destroy"))
				return null;
			return CompassDirection.NO_DIRECTION;
		}
	},
	Prompt {
		
		public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
			if (str.equals("P") || str.toLowerCase().contains("prompt")) {
				if (minecart.hasPlayerPassenger()) {
					minecart.setDataValue("Prompt Override", true);
				}
			}
			return CompassDirection.NO_DIRECTION;
		}
	},
    Vanish {
        
        public CompassDirection direction(MinecartManiaMinecart minecart, String str) {
            if (str.equals("V") || str.toLowerCase().contains("vanish"))
                return null;
            return CompassDirection.NO_DIRECTION;
        }
    },
}
