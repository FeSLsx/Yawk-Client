package net.yawk.client.gui.hub;

import net.yawk.client.Client;
import net.yawk.client.gui.Canvas;
import net.yawk.client.gui.IScalerPosition;
import net.yawk.client.gui.components.Slider;
import net.yawk.client.gui.components.buttons.BooleanButton;
import net.yawk.client.gui.maps.LargeMap;
import net.yawk.client.modmanager.values.BooleanValue;
import net.yawk.client.modmanager.values.SliderValue;
import net.yawk.client.modmanager.values.ValuesRegistry;

public class MapSlate extends Slate{
	
	private LargeMap map;
	private Canvas options;
	private SliderValue scale;
	private BooleanValue chunks, cavefinder, factions;

	public MapSlate(final GuiHub hub, Client client) {
		super("Map", null, hub);
		
		IScalerPosition pos = new IScalerPosition(){

			@Override
			public int getX() {
				return hub.width/2 - 50;
			}

			@Override
			public int getY() {
				return 3;
			}

		};

		options = new Canvas(pos, 100);

		ValuesRegistry registry = client.getValuesRegistry();

		scale = new SliderValue("Scale", "map.scale", registry, 2, 1, 8, false);
		options.addComponent(new Slider(options, scale));

		chunks = new BooleanValue("Show Chunks", "map.chunks", registry, false);
		options.addComponent(new BooleanButton(options, chunks));

		cavefinder = new BooleanValue("Cavefinder Mode", "map.cavefinder", registry, false);
		options.addComponent(new BooleanButton(options, cavefinder));
		
		factions = new BooleanValue("Record Factions", "map.factions", registry, false);
		options.addComponent(new BooleanButton(options, factions));
		
		map = new LargeMap(hub.colourModifier);
	}
	
	@Override
	public void renderSlate(int x, int y) {
		
		map.setShowFactions(factions.getValue());
		map.setCavefinder(cavefinder.getValue());
		map.setShowChunks(chunks.getValue());
		
		map.draw(hub.width/2, hub.height/2, scale.getValue());
		
		options.draw(x, y);
	}
	
	@Override
	public void init() {
		map.registerFactionsListener();
	}

	@Override
	public void close() {
		map.unregisterFactionsListener();
	}

	@Override
	public void mouseClicked(int x, int y) {
		options.mouseClicked(x, y);
	}
	
	@Override
	public void mouseReleased(int x, int y, int state) {
		options.mouseReleased(x, y, state);
	}
}
