package com.neo.neoapp.socket.client;

import java.io.IOException;


import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.content.Context;

import com.neo.neoandroidlib.NeoSocketSerializableUtils;
import com.neo.neoandroidlib.NeoThreadPool;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.entity.Message;

public class NeoAyncSocketClient {

	private String Localhost = "127.0.0.1";
	private SocketChannel client = null;
	private Selector select = null;
	private boolean isRunning = false;
	private Context mContext;
	
	public NeoAyncSocketClient(Context context){
		isRunning = true;
		mContext = context;
		try {
			select = Selector.open();
			SocketAddress addr = new InetSocketAddress(Localhost,NeoAppSetings.port);
			client = SocketChannel.open(addr);
			client.configureBlocking(false);
			client.register(select, SelectionKey.OP_READ);
			receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public NeoAyncSocketClient(Context context, String addr){
		isRunning = true;
		mContext = context;
		try {
			select = Selector.open();
			SocketAddress saddr = new InetSocketAddress(addr,NeoAppSetings.port);
			client = SocketChannel.open(saddr);
			client.configureBlocking(false);
			client.register(select, SelectionKey.OP_READ);
			receive();
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
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ByteBuffer bf = ByteBuffer.allocate(1024);
			while(isRunning){
				System.out.println("NeoAyncSocketClient:RecvTask runnning");
				try {
					select.select();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (SelectionKey sk:select.selectedKeys()){
					select.selectedKeys().remove(sk);
					if (sk.isReadable()){
						SocketChannel sc = (SocketChannel) sk.channel();
						//bf.clear();
						
						int readbytes;
						try {
							readbytes = sc.read(bf);
							bf.flip();
							sc.register(select, SelectionKey.OP_READ);
							if (readbytes<=0)
								return;
							
							Message object = NeoSocketSerializableUtils.byteArrayToMessage(bf.array());
							if (object==null)
								continue;
							
							object.setMessageType(Message.MESSAGE_TYPE.RECEIVER);
							//object.setContent("i am coming from client receiver");
							NeoAppBroadCastMessages.sendDynamicBroadCastMsg(mContext, object);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
			System.out.println("NeoAyncSocketClient: RecvTask end");
		}
	}
	
	public boolean send(Object content){
		byte[] bytes = NeoSocketSerializableUtils.objectToByteArray(content);
		
		if (bytes==null)
			return false;
		
		try {
			if (client!=null)
				client.write(ByteBuffer.wrap(bytes));
			else
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean send(Message content){
		//content.setContent("coming from client server");
		byte[] bytes = NeoSocketSerializableUtils.MessageToByteArray(content);
		
		if (bytes==null)
			return false;
		
		try {
			if (client!=null)
				client.write(ByteBuffer.wrap(bytes));
			else
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void close(){
		try {
			
			isRunning = false;
			if (client!=null)
				client.close();
			select.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

