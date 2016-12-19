package com.neo.neoapp.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import com.neo.neoandroidlib.NeoThreadPool;
import com.neo.neoapp.NeoAppSetings;

public class NeoSocketClient {

	private String Localhost = "127.0.0.1";
	private Socket client = null;
	//private LinkedList<Object> sendQueue = null;
	private boolean isRunning = false;
	
	public NeoSocketClient(){
		try {
			isRunning = true;
			client = new Socket(Localhost,NeoAppSetings.port);
			receive();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public NeoSocketClient(String addr){
		try {
			isRunning = true;
			client = new Socket(addr,NeoAppSetings.port);
			receive();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void receive(){
		NeoThreadPool.getThreadPool().execute(
				new RecvTask());
	}
	
	private class RecvTask implements Runnable {
		ObjectInputStream receiver;
		
		public RecvTask() {
			// TODO Auto-generated constructor stub
			receiver = null;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRunning){
				try {
					receiver = new ObjectInputStream(client.getInputStream());
					receiver.readObject();
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
						receiver.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public boolean send(Object content){
		ObjectOutputStream sender = null;
		try {
			sender = new ObjectOutputStream(client.getOutputStream());
			sender.writeObject(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				sender.flush();
				sender.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return true;
	}
	
	public void close(){
		try {
			
			isRunning = false;
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
