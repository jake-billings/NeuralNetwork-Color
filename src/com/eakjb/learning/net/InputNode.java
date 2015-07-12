package com.eakjb.learning.net;

import java.awt.Color;

public class InputNode extends Node {
	private Color value;

	public InputNode(int x, int y, Color value) {
		super(x,y);
		this.value=value;
	}

	@Override
	public Color getOutput() {
		return value;
	}

}
