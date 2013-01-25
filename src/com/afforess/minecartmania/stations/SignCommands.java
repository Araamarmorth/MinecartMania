package com.afforess.minecartmania.stations;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmania.events.MinecartEvent;
import com.afforess.minecartmaniacore.debug.MinecartManiaLogger;
import com.afforess.minecartmaniacore.entity.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.DirectionUtils;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.MinecartUtils;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.utils.StationUtil;
import com.afforess.minecartmaniacore.utils.StringUtils;
import com.afforess.minecartmaniacore.utils.WordUtils;

public class SignCommands {

	public static void processStation(MinecartEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		
		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(minecart, 2);
		for (Sign sign : signList) {
			convertCraftBookSorter(sign);
			for (int k = 0; k < 4; k++) {
				//Setup initial data
				String str = sign.getLine(k);
				String newLine = str;
				String val[] = str.split(":");
				if (val.length != 2) {
					continue;
				}
				//Strip header and ending characters
				val[0] = StringUtils.removeBrackets(val[0]);
				val[1] = StringUtils.removeBrackets(val[1]);
				//Strip whitespace
				val[0] = val[0].trim();
				val[1] = val[1].trim();
				boolean valid = false;
				//end of data setup
				
				for (StationCondition e : StationCondition.values()) {
					if (e.result(minecart, val[0])) {
						valid = true;
						break;
					}
				}
				
				if (valid) {
					CompassDirection direction = CompassDirection.NO_DIRECTION;
					
					for (StationDirection e : StationDirection.values()) {
						direction = e.direction(minecart, val[1]);
						if (direction != CompassDirection.NO_DIRECTION){
							break;
						}
					}
					
					//Special case - if we are at a launcher, set the launch speed as well
					//mze 2012-08-29: Remove it because it makes more problems than it solves...
					/*if (event instanceof MinecartLaunchedEvent && direction != null && direction != CompassDirection.NO_DIRECTION) {
						minecart.setMotion(direction, 0.6D);
						((MinecartLaunchedEvent)event).setLaunchSpeed(minecart.minecart.getVelocity());
					}*/
					
					//setup sign formatting
					newLine = StringUtils.removeBrackets(newLine);
					char[] ch = {' ', ':'};
					newLine = WordUtils.capitalize(newLine, ch);
					newLine = StringUtils.addBrackets(newLine);
					
					boolean handled = false;
					//Handle minecart destruction
					if (direction == null) {
					    //vanish sign, minecart is just gone ;)
					    if(val[1].equals("V") || val[1].toLowerCase().contains("vanish"))
					    {
					        minecart.kill(false);
					    }
					    
						minecart.kill();
						handled = true;
					}
					else if (MinecartUtils.validMinecartTrack(minecart.getWorld(), minecart.getX(), minecart.getY(), minecart.getZ(), 2, direction)) {
						int data = DirectionUtils.getMinetrackRailDataForDirection(direction, minecart.getDirection());
						if (data != -1) {
							handled = true;
							
							//Force the game to remember the old data of the rail we are on, and reset it once we are done
							Block oldBlock = MinecartManiaWorld.getBlockAt(minecart.getWorld(), minecart.getX(), minecart.getY(), minecart.getZ());
							ArrayList<Integer> blockData = new ArrayList<Integer>();
							blockData.add(new Integer(oldBlock.getX()));
							blockData.add(new Integer(oldBlock.getY()));
							blockData.add(new Integer(oldBlock.getZ()));
							blockData.add(new Integer(oldBlock.getData()));
							minecart.setDataValue("old rail data", blockData);
							
							//change the track dirtion
							MinecartManiaWorld.setBlockData(minecart.getWorld(), minecart.getX(), minecart.getY(), minecart.getZ(), data);
						}
						else if (DirectionUtils.getOppositeDirection(direction).equals(minecart.getDirection())) {
							//format the sign
							minecart.reverse();
							handled = true;
						}
					}
					
					if (handled){
						event.setActionTaken(true);
						//format the sign
						sign.setLine(k, newLine);
						sign.update(true);
						return;
					}
				}
			}
		}
	}

	protected static boolean processStationCommand(MinecartManiaMinecart minecart, String str) {
		boolean valid = false;
		
		if (!str.toLowerCase().contains("st-")) {
			return false;
		}
		String[] val = str.toLowerCase().split(":"); //Will doing a toLowerCase here mess up regular expressions?  Probably not since we lower case the station name anyway.
		String[] keys = val[0].split("-| ?: ?", 2);  //Split the st- from the station name. the ",2" limits it to the first "-" followed by zero or one non-capturing spaces.
		                                             //The ",2" is needed because regular expressions can have a "-" in them. example "st-[g-z].*"
		                                             //Without the limit, we would have the following array in keys: "st", "[g", "z].*" and then only work with the "[g" portion which is wrong.
		String st = keys[1];                         //Get the station name/simple pattern/regular expression
		String station = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getLastStation().toLowerCase();
		int parseSetting = (Integer)MinecartManiaWorld.getConfigurationValue("StationSignParsingMethod");
		MinecartManiaLogger.getInstance().debug("Given Sign Line: " + str + " Given Station setting: " + station);
		switch(parseSetting){
			case 0: //default with no pattern matching
				valid = station.equalsIgnoreCase(st);break;
			case 1: //simple pattern matching
				st = st.replace("\\", "\\\\") //escapes backslashes in case people use them in station names
				.replace(".", "\\.") //escapes period
				.replace("*", ".*") //converts *
				.replace("?", ".") //converts ?
				.replace("#", "\\d") //converts #
				.replace("@", "[a-zA-Z]"); //converts @  NOTE:[A-Z] is probably not needed here since everything is lower case anyway, but left for completeness.
			case 2: //full regex //note the lack of break before this, case 1 comes down here after converting
				valid = station.matches(st); break;
		}
		if (valid && MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getDataValue("Reset Station Data") == null) {
			if (!StationUtil.isStationCommandNeverResets()) {
				MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).setLastStation("");
			}
		}
		return valid;
	}

	private static void convertCraftBookSorter(Sign sign) {
		if (sign.getLine(1).contains("[Sort]")) {
			if (!sign.getLine(2).trim().isEmpty()) {
				sign.setLine(2, "st-" + sign.getLine(2).trim().substring(1) + ": L");
			}
			if (!sign.getLine(3).trim().isEmpty()) {
				sign.setLine(3, "st-" + sign.getLine(3).trim().substring(1) + ": R");
			}
			sign.setLine(1, "");
			sign.update();
		}
	}

	public static ArrayList<CompassDirection> getRestrictedDirections(MinecartManiaMinecart minecart) {
		ArrayList<CompassDirection> restricted = new ArrayList<CompassDirection>(4);
		ArrayList<Sign> signList = SignUtils.getAdjacentSignList(minecart, 2);
		for (Sign sign : signList) {
			for (int i = 0; i < 4; i++) {
				if (sign.getLine(i).toLowerCase().contains("restrict")) {
					String[] directions = sign.getLine(i).split(":");
					if (directions.length > 1) {
						for (int j = 1; j < directions.length; j++) {
							if (directions[j].contains("N")) {
								restricted.add(CompassDirection.NORTH);
							}
							if (directions[j].contains("S")) {
								restricted.add(CompassDirection.SOUTH);
							}
							if (directions[j].contains("E")) {
								restricted.add(CompassDirection.EAST);
							}
							if (directions[j].contains("W")) {
								restricted.add(CompassDirection.WEST);
							}
						}
						return restricted;
					}
				}
			}
		}
		return restricted;
	}
}
