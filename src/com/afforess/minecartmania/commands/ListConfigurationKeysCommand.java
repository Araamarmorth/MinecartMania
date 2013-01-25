package com.afforess.minecartmania.commands;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.afforess.minecartmania.config.LocaleParser;
import com.afforess.minecartmaniacore.entity.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class ListConfigurationKeysCommand extends MinecartManiaCommand{

	public boolean isPlayerOnly() {
		return false;
	}

	public CommandType getCommand() {
		return CommandType.ListConfigKeys;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ConcurrentHashMap<String, Object> configKeys= MinecartManiaWorld.getConfiguration();
		
		int page;
		if (args.length == 0) {
			page = 1;
		}
		else {
			try {
				page = Integer.valueOf(StringUtils.getNumber(args[0])); 
			}
			catch (NumberFormatException e) {
				sender.sendMessage(LocaleParser.getTextKey("AdminControlsListConfigKeyError"));
				return true;
			}
		}
		
		int curLine = 0;
		int maxPages = (int) Math.ceil(((double)configKeys.size() / 6));
		
		if (page <= maxPages) {
			sender.sendMessage(LocaleParser.getTextKey("AdminControlsListConfigKeyPageHeader", page, maxPages));
	
			Iterator<Entry<String, Object>> i = configKeys.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, Object> e = i.next();
				if (curLine / 6 == (page - 1)) {
					sender.sendMessage(LocaleParser.getTextKey("AdminControlsListConfigKey", e.getKey(), e.getValue()));
				} 
				curLine++;
			}
		}
		return true;
	}

}
