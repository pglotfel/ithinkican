package ithinkican.util;

import ithinkican.util.Vertex.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * <P>A graph that is intended to implement an application of various components.  Components have start() and stop() methods.  This graph can be used to start each component in the correct order.
 * 
 * @author Paul G.
 *
 */
public class SystemGraph implements Component {
	
	private final HashMap<String, Vertex> nodes = new HashMap<String, Vertex>();
	
	public SystemGraph() {
		
	}
	
	/**
	 * <P> Adds a node to the graph
	 * 
	 * @param identifier The identifier for the vertex
	 * @param component The component associated with the vertex
	 * @param edges The edges of a vertex.  Edges are vertices to which this vertex provides information.
	 */
	public void addVertex(String identifier, Component component, String...edges) {
		
		HashSet<String> set = new HashSet<String>();
		
		if(edges != null) {
			for(String s : edges) {
				set.add(s);
			}
		} 
		
		nodes.put(identifier, new Vertex(identifier, set, component));
	}
	
	/**
	 *<P> Removes a node from the graph.  This method should really not be used...
	 * 
	 * @param identifier The identifier of the vertex to be removed.
	 */
	public void removeNode(String identifier) {
		nodes.remove(identifier);
	}
	
	/**
	 * <P> Visit helper method for DFS.  
	 * 
	 * @param s The vector in which the solution is stored.
	 * @param u The vertex being visited
	 */
	private void topSortVisit(Vector<Vertex> s, Vertex u) {
		
		u.setColor(Color.GREY);
		
		for(String v : u.edges()){ 
			
			if(nodes.get(v).color() == Color.WHITE) {
				topSortVisit(s, nodes.get(v));
			}
		}
		
		u.setColor( Color.BLACK);
		s.add(u);
	}
	
	/**
	 * <P> Executes a topological sort on 'this' graph.
	 * 
	 * @return
	 */
	public Vector<Vertex> topologicalSort() {
		
		Vector<Vertex> result = new Vector<Vertex>();
		
		for(Vertex n : nodes.values()) {
			n.setColor(Color.WHITE);
		}
		
		for(Vertex n : nodes.values()) {
			if(n.color() == Color.WHITE) {
				topSortVisit(result, n);
			}
		}
		
		Collections.reverse(result);
		
		return result;
	}

	@Override
	public void start() {
		
		System.out.print("Component starting order: ");
		for(Vertex n : this.topologicalSort()) {
			System.out.print(n.identifier() + " ");
			n.component().start();
		}
		System.out.println();
	}

	@Override
	public void stop() {
		
		Vector<Vertex> sorted = this.topologicalSort();
		Collections.reverse(sorted);
		
		for(Vertex n : sorted) {
			System.out.println(n.identifier());
			n.component().stop();
		}
	}
}
