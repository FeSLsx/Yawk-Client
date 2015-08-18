package net.yawk.client.modmanager.values;

import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;
import net.yawk.client.gui.components.ArrayBox;
import net.yawk.client.gui.components.ArrayValueBox;

public class ArrayValue extends AbstractValue<Integer>{

	private String[] modes;
	
	public ArrayValue(String name, String saveName, ValuesRegistry registry, Integer defaultValue, String[] modes) {
		super(name, saveName, registry, defaultValue);
		this.modes = modes;
	}

	@Override
	public AbstractComponent getComponent(IPanel panel) {
		return new ArrayValueBox(panel, this);
	}

	public String getSelectedMode(){
		return modes[getValue()];
	}
	
	public void increment(){
		
		value++;
		
		if(value >= modes.length){
			value = 0;
		}
	}
	
	public void decrement(){
		
		value--;
		
		if(value < 0){
			value = modes.length-1;
		}
	}
}
