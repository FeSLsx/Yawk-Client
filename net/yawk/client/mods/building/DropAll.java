package net.yawk.client.mods.building;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.yawk.client.Client;
import net.yawk.client.events.EventMoveEntity;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.timing.MillisecondTimer;
import net.yawk.client.utils.timing.ValueTimer;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "DropAll", desc = "Drop all your items", type = Mod.Type.BUILDING)
public class DropAll extends Mod{
	
	private static SliderValue delay;
	
	public DropAll(){
		
		//TODO: Make this hack much better
		
		super(new AbstractValue[]{
				delay = new SliderValue("Drop Delay", "dropall.delay", Client.getClient().getValuesRegistry(), 2, 0, 100, true),
		});
		
		timer = new ValueTimer(delay);
	}
	
	private int slotProgress;
	private int inventoryLine;
	private boolean hasTransfered;
	private MillisecondTimer timer;
	
	@EventTarget
	public void onTick(EventTick e){
		
		if(timer.output()){
			
			if(slotProgress > 8){
				slotProgress = 0;
			}
			
			for (int slot = 44; slot >= 9; slot--)
			{
				for(int fillingSlot = 0; fillingSlot < 9; fillingSlot++){
					if(!Client.getClient().getPlayer().inventoryContainer.getSlot(fillingSlot).getHasStack()){
						Minecraft.getMinecraft().playerController.windowClick(0, slot, 0, 0, Client.getClient().getPlayer());
						Minecraft.getMinecraft().playerController.windowClick(0, fillingSlot, 0, 0, Client.getClient().getPlayer());
						Minecraft.getMinecraft().playerController.updateController();
						break;
					}
				}
			}
			
			ClientUtils.sendPacket(new C09PacketHeldItemChange(slotProgress));
			ClientUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
			
			slotProgress++;
		}
	}
	
	@Override
	public void onEnable() {
		slotProgress = 0;
		inventoryLine = 0;
	}
	
	@Override
	public void onDisable() {
		
	}
}
