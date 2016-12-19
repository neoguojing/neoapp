package com.neo.neoandroidlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.neo.neoapp.entity.Message;

public class NeoSocketMessageCacheUtil {
	private static HashMap<String,List<Message>> messageCache = 
			new  HashMap<String,List<Message>>();
	
	private static NeoSocketMessageCacheUtil messageCacheObject = null;
	public static NeoSocketMessageCacheUtil getIntance(){
		
		if (messageCacheObject==null)
			messageCacheObject = new NeoSocketMessageCacheUtil();
		return messageCacheObject;
	}
	
	public void clearMessageList(String name){
		if (messageCache.containsKey(name)){
			messageCache.get(name).clear();
		}
	}
	
	public void clearMessageCache(){
		for(String key:messageCache.keySet()){
			messageCache.get(key).clear();
		}
		messageCache.clear();
	}
	
	public void initMessageList(String name){
		if (messageCache.containsKey(name))
			return;
		List<Message> list = new ArrayList<Message>();
		messageCache.put(name, list);
	}
	
	public void addMessage(String name,Message msg){
		if (!messageCache.containsKey(name))
			initMessageList(name);
		messageCache.get(name).add(msg);
	}
	
	public List<Message> getMessageList(String name){
		initMessageList(name);	
		return messageCache.get(name);
	}
	
	public boolean isMessageListEmpty(String name){
		if (!messageCache.containsKey(name))
			return true;
		return messageCache.get(name).isEmpty();
	}
	
	public int getMessageCount(String name){
		if (!messageCache.containsKey(name))
			return 0;
		return messageCache.get(name).size();
	}
	
	public int getAllMessageCount(){
		int count = 0;
		for(String key:messageCache.keySet()){
			count+=messageCache.get(key).size();
		}
		return count;
	}
}
