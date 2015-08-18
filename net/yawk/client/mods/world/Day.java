package net.yawk.client.mods.world;

import javax.vecmath.Vector3d;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldSettings.GameType;
import net.yawk.client.Client;
import net.yawk.client.events.EventMoveEntity;
import net.yawk.client.events.EventRecievePacket;
import net.yawk.client.events.EventSendPacket;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.ArrayValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Day", desc = "Set the time to day", type = Mod.Type.WORLD)
public class Day extends Mod{
	
	private static SliderValue time;
	
	public Day(){
		
		super(new AbstractValue[]{
				time = new SliderValue("World time", "day.time", Client.getClient().getValuesRegistry(), 1000, 0, 23000, false),
		});
	}
	
	@EventTarget
	public void onTick(EventTick e){
		Client.getClient().getMinecraft().theWorld.getWorldInfo().setWorldTime(time.getValue().intValue());
	}
	
	@EventTarget
	public void onRecievePacket(EventRecievePacket e){
		if(e.packet instanceof S03PacketTimeUpdate){
			e.setCancelled(true);
		}
	}
}
