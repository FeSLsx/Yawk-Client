package net.yawk.client.gui.components.buttons;

import java.util.ArrayList;
import java.util.List;

import net.yawk.client.Client;
import net.yawk.client.gui.Canvas;
import net.yawk.client.gui.ColourType;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.Window;
import net.yawk.client.gui.WindowSubPanel;
import net.yawk.client.modmanager.Mod;
import net.yawk.client.modmanager.values.AbstractValue;
import net.yawk.client.utils.GuiUtils;

public class OptionsModButton extends ModButton{
	
	private boolean extended;
	private WindowSubPanel panel;
	
	public OptionsModButton(Window win, Mod mod) {
		super(win, mod);
		
		panel = new WindowSubPanel(win);
		
		for(AbstractValue option : mod.getOptions()){
			panel.addComponent(option.getComponent(panel));
		}
	}
	
	@Override
	public void draw(int x, int y, int cx, int cy) {
		
		if(mod.isEnabled()){
			Client.getClient().getFontRenderer().drawStringWithShadow(getText(),
					cx+3,
					cy+2,
					mouseOverButton(x, y, cx, cy)? ColourType.HIGHLIGHT.getModifiedColour():ColourType.HIGHLIGHT.getColour(),
					true);
		}else{
			Client.getClient().getFontRenderer().drawStringWithShadow(getText(),
					cx+3,
					cy+2,
					mouseOverButton(x, y, cx, cy)? ColourType.TEXT.getModifiedColour():ColourType.TEXT.getColour(),
					true);
		}
		
		GuiUtils.drawRect(cx+win.getWidth()-10, cy+2, cx+win.getWidth()-2, cy+10, extended? 0x4FFFFFFF:0x2FFFFFFF);
		
		if(extended){
			panel.draw(x, y, cx, cy+12);
		}
	}

	@Override
	public void mouseClicked(int x, int y, int cx, int cy) {
		
		if(mouseOverExtendButton(x, y, cx, cy)){
			extended = !extended;
			Client.getClient().getMinecraft().thePlayer.playSound("random.click", 1, 1);
		}else{
			super.mouseClicked(x, y, cx, cy);
		}
		
		if(extended){
			panel.mouseClicked(x, y, cx, cy+12);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if(extended){
			panel.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	public boolean mouseOverExtendButton(int x, int y, int cx, int cy){
		return x >= cx+win.getWidth()-10 && x <= cx+win.getWidth()-2 && y >= cy+2 && y <= cy+10;
	}
	
	@Override
	public boolean mouseOverButton(int x, int y, int cx, int cy){
		return x > cx && x < cx+win.getWidth() && y > cy && y < cy+12;
	}
	
	@Override
	public int getHeight() {
		
		if(extended){			
			return super.getHeight() + panel.getHeight() + 1;
		}else{
			return super.getHeight();
		}
	}
}