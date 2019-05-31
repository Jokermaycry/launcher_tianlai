package com.wyf.launch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Utils
{
  public static final int IO_BUFFER_SIZE = 8192;
  private static Context mContext;

  public static Boolean checkNetAvailable(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()))
      Log.e("checkConnection", "checkConnection - no connection found");
    for (Boolean localBoolean = Boolean.valueOf(false); ; localBoolean = Boolean.valueOf(true))
      return localBoolean;
  }

 /* public static void disableConnectionReuseIfNecessary()
  {
    if (hasHttpConnectionBug())
      System.setProperty("http.keepAlive", "false");
  }*/

  @SuppressLint({"NewApi"})
  public static int getBitmapSize(Bitmap paramBitmap)
  {
    return paramBitmap.getRowBytes() * paramBitmap.getHeight();
  }

  public static float getDensity(Activity paramActivity)
  {
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    paramActivity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    return localDisplayMetrics.density;
  }

  @SuppressLint({"NewApi"})
  public static File getExternalCacheDir(Context paramContext)
  {
    String str = "/Android/data/" + paramContext.getPackageName() + "/cache/";
    File localFile = new File(Environment.getExternalStorageDirectory().getPath() + str);
    if ((!localFile.exists()) && (!localFile.mkdirs()))
      localFile = paramContext.getCacheDir();
    Log.d("Utils", localFile.getPath());
    return localFile;
  }

  public static int getMemoryClass(Context paramContext)
  {
    return ((ActivityManager)paramContext.getSystemService("activity")).getMemoryClass();
  }

 /* @SuppressLint({"NewApi"})
  public static long getUsableSpace(File paramFile)
  {
    long l;
    if (Build.VERSION.SDK_INT >= 9)
      l = paramFile.getUsableSpace();
    while (true)
    {
      return l;
      StatFs localStatFs = new StatFs(paramFile.getPath());
      l = localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
    }
  }*/

  public static String getVerName(Context paramContext)
  {
    String str = "";
    try
    {
      str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
      return str;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("getVerName", "error__" + localException.toString());
    }
  }

  public static Map<String, Integer> getViewLocation(View paramView)
  {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    HashMap localHashMap = new HashMap();
    localHashMap.put("x", Integer.valueOf(arrayOfInt[0]));
    localHashMap.put("y", Integer.valueOf(arrayOfInt[1]));
    return localHashMap;
  }

  public static boolean hasActionBar()
  {
    return false;
  }

 /* public static boolean hasExternalCacheDir()
  {
    if (Build.VERSION.SDK_INT >= 8);
    for (int i = 1; ; i = 0)
      return i;
  }

  public static boolean hasHttpConnectionBug()
  {
    if (Build.VERSION.SDK_INT < 8);
    for (int i = 1; ; i = 0)
      return i;
  }
*/
  public static Boolean isExternalDirAccessable(Context paramContext)
  {
    String str = "/Android/data/" + paramContext.getPackageName() + "/cache/";
    if (!new File(Environment.getExternalStorageDirectory().getPath() + str).mkdirs());
    for (Boolean localBoolean = Boolean.valueOf(false); ; localBoolean = Boolean.valueOf(true))
      return localBoolean;
  }

  @SuppressLint({"NewApi"})
  public static boolean isExternalStorageRemovable()
  {
    if (Build.VERSION.SDK_INT >= 9);
    for (boolean bool = Environment.isExternalStorageRemovable(); ; bool = true)
      return bool;
  }

  public static boolean isNetworkConnected(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if (localNetworkInfo != null);
    for (boolean bool = localNetworkInfo.isAvailable(); ; bool = false)
      return bool;
  }

 /* public static boolean isWifi(Context paramContext)
  {
    int i;
    if (paramContext != null)
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo != null)
        if (1 == localNetworkInfo.getType())
          i = 1;
    }
    while (true)
    {
      return i;
      i = 0;
      continue;
      i = 0;
    }
  }*/

  /*public static void notifityNetWork(Context paramContext)
  {
    mContext = paramContext;
    Dialog localDialog = new Dialog(paramContext, 2131296266);
    localDialog.setContentView(2130903051);
    ((TextView)localDialog.findViewById(2131361852)).setText("网络不可用，立即设置？");
    localDialog.findViewById(2131361853).setFocusable(true);
    localDialog.findViewById(2131361853).setFocusableInTouchMode(true);
    localDialog.findViewById(2131361853).requestFocus();
    localDialog.findViewById(2131361853).setOnClickListener(new View.OnClickListener(localDialog)
    {
      public void onClick(View paramView)
      {
        Utils.this.dismiss();
        Intent localIntent = new Intent(Utils.mContext, ActivityWifiManage.class);
        try
        {
          Utils.mContext.startActivity(localIntent);
          label27: return;
        }
        catch (Exception localException)
        { 
          break label27;
        }
      }
    });
    localDialog.findViewById(2131361854).setOnClickListener(new View.OnClickListener(localDialog)
    {
      public void onClick(View paramView)
      {
        Utils.this.dismiss();
      }
    });
    localDialog.show();
  }*/
}