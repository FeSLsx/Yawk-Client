package net.yawk.client.hooks;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.util.EnumChatFormatting;
import net.yawk.client.Client;
import net.yawk.client.cameras.Camera;
import net.yawk.client.events.EventGuiRender;
import net.yawk.client.gui.GuiClickable;
import net.yawk.client.gui.Window;
import net.yawk.client.gui.maps.LargeMap;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.mods.world.HideClient;
import net.yawk.client.utils.Colours;

public class GuiIngameHook extends GuiIngame{
	
	public GuiIngameHook(Minecraft mc) {
		super(mc);
		
		Client.setClient(new Client(mc));
		Client.getClient().init();
		
		hideClientMod = Client.getClient().getHideClientMod();
	}
	
	private Mod hideClientMod;
	
	private EventGuiRender eventGuiRender = new EventGuiRender();
	
	private Camera camera = new Camera();
	
	@Override
	public void func_175180_a(float p_175180_1_){

		super.func_175180_a(p_175180_1_);
		
		if(!hideClientMod.isEnabled()){
			Client.getClient().getFontRenderer().drawStringWithShadow("Yawk" + EnumChatFormatting.GREEN + " v2.5" + EnumChatFormatting.LIGHT_PURPLE + " ("+(Client.getClient().getSession().isPremium()? "Premium":"Beta")+")", 3, 2, 0xFFFFFFFF, true);
		}

		if(Client.getClient().getMinecraft().currentScreen == null){
			for(Window win : Client.getClient().gui.windows){
				if(win.pinned){
					win.renderWindow(0, 0);
				}
			}
		}

		EventManager.call(eventGuiRender);

		camera.cameraPosX = Client.getClient().getPlayer().posX;
		camera.cameraPosY = Client.getClient().getPlayer().posY;
		camera.cameraPosZ = Client.getClient().getPlayer().posZ;

		camera.cameraRotationYaw = Client.getClient().getPlayer().rotationYaw;
		camera.cameraRotationPitch = Client.getClient().getPlayer().rotationPitch;

		camera.draw(0, 0, 180, 100);

		camera.updateFramebuffer();
	}
}
