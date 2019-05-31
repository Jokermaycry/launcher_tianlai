package com.weather.manager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.wyf.util.Constant;


public class WeatherManager {
	
	
	/**
	 * 获取公网IP地址 
	 */
		public static String GetNetIp()
		{
		    URL infoUrl = null;
		    InputStream inStream = null;
		    try
		    { 
		        infoUrl = new URL("http://www.ip138.com");
		    //    infoUrl = new URL("http://iframe.ip138.com/ic.asp");
		        URLConnection connection = infoUrl.openConnection();
		        HttpURLConnection httpConnection = (HttpURLConnection)connection;
		        System.out.println(httpConnection.getReadTimeout());
		        httpConnection.setConnectTimeout(10000);
		        httpConnection.setReadTimeout(10000); 
			        System.out.println(httpConnection); 
		        int responseCode = httpConnection.getResponseCode();
		        if(responseCode == HttpURLConnection.HTTP_OK)
		        {   
		            inStream = httpConnection.getInputStream(); 
			            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
		            StringBuilder strber = new StringBuilder();
		            String line = null; 
		            while ((line = reader.readLine()) != null) 
		                strber.append(line + "\n");
		            inStream.close();
		 
		            //从反馈的结果中提取出IP地址
		            int start = strber.indexOf("[");
		            int end = strber.indexOf("]", start + 1);
		            line = strber.substring(start + 1, end);
		          //  System.out.println(line);
		           // int ha = strber.indexOf("<center>");
		           // int hs = strber.indexOf("</center>");
		          //  String h = strber.substring(ha+35,hs);
		          //  System.out.println(h.getBytes("UTF-8").toString());
		            return line; 
		        } 
		    }
		    catch(MalformedURLException e) {
		        e.printStackTrace();
		    }
		    catch (IOException e) {
		    e.printStackTrace();    
		     //   System.out.println(e);
		    }
		    return null; 
		} 

		//获取城市
		public static String getCityByIp(String x) {  
	        try {  
	        	//String x = GetNetIp();
	        	if(!x.equals("")){
	            URL url = new URL("http://whois.pconline.com.cn/ip.jsp?ip=" + x);  
	            HttpURLConnection connect = (HttpURLConnection) url  
	                    .openConnection();  
	            connect.setConnectTimeout(10000);
	            connect.setReadTimeout(10000);
		            InputStream is = connect.getInputStream();  
	             ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	            byte[] buff = new byte[256];  
	            int rc = 0;  
	            while ((rc = is.read(buff, 0, 256)) > 0) {  
	                outStream.write(buff, 0, rc);  
	                   
	            }   
	            //System.out.println(outStream);  
	            byte[] b = outStream.toByteArray();  
	              
	            // 关闭   
	            outStream.close();   
	            is.close();  
	            connect.disconnect();  
	            String address;
	             address = new String(b,"GBK");  
	             System.out.println(address);
	            return address;
	        	}
	          
	        } catch (Exception e) {   
	            e.printStackTrace(); 
	            return null;
	        }
	        return null;
			  
	    } 
		public static String getAdd(String address){
			if(address==null||address.equals("")){
				return null;
			}
			int a = address.indexOf("省");
			int b = address.indexOf("市");
			String city = address.substring(a+1, b);
			return city;
		}
		public static String getAdd1(String address){
			int a = address.indexOf(".");
			int b = address.length();
			String city = address.substring(a+1,b);
			return city;
		}
		//获得天气
		/*public static Map getTianqi1(String str){
			
			 try {
//				str=new String(str.getBytes("GBK"),"ISO-8859-1");
//				 str=new String(str.getBytes("GBK"));
				 str=java.net.URLEncoder.encode(str,"gb2312");
	 		} catch (UnsupportedEncodingException e1) { 
					// TODO Auto-generated catch block
				e1.printStackTrace();  
			}    
			String link = "http://php.weather.sina.com.cn/xml.php?city="+str+"&password=DJOYnieT8234jlsK&day=0";  
	        URL url=null;       
	        try {    
	            url = new URL(link);     
	             System.out.println(url);     
	            XmlParser parser = new XmlParser(url);  

	           Constant.map =  parser.getValue(Constant.nodes);
	           return Constant.map;
	             
	             
	         } catch (Exception e) {   
	            e.printStackTrace();    
	        } 
			return null;    
	      
		}  */
		
private static final String TAG="WeatherManager";
		
		public static Map getTianqi1(String str){
              String weatherInfo=str;
			
              if(weatherInfo.equals("error")){
            	  return null;
              }
              JSONTokener jsonParser = new JSONTokener(weatherInfo); 
              
              JSONObject object1 = null;
  			try {
  				object1 = (JSONObject) jsonParser.nextValue();
  			} catch (JSONException e2) {
  				// TODO Auto-generated catch block
  				e2.printStackTrace();
  			}    
  			
  			if(object1!=null){
  		    // 接下来的就是JSON对象的操作了    
  		    try {
  				String status=object1.getString("status");
  				String msg=object1.getString("msg");
  				String result=object1.getString("result");
  				Log.d(TAG, "status=="+status);
  				Log.d(TAG, "msg=="+msg);
  				if(result.length()>30){
  					Log.d(TAG, "result=="+result.substring(0,29)+".....");
  				}else{
  					Log.d(TAG, "result=="+result);
  				}
  				if(!msg.equals("ok")){
  					Log.d(TAG, "获取天气信息出错");
  					return null;
  				}
				JSONTokener jsonParser2 = new JSONTokener(result);    
				JSONObject object2 = (JSONObject) jsonParser2.nextValue();
				if(object2==null){
					Log.d(TAG, "object2==null");
					return null;
				}
				String city=object2.getString("city");
				String weather=object2.getString("weather");
				String currentTemp=object2.getString("temp");
				String tempHigh=object2.getString("temphigh");
				String tempLow=object2.getString("templow");
				
				Log.d(TAG, "Current city:"+city);
				Log.d(TAG, "Current weather:"+weather);
				Log.d(TAG, "Current temp:"+currentTemp);
				Log.d(TAG, "High temp:"+tempHigh);
				Log.d(TAG, "Low temp:"+tempLow);

				String aqi=object2.getString("aqi");
				JSONTokener jsonParser3 = new JSONTokener(aqi);    
				JSONObject object3 = (JSONObject) jsonParser3.nextValue();
				if(object3==null){
					Log.d(TAG, "object3==null");
					return null;
				}
				String ipm2_5=object3.getString("ipm2_5");
				Log.d(TAG, "PM2.5:"+ipm2_5);
				
				
				
				for (int i = 0; i < Constant.nodes.length; i++) {  
	  			    Constant.map.put(Constant.nodes[i], null);  
		        }  
	  		     Constant.map.put(Constant.nodes[0],weather);
	  		     Constant.map.put(Constant.nodes[1],tempHigh);
	  		     Constant.map.put(Constant.nodes[2],tempLow);
	  		     Constant.map.put(Constant.nodes[3],ipm2_5);
				 return Constant.map;
  		    }catch(Exception e){
  		    	Log.d(TAG, "Json is wrong");
  		    	e.printStackTrace();
  		    }
  			}
              
              
			return null;
		}
		

}
