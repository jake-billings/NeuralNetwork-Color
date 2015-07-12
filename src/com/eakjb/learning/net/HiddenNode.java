package com.eakjb.learning.net;

import java.awt.Color;
import java.util.Map;

public class HiddenNode extends Node {	
	private final Map<Node,Float> weights;

	public HiddenNode(int x, int y, Map<Node,Float> weights) {
		super(x,y);
		this.weights = weights;
	}

	public Map<Node, Float> getWeights() {
		return weights;
	}

	@Override
	public Color getOutput() {
		float r = 0;
		float g = 0;
		float b = 0;
		
		for (Node n : weights.keySet()) {
			r += n.getOutput().getRed()*weights.get(n);
			g += n.getOutput().getGreen()*weights.get(n);
			b += n.getOutput().getBlue()*weights.get(n);
		}
		
		if (r > 255) r = 255f;
		if (g > 255) g = 255f;
		if (b > 255) b = 255f;

		return new Color((int) r,(int) g,(int) b);
	}
	
	public float getWeightAt(int x, int y) {
		for (Node n : this.weights.keySet()) {
			if (n.getX()==x&&n.getY()==y) {
				return this.weights.get(n);
			}
		}
		throw new IllegalArgumentException("Node not found");
	}

}
