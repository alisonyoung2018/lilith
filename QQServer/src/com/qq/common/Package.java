package com.qq.common;

import java.io.Serializable;

public class Package implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8122451541021268534L;
	private String type;
	private String source;
	private int status;
	private Object plusObject;

	public Package() {
		// TODO Auto-generated constructor stub
	}

	public Package(String type, String source, int status, Object plusObject) {
		this.type = type;
		this.source = source;
		this.status = status;
		this.plusObject = plusObject;
	}

	public Package(String type, int status, Object plusObject) {
		this.type = type;
		this.status = status;
		this.plusObject = plusObject;
	}

	public Package(String type, String source, Object plusObject) {
		this.type = type;
		this.source = source;
		this.plusObject = plusObject;
	}

	public Package(String type, String source) {
		this.type = type;
		this.source = source;
	}

	public Package(String type, int status) {
		this.type = type;
		this.status = status;
	}

	public Package(String type, Object plusObject) {
		this.type = type;
		this.plusObject = plusObject;
	}

	public Package(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getPlusObject() {
		return plusObject;
	}

	public void setPlusObject(Object plusObject) {
		this.plusObject = plusObject;
	}

}
