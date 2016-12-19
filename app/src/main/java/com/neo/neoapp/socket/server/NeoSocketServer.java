package com.neo.neoapp.socket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

import com.neo.neoandroidlib.NeoThreadPool;
import com.neo.neoapp.NeoAppSetings;

public class NeoSocketServer {
	
	private ServerSocket server;
	private boolean isRunning;
	
	NeoSocketServer(){
		try {
			server = new ServerSocket(NeoAppSetings.port);
			isRunning = true;
			
			while(isRunning){
				Socket client = server.accept();
				SocketAddress clentaddr = client.getRemoteSocketAddress();
				
				NeoThreadPool.getThreadPool().execute(
						new Task(client));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		isRunning = false;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class Task implements Runnable{
		
		private Socket client;
		private ObjectInputStream in;
		public Task(Socket client) {
			// TODO Auto-generated constructor stub
			this.client = client;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			readMsg();
		}
		
		private void readMsg(){
			try {
				in = new ObjectInputStream(client.getInputStream());
				Object msg =  in.readObject();

			}catch (ClassNotFoundException e) {
				e.printStackTrace();

			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					in.close();
					//client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	}
}
