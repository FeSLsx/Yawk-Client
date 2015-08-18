package net.yawk.client.modmanager;

import com.darkmagician6.eventapi.EventManager;

public class EventListener implements IToggleable{
	
	public EventListener(){
		
	}
	
	public void register(){
		EventManager.register(this);
	}
	
	public void unregister(){
		EventManager.unregister(this);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
