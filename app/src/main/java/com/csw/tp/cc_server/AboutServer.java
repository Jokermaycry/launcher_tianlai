package com.csw.tp.cc_server;

import java.io.IOException;
import java.io.OutputStream;

import android.util.Log;

public class AboutServer {
    int serverPort;
    SocketServer socketServer = new SocketServer();// TCPServer
    
	public AboutServer(int serverPort) {
		super();
		this.serverPort = serverPort;
	}

	public AboutServer() {
		super();
	}
    
	/*
	 * 创建TCP Server
	 */
	public void creatTcpServer() {
		tcpServerThread.start();
	}
	
	/*
	 * 创建TCP Server连接
	 */
	Thread tcpServerThread = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			socketServer.startTcpServer(serverPort);
		}
	});
	/*
	 * 停止TCP Server
	 */
	public void stopTcpServer(){
		if (SocketServer.serverSocket != null) {
			try {
				SocketServer.serverSocket.close();
				SocketServer.serverSocket=null;		
				while(selectCurrentClientCounnt()!=0){//只要有活跃的客户端，就把它们的输入流关了
					for(int i=0;i<SocketServer.socketList.size();i++){
						SocketServer.socketList.get(i).getInputStream().close();//关闭客户端连接的输入流
					}
				}
				
				Log.d("关闭TCP Server", "关闭成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("关闭TCP Server", "未知错误，关闭失败");
				
			}
		} else {
			Log.d("关闭TCP Server", "没有TCP Server连接，关闭应该是成功还是失败呢？");
		}
	}
	/*
	 * 查询当前客户端连接的个数
	 * 遍历当前所以客户端连接，发现获取不到客户端流的直接干掉
	 */
		public int selectCurrentClientCounnt(){
			if(SocketServer.socketList.size()!=0){
				for (int i = 0; i < SocketServer.socketList.size(); i++) {
					OutputStream os = null;
					try {
						   os = SocketServer.socketList.get(i)
								.getOutputStream();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SocketServer.socketList.remove(i);// 移除获取不到流的连接
					}
				}
				return SocketServer.socketList.size();
			}else{
				return 0;//当前没有客户端连接
			}	
		}
	
}
