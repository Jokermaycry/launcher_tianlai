package com.csw.tp.cc_server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.csw.tp.bean.DataEntity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketServer {

	public static int connectConunt = 0;// 当前的连接数
	public static ServerSocket serverSocket = null;

	// 定义保存所有连接Socket的ArrayList
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	public ServerThread serverThread;

	/*
	 * 创建TCP ServerSocket连接
	 */
	public ServerSocket creatServerSocket(int port) {
		ServerSocket serverSocket = null;
		// 监听端口
		System.out.println("创建TCPServer连接：");
		try {
			serverSocket = new ServerSocket(port);
			return serverSocket;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("创建TCPServer连接", "创建TCPServer连接失败");

			return null;
		}

	}

	Socket socket = null;

	/*
	 * 开启TCP Server服务器
	 */
	public void startTcpServer(int port) {
		System.out.println("SocketServer的startTcpServer方法");

		try {
			serverSocket = creatServerSocket(port);
			if (serverSocket != null) {
				serverHandler.sendEmptyMessage(1);
				while (true) {

					if (!serverSocket.isClosed()) {
						// 获得连接
						Log.i("TCP Server创建成功", "等待客户端连接");
						socket = serverSocket.accept();
						serverHandler.sendEmptyMessage(2);
					} else {
						Log.d("TCP Server等待连接", "ServerSocket已经关闭");
						break;
					}

				}
			} else {
				serverHandler.sendEmptyMessage(0);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("TCP Server创建", "出现异常");
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
			}
		}
	}

	/*
	 * 关闭相应连接
	 */
	public void closeSocket() {

	}

	Handler serverHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				Log.d("开启TCPServer", "创建TCPServer连接失败");
				break;
			case 1:

				Log.d("开启TCPServer", "创建TCPServer连接成功");
				break;
			case 2:
				AboutServer someThread = new AboutServer();
				int currentActiveClientCount = someThread
						.selectCurrentClientCounnt();// 当前活跃的客户端连接数目
				if (currentActiveClientCount < 8) {
					socketList.add(socket);
					// connectConunt++;
					Log.d("等待连接", "连接成功！");
					// 启动线程，处理连接
					serverThread = new ServerThread(socket);
					if (serverThread.isAlive()) {
						Log.d("创建数据处理线程类", "线程存在，不启动");
					} else {
						serverThread.start();
					}
				} else {
					try {
						if (socket != null) {
							socket.close();// 如果超过8个了，关掉客户端连接
							socket = null;// 给它空
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.d("开启一个TCP Server连接", "最大连接数不超过8个，连接失败");
				}
				break;
			case 3:

				break;
			}
			super.handleMessage(msg);
		}
	};

	public static void SendDataClient( final String data){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			 if (socketList.size()>0) {
				 for (int j = 0; j < socketList.size(); j++) {
					 try {
						OutputStream os = SocketServer.socketList.get(j).getOutputStream();
						byte[] dataByte = data.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
				
			}
				
			}
		}).start();
	}
	
}
