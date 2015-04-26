package ithinkican.util;

import java.util.HashSet;

public class Node {
	
	public enum Color {
		WHITE, 
		GREY, 
		BLACK
	}
	
	private HashSet<String> edges; 
	private Component component;
	private Color color = Color.WHITE;
	private String identifier;
	
	public Node(String identifier, HashSet<String> edges, Component component) {
		
		this.identifier = identifier;
		this.edges = edges;
		this.component = component;
	}

	public String identifier() {
		return this.identifier;
	}
	
	public HashSet<String> edges() {
		return this.edges;
	}
	
	public Component component() {
		return this.component;
	}
	
	public Color color() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
