package net.yawk.client.modmanager.values;

import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;

public abstract class AbstractValue<T> {
	
	protected T value;
	private String name;
	private ValuesRegistry registry;
	private String saveName;
	
	public AbstractValue(String name, String saveName, ValuesRegistry registry, T defaultValue){
		
		if(registry.hasValue(saveName)){
			this.value = (T) registry.get(saveName);
		}else{
			this.value = defaultValue;
		}
		
		this.registry = registry;
		this.name = name;
		this.saveName = saveName;
		
		System.out.println(saveName);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
		registry.set(saveName, value);
	}
	
	public abstract AbstractComponent getComponent(IPanel panel);
}
