package net.yawk.client.gui.hub;

import java.io.IOException;

import net.yawk.client.Client;
import net.yawk.client.gui.Canvas;
import net.yawk.client.gui.IScalerPosition;

public abstract class ComponentSlate extends Slate{

	private Client client;
	protected Canvas options;
	private GuiHub hub;
	
	public ComponentSlate(String name, final GuiHub hub, Client client) {
		super(name, null, hub);
		this.client = client;
		this.hub = hub;
		
		IScalerPosition pos = new IScalerPosition(){
			
			@Override
			public int getX() {
				return hub.width/2 - 150;
			}
			
			@Override
			public int getY() {
				return hub.height/2 - options.getHeight()/2;
			}
			
		};
		
		options = new Canvas(pos, 300);
	}

	@Override
	public void renderSlate(int x, int y) {
		
		options.draw(x, y);
	}
	
	@Override
	public void mouseClicked(int x, int y) {
		options.mouseClicked(x, y);
	}
	
	@Override
	public void mouseReleased(int x, int y, int state) {
		options.mouseReleased(x, y, state);
	}
	
	@Override
	public void keyTyped(char c, int key) throws IOException {
		options.keyPress(c, key);
	}
	
	public abstract void init();
}
