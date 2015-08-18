package net.yawk.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.yawk.client.utils.GuiUtils;

public class Canvas implements IPanel{
	
	private IScalerPosition pos;
	private int width, height;
	protected List<AbstractComponent> components = new CopyOnWriteArrayList<AbstractComponent>();
	
	public Canvas(IScalerPosition pos, int width) {
		this.pos = pos;
		this.width = width;
	}
	
	public void draw(int x, int y){
		
		int posX = pos.getX();
		int posY = pos.getY();
		
		int h = 0;
		
		for(AbstractComponent comp : components){
			h += comp.getHeight();
		}
		
		this.height = h;
		GuiUtils.drawRect(posX, posY, posX+width, posY+h, 0x5F5F5F5F);
		
		h = 0;
		
		for(AbstractComponent comp : components){
			comp.draw(x, y, posX, posY+h);
			h += comp.getHeight();
		}
	}
	
	public void mouseClicked(int x, int y) {
		
		int posX = pos.getX();
		int posY = pos.getY();
		
		int h = 0;
		
		for (AbstractComponent comp : components){
			comp.mouseClicked(x, y, posX, posY+h);
			h += comp.getHeight();
		}
	}
	
	public void keyPress(char c, int key) {
				
		for(AbstractComponent comp : components){
			comp.keyPress(key, c);
		}
	}
	
	public void mouseReleased(int x, int y, int state) {
		
		int h = 0;
		
		for(AbstractComponent comp : components){
			comp.mouseReleased(x, y, state);
			h += comp.getHeight();
		}
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public void addComponent(AbstractComponent c){
		this.components.add(c);
	}
	
	public void clearComponents(){
		this.components.clear();
	}
}
