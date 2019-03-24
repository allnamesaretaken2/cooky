package de.cooky.exceptions;

public class CookyErrorMsg extends RuntimeException {

	private static final long serialVersionUID = -2378355654542433765L;

	public CookyErrorMsg(String msg) {
		super(msg);
	}
	
	public CookyErrorMsg(Exception e) {
		super(e);
	}

}
