package com.csw.tp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.io.InputStream;
import java.util.regex.Pattern;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;

 

public class CpuManager {

 

        // 锟斤拷取CPU锟斤拷锟狡碉拷剩锟斤拷锟轿籏HZ锟斤拷

     // "/system/bin/cat" 锟斤拷锟斤拷锟斤拷

     // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 锟芥储锟斤拷锟狡碉拷实锟斤拷募锟斤拷锟铰凤拷锟?

        public static String getMaxCpuFreq() {

                String result = "";

                ProcessBuilder cmd;

                try {

                        String[] args = { "/system/bin/cat",

                                        "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };

                        cmd = new ProcessBuilder(args);

                        Process process = cmd.start();

                        InputStream in = process.getInputStream();

                        byte[] re = new byte[24];

                        while (in.read(re) != -1) {

                                result = result + new String(re);

                        }

                        in.close();

                } catch (IOException ex) {

                        ex.printStackTrace();

                        result = "N/A";

                }

                return result.trim();

        }

 

         // 锟斤拷取CPU锟斤拷小频锟绞ｏ拷锟斤拷位KHZ锟斤拷

        public static String getMinCpuFreq() {

                String result = "";

                ProcessBuilder cmd;

                try {

                        String[] args = { "/system/bin/cat",

                                        "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };

                        cmd = new ProcessBuilder(args);

                        Process process = cmd.start();

                        InputStream in = process.getInputStream();

                        byte[] re = new byte[24];

                        while (in.read(re) != -1) {

                                result = result + new String(re);

                        }

                        in.close();

                } catch (IOException ex) {

                        ex.printStackTrace();

                        result = "N/A";

                }

                return result.trim();

        }

 

         // 实时锟斤拷取CPU锟斤拷前频锟绞ｏ拷锟斤拷位KHZ锟斤拷

        public static String getCurCpuFreq() {

                String result = "N/A";

                try {

                        FileReader fr = new FileReader(

                                        "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");

                        BufferedReader br = new BufferedReader(fr);

                        String text = br.readLine();

                        result = text.trim();

                } catch (FileNotFoundException e) {

                        e.printStackTrace();

                } catch (IOException e) {

                        e.printStackTrace();

                }

                return result;

        }

 

        // 锟斤拷取CPU锟斤拷锟斤拷

        public static String getCpuName() {

                try {

                        FileReader fr = new FileReader("/proc/cpuinfo");

                        BufferedReader br = new BufferedReader(fr);

                        String text = br.readLine();

                        String[] array = text.split(":\\s+", 2);

                        for (int i = 0; i < array.length; i++) {

                        }

                        return array[1];

                } catch (FileNotFoundException e) {

                        e.printStackTrace();

                } catch (IOException e) {

                        e.printStackTrace();

                }

                return null;

        }
        
        public void getTotalMemory() {   
            String str1 = "/proc/meminfo";   
            String str2="";   
            try {   
                FileReader fr = new FileReader(str1);   
                BufferedReader localBufferedReader = new BufferedReader(fr, 8192);   
                while ((str2 = localBufferedReader.readLine()) != null) {   
               
                }   
            } catch (IOException e) {   
            }   
        } 
        
        public static long[] getRomMemroy() {   
            long[] romInfo = new long[2];   
            //Total rom memory   
            romInfo[0] = getTotalInternalMemorySize();   
        
            //Available rom memory   
            File path = Environment.getDataDirectory();   
            StatFs stat = new StatFs(path.getPath());   
            long blockSize = stat.getBlockSize();   
            long availableBlocks = stat.getAvailableBlocks();   
            romInfo[1] = blockSize * availableBlocks;   
            getVersion();   
            return romInfo;   
        }   
        
        public static long getTotalInternalMemorySize() {   
            File path = Environment.getDataDirectory();   
            StatFs stat = new StatFs(path.getPath());   
            long blockSize = stat.getBlockSize();   
            long totalBlocks = stat.getBlockCount();   
            return totalBlocks * blockSize;   
        }         
        
        public static long[] getSDCardMemory() {   
            long[] sdCardInfo=new long[2];   
            String state = Environment.getExternalStorageState();   
            if (Environment.MEDIA_MOUNTED.equals(state)) {   
                File sdcardDir = Environment.getExternalStorageDirectory();   
                StatFs sf = new StatFs(sdcardDir.getPath());   
                long bSize = sf.getBlockSize();   
                long bCount = sf.getBlockCount();   
                long availBlocks = sf.getAvailableBlocks();   
        
                sdCardInfo[0] = bSize * bCount;//锟杰达拷小   
                sdCardInfo[1] = bSize * availBlocks;//锟斤拷锟矫达拷小   
            }   
            return sdCardInfo;   
        } 
        
        public static String[] getVersion(){   
            String[] version={"null","null","null","null"};   
            String str1 = "/proc/version";   
            String str2;   
            String[] arrayOfString;   
            try {   
                FileReader localFileReader = new FileReader(str1);   
                BufferedReader localBufferedReader = new BufferedReader(   
                        localFileReader, 8192);   
                str2 = localBufferedReader.readLine();   
                arrayOfString = str2.split("\\s+");   
                version[0]=arrayOfString[2];//KernelVersion   
                localBufferedReader.close();   
            } catch (IOException e) {   
            }   
            version[1] = Build.VERSION.RELEASE;// firmware version   
            version[2]=Build.MODEL;//model   
            version[3]=Build.DISPLAY;//system version   
            return version;   
        }   
     
        
//        public String[] getOtherInfo(){   
//            String[] other={"null","null"};   
//               WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);   
//               WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
//               if(wifiInfo.getMacAddress()!=null){   
//                other[0]=wifiInfo.getMacAddress();   
//            } else {   
//                other[0] = "Fail";   
//            }   
//            other[1] = getTimes();   
//               return other;   
//        }   
//        private String getTimes() {   
//            long ut = SystemClock.elapsedRealtime() / 1000;   
//            if (ut == 0) {   
//                ut = 1;   
//            }   
//            int m = (int) ((ut / 60) % 60);   
//            int h = (int) ((ut / 3600));   
//            return h + " " + mContext.getString(R.string.info_times_hour) + m + " "  
//                    + mContext.getString(R.string.info_times_minute);   
//        }     

     // 判断CPU核心数
    	public static int getNumCores() {
    		class CpuFilter implements FileFilter {
    			@Override
    			public boolean accept(File pathname) {
    				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
    					return true;
    				}
    				return false;
    			}
    		}

    		try {
    			File dir = new File("/sys/devices/system/cpu/");
    			File[] files = dir.listFiles(new CpuFilter());
    			Log.d(null, "CPU Count: " + files.length);
    			return files.length;
    		} catch (Exception e) {
    			Log.d(null, "CPU Count: Failed.");
    			e.printStackTrace();
    			return 1;
    		}
    	}

    	// 获取内存大小
    	public static long getmem_TOLAL() {
    		long mTotal;
    		// /proc/meminfo读出的内核信息进行解释
    		String path = "/proc/meminfo";
    		String content = null;
    		BufferedReader br = null;
    		try {
    			br = new BufferedReader(new FileReader(path), 8);
    			String line;
    			if ((line = br.readLine()) != null) {
    				content = line;
    			}
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			if (br != null) {
    				try {
    					br.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		// beginIndex
    		int begin = content.indexOf(':');
    		// endIndex
    		int end = content.indexOf('k');
    		// 截取字符串信息

    		content = content.substring(begin + 1, end).trim();
    		mTotal = Integer.parseInt(content);
    		long x = mTotal / 1024;
    		return x;
    	}
    	
}

