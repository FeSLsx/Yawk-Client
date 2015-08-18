package net.yawk.client.gui.hub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.yawk.client.Client;
import net.yawk.client.gui.Canvas;
import net.yawk.client.gui.IScalerPosition;
import net.yawk.client.gui.components.Slider;
import net.yawk.client.gui.components.buttons.TextButton;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.utils.ClientUtils;
import net.yawk.client.utils.GuiUtils;
import net.yawk.client.utils.timing.FlatTimer;
import net.yawk.client.utils.timing.MillisecondTimer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class GuiHub extends GuiScreen {

	public int buttonPaddingX = 30;
	public int buttonWidth = 16;
	public int buttonSlateSpace = 30;
	public int cellSize = 100;
	public int cellPadding = 5;

	private int transition, slateIndex, rotation;
	private Slate lastSlate;
	private boolean direction;

	private State state = State.IDLE;
	private String trail = "";
	private MillisecondTimer timer = new FlatTimer(5);

	public ColourModifier colourModifier;
	
	public List<Slate> slates = new ArrayList<Slate>();
	private Canvas options;
	
	private Client client;
	
	public GuiHub(Client client){
		
		this.client = client;
		this.colourModifier = new ColourModifier();
		
		IScalerPosition pos = new IScalerPosition(){

			@Override
			public int getX() {
				return width/2 - 50;
			}

			@Override
			public int getY() {
				return 3;
			}
			
		};
		
		options = new Canvas(pos, 100);
	}

	@Override
	public void drawScreen(int x, int y, float f){

		if(this.state == State.LOADING){
			drawLoadingScreen();
		}else if(this.state == State.FAILED){
			drawFailedScreen();
		}else if(this.state == State.CONNECTED){
			renderSlates(x, y);
		}
		
		options.draw(x, y);
	}

	private void updateTrail() {

		if(trail.length() >= 3){
			trail = "";
			return;
		}

		trail += ".";
	}

	public void renderSlates(int x, int y){

		if(lastSlate != null){

			if(direction){
				
				transition+=20;
				
				glTranslatef(transition, 0, 0);
				lastSlate.renderSlate(x, y);
				glTranslatef(-width, 0, 0);
				slates.get(slateIndex).renderSlate(x, y);
				glTranslatef(transition+width, 0, 0);
				
			}else{
				
				transition-=20;
				
				glTranslatef(transition, 0, 0);
				lastSlate.renderSlate(x, y);
				glTranslatef(width, 0, 0);
				slates.get(slateIndex).renderSlate(x, y);
				glTranslatef(transition-width, 0, 0);
			}
			
			if(Math.abs(transition) >= width-20){
				transition = 0;
				lastSlate.close();
				lastSlate = null;
				colourModifier.clear();
			}
			
		}else{
			
			if(needsLeft()){
				if(mouseOverLeft(x, y)){
					GuiUtils.drawBorderedRect(buttonPaddingX, this.height/2 - 20, buttonPaddingX+buttonWidth, this.height/2 + 20, 1, 0xFFFFFFFF, 0x5F000000);
					GuiUtils.drawTriangle(buttonPaddingX + buttonWidth/2, this.height/2, 270, 0xFFFFFFFF);
				}else{
					GuiUtils.drawRect(buttonPaddingX, this.height/2 - 20, buttonPaddingX+buttonWidth, this.height/2 + 20, 0x5F000000);
					GuiUtils.drawTriangle(buttonPaddingX + buttonWidth/2, this.height/2, 270, 0xFF9F9F9F);
				}
			}
			
			if(needsRight()){
				if(mouseOverRight(x, y)){
					GuiUtils.drawBorderedRect(this.width - buttonPaddingX - buttonWidth, this.height/2 - 20, this.width - buttonPaddingX, this.height/2 + 20, 1, 0xFFFFFFFF, 0x5F000000);
					GuiUtils.drawTriangle(this.width - buttonPaddingX - buttonWidth/2, this.height/2, 90, 0xFFFFFFFF);
				}else{
					GuiUtils.drawRect(this.width - buttonPaddingX - buttonWidth, this.height/2 - 20, this.width - buttonPaddingX, this.height/2 + 20, 0x5F000000);
					GuiUtils.drawTriangle(this.width - buttonPaddingX - buttonWidth/2, this.height/2, 90, 0xFF9F9F9F);
				}
			}
			
			slates.get(slateIndex).renderSlate(x, y);
		}
	}

	private void drawLoadingScreen(){

		if(timer.output()){

			rotation += 2;

			if(rotation % 50 == 0){
				updateTrail();
			}
		}

		int posX = width/2;
		int posY = height/2;
		int size = 10;

		glTranslatef(posX, posY, 0);
		glRotatef(rotation, 0, 0, 1);

		GuiUtils.drawRect(-size, -size, size, size, 0xFF58B4ED);

		glRotatef(-rotation, 0, 0, 1);
		glTranslatef(-posX, -posY, 0);

		String loading = "Loading"+trail;
		Client.getClient().getFontRenderer().drawString(loading, posX - Client.getClient().getFontRenderer().getStringWidth(loading)/2, posY + 30, 0xFFFFFFFF);
	}

	private void drawFailedScreen(){

		int posX = width/2;
		int posY = height/2;
		int size = 10;

		glTranslatef(posX, posY, 0);
		glRotatef(45, 0, 0, 1);
		
		GuiUtils.drawRect(-size, -size, size, size, 0xFFFF0000);
		
		glRotatef(-45, 0, 0, 1);
		glTranslatef(-posX, -posY, 0);

		String failed = "Failed";
		Client.getClient().getFontRenderer().drawString(failed, posX - Client.getClient().getFontRenderer().getStringWidth(failed)/2, posY + 30, 0xFFFFFFFF);
	}
	
	
	@Override
	public void initGui() {
		
		Keyboard.enableRepeatEvents(true);
		
		if(this.state == State.IDLE && slates.size() == 0){
			connect();
		}
	}
	
	private void connect(){
		this.state = State.LOADING;
		Thread thread = new Thread(new HubLoadingThread(this));
		thread.start();
	}
	
	public void postConnection() {
		
		slates.add(new MapSlate(this, client));
		slates.add(new PluginSlate(this, client));
		slates.add(new KeybindSlate(this, client));
		slates.add(new PrivatePluginSlate(this, client));
		
		for(Slate slate : slates){
			slate.init();
		}
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int x, int y, int b) throws IOException {
		
		if(needsLeft() && mouseOverLeft(x,y)){

			lastSlate = slates.get(slateIndex);

			slateIndex++;

			if(slateIndex >= slates.size()){
				slateIndex = slates.size() - 1;
				lastSlate = null;
			}

			direction = true;
			transition = 0;

		}else if(needsRight() && mouseOverRight(x,y)){

			lastSlate = slates.get(slateIndex);

			slateIndex--;

			if(slateIndex < 0){
				slateIndex = 0;
				lastSlate = null;
			}

			direction = false;
			transition = 0;
			
		}else if(slates.size() > 0){
			slates.get(slateIndex).mouseClicked(x, y);
		}
		
		options.mouseClicked(x, y);
	}

	@Override
	protected void mouseReleased(int x, int y, int keyState) {
		if(state == State.CONNECTED){
			slates.get(slateIndex).mouseReleased(x, y, keyState);
			options.mouseReleased(x, y, keyState);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(state == State.CONNECTED){
			slates.get(slateIndex).keyTyped(typedChar, keyCode);
		}
		
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public boolean mouseOverLeft(int x, int y){
		return x >= buttonPaddingX && x <= buttonPaddingX+buttonWidth && y >= this.height/2 - 20 && y <= this.height/2 + 20;
	}

	public boolean mouseOverRight(int x, int y){
		return x >= this.width - buttonPaddingX - buttonWidth && x <= this.width - buttonPaddingX && y >= this.height/2 - 20 && y <= this.height/2 + 20;
	}

	private boolean needsLeft(){
		return slateIndex < slates.size()-1;
	}

	private boolean needsRight(){
		return slateIndex > 0;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		
		this.state = state;
		
		if(state == State.FAILED){
			
			options.addComponent(new TextButton(options, "Offline mode", true){
				
				@Override
				public void toggle() {
					setState(State.CONNECTED);
					postConnection();
				}
				
			});
			
			options.addComponent(new TextButton(options, "Retry", true){
				
				@Override
				public void toggle() {
					clearOptions();
					connect();
				}
				
			});
			
		}else if(state == State.CONNECTED){
			
		}
	}
	
	public void clearOptions(){
		options.clearComponents();
	}
}

enum State{
	IDLE, CONNECTED, LOADING, FAILED;
}
