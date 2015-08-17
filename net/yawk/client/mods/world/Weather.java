package net.yawk.client.mods.world;

import net.yawk.client.Client;
import net.yawk.client.events.EventRecievePacket;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Weather", desc = "Hide rain and snow", type = Mod.Type.WORLD)
public class Weather extends Mod{
	
	public Weather(){
		
	}
	
	@EventTarget
	public void onTick(EventTick e){
		Client.getClient().getMinecraft().theWorld.getWorldInfo().setRaining(false);
	}
}
