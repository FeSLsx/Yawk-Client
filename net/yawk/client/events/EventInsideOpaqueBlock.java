package net.yawk.client.events;

import com.darkmagician6.eventapi.events.Event;

public class EventInsideOpaqueBlock implements Event{

	public boolean inside;

	public EventInsideOpaqueBlock(boolean inside) {
		super();
		this.inside = inside;
	}
	
}
