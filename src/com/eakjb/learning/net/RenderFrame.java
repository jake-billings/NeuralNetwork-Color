package com.eakjb.learning.net;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

public class RenderFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final NetworkCanvas networkCanvas;

	public RenderFrame(int nScale) {
		this(null,nScale);
	}

	public RenderFrame(Network network, int networkScale) {
		this.networkCanvas = new NetworkCanvas(network,networkScale);
		this.add(this.networkCanvas);
		this.setBounds(50, 50, 850, 550);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void repaintCanvases() {
		this.networkCanvas.repaint();
	}

	public static class NetworkCanvas extends Canvas {
		private Network network;
		private int scale;
		
		private final HashMap<String,String> stats = new HashMap<String,String>();

		public Network getNetwork() {
			return network;
		}

		public void setNetwork(Network network) {
			this.network = network;
			updateDimension();
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
			updateDimension();
		}

		private void updateDimension() {
			if (network!=null) {
				this.setMinimumSize(new Dimension(network.getWidth()*scale,network.getHeight()*scale));
			}
		}

		public NetworkCanvas(Network network, int scale) {
			this.network = network;
			this.scale = scale;
			updateDimension();
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics gNormal) {
			Graphics2D g = (Graphics2D) gNormal;

			for (List<Node> layer : this.network) {
				for (Node node : layer) {
					g.setColor(node.getOutput());
					g.fillOval((node.getX()*scale)+(node.getX()*scale),
							(node.getY()*scale)+(node.getY()*scale/2),
							scale, scale);

					g.setColor(Color.BLACK);
					if (node instanceof HiddenNode) {
						for (Node other : ((HiddenNode) node).getWeights().keySet()) {
							g.setStroke(new BasicStroke(5*((HiddenNode) node).getWeights().get(other)));
							g.drawLine((node.getX()*scale)+(node.getX()*scale)+scale/2,
										(node.getY()*scale)+(node.getY()*scale/2+scale/2),
										(other.getX()*scale)+(other.getX()*scale+scale/2),
										(other.getY()*scale)+(other.getY()*scale/2+scale/2));
						}
					}
				}
			}
			
			g.drawString("Fitness: " + this.network.getFitness(), scale/2, scale/2);
			int i = 2;
			for (String key : this.stats.keySet()) {
				g.drawString(key+": "+stats.get(key), scale/2, scale/2*i++);
			}
		}

		public HashMap<String,String> getStats() {
			return stats;
		}
	}

	public NetworkCanvas getNetworkCanvas() {
		return networkCanvas;
	}
}
