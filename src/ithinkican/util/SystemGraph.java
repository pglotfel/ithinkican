package ithinkican.util;

import ithinkican.util.Node.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class SystemGraph implements Component {
	
	private final HashMap<String, Node> nodes = new HashMap<String, Node>();
	
	public SystemGraph() {
		
	}
	
	public void addNode(String identifier, Component component, String...edges) {
		
		HashSet<String> set = new HashSet<String>();
		
		if(edges != null) {
			for(String s : edges) {
				set.add(s);
			}
		} 
		
		nodes.put(identifier, new Node(identifier, set, component));
	}
	
	public void removeNode(String identifier) {
		nodes.remove(identifier);
	}
	
	private void topSortVisit(Vector<Node> s, Node u) {
		
		u.setColor(Color.GREY);
		
		for(String v : u.edges()){ 
			
			if(nodes.get(v).color() == Color.WHITE) {
				topSortVisit(s, nodes.get(v));
			}
		}
		
		u.setColor( Color.BLACK);
		s.add(u);
	}
	
	public Vector<Node> topologicalSort() {
		
		Vector<Node> result = new Vector<Node>();
		
		for(Node n : nodes.values()) {
			n.setColor(Color.WHITE);
		}
		
		for(Node n : nodes.values()) {
			if(n.color() == Color.WHITE) {
				topSortVisit(result, n);
			}
		}
		
		Collections.reverse(result);
		
		return result;
	}

	@Override
	public void start() {
		
		for(Node n : this.topologicalSort()) {
			System.out.println(n.identifier());
			n.component().start();
		}
	}

	@Override
	public void stop() {
		
		Vector<Node> sorted = this.topologicalSort();
		Collections.reverse(sorted);
		
		for(Node n : sorted) {
			System.out.println(n.identifier());
			n.component().stop();
		}
	}
}
