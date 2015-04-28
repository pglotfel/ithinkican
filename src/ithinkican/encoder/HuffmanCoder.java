package ithinkican.encoder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A coder that takes a series of nodes and transforms them into a Huffman code.  As of now, it only stores codes for the original nodes.  This class is intended to generate identifiers for packets.
 * 
 * @author Paul G.
 *
 */
public class HuffmanCoder {
	
	private final HashMap<String, String> codebook = new HashMap<String, String>();
	private final HuffmanNode[] nodes;

	/**
	 * 
	 * @param nodes The sequence of nodes that will be encoded.
	 */
	public HuffmanCoder(HuffmanNode...nodes) {
		
		this.nodes = new HuffmanNode[nodes.length];
		
		for(int i = 0; i < nodes.length; i++) {
			this.nodes[i] = nodes[i];
			codebook.put(nodes[i].getIdentifier(), "");
		}
	}
	
	/**
	 * <p> Recursively assigns a binary value (i.e., 1 or 0) to each Huffman node.  Always assigns 0 to left nodes and 1 to right nodes.
	 * 
	 * @param root The node that is being processes. 
	 * @param value The binary value
	 */
	private void assignBinaryValue(HuffmanNode root, boolean value) {
		
		if(root != null) {
			root.setBinaryValue(value);
			assignBinaryValue(root.getLeft(), false); 
			assignBinaryValue(root.getRight(), true);
		}
	}
	
	/**
	 * <p> Takes a string and a binary value and adds either 1 or 0 onto the string.
	 * 
	 * @param currentValue A string.
	 * @param binaryValue A boolean (i.e., binary value).
	 * @return The modified string.
	 */
	private String concatValue(String currentValue, boolean binaryValue) {
		
		if(binaryValue) {
			return currentValue += "1";
		} else {
			return currentValue += "0";
		}
	}
	
	/**
	 * <p> Recursively adds the encoded values to the codebook.
	 * 
	 * @param root The node that is being recursively processed.
	 * @param currentValue The current binary string.
	 */
	private void addCodes(HuffmanNode root, String currentValue) {
		
		if(root != null) {
			
			String newValue = concatValue(currentValue, root.getBinaryValue());
			
			if(codebook.get(root.getIdentifier()) != null) {
				codebook.put(root.getIdentifier(), newValue);
			}
			
			addCodes(root.getLeft(), newValue);
			addCodes(root.getRight(), newValue);
		}
	}
	
	/**
	 * <p> Returns the decimal value of a binary string.
	 *  
	 * @param binaryValue A binary value as a string.
	 * @return The decimal value of the binary valued string.
	 */
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
	
	/**
	 * <p> Finds the node with the minimum probability.
	 * 
	 * @param nodes The current nodes.
	 * @return The minimum node.
	 */
	private HuffmanNode findMinProbability(ArrayList<HuffmanNode> nodes) {
		
		HuffmanNode min = new HuffmanNode("", 2); 
		
		for(HuffmanNode h : nodes) {
			if(h.getProbability() < min.getProbability()) {
				min = h;
			}
		}
		
		return min;
	}
	
	/**
	 * <p> Combines two nodes into one parent node that has the 'left' and 'right' nodes as children.
	 * 
	 * @param left The left child of the resulting parent node.
	 * @param right The right child of the resulting parent node.
	 * @return The new parent node.
	 */
	private HuffmanNode combine(HuffmanNode left, HuffmanNode right) {
		
		HuffmanNode parent = new HuffmanNode(left.getIdentifier() + right.getIdentifier(), left.getProbability() + right.getProbability());
		parent.setLeft(left);
		parent.setRight(right);
		return parent;
	}
	
	/**
	 * <p> Generates the Huffman code for the given nodes.
	 * 
	 * @return The generated codebook.
	 */
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
		
		for(String s : codebook.keySet()) {
			System.out.println("Identifier: " + s + " Binary Value: " + codebook.get(s) + " Hex Value: " + Integer.toHexString(decimalValue(codebook.get(s))).toUpperCase());
		}
		
		return codebook;
	}
}
