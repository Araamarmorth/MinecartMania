package com.afforess.minecartmania.events;

import java.util.Calendar;

import com.afforess.minecartmania.MinecartManiaMinecart;

public class MinecartTimeEvent extends MinecartManiaEvent {
	private MinecartManiaMinecart minecart;	
	private Calendar oldCalendar;
	private Calendar currentCalendar;
	public MinecartTimeEvent(MinecartManiaMinecart cart, Calendar oldCal, Calendar newCal) {
		super("MinecartTimeEvent");
		minecart = cart;
		oldCalendar = oldCal;
		currentCalendar = newCal;
	}
	
	
	public MinecartManiaMinecart getMinecart() {
		return minecart;
	}
	
	public Calendar getOldCalendar() {
		return oldCalendar;
	}
	
	public Calendar getCurrentCalendar() {
		return currentCalendar;
	}
}