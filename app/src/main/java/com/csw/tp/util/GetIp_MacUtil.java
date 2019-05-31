package com.csw.tp.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/*
 * 获取本地IP、mac、或路由mac、当前路由工作信道
 * 适用于电视盒
 */
public class GetIp_MacUtil {
    
///////////////////////////////////////获取IP方法///////////////////////////////////////////////////////////////////////	
	/*
	 * 获取本地IP最佳方法，有网络情况下
	 */
	/**
	   * Get IP address from first non-localhost interface
	   * 
	   * @param ipv4
	   *            true=return ipv4, false=return ipv6
	   * @return address or empty string
	   */
	  public static String getNetIPAddress(boolean useIPv4,Context context) {
	          WifiManager wifimanage = (WifiManager)context.getSystemService(context.WIFI_SERVICE);// 获取WifiManager
	          // 检查wifi是否开启

	          NetUtil netUtil=new NetUtil(context);
	          if (netUtil.isWifiConnected(context)) {//如果连的是Wifi

	                  WifiInfo wifiinfo = wifimanage.getConnectionInfo();

	                  String wifiip = intToIp(wifiinfo.getIpAddress());
	                  Log.i("本机IP", "-------wifiip----" + wifiip);
	                  return wifiip;
	          }else {//如果插的网线
	                  String comstr = "ifconfig eth0";
	                  String ip = execRootCmd(comstr);
	                  Log.i("本机IP", "---process ifconfig eth0-----" + ip);
	                  final String myip = ip.substring(ip.indexOf("ip") + 2,
	                                  ip.indexOf("mask")).trim();
	                  return myip;
	                  
	          }
	  }
	  
		  private static String intToIp(int i) {
	          return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
	                          + "." + ((i >> 24) & 0xFF);
	  }
	  
	  protected static String execRootCmd(String paramString) {
	          DataInputStream dis = null;
	          Runtime r = Runtime.getRuntime();
	          try {
	                  // r.exec("su"); // get root
	                  StringBuilder sb = new StringBuilder();
	                  Process p = r.exec(paramString);
	                  InputStream input = p.getInputStream();
	                  dis = new DataInputStream(input);
	                  String content = null;
	                  while ((content = dis.readLine()) != null) {
	                          sb.append(content).append("\n");
	                  }
	                  // r.exec("exit"); Log.i("UERY", "sb = " + sb.toString());
	                  // localVector.add(sb.toString());
	                  return sb.toString();
	          } catch (IOException e) {
	                  e.printStackTrace();
	          } finally {
	                  if (dis != null) {
	                          try {
	                                  dis.close();
	                          } catch (IOException e) {
	                                  e.printStackTrace();
	                          }
	                  }
	          }
	          return null;
	  }
	  
 /*****************************************************************************************************/
	  /*
	   * 获取本地IP，插网线获取的IP地址跟断网的IP地址一样，Wifi连接获取的IP地址跟WiFimanager获取的IP一样，即无线IP
	   * 可获取断网时本地IP
	   */
		public static String getLocalHostIp() {
			String ipaddress = "";
			try {
				Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces();
				// 遍历所用的网络接口
				while (en.hasMoreElements()) {
					NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
					Enumeration<InetAddress> inet = nif.getInetAddresses();
					// 遍历每一个接口绑定的所有ip
					while (inet.hasMoreElements()) {
						InetAddress ip = inet.nextElement();
						if (!ip.isLoopbackAddress()
								&& InetAddressUtils.isIPv4Address(ip
										.getHostAddress())) {
							return ipaddress = "本机的ip是" + "：" + ip.getHostAddress();
						}
					}

				}
			} catch (SocketException e) {
				Log.e("feige", "获取本地ip地址失败");
				e.printStackTrace();
			}
			return ipaddress;

		}
	  
///////////////////////////////////////获取mac方法///////////////////////////////////////////////////////////////////////	

		/*
		 * 获取本机mac地址，转大写不要冒号,Wifi环境下，断网也可获取
		 */
		// 得到本机Mac地址
		public String getLocalMac(Context context) {
			String mac = "";
			String result="";
			// 获取wifi管理器
			WifiManager wifiMng = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfor = wifiMng.getConnectionInfo();
			mac = wifiInfor.getMacAddress();
			if(mac.length()>1){
				mac = mac.replaceAll(" ", "");
		  		result = "";
		  		String[] tmp = mac.split(":");
		  		for(int i = 0;i<tmp.length;++i){
		  			result +=tmp[i];
		  		}
		  	}   	
			return result.toUpperCase();
		}
		/***********************************************************************************************************/
		/*
		 * 根据Ip地址获取本地mac，跟上面获取一样，插网线、断网、Wifi环境下都一样
		 */
		//根据IP获取本地Mac
		  @SuppressLint("NewApi")
		public static String getLocalMacAddressFromIp(Context context) {
		      String mac_s= "";
		     try {
		          byte[] mac;
		          NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
		          mac = ne.getHardwareAddress();
		          mac_s = byte2hex(mac);
		     } catch (Exception e) {
		         e.printStackTrace();
		     }
		      return mac_s;
		  }
		  
		  @SuppressLint("NewApi")
		public static  String byte2hex(byte[] b) {
		       StringBuffer hs = new StringBuffer(b.length);
		       String stmp = "";
		       int len = b.length;
		       for (int n = 0; n < len; n++) {
		        stmp = Integer.toHexString(b[n] & 0xFF);
		        if (stmp.length() == 1)
		         hs = hs.append("0").append(stmp);
		        else {
		         hs = hs.append(stmp);
		        }
		       }
		       return String.valueOf(hs);
		      }

		//获取本地IP
		  public static String getLocalIpAddress() {  
		         try {  
		             for (Enumeration<NetworkInterface> en = NetworkInterface  
		                             .getNetworkInterfaces(); en.hasMoreElements();) {  
		                         NetworkInterface intf = en.nextElement();  
		                        for (Enumeration<InetAddress> enumIpAddr = intf  
		                                 .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
		                             InetAddress inetAddress = enumIpAddr.nextElement();  
		                             if (!inetAddress.isLoopbackAddress()) {  
		                             return inetAddress.getHostAddress().toString();  
		                             }  
		                        }  
		                     }  
		                 } catch (SocketException ex) {  
		                     Log.e("WifiPreference IpAddress", ex.toString());  
		                 }  
		         
		              return null;  
		 }  
		  /***********************************************************************************************************/
		/*
		 * 插网线可以获取，应该是eth0网口的mac地址，不插网线也可以获取，跟本机mac不同
		 */
		    
		    public String getMacAddressEht0(){   
		        String result = "";     
		        String Mac = "";
		        result = callCmd("busybox ifconfig","HWaddr");
		        
		        //如果返回的result == null，则说明网络不可取
		        if(result==null){
		        	return "网络出错，请检查网络";
		        }
		        
		        //对该行数据进行解析
		        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
		        if(result.length()>0 && result.contains("HWaddr")==true){
		        	Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
		        	Log.i("test","Mac:"+Mac+" Mac.length: "+Mac.length());
		        	
		        	if(Mac.length()>1){
		        		Mac = Mac.replaceAll(" ", "");
		        		result = "";
		        		String[] tmp = Mac.split(":");
		        		for(int i = 0;i<tmp.length;++i){
		        			result +=tmp[i];
		        		}
		        	}
		        	Log.i("test",result+" result.length: "+result.length());        	
		        }
		        return result;
		    }   

		    
		    public String callCmd(String cmd,String filter) {   
		        String result = "";   
		        String line = "";   
		        try {
		        	Process proc = Runtime.getRuntime().exec(cmd);
		            InputStreamReader is = new InputStreamReader(proc.getInputStream());   
		            BufferedReader br = new BufferedReader (is);   
		            
		            //执行命令cmd，只取结果中含有filter的这一行
		            while ((line = br.readLine ()) != null && line.contains(filter)== false) {   
		            	//result += line;
		            	Log.i("test","line: "+line);
		            }
		            
		            result = line;
		            Log.i("test","result: "+result);
		        }   
		        catch(Exception e) {   
		            e.printStackTrace();   
		        }   
		        return result;   
		    }
		 
		  /**************************************************************************************************/
		    
		    /*
		     * 插网线可以获取，应该是eth0网口的mac地址，不插网线也可以获取，跟本机mac不同,方法同上
		     */
		    public static String getMacAddressEth0(){  
		        try {   
		            return loadFileAsString("/sys/class/net/eth0/address") .toUpperCase().substring(0, 17);  
		            } catch (IOException e) {   
		                e.printStackTrace();   
		                return null;  
		            }  
		    }  
		    public static String loadFileAsString(String filePath) throws java.io.IOException{  
		        StringBuffer fileData = new StringBuffer(1000);  
		        BufferedReader reader = new BufferedReader(new FileReader(filePath));   
		        char[] buf = new char[1024]; int numRead=0;   
		        while((numRead=reader.read(buf)) != -1){   
		            String readData = String.valueOf(buf, 0, numRead);   
		            fileData.append(readData);   
		        }   
		        reader.close();   
		        return fileData.toString();  
		    }/** Get the STB MacAddress*/  
		      
		    /**************************************************************************************************/
		    /*
		     * 获取设备所连路由器mac地址
		     */
		    public String getLuyouMac(Context context) {
				
				String mac="";
				String result="";
				WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wm.getConnectionInfo();
				mac=wifiInfo.getBSSID();
				if(mac.length()>1){
					mac = mac.replaceAll(" ", "");
			  		result = "";
			  		String[] tmp = mac.split(":");
			  		for(int i = 0;i<tmp.length;++i){
			  			result +=tmp[i];
			  		}
			  	}   	
				return result.toUpperCase();
			}

		    ////////////////////////////////////获取当前路由工作信道///////////////////////////////////////////
		    
		    /*
			 * 获取当前的工作信道,wifi环境下
			 */
			
			public int getCurrentWifiChanel(Context context) {
				WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wm.getConnectionInfo();
				String currentBssid = wifiInfo.getBSSID();// 当前热点的mac
				List<ScanResult> wifiList = wm.getScanResults();
				int currentFrequency = 0;// 当前通信信道的频率
				int currentChannel;// 当前通信信道的频率

				for (int i = 0; i < wifiList.size(); i++) {

					if (wifiList.get(i).BSSID.equals(currentBssid)) {
						currentFrequency = wifiList.get(i).frequency;
					}
				}

				switch (currentFrequency) {
				case 2412:
					currentChannel = 1;
					break;
				case 2417:
					currentChannel = 2;
					break;
				case 2422:
					currentChannel = 3;
					break;
				case 2427:
					currentChannel = 4;
					break;
				case 2432:
					currentChannel = 5;
					break;
				case 2437:
					currentChannel = 6;
					break;
				case 2442:
					currentChannel = 7;
					break;
				case 2447:
					currentChannel = 8;
					break;
				case 2452:
					currentChannel = 9;
					break;
				case 2457:
					currentChannel = 10;
					break;
				case 2462:
					currentChannel = 11;
					break;
				case 2467:
					currentChannel = 12;
					break;
				case 2472:
					currentChannel = 13;
					break;
				default:
					currentChannel = 0;
					break;
				}

				return currentChannel;
			}
		  
		  
}
