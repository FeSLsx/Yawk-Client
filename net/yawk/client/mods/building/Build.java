package net.yawk.client.mods.building;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.yawk.client.Client;
import net.yawk.client.events.EventClickBlock;
import net.yawk.client.events.EventPlaceBlock;
import net.yawk.client.events.EventSendPacket;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.ArrayValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Build", desc = "Automatically build structures", type = Mod.Type.BUILDING)
public class Build extends Mod{
	
	private static ArrayValue modeValue;
	
	public Build(){

		super(new AbstractValue[]{
				modeValue = new ArrayValue("Mode", "build.mode", Client.getClient().getValuesRegistry(), 0, new String[]{
					"Floor",
					"Pole",
					"Swastika",
				}),
		});
	}
	
	@EventTarget
	public void onPlaceBlock(EventPlaceBlock e){
		
		switch(modeValue.getValue()){
		case 0: floor(e);
		break;
		case 1: pole(e);
		break;
		case 2: swastika(e);
		break;
		};
	}
	
	private void floor(EventPlaceBlock e){
		
		place(e.getPos().getX()-1, e.getPos().getY(), e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()-1, e.getPos().getY(), e.getPos().getZ()-1, e.getFacing());
		place(e.getPos().getX()-1, e.getPos().getY(), e.getPos().getZ()+1, e.getFacing());
		
		place(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()-1, e.getFacing());
		place(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()+1, e.getFacing());
		
		place(e.getPos().getX()+1, e.getPos().getY(), e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()+1, e.getPos().getY(), e.getPos().getZ()-1, e.getFacing());
		place(e.getPos().getX()+1, e.getPos().getY(), e.getPos().getZ()+1, e.getFacing());
	}
	
	private void pole(EventPlaceBlock e){
		
		place(e.getPos().getX(), e.getPos().getY()+1, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX(), e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX(), e.getPos().getY()+3, e.getPos().getZ(), e.getFacing());
	}
	
	private void swastika(EventPlaceBlock e){
		
		place(e.getPos().getX(), e.getPos().getY()+1, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX(), e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		
		place(e.getPos().getX()-1, e.getPos().getY(), e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()-1, e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()-2, e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()-2, e.getPos().getY()+3, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX(), e.getPos().getY()+3, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX(), e.getPos().getY()+4, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()+1, e.getPos().getY()+4, e.getPos().getZ(), e.getFacing());
		
		place(e.getPos().getX()+1, e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()+2, e.getPos().getY()+2, e.getPos().getZ(), e.getFacing());
		place(e.getPos().getX()+2, e.getPos().getY()+1, e.getPos().getZ(), e.getFacing());
	}
		
	private void place(int x, int y, int z, EnumFacing facing){
		
		ClientUtils.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(x, y, z),
				facing.getIndex(),
				Client.getClient().getPlayer().getHeldItem(),
				x,
				y,
				z));
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
}
