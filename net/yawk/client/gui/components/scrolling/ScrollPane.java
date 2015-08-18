package net.yawk.client.gui.components.scrolling;

import java.util.ArrayList;
import java.util.List;

import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;
import net.yawk.client.gui.Window;
import net.yawk.client.utils.GuiUtils;
import net.yawk.client.utils.Scissor;

import org.lwjgl.opengl.GL11;

public class ScrollPane extends AbstractComponent implements IPanel{
	
	protected List<AbstractComponent> components = new ArrayList<AbstractComponent>();
	protected int height, dragged, mouseYOffset, BAR_HEIGHT = 12, BAR_WIDTH = 4;
	private boolean dragging;
	protected IPanel win;
	
	public ScrollPane(IPanel win, int height){
		this.win = win;
		this.height = height;
	}
	
	@Override
	public void draw(int x, int y, int cx, int cy) {
		
		if(dragging){
			
			dragged = y - cy + mouseYOffset;
			
			if(dragged > height-BAR_HEIGHT){
				dragged = height-BAR_HEIGHT;
			}
			
			if(dragged < 0){
				dragged = 0;
			}
		}
		
		GuiUtils.drawRect(cx + win.getWidth() - BAR_WIDTH - 1, cy, cx + win.getWidth(), cy + height, 0x1FCFCFCF);
		
		GuiUtils.drawRect(cx + win.getWidth() - BAR_WIDTH - 1, cy + dragged, cx + win.getWidth(), cy + dragged + BAR_HEIGHT, 0x4FFFFFFF);
		
		int drag = getScrollHeight();
		
		Scissor.enableScissoring();
		Scissor.scissor(cx, cy, win.getWidth(), height);
		
		GL11.glTranslatef(0, -drag, 0);
		
		int h = 0;
		
		if(isWithinScrollPane(x, y, cx, cy)){
			for(AbstractComponent c : components){
				c.draw(x, y + drag, cx, cy+h);
				h += c.getHeight();
			}
		}else{
			for(AbstractComponent c : components){
				c.draw(-999, -999, cx, cy+h);
				h += c.getHeight();
			}
		}
		
		GL11.glTranslatef(0, drag, 0);
		
		Scissor.disableScissoring();
	}
	
	public int getScrollHeight(){
		return (int) ((float)(dragged/(float)(height-BAR_HEIGHT)) * (getComponentsHeight()-height));
	}
	
	public int getComponentsHeight(){
		
		int h = 0;
		
		for(AbstractComponent c : components){
			h += c.getHeight();
		}
		
		return h;
	}
	
	public boolean mouseOverBar(int x, int y, int cx, int cy){
		return x >= cx + win.getWidth() - BAR_WIDTH - 1 && x < cx + win.getWidth() && y > cy + dragged && y <= cy + dragged + BAR_HEIGHT;
	}
	
	public boolean mouseOverBarArea(int x, int y, int cx, int cy){
		return x >= cx + win.getWidth() - BAR_WIDTH - 1 && x <= cx + win.getWidth() && y >= cy && y <= cy + height;
	}
	
	private boolean isWithinScrollPane(int x, int y, int cx, int cy){
		return x > cx && x < cx+win.getWidth() - BAR_WIDTH - 1 && y > cy && y < cy + height;
	}
	
	@Override
	public void mouseClicked(int x, int y, int cx, int cy) {
		if(mouseOverBar(x, y, cx, cy)){
			dragging = true;
			
			mouseYOffset = (cy + dragged) - y;
		}else{
			
			int h = 0;
			int drag = getScrollHeight();
			
			if(isWithinScrollPane(x, y, cx, cy)){
				for(AbstractComponent c : components){
					c.mouseClicked(x, y + drag, cx, cy+h);
					h += c.getHeight();
				}
			}else{
				for(AbstractComponent c : components){
					c.mouseClicked(-999, -999, cx, cy+h);
					h += c.getHeight();
				}
			}
		}
	}
	
	@Override
	public void keyPress(int key, char c) {
		for(AbstractComponent comp : components){
			comp.keyPress(key, c);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getWidth() {
		return win.getWidth() - BAR_WIDTH - 1;
	}
	
	public void addComponent(AbstractComponent c){
		this.components.add(c);
	}
}
