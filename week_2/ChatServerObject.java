package chat;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

class ChatServerObject 
{ 
	private ServerSocket serverSocket;
	private List <ChatHandlerObject> list;
	
	int socketPort = 14096;
	public ChatServerObject(){
		try{
			serverSocket = new ServerSocket ();
			serverSocket.bind(new InetSocketAddress("192.168.35.158", socketPort));
			System.out.println("서버 준비 완료");
			list = new  ArrayList<ChatHandlerObject>();
			while(true){
				Socket socket = serverSocket.accept();
				ChatHandlerObject handler = new  ChatHandlerObject(socket,list);
				handler.start();
				list.add(handler);
			}//while
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) 
	{
		new ChatServerObject();
	}
}