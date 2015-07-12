package com.eakjb.learning.net;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Network extends ArrayList<ArrayList<Node>> implements Grid, Comparable<Network> {
	private static final long serialVersionUID = 1L;
	
	private final List<Node> targetOutputLayer;
	private final List<Node> outputLayer;
	
	private final int width;
	private final int height;
	
	public Network(int width, int height, List<Node> targetOutputLayer, List<Node> outputLayer) {
		this.width=width;
		this.height=height;
		this.targetOutputLayer=targetOutputLayer;
		this.outputLayer=outputLayer;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public List<Node> getTargetOutputLayer() {
		return targetOutputLayer;
	}

	public List<Node> getOutputLayer() {
		return outputLayer;
	}
	
	public float getFitness() {
		float fitness = 0;
		
		for (int i=0;i<this.outputLayer.size();i++) {
			Color actual = this.outputLayer.get(i).getOutput();
			Color target = this.getTargetOutputLayer().get(i).getOutput();
			
			fitness+=Math.abs(target.getRed()-actual.getRed());
			fitness+=Math.abs(target.getGreen()-actual.getGreen());
			fitness+=Math.abs(target.getBlue()-actual.getBlue());
		}
		
		return fitness;
	}

	@Override
	public int compareTo(Network o) {
		return (int) (this.getFitness()-o.getFitness());
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.getFitness());
	}

}
