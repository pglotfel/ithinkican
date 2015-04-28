package ithinkican.encoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HuffmanCoder {
	
	private final HashMap<String, String> codes = new HashMap<String, String>();
	private final HuffmanNode[] nodes;

	
	public HuffmanCoder(HuffmanNode...nodes) {
		
		this.nodes = new HuffmanNode[nodes.length];
		
		for(int i = 0; i < nodes.length; i++) {
			this.nodes[i] = nodes[i];
			codes.put(nodes[i].getIdentifier(), "");
		}
	}
	
	public void print(HuffmanNode root) {
		
		if(root != null) {
			System.out.println(root.getIdentifier());
			print(root.getLeft());
			print(root.getRight());
		}
	}
	
	private void assignBinaryValue(HuffmanNode root, boolean value) {
		
		if(root != null) {
			root.setBinaryValue(value);
			assignBinaryValue(root.getLeft(), false); 
			assignBinaryValue(root.getRight(), true);
		}
	}
	
	private String concatValue(String currentValue, boolean binaryValue) {
		
		if(binaryValue) {
			return currentValue += "1";
		} else {
			return currentValue += "0";
		}
	}
	
	private void addCodes(HuffmanNode root, String currentValue) {
		
		if(root != null) {
			
			String newValue = concatValue(currentValue, root.getBinaryValue());
			
			if(codes.get(root.getIdentifier()) != null) {
				codes.put(root.getIdentifier(), newValue);
			}
			
			addCodes(root.getLeft(), newValue);
			addCodes(root.getRight(), newValue);
		}
	}
	
	private static int decimalValue(String binaryValue) {

		int ret = 0;
		binaryValue = new StringBuilder(binaryValue).reverse().toString();
		
		for(int i = binaryValue.length() - 1; i >= 0; i--) {
			
			if(binaryValue.charAt(i) == '1') {
				ret += Math.pow(2, i);
			}
		}
		
		return ret;
	}
	
	public HashMap<String, String> generate() {
		
		ArrayList<HuffmanNode> todo = new ArrayList<HuffmanNode>();
	
		for(int i = 0; i < this.nodes.length; i++) {
			todo.add(this.nodes[i]);
		}
		
		while(todo.size() > 1) {
			
			HuffmanNode left = findMinProbability(todo);
			todo.remove(left);
			HuffmanNode right = findMinProbability(todo);
			todo.remove(right);
			todo.add(combine(left, right));		
		}
		
		HuffmanNode root = todo.get(0);
		
		assignBinaryValue(root, true);
		
		addCodes(root, "");
		
		for(String s : codes.keySet()) {
			System.out.println("Identifier: " + s + " Binary Value: " + codes.get(s) + " Hex Value: " + Integer.toHexString(decimalValue(codes.get(s))).toUpperCase());
		}
		
		return codes;
	}
	
	private HuffmanNode findMinProbability(ArrayList<HuffmanNode> nodes) {
		
		HuffmanNode min = new HuffmanNode("", 2); 
		
		for(HuffmanNode h : nodes) {
			if(h.getProbability() < min.getProbability()) {
				min = h;
			}
		}
		
		return min;
	}
	
	public HuffmanNode combine(HuffmanNode left, HuffmanNode right) {
		
		HuffmanNode parent = new HuffmanNode(left.getIdentifier() + right.getIdentifier(), left.getProbability() + right.getProbability());
		parent.setLeft(left);
		parent.setRight(right);
		return parent;
	}
	
	public static void main(String[] args) {
		
		HuffmanCoder c = new HuffmanCoder(new HuffmanNode("A", 0.4), new HuffmanNode("B", 0.4), new HuffmanNode("C", 0.1), new HuffmanNode("D", 0.1));
		
		c.generate();
		
	}
}
