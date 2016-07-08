package com.klatencor.klara.future.support;

public class BytesWapper {
	
	private int pos;
	private byte[] buffer;
	
	public BytesWapper(byte[] buff) {
		this.buffer = buff;
		pos = 0;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buff) {
		this.buffer = buff;
	}
	
	/**
	 * Move the pos forward or backward.
	 * @param inc
	 */
	public void movePos(int inc) {
		this.pos += inc;
	}
}
