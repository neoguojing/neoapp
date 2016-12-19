package com.neo.neoandroidlib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.neo.neoapp.entity.Message;

public class NeoSocketSerializableUtils {
	
	public static byte[] objectToByteArray(Object obj){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] bytes =null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (oos!=null)
					oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bytes;		
	}
	
	public static byte[] MessageToByteArray(Message obj){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] bytes =null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (oos!=null)
					oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bytes;		
	}
	
	public static Object byteArrayToObject(byte[] bytes){
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (ois!=null)
					ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return obj;
		
	}
	
	public static Message byteArrayToMessage(byte[] bytes){
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		Message obj = null;
		try {
			
			ois = new ObjectInputStream(bis);
			obj = (Message) ois.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bis.close();
				if (ois!=null)
					ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return obj;
		
	}
}
