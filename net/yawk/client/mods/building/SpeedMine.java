package net.yawk.client.mods.building;

import net.yawk.client.Client;
import net.yawk.client.events.*;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.BooleanValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.timing.MillisecondTimer;
import net.yawk.client.utils.timing.ValueTimer;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Speedmine", desc = "Break blocks faster", type = Mod.Type.BUILDING)
public class SpeedMine extends Mod{
	
	private MillisecondTimer timer;
	private static SliderValue delay;
	
	public SpeedMine(){
		
		super(new AbstractValue[]{
				delay = new SliderValue("Delay", "speedmine.delay", Client.getClient().getValuesRegistry(), 100, 0, 100, true),
		});
		
		timer = new ValueTimer(delay);
	}
	
	@EventTarget
	public void onTick(EventTick e){
		
		Client.getClient().getMinecraft().playerController.blockHitDelay = 0;
		
		if(timer.output()){
			Client.getClient().getMinecraft().playerController.curBlockDamageMP += 0.05f;
		}
	}
	
	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		Client.getClient().getMinecraft().playerController.blockHitDelay = 5;
	}
}
