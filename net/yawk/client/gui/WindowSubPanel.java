package net.yawk.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.yawk.client.Client;
import net.yawk.client.utils.GuiUtils;

public class WindowSubPanel implements IPanel{

	private List<AbstractComponent> components;
	private Window win;
	
	public WindowSubPanel(Window win){
		this.win = win;
		components = new ArrayList<AbstractComponent>();
	}
	
	public void draw(int x, int y, int posX, int posY){
		
		int h = 0;
		
		for(AbstractComponent c : components){
			h += c.getHeight();
		}
		
		GuiUtils.drawRect(posX+2, posY, posX+win.getWidth()-2, posY+h, 0x3FFFFFFF);
		
		h = 0;
		
		for(AbstractComponent c : components){
			c.draw(x, y, posX+2, posY+h);
			h += c.getHeight();
		}
	}
	
	public void addComponent(AbstractComponent comp){
		components.add(comp);
	}
	
	public void mouseClicked(int x, int y, int posX, int posY) {
		
		int h = 0;
		
		for(AbstractComponent c : components){
			c.mouseClicked(x, y, posX+2, posY+h);
			h += c.getHeight();
		}
	}
	
	@Override
	public int getWidth() {
		return win.getWidth()-4;
	}

	@Override
	public int getHeight() {
		
		int h = 0;
		
		for(AbstractComponent c : components){
			h += c.getHeight();
		}
		
		return h;
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		
		for(AbstractComponent c : components){
			c.mouseReleased(mouseX, mouseY, state);
		}
	}
	
}
