package ithinkican.encoder;

public class HuffmanNode {
	
	private double probability; 
	private String identifier; 
	
	//False = 0, True = 1
	private boolean binaryValue = false;
	private HuffmanNode left = null; 
	private HuffmanNode right = null;
	
	public HuffmanNode(String identifier, double probability) {
		this.identifier = identifier;
		this.probability = probability;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public boolean getBinaryValue() {
		return binaryValue;
	}
	
	public void setBinaryValue(boolean binaryValue) {
		this.binaryValue = binaryValue;
	}
	
	public HuffmanNode getLeft() {
		return left;
	}
	
	public HuffmanNode getRight() {
		return right;
	}
	
	public void setLeft(HuffmanNode left) {
		this.left = left;
	}
	
	public void setRight(HuffmanNode right) {
		this.right = right;
	}
}
