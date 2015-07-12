package com.eakjb.learning.net;

import java.awt.Color;

public abstract class Node {
	private final int x;
	private final int y;
	
	public Node(int x, int y) {
		this.x=x;
		this.y=y;
	}

	public abstract Color getOutput();

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
