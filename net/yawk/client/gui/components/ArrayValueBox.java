package net.yawk.client.gui.components;

import net.yawk.client.Client;
import net.yawk.client.gui.ColourType;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;
import net.yawk.client.modmanager.values.ArrayValue;

public class ArrayValueBox extends AbstractComponent{
	
	private IPanel win;
	private ArrayValue val;
	
	public ArrayValueBox(IPanel win, ArrayValue val) {
		super();
		this.win = win;
		this.val = val;
	}
	
	@Override
	public void draw(int x, int y, int cx, int cy) {
		
		Client.getClient().getFontRenderer().drawStringWithShadow("<",
				cx + 3,
				cy + 2,
				mouseOverLeft(x,y,cx,cy)? ColourType.TEXT.getModifiedColour():ColourType.TEXT.getColour(),
				true);
		
		Client.getClient().getFontRenderer().drawStringWithShadow(">",
				cx + win.getWidth() - 3 - Client.getClient().getFontRenderer().getStringWidth(">"),
				cy + 2,
				mouseOverRight(x,y,cx,cy)? ColourType.TEXT.getModifiedColour():ColourType.TEXT.getColour(),
				true);
		
		String whole = val.getSelectedMode();
		
		Client.getClient().getFontRenderer().drawStringWithShadow(whole,
				cx + win.getWidth()/2 - Client.getClient().getFontRenderer().getStringWidth(whole)/2,
				cy + 2,
				ColourType.TEXT.getColour(),
				true);
	}
	
	@Override
	public void mouseClicked(int x, int y, int cx, int cy) {
		
		if(mouseOverLeft(x, y, cx, cy)){
			
			Client.getClient().getMinecraft().thePlayer.playSound("random.click", 1, 1);
			
			val.increment();
			
		}else if(mouseOverRight(x, y, cx, cy)){
			
			Client.getClient().getMinecraft().thePlayer.playSound("random.click", 1, 1);
			
			val.decrement();
		}
	}
	
	private boolean mouseOverLeft(int x, int y, int cx, int cy){
		return x > cx && x < cx+win.getWidth()/2 && y > cy && y < cy+getHeight();
	}
	
	private boolean mouseOverRight(int x, int y, int cx, int cy){
		return x > cx+win.getWidth()/2 && x < cx+win.getWidth() && y > cy && y < cy+getHeight();
	}
		
	public String getSelectedOption(){
		return val.getSelectedMode();
	}
	
	@Override
	public int getHeight() {
		return 12;
	}
}
