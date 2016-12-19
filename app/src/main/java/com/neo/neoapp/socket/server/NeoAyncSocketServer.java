package com.neo.neoapp.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

//import android.util.Log;





import java.util.HashMap;

import android.content.Context;

import com.neo.neoandroidlib.NeoSocketSerializableUtils;
import com.neo.neoandroidlib.NeoThreadPool;
import com.neo.neoapp.NeoAppSetings;
import com.neo.neoapp.broadcasts.NeoAppBroadCastMessages;
import com.neo.neoapp.entity.Message;
import com.neo.neoapp.handlers.NeoAppUIThreadHandler;

public class NeoAyncSocketServer {
	private String Tag = "NeoAyncSocketServer";
	private Selector selector;
	private ServerSocketChannel server;
	private boolean isRunning;
	//for test
	private Charset charset=Charset.forName("UTF-8");  
	
	private Context mContext = null;
	public static HashMap socketMap =  new HashMap<String,SocketChannel>();
	
	public NeoAyncSocketServer(Context context){
		isRunning = true;
		mContext = context;
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(NeoAppSetings.port));
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			NeoThreadPool.getThreadPool().execute(
					new AcceptTask());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public static boolean send(SocketChannel client,Object content){
		byte[] bytes = NeoSocketSerializableUtils.objectToByteArray(content);
		
		if (bytes==null)
			return false;
		
		try {
			client.write(ByteBuffer.wrap(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean send(SocketChannel client,Message content){
		//content.setContent("send from socket server");
		byte[] bytes = NeoSocketSerializableUtils.objectToByteArray(content);
		
		if (bytes==null)
			return false;
		
		try {
			client.write(ByteBuffer.wrap(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void close(){
		isRunning = false;
		try {
			server.close();
			selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class AcceptTask implements Runnable{

		public AcceptTask() {
			// TODO Auto-generated constructor stub
			System.out.println("AcceptTask()");
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while(isRunning){
				//Log.v(Tag, "NeoAyncSocketServer run()");
				System.out.println("NeoAyncSocketServer:running");
				try {
					selector.select();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (SelectionKey sk:selector.selectedKeys()){
					selector.selectedKeys().remove(sk);
					if (sk.isAcceptable()){
						System.out.println("NeoAyncSocketServer:there is a connection");
						ServerSocketChannel serch = (ServerSocketChannel) sk.channel();
						SocketChannel client;
						try {
							client = serch.accept();
							client.configureBlocking(false);
							//client.write(ByteBuffer.wrap(new String("hello client").getBytes()));
							client.register(selector, SelectionKey.OP_READ);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					if (sk.isReadable()){
						System.out.println("NeoAyncSocketServer:got a msg");
						SocketChannel client = (SocketChannel) sk.channel();
						NeoThreadPool.getThreadPool().execute(
								new RecvTask(client));
					}
				}
			}
			
			System.out.println("NeoAyncSocketServer:terminated");
		}
		
	}
	
	private class RecvTask implements Runnable{
		private SocketChannel clent = null;
		
		public RecvTask(SocketChannel clent){
			this.clent = clent;
		}

		@Override
		public void run() {
			ByteBuffer bf = ByteBuffer.allocate(1024);
			// TODO Auto-generated method stub
			//bf.clear();
			
			/*String content = "";
			try {
				while(clent.read(bf)>0){  
					clent.read(bf);  
                    bf.flip();  
                    content+=charset.decode(bf);  
                } 
				System.out.println("msg:"+content);
				NeoAppBroadCastMessages.sendDynamicBroadCastMsg(mContext, content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			try {
				int readbytes = clent.read(bf);
				clent.register(selector, SelectionKey.OP_READ);
				if (readbytes<=0){
					return;
				}
				
				Message object = NeoSocketSerializableUtils.byteArrayToMessage(bf.array());
				if (object==null)
					return;
				
				synchronized(this){
					if (!socketMap.containsKey(object.getName()))
						socketMap.put(object.getName(),clent);
				}
				object.setMessageType(Message.MESSAGE_TYPE.RECEIVER);
				//object.setContent("i am coming from server receiver");
				NeoAppBroadCastMessages.sendDynamicBroadCastMsg(mContext, object);

			}catch (ClosedChannelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			}
			
		}
		
	}
	
}
