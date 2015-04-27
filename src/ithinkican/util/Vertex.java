package ithinkican.util;

import java.util.HashSet;

/**
 * <P> Reprents a vertex in the graph of a system.  Vertices contain an identifier, edges, and a component.
 * 
 * @author Paul G.
 *
 */
public class Vertex {
	
	public enum Color {
		WHITE, 
		GREY, 
		BLACK
	}
	
	private HashSet<String> edges; 
	private Component component;
	private Color color = Color.WHITE;
	private String identifier;
	
	/**
	 * 
	 * @param identifier The name of the vertex
	 * @param edges The edges of the vertex (components to which it provides information)
	 * @param component The component of the vertex
	 */
	public Vertex(String identifier, HashSet<String> edges, Component component) {
		
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
