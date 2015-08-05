package net.yawk.client.gui.components.buttons;

import net.yawk.client.gui.Window;
import net.yawk.client.gui.components.FriendScrollPane;
import net.yawk.client.gui.components.selectors.SelectorButton;
import net.yawk.client.gui.components.selectors.SelectorSystem;

public class FriendsExecuteButton extends Button{
	
	private SelectorSystem<SelectorButton> system;
	
	public FriendsExecuteButton(Window win, SelectorSystem<SelectorButton> system) {
		super(win);
		this.system = system;
	}

	@Override
	public boolean isCentered() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void toggle() {
		
	}

	@Override
	public String getText() {
		return system.selectedButton != null? "Remove":"Add";
	}

}