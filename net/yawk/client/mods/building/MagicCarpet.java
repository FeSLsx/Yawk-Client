package net.yawk.client.mods.building;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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

@RegisterMod(name = "MagicCarpet", desc = "Place blocks underneath you", type = Mod.Type.BUILDING)
public class MagicCarpet extends Mod{
	
	public MagicCarpet(){
		
	}
	
	@EventTarget
	public void onTick(EventTick e){

		if(Client.getClient().getPlayer().getHeldItem() != null){
			
			int playerX = (int) Client.getClient().getPlayer().posX;
			int playerY = (int) (Client.getClient().getPlayer().posY - 2);
			int playerZ = (int) Client.getClient().getPlayer().posZ;
			
			for(int x = -2; x < 2; x++){
				for(int z = -2; z < 2; z++){
					
					BlockPos pos = new BlockPos(playerX+x, playerY, playerZ+z);
					
					Block block = Client.getClient().getMinecraft().theWorld.getBlockState(pos).getBlock();
					
					if(block == Blocks.air || block == Blocks.tallgrass){
						place(pos);
					}
				}
			}
		}
	}
	
	private void place(BlockPos pos){
		
		ClientUtils.sendPacket(new C08PacketPlayerBlockPlacement(pos,
				EnumFacing.UP.getIndex(),
				Client.getClient().getPlayer().getHeldItem(),
				pos.getX(),
				pos.getY(),
				pos.getZ()));
	}
	
}
