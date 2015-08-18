package net.yawk.client.mods.building;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.yawk.client.Client;
import net.yawk.client.events.EventSendPacket;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.BooleanValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "FastPlace", desc = "Place blocks faster", type = Mod.Type.BUILDING)
public class FastPlace extends Mod{
	
	private static SliderValue delay;
	
	public FastPlace(){
		
		super(new AbstractValue[]{
				delay = new SliderValue("Place delay", "fastplace.delay", Client.getClient().getValuesRegistry(), 2, 0, 5, true),
		});
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket e){
		if(e.packet instanceof C08PacketPlayerBlockPlacement){
			Client.getClient().getMinecraft().rightClickDelayTimer = delay.getValue().intValue();
		}
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
}
