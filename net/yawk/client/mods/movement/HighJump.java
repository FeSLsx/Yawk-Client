package net.yawk.client.mods.movement;

import net.yawk.client.Client;
import net.yawk.client.events.EventJump;
import net.yawk.client.events.EventMoveEntity;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@RegisterMod(name = "HighJump", desc = "Jump higher", type = Mod.Type.MOVEMENT)
public class HighJump extends Mod{
	
	private static SliderValue height;
	
	public HighJump(){
		
		super(new AbstractValue[]{
				height = new SliderValue("Height", "highjump.height", Client.getClient().getValuesRegistry(), 1.2, 0, 3, false),
		});
	}
	
	@EventTarget
	public void onJump(EventJump e){
		if(e.type == EventType.POST){
			Client.getClient().getPlayer().motionY = height.getValue();
		}
	}
	
	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		
	}
}
