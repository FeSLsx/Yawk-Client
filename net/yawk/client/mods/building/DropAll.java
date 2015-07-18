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
import net.yawk.client.modmanager.ModDetails;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.MillisecondTimer;

import com.darkmagician6.eventapi.EventTarget;

@ModDetails(name = "DropAll", desc = "Drop all your items", type = Mod.Type.BUILDING)
public class DropAll extends Mod{
		
	public DropAll(){
		//TODO: Make this hack much better
	}
	
	private int slotProgress;
	private int inventoryLine;
	private boolean hasTransfered;
	private MillisecondTimer timer = new MillisecondTimer(2);
	
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
