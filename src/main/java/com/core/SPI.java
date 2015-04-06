package com.core;

import java.io.IOException;

import com.pi4j.io.spi.*;

public class SPI {
	
	SpiDevice spi;
	
	public SPI(SpiDevice spi) {
		this.spi = spi;
	}
	
	public void reset() {
		try {
			spi.write((byte) 0xC0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printBytes(byte [] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i] + " ");
		}
		System.out.println();
	}
	
	public void init() {
		
		byte [] src = {(byte) 0x05, (byte) 0x60, (byte) 0x60, (byte) 0xFF};
		try {
			spi.write(src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.send(0x0F,  0x00);
		
		byte[] src2 = {(byte) 0x05, (byte) 0x2B, (byte) 0x03, (byte) 0xFF};
		try {
			spi.write(src2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] readByte(int address) {
		
		byte[] msg = {(byte) 0x03, (byte) address, (byte) 0xDF};
		byte [] bs = null;
		
		try {
			bs = spi.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bs;
	}
	
	public void clearBuffers() {
		
		byte [] msg = {(byte) 0x05, (byte) 0x2C, (byte) 0x03, (byte) 0x00};
		
		try {
			spi.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getBufferData() {
		
		printBytes(readByte(0x66));
		printBytes(readByte(0x67));
		printBytes(readByte(0x68));
		printBytes(readByte(0x69));
		
	}
	
	public byte[] send(int address, int b) {
		
		byte[] bs = {(byte) 0x02, (byte) address, (byte) b};

		byte[] read = null;
		
		try {
			read = spi.write(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return read;
	}
	
	public void ack() {
		
		send(0x31, 0x11);
		send(0x32, 0x00);
		send(0x35, 0x04);
		 
		send(0x36, 0x03);
		send(0x37, 0x03);
		send(0x38, 0x03);
		send(0x39, 0x03);
		
		try {
			spi.write((byte) 0x81);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void lock() {
		
		send(0x31, 0x11);
		send(0x32, 0x00);
		send(0x35, 0x04);
		 
		send(0x36, 0x09);
		send(0x37, 0x09);
		send(0x38, 0x09);
		send(0x39, 0x09);
		
		try {
			spi.write((byte) 0x81);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unlock() {
		
		send(0x31, 0x11);
		send(0x32, 0x00);
		send(0x35, 0x04);
		 
		send(0x36, 0x16);	
		send(0x37, 0x16);
		send(0x38, 0x16);
		send(0x39, 0x16);
		
		try {
			spi.write((byte) 0x81);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		
		SpiDevice s = null;
		
		try {
			s = SpiFactory.getInstance(SpiChannel.CS0, 10000000, SpiMode.MODE_0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SPI spi = new SPI(s);
		
		spi.reset();
		spi.init();
		spi.clearBuffers();
		
		while(true) {
			
			/*spi.ack();
			spi.getBufferData();
			spi.clearBuffers();*/
			
			spi.unlock();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			spi.lock();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
			System.out.println("END");
		}
		
	}
}
