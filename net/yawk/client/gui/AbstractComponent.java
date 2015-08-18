package net.yawk.client.gui;

public abstract class AbstractComponent {

	public void draw(int x, int y, int cx, int cy){
		
	}
	
	public void keyPress(int key, char c){
		
	}
	
	public void mouseClicked(int x, int y, int cx, int cy){
		
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
		
	public int getWidth(){
		return 85;
	}
	
	public abstract int getHeight();
	
}
