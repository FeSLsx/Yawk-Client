package net.yawk.client.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventDamageBlock extends EventCancellable{
	
	public BlockPos pos;
	public EnumFacing facing;
	
	public EventDamageBlock(BlockPos pos, EnumFacing facing) {
		this.pos = pos;
		this.facing = facing;
	}
}
