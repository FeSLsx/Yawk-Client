package net.yawk.client.gui.components;

import net.yawk.client.Client;
import net.yawk.client.cameras.Camera;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.GuiClickable;
import net.yawk.client.gui.IPanel;
import net.yawk.client.gui.Window;
import net.yawk.client.utils.GuiUtils;

public class CameraDisplay extends AbstractComponent{
	
	private Window window;
	private Camera camera;
	private boolean draggingHeight, draggingWidth;
	
	public CameraDisplay(Window window, Camera camera) {
		super();
		this.window = window;
		this.camera = camera;
	}
	
	@Override
	public void draw(int x, int y, int cx, int cy) {
		
		if(draggingWidth){
			
			camera.setWidth((x - cx) * 2);
			
			if(camera.getWidth() > 800){
				camera.setWidth(800);
			}
			
			if(camera.getWidth() < 120){
				camera.setWidth(120);
			}
		}
		
		if(draggingHeight){
			
			camera.setHeight((y - cy) * 2);
			
			if(camera.getHeight() > 600){
				camera.setHeight(600);
			}
			
			if(camera.getHeight() < 60){
				camera.setHeight(60);
			}
		}
		
		if(camera.isFrameBufferUpdated()){
			camera.draw(cx, cy, cx+camera.getWidth()/2, cy+camera.getHeight()/2);
		}else{
			
			String closeMessage = "Close GUI";
			String updateMessage = "to update";
			
			Client.getClient().getFontRenderer().drawString(closeMessage,
					cx+camera.getWidth()/4 - Client.getClient().getFontRenderer().getStringWidth(closeMessage)/2,
					cy+camera.getHeight()/4 - Client.getClient().getFontRenderer().FONT_HEIGHT/2 - 6,
					0xFFFFFFFF);
			
			Client.getClient().getFontRenderer().drawString(updateMessage,
					cx+camera.getWidth()/4 - Client.getClient().getFontRenderer().getStringWidth(updateMessage)/2,
					cy+camera.getHeight()/4 - Client.getClient().getFontRenderer().FONT_HEIGHT/2 + 6,
					0xFFFFFFFF);
		}
		
		if(Client.getClient().getMinecraft().currentScreen instanceof GuiClickable){
			GuiUtils.drawRect(cx+camera.getWidth()/4 - 15, cy+camera.getHeight()/2, cx+camera.getWidth()/4 + 15, cy+camera.getHeight()/2 + 4, 0x5FFFFFFF);
			GuiUtils.drawRect(cx+camera.getWidth()/2, cy+camera.getHeight()/4-15, cx+camera.getWidth()/2+4, cy+camera.getHeight()/4+15, 0x5FFFFFFF);
		}
	}
	
	@Override
	public void mouseClicked(int x, int y, int cx, int cy) {
		
		if(mouseOverWidthSlider(x, y, cx, cy)){
			draggingWidth = true;
		}
		
		if(mouseOverHeightSlider(x, y, cx, cy)){
			draggingHeight = true;
		}
		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
		if(draggingWidth || draggingHeight){
			camera.makeNewFrameBuffer();
		}
		
		draggingWidth = false;
		draggingHeight = false;
	}
	
	private boolean mouseOverHeightSlider(int mouseX, int mouseY, int cx, int cy){
		return mouseX > cx+camera.getWidth()/4-15 && mouseX <= cx+camera.getWidth()/4+15 && mouseY > cy+camera.getHeight()/2 && mouseY <= cy+camera.getHeight()/2+5;
	}
	
	private boolean mouseOverWidthSlider(int mouseX, int mouseY, int cx, int cy){
		return mouseX > cx+camera.getWidth()/2 && mouseX <= cx+camera.getWidth()/2+5 && mouseY > cy+camera.getHeight()/4-15 && mouseY <= cy+camera.getHeight()/4+15;
	}
	
	@Override
	public int getWidth() {
		return camera.getWidth()/2;
	}
	
	@Override
	public int getHeight() {
		return camera.getHeight()/2;
	}

}
