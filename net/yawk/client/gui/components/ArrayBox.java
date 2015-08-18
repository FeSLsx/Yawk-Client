package net.yawk.client.gui.components;

import net.yawk.client.Client;
import net.yawk.client.gui.ColourType;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;
import net.yawk.client.gui.Window;

//TODO: Maybe merge this with the NumberSelector
public class ArrayBox<T> extends AbstractComponent{
	
	private T[] options;
	private int index;
	private IPanel win;
	
	public ArrayBox(IPanel win, T[] options) {
		super();
		this.win = win;
		this.options = options;
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
		
		String whole = options[index].toString();
		
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
			
			index--;
			
			if(index < 0){
				index = options.length-1;
			}
			
		}else if(mouseOverRight(x, y, cx, cy)){
			
			Client.getClient().getMinecraft().thePlayer.playSound("random.click", 1, 1);
			
			index++;
			
			if(index >= options.length){
				index = 0;
			}
		}
	}
	
	private boolean mouseOverLeft(int x, int y, int cx, int cy){
		return x > cx && x < cx+win.getWidth()/2 && y > cy && y < cy+getHeight();
	}
	
	private boolean mouseOverRight(int x, int y, int cx, int cy){
		return x > cx+win.getWidth()/2 && x < cx+win.getWidth() && y > cy && y < cy+getHeight();
	}
	
	public T[] getOptions() {
		return options;
	}
	
	public void setOptions(T[] options) {
		this.options = options;
	}
	
	public T getSelectedOption(){
		return options[index];
	}
	
	@Override
	public int getHeight() {
		return 12;
	}
}
