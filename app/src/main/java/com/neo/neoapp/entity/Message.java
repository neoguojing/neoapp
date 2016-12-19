package com.neo.neoapp.entity;

import java.io.Serializable;

public class Message extends Entity implements Serializable {
	
	private String name;
	private String avatar;
	private long time;
	private String distance;
	private String content;
	

	private CONTENT_TYPE contentType;
	private MESSAGE_TYPE messageType;
	private MESSAGE_STATUS msgStatus;

	public Message(String name, String avatar, long time, String distance, String content,
			CONTENT_TYPE contentType, MESSAGE_TYPE messageType) {
		super();
		this.name = name;
		this.avatar = avatar;
		this.time = time;
		this.distance = distance;
		this.content = content;
		this.contentType = contentType;
		this.messageType = messageType;
		this.msgStatus = MESSAGE_STATUS.SENDSUCCESS;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CONTENT_TYPE getContentType() {
		return contentType;
	}

	public void setContentType(CONTENT_TYPE contentType) {
		this.contentType = contentType;
	}

	public MESSAGE_TYPE getMessageType() {
		return messageType;
	}

	public void setMessageType(MESSAGE_TYPE messageType) {
		this.messageType = messageType;
	}
	
	public MESSAGE_STATUS getMessageStatu() {
		return msgStatus;
	}

	public void setMessageStatu(MESSAGE_STATUS messageStatu) {
		this.msgStatus = messageStatu;
	}

	public enum CONTENT_TYPE {
		TEXT, IMAGE, MAP, VOICE;
	}

	public enum MESSAGE_TYPE {
		RECEIVER, SEND;
	}
	
	public enum MESSAGE_STATUS {
		SENDSUCCESS, SENDFAILED;
	}
}
