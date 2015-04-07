package com.core;

public interface IProtocol {

	//Messages that can be sent
	
	public void ack();
	public void getStatus();
	public void unlock();
	public void lock();
	public void enable();
	public void disable();
	public void getBikeID();
	
	//Receive a message
	public byte[] getMessage();
}
