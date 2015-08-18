package net.yawk.client.modmanager.values;

import net.yawk.client.gui.AbstractComponent;
import net.yawk.client.gui.IPanel;
import net.yawk.client.gui.components.Slider;
import net.yawk.client.gui.components.buttons.BooleanButton;

public class SliderValue extends AbstractValue<Double>{
	
	private double lowerBound;
	private double upperBound;
	private boolean rounded;
	
	public SliderValue(String name, String saveName, ValuesRegistry registry, double defaultValue, double lowerBound, double upperBound, boolean rounded) {
		super(name, saveName, registry, defaultValue);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.rounded = rounded;
	}
	
	public double getLowerBound() {
		return lowerBound;
	}
	
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	public double getUpperBound() {
		return upperBound;
	}
	
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
	
	public boolean isRounded() {
		return rounded;
	}

	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	public void setValue(double value){
		
		if(rounded){
			value = (int)Math.round(value);
		}
		
		if(value > upperBound){
			value = upperBound;
		}
		
		if(value < lowerBound){
			value = lowerBound;
		}
		
		super.setValue(value);
	}
	
	@Override
	public AbstractComponent getComponent(IPanel panel) {
		return new Slider(panel, this);
	}
}
