package com.eakjb.learning.net;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Main {
	private static final int NETWORK_HEIGHT = 20;
	private static final int NETWORK_WIDTH = 10;
	
	public static final int POPULATION_SIZE = 200;
	public static final float MUTATION_RATE = 0.005f;
	
	public static final int SCALE = 48;
	public static final int FPS = 60;
	
	public static final int MILLISECOND_FRAME_DELAY = 1000 / FPS;

	public static final int STEPS_PER_SECOND = 10000;
	public static final int MILLISECOND_STEP_DELAY = 1000 / STEPS_PER_SECOND;

	private static List<Network> population = new ArrayList<Network>();

	private static ArrayList<Node> getTargetLayer() {
		ArrayList<Node> targetLayer = new ArrayList<Node>();
		targetLayer.add(new InputNode(4,2,Color.CYAN));
		targetLayer.add(new InputNode(4,3,Color.ORANGE));
		return targetLayer;
	}

	private static ArrayList<Node> getInputLayer() {
		ArrayList<Node> inputLayer = new ArrayList<Node>();
		inputLayer.add(new InputNode(1,2,Color.RED));
		inputLayer.add(new InputNode(1,3,Color.GREEN));
		inputLayer.add(new InputNode(1,4,Color.BLUE));
		return inputLayer;
	}

	public static Network getRandomNetwork() {
		ArrayList<Node> targetLayer = getTargetLayer();

		ArrayList<Node> inputLayer = getInputLayer();

		ArrayList<Node> hiddenLayer = new ArrayList<Node>();
		for (int i = 1; i<=4; i++) {
			HashMap<Node,Float> weights = new HashMap<Node,Float>();
			for (Node node : inputLayer) {
				weights.put(node, (float) (Math.random()));
			}
			hiddenLayer.add(new HiddenNode(2,i,weights));
		}

		ArrayList<Node> outputLayer = new ArrayList<Node>();
		for (int i = 2; i<=3; i++) {
			HashMap<Node,Float> weights = new HashMap<Node,Float>();
			for (Node node : hiddenLayer) {
				weights.put(node, (float) (Math.random()));
			}
			outputLayer.add(new HiddenNode(3,i,weights));
		}

		Network n = new Network(NETWORK_WIDTH,NETWORK_HEIGHT,targetLayer,outputLayer);

		n.add(inputLayer);
		n.add(hiddenLayer);
		n.add(outputLayer);
		n.add(targetLayer);

		return n;
	}

	public static Network breed(Network a, Network b) {
		ArrayList<Node> targetLayer = getTargetLayer();
		ArrayList<Node> inputLayer = getInputLayer();

		ArrayList<Node> hiddenLayer = new ArrayList<Node>();
		ArrayList<Node> outputLayer = new ArrayList<Node>();

		for (int i = 0; i<4; i++) {
			HashMap<Node,Float> weights = new HashMap<Node,Float>();

			for (Node node : inputLayer) {
				if (Math.random() < MUTATION_RATE) {
					weights.put(node, (float) Math.random());
				} else {
					Network n = (Math.random()>0.5)?a:b;
					List<Node> otherHiddenLayer = n.get(1);

					HiddenNode other = (HiddenNode) otherHiddenLayer.get(i);

					float otherWeight = other.getWeightAt(node.getX(), node.getY());

					weights.put(node, otherWeight);
				}
			}

			hiddenLayer.add(new HiddenNode(2,i+1,weights));
		}

		for (int i = 0; i<2; i++) {
			HashMap<Node,Float> weights = new HashMap<Node,Float>();

			for (Node node : hiddenLayer) {
				if (Math.random() < MUTATION_RATE) {
					weights.put(node, (float) Math.random());
				} else {
					Network n = (Math.random()>0.5)?a:b;
					List<Node> otherHiddenLayer = n.get(2);

					HiddenNode other = (HiddenNode) otherHiddenLayer.get(i);

					float otherWeight = other.getWeightAt(node.getX(), node.getY());

					weights.put(node, otherWeight);
				}
			}

			outputLayer.add(new HiddenNode(3,i+2,weights));
		}

		Network c = new Network(NETWORK_WIDTH,NETWORK_HEIGHT,
				targetLayer,outputLayer);

		c.add(inputLayer);
		c.add(hiddenLayer);
		c.add(outputLayer);
		c.add(targetLayer);

		return c;
	}

	public static void main(String[] args) {
		RenderFrame frame = new RenderFrame(SCALE);

		for (int i=0;i<POPULATION_SIZE;i++) {
			population.add(getRandomNetwork());
		}

		frame.setVisible(true);


		//--Render Thread--
		Thread renderThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					frame.repaintCanvases();

					try {
						Thread.sleep(MILLISECOND_FRAME_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		//--End Render Thread--		
		//--Step Thread--
		Thread stepThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int steps = 0;
				while (true) {
					//Order by fitness, so less fit individuals can be removed later					
					frame.getNetworkCanvas().getStats().put("Best Step Fitness",
							String.valueOf(population.get(population.size()-1).getFitness()));

					frame.getNetworkCanvas().getStats().put("Steps",
							String.valueOf(steps++));
					frame.getNetworkCanvas().getStats().put("Generation",
							String.valueOf(Math.floor(steps/100)));

					//Render all networks
					int sum = 0;
					for (Network network : population) {
						sum+=network.getFitness();
						frame.getNetworkCanvas().setNetwork(network);

						try {
							Thread.sleep(MILLISECOND_STEP_DELAY);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					frame.getNetworkCanvas().getStats().put("Average Fitness",
							String.valueOf(sum/population.size()));


					if (population.size()<3) break;

					ArrayList<Network> subset = new ArrayList<Network>();

					//Delete less fit individuals
					for (int i=0; i<3;) {
						Network n = population.get((int) (Math.random()*population.size()));
						if (!subset.contains(n)) {
							subset.add(n);
							i++;
						}
					}

					Collections.sort(subset);

					population.remove(subset.get(2));
					Network n = breed(subset.get(0),subset.get(1));
					population.add(n);

				}

			}
		});
		//--End Step Thread--

		stepThread.start();
		renderThread.start();
	}

}
