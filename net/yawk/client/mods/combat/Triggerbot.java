package net.yawk.client.mods.combat;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.yawk.client.Client;
import net.yawk.client.events.EventGuiRender;
import net.yawk.client.events.EventTick;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.RegisterMod;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.CombatUtils;
import net.yawk.client.utils.timing.MillisecondTimer;
import net.yawk.client.utils.timing.ValueTimer;

import com.darkmagician6.eventapi.EventTarget;

@RegisterMod(name = "Triggerbot", desc = "Hit the player you're looking at", type = Mod.Type.COMBAT)
public class Triggerbot extends Mod{
	
	private MillisecondTimer timer;
	private static SliderValue delay;
	
	public Triggerbot(){
		
		super(new AbstractValue[]{
				delay = new SliderValue("Hit Delay", "triggerbot.hitdelay", Client.getClient().getValuesRegistry(), 120, 0, 1000, true),
		});
		
		timer = new ValueTimer(delay);
	}
	
	@EventTarget
	public void onTick(EventTick e){
		if(Client.getClient().getMinecraft().objectMouseOver != null && Client.getClient().getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.ENTITY){
			Entity entity = Client.getClient().getMinecraft().objectMouseOver.entityHit;
			if(CombatUtils.isAttackable(entity) && timer.output()){
				CombatUtils.hit(entity);
			}
		}
	}

	@Override
	public void onEnable() {
		timer.reset();
	}

	@Override
	public void onDisable() {
		
	}
}
