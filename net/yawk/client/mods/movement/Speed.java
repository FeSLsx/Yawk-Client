package net.yawk.client.mods.movement;

import java.util.Random;

import net.minecraft.item.EnumAction;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.yawk.client.Client;
import net.yawk.client.events.*;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.ArrayValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@RegisterMod(name = "Speed", desc = "Move faster", type = Mod.Type.MOVEMENT)
public class Speed extends Mod{
	
	private static SliderValue speed;
	private static ArrayValue modeValue;
	
	public Speed(){

		super(new AbstractValue[]{
				speed = new SliderValue("Walk Speed", "speed.walkspeed", Client.getClient().getValuesRegistry(), 2, 0, 5, false),
						modeValue = new ArrayValue("Mode", "speed.mode", Client.getClient().getValuesRegistry(), 0, new String[]{
							"Default",
							"NCP Bypass",
						}),
		});
	}
	
	@EventTarget
	public void onMove(EventMoveEntity e){
		
		if(e.type == EventType.PRE){
			
			if(modeValue.getValue() == 0){
				
				e.x *= speed.getValue();
				e.z *= speed.getValue();
				
				if(e.y > 0){
					e.y *= speed.getValue();
				}
				
			}else{
				
				Client.getClient().getPlayer().motionY =- 999;
				Client.getClient().getMinecraft().gameSettings.keyBindJump.pressed = !Client.getClient().getMinecraft().gameSettings.keyBindJump.pressed;    
				
				Client.getClient().getPlayer().setSprinting(Client.getClient().getPlayer().moveForward > 0
						&& Client.getClient().getPlayer().getFoodStats().getFoodLevel() > 4);
			}
		}
	}
		
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
}
