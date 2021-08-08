package org.github.arosecra.brooke.model;

import java.util.ArrayList;
import java.util.List;

public class ButtonSet {
	private List<Button> buttons = new ArrayList<>();

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	
	public ButtonSet addButton(Button button) {
		this.buttons.add(button);
		return this;
	}
	
	public ButtonSet addButtons(ButtonSet buttonSet) {
		this.buttons.addAll(buttonSet.getButtons());
		return this;
	}
}
