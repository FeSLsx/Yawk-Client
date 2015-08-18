package net.yawk.client.mods.combat;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.yawk.client.Client;
import net.yawk.client.events.EventGuiRender;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.BooleanValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.CombatUtils;
import net.yawk.client.utils.timing.MillisecondTimer;
import net.yawk.client.utils.timing.ValueTimer;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "KillAura", desc = "Kill people near you", type = Mod.Type.COMBAT)
public class KillAura extends Mod{
	
	private MillisecondTimer timer;
	private MillisecondTimer rotTimer;
	
	private static SliderValue delay;
	private static SliderValue rotDelay;
	private static SliderValue range;
	private static BooleanValue silent;
	private static BooleanValue smooth;
	
	public KillAura(){
		
		super(new AbstractValue[]{
				delay = new SliderValue("Hit Delay", "killaura.hitdelay", Client.getClient().getValuesRegistry(), 120, 0, 2000, true),
				range = new SliderValue("Range", "killaura.range", Client.getClient().getValuesRegistry(), 3.95, 0, 6, true),
				silent = new BooleanValue("Silent", "killaura.silent", Client.getClient().getValuesRegistry(), false),
				smooth = new BooleanValue("Smooth", "killaura.smooth", Client.getClient().getValuesRegistry(), false),
				rotDelay = new SliderValue("Rot Delay", "killaura.rotdelay", Client.getClient().getValuesRegistry(), 20, 0, 100, true),
		});
		
		timer = new ValueTimer(delay);
		rotTimer = new ValueTimer(rotDelay);
	}
	
	@EventTarget
	public void onTick(EventTick e){
		
		EntityPlayer player = CombatUtils.getClosestPlayer(range.getValue());
		
		if (player != null) {
			
			if(timer.output()){
				if(smooth.getValue()){
					if(CombatUtils.faceEntitySmooth(player, false, silent.getValue())){
						CombatUtils.hit(player);
					}
				}else{
					CombatUtils.faceEntity(player, silent.getValue());
					CombatUtils.hit(player);
				}
			}
			
			if (smooth.getValue() && rotTimer.output()) {
				CombatUtils.faceEntitySmooth(player, true, silent.getValue());
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
