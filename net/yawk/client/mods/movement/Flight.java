package net.yawk.client.mods.movement;

import net.yawk.client.Client;
import net.yawk.client.events.*;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Flight", defaultKey = Keyboard.KEY_R, desc = "Fly in the air", type = Mod.Type.MOVEMENT)
public class Flight extends Mod{
	
	private static SliderValue speed;
	
	public Flight(){
		
		super(new AbstractValue[]{
				speed = new SliderValue("Fly Speed", "flight.speed", Client.getClient().getValuesRegistry(), 0.5, 0, 3, false),
		});
	}
	
	@EventTarget
	public void onTick(EventTick e){
		fly();
	}
	
	public static void fly(){
		
		Client.getClient().getPlayer().onGround = true;
		
		if(Client.getClient().getMinecraft().inGameHasFocus){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				Client.getClient().getPlayer().motionY = 0.5d;
			}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				Client.getClient().getPlayer().motionY = -0.5d;
			}else{
				Client.getClient().getPlayer().motionY = 0;
			}
			
			float yaw = Client.getClient().getPlayer().rotationYaw + 90;
			
			boolean keyW = Keyboard.isKeyDown(Keyboard.KEY_W), 
					keyS = Keyboard.isKeyDown(Keyboard.KEY_S), 
					keyA = Keyboard.isKeyDown(Keyboard.KEY_A), 
					keyD = Keyboard.isKeyDown(Keyboard.KEY_D);
			
			if(keyW)
			{
				if(keyA)
				{
					yaw -= 45;
				}else if(keyD){
					yaw += 45;
				}
			}else if(keyS){
				yaw += 180;
				if(keyA)
				{
					yaw += 45;
				}else if(keyD){
					yaw -= 45;
				}
			}else if(keyA){
				yaw -= 90;
			}else if(keyD){
				yaw += 90;
			}
			
			if(keyW || keyA || keyS || keyD)
			{
				Client.getClient().getPlayer().motionX = Math.cos(Math.toRadians(yaw))*speed.getValue();
				Client.getClient().getPlayer().motionZ = Math.sin(Math.toRadians(yaw))*speed.getValue();
			}else{
				Client.getClient().getPlayer().motionX = 0;
				Client.getClient().getPlayer().motionZ = 0;
			}
		}else{
			Client.getClient().getPlayer().motionY = 0;
		}

	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		
	}
}
