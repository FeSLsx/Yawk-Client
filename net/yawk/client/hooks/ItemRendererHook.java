package net.yawk.client.hooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.yawk.client.cameras.Camera;

public class ItemRendererHook extends ItemRenderer{

	public ItemRendererHook(Minecraft mcIn) {
		super(mcIn);
	}
	
	@Override
    public void renderFireInFirstPerson(float f){
		
    	if(!Camera.isCapturing()){
    		super.renderFireInFirstPerson(f);
    	}
    }

}
