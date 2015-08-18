package net.yawk.client.gui.components;

import net.minecraft.util.EnumChatFormatting;
import net.yawk.client.Client;
import net.yawk.client.gui.ColourType;
import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.Window;
import net.yawk.client.utils.Colours;

public class NumberSelector extends AbstractComponent{
	
	protected Window win;
	private int number;
	private int max;
	private int min;
	private int increment;
	private String name;
	private String unit = "";
	
	public NumberSelector(Window win, String name, int min, int max, int start, int increment){
		this.win = win;
		this.name = name;
		this.min = min;
		this.max = max;
		this.number = start;
		this.increment = increment;
	}
	
	public NumberSelector(Window win, String name, int min, int max, int start, int increment, String unit){
		this.win = win;
		this.name = name;
		this.min = min;
		this.max = max;
		this.number = start;
		this.increment = increment;
		this.unit = unit;
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
		
		String whole = name + ": " + number + unit;
		
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
			
			number-=increment;
			
			if(number < min){
				number = min;
			}
			
		}else if(mouseOverRight(x, y, cx, cy)){
			
			Client.getClient().getMinecraft().thePlayer.playSound("random.click", 1, 1);
			
			number+=increment;
			
			if(number > max){
				number = max;
			}
		}
	}
	
	private boolean mouseOverLeft(int x, int y, int cx, int cy){
		return x > cx && x < cx+win.getWidth()/2 && y > cy && y < cy+getHeight();
	}
	
	private boolean mouseOverRight(int x, int y, int cx, int cy){
		return x > cx+win.getWidth()/2 && x < cx+win.getWidth() && y > cy && y < cy+getHeight();
	}
	
	@Override
	public int getHeight() {
		return 12;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
