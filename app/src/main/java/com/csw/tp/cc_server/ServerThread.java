package com.csw.tp.cc_server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongqin.tianlai.R;
import com.csw.tp.activity.MusicplayAct;
import com.csw.tp.babei.wyf.model.Gequinfo;
import com.csw.tp.babei.wyf.model.Getallmusicinfo;
import com.csw.tp.bean.DataEntity;
import com.csw.tp.bean.GetAppInfo;
import com.csw.tp.bean.MusicInfoBook;
import com.csw.tp.bean.MusicInfoEntity;
import com.csw.tp.bean.MusicSavedInfo;
import com.csw.tp.bean.RootCmd;
import com.csw.tp.bean.SocketDataPackage;
import com.csw.tp.bean.SystemInfo;
import com.csw.tp.bean.TimeSetInfoBook;
import com.csw.tp.bean.TouchModel;
import com.csw.tp.clean.AppUtil;
import com.csw.tp.util.FileReadWriteUtil;
import com.csw.tp.util.Selectpopwindow;
import com.csw.tp.util.downloadApkThread;
import com.example.system.qiActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyf.util.SharedPreferencesUtils;

public class ServerThread extends Thread {
	Socket socket;
	InputStream is;
	OutputStream os;
	String receiveClientData;
	 WindowManager	 wManager;
	 WindowManager.LayoutParams mParams	 ;
	 View myview;
	// SnapshotPicturesThread picturesThread = new SnapshotPicturesThread();

	private Gson gson = new Gson();

	public static Context Maincontext;

	public static Context IntentMaincontext;
	public static Context AllMaincontext;

	public Bitmap mScreenBitmap;
	public static Bitmap mScreenBitmap_buffer[] = new Bitmap[5];
	public static int write_cishu = 0;

	public static double ScaleValue;
	public static double ScreenScale;

	TouchModel model = new TouchModel();
	private boolean flag = true;

	/** */
	String jishenneicun = "";
	String neicundaxiao = "";
	String hexinshu = "";
	int flashSum = 0;
	int flashSize = 0;
	float flashSize_2;
	/** */
	private AudioManager audioManager;// 音量管理者
	private SharedPreferencesUtils preferencesUtils;

	/* 下载保存路径 */
	private String mSavePath;

	private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
    public boolean downflag=false;//下载标记
    private GetAppInfo  getAppInfo;
	public ServerThread(Socket socket) {
		this.socket = socket;
		DataEntity.currentSocket = this.socket;
		Maincontext = DataEntity.currentContext;
		// System.out.println("aaaaaaaaaaaaaaaaaaaa"+DataEntity.currentSocket.toString());
		System.out.println("一个服务端Server线程启动了");
		if (DataEntity.currentSocket == null) {
			System.out.println("scoket是空的哦亲!");
		}
		getAppInfo=new GetAppInfo(Maincontext);
		// os = socket.getOutputStream();
		// is = socket.getInputStream();

	}

	/*
	 * 将对数据的逻辑处理写在run()方法中
	 */
	public void run() {
		byte[] b = new byte[1024];

		try {
			os = socket.getOutputStream();
			DataEntity.currentos = os;
			is = socket.getInputStream();
			if (Maincontext != null) {
				AllMaincontext = Maincontext;
			} else {
				AllMaincontext = IntentMaincontext;
			}
			/** */
			audioManager = (AudioManager) AllMaincontext
					.getSystemService(Context.AUDIO_SERVICE);
			// int n = is.read(b);// 读取数据,得到数据的长度
			int hasReadLength = 0;// 已经读取的数据长度
			int dataLength = 0;// 数据本身的长度
			while ((hasReadLength = readFromClient(b)) > 0) {
				dataLength = hasReadLength;
				System.out.println("服务端有获取数据");
				// 逻辑处理，将有效数据拷贝到数组response中
				byte[] response = new byte[dataLength];
				System.arraycopy(b, 0, response, 0, dataLength);
				System.out
				.println("线程处理" + new String(response, 0, dataLength));
				String currentRe2 = new String(response, "GBK");
				if(currentRe2.contains("sendPlayList:")&&!currentRe2.contains(":sendPlayList")){
					receiveClientData += currentRe2;
					System.out.println("receiveClientData1:"+receiveClientData);
					continue;
				}
				if (null!=receiveClientData&&receiveClientData.contains("sendPlayList:")&&!receiveClientData.contains(":sendPlayList")) {
					receiveClientData += currentRe2;
					if (receiveClientData.contains("sendPlayList:")&&receiveClientData.contains(":sendPlayList")) {
						receiveClientData = receiveClientData;
					}else {
						continue;
					}
					System.out.println("receiveClientData2:"+receiveClientData);
					
				}
				else{
				receiveClientData = currentRe2;
				}
				if (Maincontext != null) {
					AllMaincontext = Maincontext;
				} else {
					AllMaincontext = IntentMaincontext;
				}
				  System.out.println("IntentMaincontext="+IntentMaincontext);
				if (!receiveClientData.contains("{")) {

				} else {

					if (receiveClientData.contains("comeFromMobile")
							&& receiveClientData.contains("finely")) {
						model = null;// 为了避免receiveClientData同时存在
					} else {
						
						if(receiveClientData.contains("sendPlayList:")&&receiveClientData.contains(":sendPlayList")){
							int end = receiveClientData.lastIndexOf("}");
							receiveClientData = receiveClientData.substring(13,
									end + 1);
							System.out.println("receiveClientData3:"+receiveClientData);
						}else{
							int end = receiveClientData.indexOf("}");
						receiveClientData = receiveClientData.substring(0,
								end + 1);
						}
						model = gson.fromJson(receiveClientData,
								new TypeToken<TouchModel>() {
						}.getType());
						if (model == null) {
							continue;
						}
						receiveClientData="";
					}

				}
				// 结束截图进程
				// 向客户端发送反馈内容

				String modelSign = "";
				String modelAdbOrder = "";
				if (model != null) {
					modelSign = model.getSign();
					modelAdbOrder = model.getAdbOrder();
				}
				System.out.println("modelSign=" + modelSign);
				System.out.println("modelAdbOrder=" + modelAdbOrder);

				if (modelSign != null) {
					if (modelSign.equals("getAllapp")) {
						flag = false;

						String jsonResult = gson
								.toJson(getAppInfo.getAllAPK_add());
						jsonResult = jsonResult + "getAppEnd:" + "error"
								+ ":getAppEnd";
						// Log.e(null, jsonResult);
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						Toast.makeText(AllMaincontext, "拿到所有的app啦", 1).show();
					} else if (modelSign
							.equals("355C73A013E75F749C33DC5901307461")) {

						Intent intent = new Intent(
								"android.intent.action.PLAYVEDIOACTION");
						intent.putExtra("videoId",
								"355C73A013E75F749C33DC5901307461");
						intent.putExtra("search", false);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						if (AllMaincontext != null) {
							AllMaincontext.startActivity(intent);
						}

					} else if (modelSign.equals("jietu")) {// 截图操作

						jietu();

					} else if (modelSign.equals("getappstart")) {
						flag = false;
						String startApp = "error";
						if (AllMaincontext != null) {
							startApp = preferencesUtils.getData(AllMaincontext,
									"SetAppStart");
							if (startApp == null) {
								startApp = "error";

							}
						}
						String jsonResult = gson.toJson(getAppInfo
								.getAllAPK_add());
						jsonResult = jsonResult + "getappstart:" + startApp
								+ ":getappstart";
						System.out.println(" jsonresult length "+jsonResult.length());
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						
							/*String jsonResult2 = "getmusiclistSucess"; 
							byte[] dataByte2 = jsonResult2.getBytes("GBK");
							os.write(dataByte, 0, dataByte2.length);
							os.flush();*/
						
					} else if (modelSign.equals("openApp")) {
						flag = false;
						if (modelAdbOrder != null || modelAdbOrder != "") {
							// Log.e(null, model.getAdbOrder());
							Intent intent = getAppInfo.pkgLaber_compare(modelAdbOrder);
							if (intent != null)
								if (AllMaincontext != null) {
									// Musicplay.dlnaMusicFlag=true;
									// Musicplay.weatherPlayMusic=false;
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									AllMaincontext.startActivity(intent);
								}
						}
					} else if (modelSign.equals("SetAppDelete")) {
						flag = false;
						if (IntentMaincontext!=null) {
							Intent intent = new Intent(qiActivity.SET_APP_DELETE);
							IntentMaincontext.sendBroadcast(intent);
						}
						if (AllMaincontext != null) {
							preferencesUtils.setData(AllMaincontext,
									"SetAppStart", "");
							preferencesUtils.setData(AllMaincontext, "activityName",
									"");
							preferencesUtils.setData(AllMaincontext, "bao",
									"");
							/*String internal_sdPath = FileReadWriteUtil
									.getSDPath(AllMaincontext);// sd卡路径
							String fileDirName = "baomingleiming";// 文件夹名称
							String fileName = "baomingleiming.txt";// 文件名称
							boolean weatherHasFile = FileReadWriteUtil
									.isFileExist(internal_sdPath, fileDirName);
							if (weatherHasFile == false) {
								System.out.println("文件夹不存在");
								try {
									FileReadWriteUtil.creatSDDir(
											internal_sdPath, fileDirName);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 向文件中写数据
							} else {
								System.out.println("文件夹存在");
								if (FileReadWriteUtil.isFileExist(
										internal_sdPath + "/" + fileDirName,
										fileName)) {
									System.out.println("如果文件存在，删除");
									boolean deleteFlag = new File(
											internal_sdPath + "/" + fileDirName
											+ "/" + fileName).delete();
									System.out.println("删除" + deleteFlag);
								}
							}
							try {
								File finalFileName = FileReadWriteUtil
										.creatSDFile(internal_sdPath + "/"
												+ fileDirName, fileName);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/

						} else {
							System.out.println("对象为空");
						}
					} else if (modelSign.equals("SetAppStart")) {
						flag = false;
						
						if (!modelAdbOrder.equals("") && modelAdbOrder != null) {
							if (AllMaincontext != null) {
								preferencesUtils.setData(AllMaincontext,
										"SetAppStart", modelAdbOrder);
									for (int i = 0; i < getAppInfo.getAllAPK_add1().size(); i++) {
										if (modelAdbOrder.equals(getAppInfo.getAllAPK_add1().get(i).getAppLabel())) {
											System.out.println("开始保存包名类名");
											preferencesUtils.setData(AllMaincontext, "activityName",
													getAppInfo.getAllAPK_add1().get(i).getActivityname());
											preferencesUtils.setData(AllMaincontext, "bao",
													getAppInfo.getAllAPK_add1().get(i).getPagename());
										}
								}
							}
							if (IntentMaincontext!=null) {
								Intent intent = new Intent(qiActivity.SET_APP);
								IntentMaincontext.sendBroadcast(intent);
							}
							/*System.out.println("执行保存开机apk信息操作");
							String internal_sdPath = FileReadWriteUtil
									.getSDPath(AllMaincontext);// sd卡路径
							String fileDirName = "baomingleiming";// 文件夹名称
							String fileName = "baomingleiming.txt";// 文件名称
							boolean weatherHasFile = FileReadWriteUtil
									.isFileExist(internal_sdPath, fileDirName);
							if (weatherHasFile == false) {
								System.out.println("文件夹不存在");
								try {
									FileReadWriteUtil.creatSDDir(
											internal_sdPath, fileDirName);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// 向文件中写数据
							} else {
								System.out.println("文件夹存在");
								if (FileReadWriteUtil.isFileExist(
										internal_sdPath + "/" + fileDirName,
										fileName)) {
									System.out.println("如果文件存在，删除");
									boolean deleteFlag = new File(
											internal_sdPath + "/" + fileDirName
											+ "/" + fileName).delete();
									System.out.println("删除" + deleteFlag);
								}
							}
							File finalFileName = FileReadWriteUtil.creatSDFile(
									internal_sdPath + "/" + fileDirName,
									fileName);
							String packageAndActivityName = GetAppInfo
									.pkgLaber_compareName_activityName(modelAdbOrder);
							byte[] content = packageAndActivityName.getBytes();
							try {
								FileReadWriteUtil.writeFile(content,
										finalFileName.getPath());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								System.out.println("开机要启动的apk信息保存失败");
								e.printStackTrace();
							}
							System.out.println("开机要启动的apk信息保存成功");*/
						} else {
							System.out.println("开机要启动的apk信息保存失败No Context");
						}
					}else if (modelSign.equals("installApp")) {
						System.out.println("需要静默安装APP");
						if (modelAdbOrder!=null && modelAdbOrder!="") {
							if (!downflag) {
								downloadApkThread dApkThread = new downloadApkThread(modelAdbOrder, "install.apk", AllMaincontext,this);
								dApkThread.start();
								downflag=true;
							}
							
						}
					}else if (modelSign.equals("uninstallApp")) {
						flag = false;
						if (modelAdbOrder != null && modelAdbOrder != "") {

							String pkg_name1 = GetAppInfo
									.pkgLaber_compareName(modelAdbOrder);
							Log.d("ServerThread", pkg_name1);
							if (pkg_name1 == null) {
								continue;
							}

							// /*
							// * Uri packageURI =
							// * Uri.fromParts("package",pkg_name1,null); Intent
							// * uninstallIntent = new Intent(
							// * Intent.ACTION_DELETE, packageURI);
							// * Maincontext.startActivity(uninstallIntent);
							// */
							// try {
							//
							// //send_key_touch("adb uninstall -r " +
							// pkg_name1);
							// send_key_touch_install("adb uninstall " +
							// pkg_name1);
							// /*
							// * String
							// * paramString="adb uninstall "+pkg_name1+"\n";
							// * System.out.println("准备执行卸载"+pkg_name1); if
							// * (RootCmd.haveRoot()) { Log.d("ServerThread",
							// * paramString); if
							// * (RootCmd.execRootCmdSilent(paramString) ==
							// * -1) { System.out.println("执行不成功"); } else {
							// * System.out.println("执行成功");
							// *
							// *
							// * } } else { System.out. println("没有root权限"); }
							// */
							// //
							// String jsonResult = "uninstallSucess" + "("
							// + modelAdbOrder + ")";
							// // Log.e(null, jsonResult);
							// byte[] dataByte = jsonResult.getBytes("GBK");
							// os.write(dataByte, 0, dataByte.length);
							// os.flush();
							// } catch (Exception ex) {
							// ex.printStackTrace();
							// }
							PackageManager pm = AllMaincontext
									.getPackageManager();
							IPackageDeleteObserver observer = new MyPackageDeleteObserver();


							String jsonResult = "uninstallSucess" + "("
									+ modelAdbOrder + ")";
							// Log.e(null, jsonResult);
							byte[] dataByte = jsonResult.getBytes("GBK");
							os.write(dataByte, 0, dataByte.length);
							os.flush();
						}

					}else if (modelSign.equals("music_list")) {//拿到手机端的所有歌曲数据
						List<Gequinfo> g = gson.fromJson(modelAdbOrder,
								new TypeToken<List<Gequinfo>>() {
								}.getType());
						if (Gequinfo.music_list.size()>0) {
							Gequinfo.music_list.clear();
						}
						for (int i = 0; i < g.size(); i++) {
							Gequinfo get = new Gequinfo();
							get.setName(g.get(i).getName());
							get.setIscheck(false);
							Gequinfo.music_list.add(get);
						}
						String jsonResult = "getmusiclistSucess"; 
						// Log.e(null, jsonResult);
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						System.out.println("发送music");
					} 
					else if (modelSign.equals("playMusic_stop")) {
						flag = false;
						if (player != null) {
							if (player.isPlaying()) {
								player.stop();
							}
						}
					} else if (modelSign.equals("playMusic_station")) {
						flag = false;
						if (player != null) {
							if (player.isPlaying()) {
								player.stop();
							} else {
								try {
									player.prepare();
									player.start();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else if (modelSign.equals("playMusic_progress")) {
						flag = false;
						int proTo = Integer.parseInt(model.getAdbOrder());
						if (player != null) {
							if (player.getCurrentPosition() < proTo) {

							} else {
								player.seekTo(proTo);
								player.start();
							}
						}
					} else if (modelSign.equals("playMusic_volume")) {
						flag = false;
						int progessNum = Integer.parseInt(modelAdbOrder);
						if (audioManager != null) {
							audioManager.setStreamVolume(
									AudioManager.STREAM_MUSIC, progessNum,
									AudioManager.FLAG_ALLOW_RINGER_MODES);
						}
					} else if (modelSign.equals("clean")) {// 一键清理的操作
//						Intent intent = new Intent();
//						intent.setComponent(new ComponentName(
//								"com.example.cleanspeed",
//								"com.example.cleanspeed.MainActivity"));
//						AllMaincontext.startActivity(intent);
						String neicun = clear(AllMaincontext);
						//String neicun = 1524522+"";
						String startclean = "cleanstart:";
						String endclean = ":cleanend";
						String jsonResult =  startclean + neicun+endclean;
						System.out.println(">>>>>>>jsonresult:"+jsonResult);
						// Log.e(null, jsonResult);
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						
					} else if (modelSign.equals("finish")) {
						hand.sendEmptyMessage(1);
					}else if (modelSign.equals("left")) {
						send_key(KeyEvent.KEYCODE_DPAD_LEFT);
						Log.e("左", "左");
					} else if (modelSign.equals("below")) {
						send_key(KeyEvent.KEYCODE_DPAD_DOWN);

					} else if (modelSign.equals("up")) {
						send_key(KeyEvent.KEYCODE_DPAD_UP);
					} else if (modelSign.equals("right")) {
						send_key(KeyEvent.KEYCODE_DPAD_RIGHT);

					} else if (modelSign.equals("home")) {
						send_key(KeyEvent.KEYCODE_HOME);

					} else if (modelSign.equals("ok")) {
						send_key(KeyEvent.KEYCODE_DPAD_CENTER);

					} else if (modelSign.equals("menu")) {
						send_key(KeyEvent.KEYCODE_MENU);

					} else if (modelSign.equals("back")) {
						send_key(KeyEvent.KEYCODE_BACK);

					} else if (modelSign.equals("audioSilent")) {
						send_key(KeyEvent.KEYCODE_MUTE);

					} else if (modelSign.equals("audiojia")) {
						send_key(KeyEvent.KEYCODE_VOLUME_UP);

					} else if (modelSign.equals("audiojian")) {
						send_key(KeyEvent.KEYCODE_VOLUME_DOWN);
					} else if (modelSign.equals("playtvMusic")) {
						if(SystemInfo.isfirst){
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action", "index");
							mIntent.putExtra("action_index", modelAdbOrder);
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
							} 
						}else if(!SystemInfo.isfirst) {
							Intent mIntent = new Intent(
									"android.intent.action.MUSICPLAY");
							mIntent.putExtra("istvmusic", true);
							mIntent.putExtra("action_index", modelAdbOrder);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							AllMaincontext.startActivity(mIntent);						
							}
					}else if (modelSign.equals("musicOfphone")) {
						if(SystemInfo.isfirst){
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action", "action_phone");
							mIntent.putExtra("action_phone", "action_phone");
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
							} 
						}
						
					}else if (modelSign.equals("musicOftv")) {
						if (Getallmusicinfo.data.size()>0) {
							Getallmusicinfo.data.clear();
						}
						Getallmusicinfo get = new Getallmusicinfo(AllMaincontext);
						String jsonResult = gson.toJson(get.data);
						for (int i = 0; i < get.data.size(); i++) {
							System.out.println(get.data.get(i).getName());
						}
						jsonResult = jsonResult + "gettvmusic:";
						// Log.e(null, jsonResult);
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						if(SystemInfo.isfirst){
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action", "gettvmusic");
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
							} 
						}
					} else if (modelSign.equals("musicOfUsb")) {
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action",
									"musicOfUsb");
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
							} else {
								System.out.println("IntentMaincontext=null");
								Thread.sleep(1000);
								if (IntentMaincontext != null) {
									IntentMaincontext.sendBroadcast(mIntent);
								}
							}

						Log.i("currentPath", whichPathOwn);
					} else if (modelSign.equals("musicExternal_sd")) {
						whichPathOwn = "external_sd";
						try {
							scanMusicToClient();
							System.out.println("外置sd卡");
							openYaHaoMusicPlayer();
							System.out.println("已打开播放器");
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action",
									"client_come_path");
							mIntent.putExtra("whichPath", "external_sd");
							System.out.println("准备发送广播");

							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
								System.out.println("已发送广播");
							} else {
								System.out.println("IntentMaincontext=null");
								Thread.sleep(1000);
								if (IntentMaincontext != null) {
									IntentMaincontext.sendBroadcast(mIntent);
									System.out.println("已发送广播2");
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Log.i("currentSource", whichPathOwn);
					} else if (modelSign.equals("musicOfAux")) {
						whichPathOwn = "musicOfAux";
						Log.i("currentSource", whichPathOwn);
						try {
							
							Intent intent_but_dianbo_5_ss = getAppInfo
									.pkgLaber_compare("com.csw.setaudio");
							if (intent_but_dianbo_5_ss == null) {
								Log.i("currentSource", whichPathOwn
										+ "未安装音源切换apk");
							} else {
								intent_but_dianbo_5_ss
								.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								// IntentMaincontext.startActivity(intent_but_dianbo_5_ss);

								if (IntentMaincontext != null) {
									IntentMaincontext
									.startActivity(intent_but_dianbo_5_ss);
								} else {
									System.out
									.println("IntentMaincontext=null");
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (modelSign.equals("server_guanji")) {// 关机
						// guanji();

						if (MusicplayAct.mMediaPlayer != null) {
							if (MusicplayAct.mMediaPlayer.isPlaying()) {
								MusicplayAct.mMediaPlayer.pause();
								// Musicplay.mMediaPlayer.release();
								// Musicplay.mMediaPlayer = null;
								System.out.println("暂停音乐播放器的音乐");
							}
						}

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "finish_app");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
							Thread.sleep(1000);
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
							}
						}

						Log.i("Order from Client", modelSign);
						send_key(KeyEvent.KEYCODE_POWER);// 待机其实

						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_daiji")) {// 待机

						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_volumeUp")) {// 音量加
						send_key(KeyEvent.KEYCODE_VOLUME_UP);
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_volumeDown")) {// 音量减
						send_key(KeyEvent.KEYCODE_VOLUME_DOWN);
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_mute")) {// 静音

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_mute");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}

						// send_key(KeyEvent.KEYCODE_MUTE);
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_previousSong")) {// 上一曲

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_previousSong");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_play")) {// 播放

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_play");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_nextSong")) {// 下一曲

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_nextSong");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} 
//					else if (modelSign.equals("server_fullCirculation")) {// 全部循环
//
//						Intent mIntent = new Intent(
//								MusicplayAct.CLIENT_ACTION_NAME);
//						mIntent.putExtra("action_action",
//								"server_fullCirculation");
//						if (IntentMaincontext != null) {
//							IntentMaincontext.sendBroadcast(mIntent);
//						} else {
//							System.out.println("IntentMaincontext=null");
//						}
//						Log.i("Order from Client", modelSign);
//					} 
					else if (modelSign.equals("server_singleCycle")) {// 单曲循环

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_singleCycle");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_turnPlay")) {// 顺序播放

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_turnPlay");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("server_randomPlay")) {// 随机播放

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action", "server_randomPlay");
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}
						Log.i("Order from Client", modelSign);
					} else if (modelSign.equals("musicModel_normal")) {// 音乐音效选择
						
						if (SystemInfo.isfirst) {// 打开了musicplay这个activity
							if (null != modelAdbOrder && "" != modelAdbOrder) {
								Intent mIntent = new Intent(
										MusicplayAct.CLIENT_ACTION_NAME);
								mIntent.putExtra("action_action", modelAdbOrder);
								if (IntentMaincontext != null) {
									IntentMaincontext.sendBroadcast(mIntent);
								} else {
									System.out
									.println("IntentMaincontext=null");
								}
								Log.i("Order from Client", modelSign);
							}
						} else {// 发送手机端数据提示说没有打开musicplay这个activity
							String jsonResult = "play_fial" + "("
									+ modelAdbOrder + ")";
							// Log.e(null, jsonResult);
							byte[] dataByte = jsonResult.getBytes("GBK");
							os.write(dataByte, 0, dataByte.length);
							os.flush();
						}

					} else if (modelSign.equals("playMusic")) {// 打开musicplay这个activity
						String url = modelAdbOrder.substring(0, modelAdbOrder.indexOf("&"));
						String name = modelAdbOrder.substring(modelAdbOrder.indexOf("&")+1,modelAdbOrder.indexOf("%"));
						String index = modelAdbOrder.substring(modelAdbOrder.indexOf("%")+1, modelAdbOrder.length());
						if (!SystemInfo.isfirst) {
							Intent mIntent = new Intent(
									"android.intent.action.MUSICPLAY");
							mIntent.putExtra("url", url);
							mIntent.putExtra("name", name);
							mIntent.putExtra("phone_index", index);
							System.out.println("modelAdbOrder:"+modelAdbOrder);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							AllMaincontext.startActivity(mIntent);
						}else if(SystemInfo.isfirst){
							Intent mIntent = new Intent(
									MusicplayAct.CLIENT_ACTION_NAME);
							mIntent.putExtra("action_action", "music_path");
							mIntent.putExtra("music_url",url );
							mIntent.putExtra("name", name);
							mIntent.putExtra("phone_index", index);
							if (IntentMaincontext != null) {
								IntentMaincontext.sendBroadcast(mIntent);
								System.out.println("发送广播---播放的");
							} 
						}
						
					}
					// } else if (modelSign.equals("musicModel_classical")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action",
					// "musicModel_classical");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// } else if (modelSign.equals("musicModel_Hip_Hop")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action", "musicModel_Hip_Hop");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// } else if (modelSign.equals("musicModel_jazz")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action", "musicModel_jazz");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// } else if (modelSign.equals("musicModel_Pop")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action", "musicModel_Pop");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// } else if (modelSign.equals("musicModel_rock")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action", "musicModel_rock");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// } else if (modelSign.equals("musicModel_heavy")) {
					//
					// Intent mIntent = new Intent(
					// MusicplayAct.CLIENT_ACTION_NAME);
					// mIntent.putExtra("action_action", "musicModel_heavy");
					// if (IntentMaincontext != null) {
					// IntentMaincontext.sendBroadcast(mIntent);
					// | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					// IntentMaincontext.startActivity(mIntent);
					// } else {
					// System.out.println("IntentMaincontext=null");
					// }
					// Log.i("Order from Client", modelSign);
					// }
					else if (receiveClientData.contains("comeFromMobile")
							&& receiveClientData.contains("finely")) {// 增删改查

						int index = receiveClientData.indexOf("finely");
						String musicTimeData = receiveClientData.substring(14,
								index);
						/*
						 * MusicSavedInfo musicSavedInfo =
						 * gson.fromJson(musicTimeData, new
						 * TypeToken<MusicSavedInfo>() { }.getType());
						 */
						// JSONObject jsonObject =
						// JSONObject.fromObject(musicTimeData);
						// MusicSavedInfo musicSavedInfo = (MusicSavedInfo)
						// JSONObject.toBean(jsonObject, MusicSavedInfo.class);
						//
						MusicSavedInfo musicSavedInfo = null;
						try {
							musicSavedInfo = gson.fromJson(musicTimeData,
									MusicSavedInfo.class);

							Uri uri = Uri
									.parse("content://com.csw.csw_musictimeset.provider/musicTimeSavedInfo");
							ContentValues contentValues = new ContentValues();

							contentValues.put("hourAndMinute",
									musicSavedInfo.getHourAndMinute());
							contentValues.put("openOrCloseState",
									musicSavedInfo.getOpenOrCloseState());
							contentValues.put("whichDays",
									musicSavedInfo.getWhichDays());
							contentValues.put("onTimeMusicTitle",
									musicSavedInfo.getOnTimeMusicTitle());
							contentValues.put("onTimeMusicPath",
									musicSavedInfo.getOnTimeMusicPath());
							contentValues.put("on_off",
									musicSavedInfo.getOn_off());
							/*Uri newUri = AllMaincontext.getContentResolver()
									.insert(uri, contentValues);
							String newId = newUri.getPathSegments().get(1);*/

							Log.d("comeFromMobile+finely", "定时保存成功");

							String paramString = "adb shell"
									+ "\n"
									+ "su"
									+ "\n"
									+ "am start -n com.csw.csw_musictimeset/com.csw.csw_musictimeset.MusicTimeHasBeenSavedActivity"
									+ "\n" + "exit" + "\n" + "exit";
							Log.d("comeFromMobile+finely", "定时保存成功2");
							if (RootCmd.haveRoot()) {
								System.out.println(paramString);
								if (RootCmd.execRootCmdSilent(paramString) == -1) {
									System.out.println("执行不成功");
								} else {
									System.out.println("执行成功");
								}
							} else {
								System.out.println("没有root权限");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if (modelSign.equals("getalltvmusic")) {
						if (Getallmusicinfo.data.size()>0) {
							Getallmusicinfo.data.clear();
						}
						Getallmusicinfo get = new Getallmusicinfo(AllMaincontext);
						String jsonResult = gson.toJson(get.data);
						for (int i = 0; i < get.data.size(); i++) {
							System.out.println("name:"+get.data.get(i).getName()+"path:"+get.data.get(i).getUrl());
						}
						jsonResult = jsonResult + "getalltvmusic:";
						// Log.e(null, jsonResult);
						byte[] dataByte = jsonResult.getBytes("GBK");
						os.write(dataByte, 0, dataByte.length);
						os.flush();
						
					} else if (modelSign.equals("dlnaMusicPlay")) {
						// Musicplay.dlnaMusicFlag = true;

						if (MusicplayAct.mMediaPlayer != null) {
							if (MusicplayAct.mMediaPlayer.isPlaying()) {
								MusicplayAct.mMediaPlayer.pause();
								MusicplayAct.mMediaPlayer.stop();
								// Musicplay.mMediaPlayer.release();
							}
						}

					} else if (modelSign.equals("dlnaMusicPause")) {
						// Musicplay.dlnaMusicFlag = false;
					}/*
					 * else if(modelSign.equals("dingshi_liebiao")){
					 * Log.i("dingshi_liebiao", "手机在获取定时信息1");
					 * scanTimeSetListToClient(); Log.i("dingshi_liebiao",
					 * "手机在获取定时信息2"); }else
					 * if(modelSign.contains("updateOff")){//更新数据updateOff
					 * 
					 * Log.i("updateOff", "手机在获取定时更新信息"); String musicId =
					 * modelSign.substring(9);
					 * System.out.println("当前需要更新数据项的ID为"+musicId);
					 * if(modelAdbOrder.equals("1")){
					 * 
					 * Uri uri=Uri.parse(
					 * "content://com.csw.csw_musictimeset.provider/musicTimeSavedInfo"
					 * ); ContentValues contentValues=new ContentValues();
					 * contentValues.put("on_off", "1"); int
					 * newUri=Maincontext.getContentResolver().update(uri,
					 * contentValues, "musicId =?", new String[]{musicId });
					 * System.out.println("更新了数据库ID为"+musicId+"开关状态"+newUri);
					 * 
					 * 
					 * }else if(modelAdbOrder.equals("0")){
					 * 
					 * Uri uri=Uri.parse(
					 * "content://com.csw.csw_musictimeset.provider/musicTimeSavedInfo"
					 * ); ContentValues contentValues=new ContentValues();
					 * contentValues.put("on_off", "0"); int
					 * newUri=Maincontext.getContentResolver().update(uri,
					 * contentValues, "musicId =?", new String[]{musicId });
					 * System.out.println("更新了数据库ID为"+musicId+"开关状态"+newUri);
					 * 
					 * } Log.i("updateOff", "手机在更新数据"); }else
					 * if(modelSign.equals("deleteCurrentData")){
					 * 
					 * String musicId=modelAdbOrder; Uri uri=Uri.parse(
					 * "content://com.csw.csw_musictimeset.provider/musicTimeSavedInfo"
					 * ); int
					 * newUri=Maincontext.getContentResolver().delete(uri,
					 * "musicId =?", new String[]{musicId });
					 * Log.i("deleteCurrentData", "手机在删除数据"+newUri); }
					 */
				}
				if (modelAdbOrder != null) {// 像这种modelAdbOrder都放在后面吧

					if (modelAdbOrder.equals("clientMusicToServer")) {// 根据地址播放音乐
						// if(Musicplay.isRunningInBack==true){
						// send_key(KeyEvent.KEYCODE_BACK);
						// }

						String paramString = "adb shell"
								+ "\n"
								+ "su"
								+ "\n"
								+ "am start -n com.csw.ujplayerui/com.csw.tp.activity.MusicplayAct"
								+ "\n" + "exit" + "\n" + "exit";

						if (RootCmd.haveRoot()) {

							if (RootCmd.execRootCmdSilent(paramString) == -1) {
								System.out.println("执行不成功");
							} else {
								System.out.println("执行成功");
							}
						} else {
							System.out.println("没有root权限");
						}
						Thread.sleep(100);
						String comeMusicPath = modelSign;

						// if(comeMusicPath.contains("internal_sd")){
						//
						// Intent mIntent=new
						// Intent(MusicplayAct.CLIENT_ACTION_NAME);
						// mIntent.putExtra("action_action",
						// "server_clientMusicToServer");
						// mIntent.putExtra("whichPath", "internal_sd");
						// mIntent.putExtra("musicPath", comeMusicPath);
						// AllMaincontext.sendBroadcast(mIntent);
						// Log.i("comePath", "-----------------" +
						// "internal_sd");
						//
						// }else if(comeMusicPath.contains("usb_storage")){
						// Intent mIntent=new
						// Intent(MusicplayAct.CLIENT_ACTION_NAME);
						// mIntent.putExtra("action_action",
						// "server_clientMusicToServer");
						// mIntent.putExtra("whichPath", "usb_storage");
						// mIntent.putExtra("musicPath", comeMusicPath);
						// AllMaincontext.sendBroadcast(mIntent);
						// Log.i("comePath", "-----------------" +
						// "usb_storage");
						//
						// }else if(comeMusicPath.contains("external_sd")){
						// Intent mIntent=new
						// Intent(MusicplayAct.CLIENT_ACTION_NAME);
						// mIntent.putExtra("action_action",
						// "server_clientMusicToServer");
						// mIntent.putExtra("whichPath", "external_sd");
						// mIntent.putExtra("musicPath", comeMusicPath);
						// AllMaincontext.sendBroadcast(mIntent);
						// Log.i("comePath", "-----------------" +
						// "external_sd");
						//
						// }

						Intent mIntent = new Intent(
								MusicplayAct.CLIENT_ACTION_NAME);
						mIntent.putExtra("action_action",
								"server_clientMusicToServer");
						// mIntent.putExtra("whichPath", "usb_storage");
						mIntent.putExtra("musicPath", comeMusicPath);
						if (IntentMaincontext != null) {
							IntentMaincontext.sendBroadcast(mIntent);
						} else {
							System.out.println("IntentMaincontext=null");
						}

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close();
		}
	}

	/**
	 * 关闭相应资源
	 */
	public void close() {
		try {
			os.close();
			is.close();
			socket.close();
			Log.d("关闭资源", "数据流、Socket关闭");
		} catch (Exception e) {
		}
	}

	/*
	 * 定义读取客户端数据的方法
	 */
	private int readFromClient(byte[] b) {
		// byte[] b = new byte[1024];
		try {
			return is.read(b);
		} catch (IOException e) {
			SocketServer.socketList.remove(socket);// 如果读取有异常，说明该Socket对应的客户端已经关闭,移除
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	MusicSavedInfo musicSavedInfo = null;

	List<MusicSavedInfo> mMusicSavedList = new ArrayList<MusicSavedInfo>();

	private ContentResolver resolver;

	/**
	 * 扫描定时信息发送到手机端
	 */
	private void scanTimeSetListToClient() {
		Uri uri = Uri
				.parse("content://com.csw.csw_musictimeset.provider/musicTimeSavedInfo");
		resolver = Maincontext.getContentResolver();

		String[] PROJECTION = new String[] { "musicId", "hourAndMinute",
				"openOrCloseState", "whichDays", "onTimeMusicTitle",
				"onTimeMusicPath", "on_off" };

		Cursor cursor = resolver.query(uri, null, null, null, null);

		if (mMusicSavedList.size() != 0) {
			mMusicSavedList.clear();
		}
		System.out.println("开始遍历");
		if (cursor.moveToFirst()) {
			do {
				String musicId = cursor.getString(cursor
						.getColumnIndex("musicId"));
				int musicIdd = Integer.parseInt(musicId);
				String hourAndMinute = cursor.getString(cursor
						.getColumnIndex("hourAndMinute"));
				String openOrCloseState = cursor.getString(cursor
						.getColumnIndex("openOrCloseState"));
				String whichDays = cursor.getString(cursor
						.getColumnIndex("whichDays"));
				String onTimeMusicTitle = cursor.getString(cursor
						.getColumnIndex("onTimeMusicTitle"));
				String onTimeMusicPath = cursor.getString(cursor
						.getColumnIndex("onTimeMusicPath"));
				String on_off = cursor.getString(cursor
						.getColumnIndex("on_off"));

				MusicSavedInfo musicSavedInfo = new MusicSavedInfo();

				musicSavedInfo.setMusicId(musicIdd);
				musicSavedInfo.setHourAndMinute(hourAndMinute);
				musicSavedInfo.setOpenOrCloseState(openOrCloseState);
				musicSavedInfo.setWhichDays(whichDays);
				musicSavedInfo.setOnTimeMusicTitle(onTimeMusicTitle);
				musicSavedInfo.setOnTimeMusicPath(onTimeMusicPath);
				musicSavedInfo.setOn_off(on_off);

				mMusicSavedList.add(musicSavedInfo);

			} while (cursor.moveToNext());

			if (mMusicSavedList.size() != 0) {// 说明有定时信息

				TimeSetInfoBook timeinfoBook = new TimeSetInfoBook(
						"finelx_time", mMusicSavedList);
				String jsonResult2 = gson.toJson(timeinfoBook);
				String timeDataEnd2 = "timeDataEnd";
				String dataPackage2 = jsonResult2 + timeDataEnd2;
				byte[] dataByte = StringToByte(dataPackage2);

				try {
					os.write(dataByte, 0, dataByte.length);
					os.flush();
					cursor.close();
					System.out.println("有定时信息文件！os.write" + "反馈成功！");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				String dataPackage3 = "serverHaveNoTime";
				System.out.println("mMusicSavedList.size() == 0没有time"
						+ dataPackage3);
				byte[] dataByte = StringToByte(dataPackage3);

				try {
					os.write(dataByte, 0, dataByte.length);
					os.flush();
					cursor.close();
					System.out.println("mMusicSavedList.size() == 0没有time"
							+ dataPackage3 + "反馈成功!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			String dataPackage3 = "serverHaveNoTime";
			System.out.println("mMusicSavedList.size() == 0没有time"
					+ dataPackage3);
			byte[] dataByte = StringToByte(dataPackage3);

			try {
				os.write(dataByte, 0, dataByte.length);
				os.flush();
				cursor.close();
				System.out.println("mMusicSavedList.size() == 0没有time"
						+ dataPackage3 + "反馈成功!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Cursor cursor;
	List<MusicInfoEntity> musicInfoEntityList = new ArrayList<MusicInfoEntity>();

	private void scanMusicToClient() {

		String[] audioColumns = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM };
		cursor = Maincontext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns,
				null, null, null);

		if (musicInfoEntityList.size() != 0) {
			musicInfoEntityList.clear();
		}

		if (cursor.moveToFirst()) {
			do {

				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// String creator = cursor.getString(cursor
				// .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				String filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				long duration = cursor
						.getLong(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				if (filePath.contains(whichPathOwn)) {
					MusicInfoEntity musicInfoEntity = new MusicInfoEntity(
							title, filePath, duration);
					musicInfoEntityList.add(musicInfoEntity);
				}
			} while (cursor.moveToNext());

			if (musicInfoEntityList.size() == 0) {
				String dataPackage = "serverHaveNoMusic";
				System.out.println("musicInfoEntityList.size() == 0没有音频文件"
						+ dataPackage);
				byte[] dataByte = StringToByte(dataPackage);

				try {
					os.write(dataByte, 0, dataByte.length);
					os.flush();
					cursor.close();
					System.out.println("musicInfoEntityList.size() == 0没有音频文件"
							+ dataPackage + "反馈成功!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				MusicInfoBook friendsBook = new MusicInfoBook("finely",
						musicInfoEntityList);
				String jsonResult = gson.toJson(friendsBook);
				String musicDataEnd = "musicDataEnd";
				String dataPackage = jsonResult + musicDataEnd;
				byte[] dataByte = StringToByte(dataPackage);

				try {
					os.write(dataByte, 0, dataByte.length);
					os.flush();
					cursor.close();
					System.out.println("有音频文件！os.write" + "反馈成功！");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			String dataPackage = "serverHaveNoMusic";
			System.out.println("cursor.moveToFirst()没有音频文件！" + dataPackage);
			byte[] dataByte = StringToByte(dataPackage);

			try {
				os.write(dataByte, 0, dataByte.length);
				os.flush();
				cursor.close();
				System.out.println("cursor.moveToFirst()没有音频文件！" + dataPackage
						+ "反馈成功！");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 执行打开播放器apk的操作
	 */

	private void openYaHaoMusicPlayer() {
		String paramString = "adb shell"
				+ "\n"
				+ "su"
				+ "\n"
				+ "am start -n com.example.musicinterface/com.example.musicinterface.MainActivity"
				+ "\n" + "exit" + "\n" + "exit";

		if (RootCmd.haveRoot()) {

			if (RootCmd.execRootCmdSilent(paramString) == -1) {
				System.out.println("执行不成功");
			} else {
				System.out.println("执行成功");
			}
		} else {
			System.out.println("没有root权限");
		}

	}

	/**
	 * 默认的字符集编码 UTF-8 一个汉字占三个字节
	 */
	private static String CHAR_ENCODE = "GBK";

	public static Process localProcess = null;

	public void send_key(int keycode) {

		sendKeyCode(keycode);

	}

	/**
	 * 传入需要的键值即可
	 * 
	 * @param keyCode
	 */
	private void sendKeyCode(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					Log.e("Exception when sendPointerSync", e.toString());
				}
			}
		}.start();
	}

	//
	public boolean send_key_touch(String keycode) {

		try {
			String keyCommand = keycode;
			System.out.println("当前命令:" + keyCommand);
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);
			System.out.println("执行完了" + proc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("执行出现异常");
			e.printStackTrace();
		}
		// PrintWriter PrintWriter = null;
		// Process process = null;
		// try {
		// process = Runtime.getRuntime().exec("su");
		// PrintWriter = new PrintWriter(process.getOutputStream());
		// PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
		// // PrintWriter.println("pm uninstall " + packageName);
		// PrintWriter.println("pm uninstall " + packageName);
		// PrintWriter.flush();
		// PrintWriter.close();
		// int value = process.waitFor();
		// return returnResult(value);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (process != null) {
		// process.destroy();
		// }
		// }
		return false;

	}

	public void send_key_touch_install(String keycode) {

		try {

			// if(localProcess==null)
			localProcess = Runtime.getRuntime().exec("su", null, null);
			OutputStream localOutputStream = localProcess.getOutputStream();
			byte[] arrayOfByte = (keycode).getBytes("ASCII");
			localOutputStream.write(arrayOfByte);
			localOutputStream.flush();
			localOutputStream.close();
			System.out.println("当前命令:" + keycode);
			int j = localProcess.waitFor();

			for (int i = 0; i < 10; i++) {
				System.out.println("++++++++++++++++++++++++++++++++");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static MediaPlayer player = null;

	private void playurl(String url) {
		/*
		 * String url2 ; url = getUtf8Url(url);;
		 */

		Uri myUri = Uri.parse(url);
		if (player != null)
			if (player.isPlaying()) {
				player.stop();
				player.release();
				player = null;
			}
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setWakeMode(AllMaincontext, PowerManager.PARTIAL_WAKE_LOCK);

		try {
			player.setDataSource(AllMaincontext, myUri);
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}

		player.start();
	}

	public String getUtf8Url(String s) {
		try {
			s = URLEncoder.encode(s, "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	/* 静默卸载回调 */
	class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {

		@Override
		public void packageDeleted(String packageName, int returnCode) {
			if (returnCode == 1) {
				new ToastThread(AllMaincontext, "卸载成功").start();
			} else {
				Log.e("DEMO", "卸载失败...返回码:" + returnCode);
				new ToastThread(AllMaincontext, "卸载失败...返回码:" + returnCode)
				.start();
			}
		}
	}

	private void jietu() {
		try {
			mScreenBitmap = null;
			takeScreenshot();
			if (mScreenBitmap != null) {
				String datastart = "csw_cswstart:";
				String dataEnd = ":csw_cswend";
				// System.out.println("Bitmap is not null，comming");
				// String imageDate3 = new SimpleDateFormat(
				// "yyyy-MM-dd-HH-mm-ss").format(new Date(
				// System.currentTimeMillis()));
				// System.out
				// .println("当前时间-------准备把位图转换成字符串;---------》"
				// + imageDate3);
				String bitmapStr = convertIconToString(mScreenBitmap);

				// String imageDate5 = new SimpleDateFormat(
				// "yyyy-MM-dd-HH-mm-ss").format(new Date(
				// System.currentTimeMillis()));
				// System.out
				// .println("当前时间-------位图已经转换成字符串了;---------》"
				// + imageDate5);
				//SocketDataPackage socketDataPackage = new SocketDataPackage(
						///bitmapStr, 100);
				//String jsonResult = gson.toJson(socketDataPackage);

				String dataPackage = datastart + bitmapStr  + dataEnd;
				System.out.println(">>>>>>." + dataPackage);
				byte[] dataByte = StringToByte(dataPackage);
				// String imageDate = new SimpleDateFormat(
				// "yyyy-MM-dd-HH-mm-ss").format(new Date(
				// System.currentTimeMillis()));
				// System.out
				// .println("当前时间-------os.write准备写入数据流;---------》"
				// + imageDate);
				os.write(dataByte, 0, dataByte.length);
				os.flush();
				System.out.println("写入的字节长度：------------------》"
						+ dataByte.length);
				String imageDate2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
				.format(new Date(System.currentTimeMillis()));
				System.out.println("当前时间-------os.write写入数据流完成;---------》"
						+ imageDate2);
				// mScreenBitmap.recycle();//bitmap释放
				// Drawable drawable =new BitmapDrawable(mScreenBitmap);
				// imaiv.setImageBitmap(mScreenBitmap);
			} else {

				Log.d("send dataPackage", "-------------------->no data");
			}
			// Thread.sleep(200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("send dataPackage", "-------------------->出现Exception");
			e.printStackTrace();

		}
	} // else {
	// break;
	// }
	// }

	// }

	// });
	// }

	@SuppressLint("NewApi")
	public void takeScreenshot() {

		WindowManager mWindowManager = (WindowManager) AllMaincontext
				.getSystemService(Context.WINDOW_SERVICE);
		Display mDisplay = mWindowManager.getDefaultDisplay();
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		mDisplay.getRealMetrics(mDisplayMetrics);
		Matrix mDisplayMatrix = new Matrix();
		float[] dims = { mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels };

		int value = mDisplay.getRotation();
		String hwRotation = SystemProperties.get("ro.sf.hwrotation", "0");
		if (hwRotation.equals("270") || hwRotation.equals("90")) {
			value = (value + 3) % 4;
		}
		float degrees = getDegreesForRotation(value);

		boolean requiresRotation = (degrees > 0);
		if (requiresRotation) {
			// Get the dimensions of the device in its native orientation
			mDisplayMatrix.reset();
			mDisplayMatrix.preRotate(-degrees);
			mDisplayMatrix.mapPoints(dims);

			dims[0] = Math.abs(dims[0]);
			dims[1] = Math.abs(dims[1]);
		}
		// mScreenBitmap.recycle();
		// mScreenBitmap = Surface.screenshot((int) dims[0], (int) dims[1]);
		// mScreenBitmap = Surface.screenshot(480, 320);
		mScreenBitmap = SurfaceControl.screenshot(960, 540);
		// System.out.println("-----开始截图------分辨率属性----》"+480+"------------"+320);
		// System.out.println("-----开始截图------分辨率属性----》"+(int)
		// dims[0]+"------------"+(int) dims[1]);

		String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
		.format(new Date(System.currentTimeMillis()));
		System.out.println("当前时间------开始截图----------》" + imageDate);

		if (requiresRotation) {
			// Rotate the screenshot to the current orientation
			Bitmap ss = Bitmap.createBitmap(960, 540, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(ss);
			c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
			c.rotate(degrees);
			c.translate(-960 / 2, -540 / 2);
			c.drawBitmap(mScreenBitmap, 0, 0, null);
			c.setBitmap(null);
			mScreenBitmap = ss;
		}

		// If we couldn't take the screenshot, notify the user
		if (mScreenBitmap == null) {
			return;
		}

		// Optimizations
		mScreenBitmap.setHasAlpha(false);
		mScreenBitmap.prepareToDraw();
		// Log.e(null, "——————————————————————————————————————————————");

		/*
		 * try { saveBitmap(mScreenBitmap); } catch (IOException e) {
		 * System.out.println(e.getMessage()); }
		 */
		/*
		 * if(write_cishu < 5 && write_cishu>=0) {
		 * System.out.println("current cishu----------------》"+write_cishu);
		 * mScreenBitmap_buffer[write_cishu]=mScreenBitmap;
		 * 
		 * write_cishu++; if(write_cishu ==5) write_cishu = 0; }
		 * 
		 * 
		 * mScreenBitmap.recycle();
		 */
	}

	private float getDegreesForRotation(int value) {
		switch (value) {
		case Surface.ROTATION_90:
			return 360f - 90f;
		case Surface.ROTATION_180:
			return 360f - 180f;
		case Surface.ROTATION_270:
			return 360f - 270f;
		}
		return 0f;
	}

	public byte[] StringToByte(String str) {
		return StringToByte(str, "GBK");
	}

	/**
	 * UTF-8 一个汉字占三个字节
	 * 
	 * @param str
	 *            源字符串 转换成字节数组的字符串
	 * @return
	 */
	public byte[] StringToByte(String str, String charEncode) {
		byte[] destObj = null;
		try {
			if (null == str || str.trim().equals("")) {
				destObj = new byte[0];
				return destObj;
			} else {
				destObj = str.getBytes(charEncode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return destObj;
	}

	// bitmap对象转string
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, 80, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);
	}

	private String clear(Context context) {
		ActivityManager activityManger = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
				// System.out.println("pid---->>>>>>>" + apinfo.pid);
				// System.out.println("processName->> " + apinfo.processName);
				// System.out.println("importance-->>" + apinfo.importance);
				String[] pkgList = apinfo.pkgList;
				if (apinfo.processName.equals("com.csw.ujplayerui")
						|| apinfo.processName.equals("com.csw.cc_server")) {
					System.out.println("22222222222222222222222222222222222");
					continue;
				}
				if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
					// Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						// 2.2以上是过时的,请用killBackgroundProcesses代替
						/** 清理不可用的内容空间 **/
						// activityManger.restartPackage(pkgList[j]);
						activityManger.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
		int after = calculate(context);
		String neicuistr = after+"";
		Selectpopwindow(AllMaincontext,neicuistr);
		System.out.println(">>>>>>>>neicuistr:"+neicuistr);
		return neicuistr;
	}
	private int calculate(Context mContext) {
		long unablememory = AppUtil.getAvailMemory(mContext);
		long totalmemory = AppUtil.getTotalMemory(mContext);
		double p = ((totalmemory - unablememory) * 100) / totalmemory;
		int percentage = (int) p;
		Log.i("info", "ddddddddddddddddddddddddd" + percentage);
		return percentage;

	}
	/**
	 * 
	 * @author 接受广播的操作
	 *
	 */
//	class MyBroadcastReceiver extends BroadcastReceiver {
//		   //接收到广播会被自动调用	
//		  @Override
//		  public void onReceive (Context context, Intent intent) {
//		    if ("scend_message".equals(intent.getAction())) {
//				String neicun = intent.getStringExtra("neicun");
//				try {
//					sendcount(neicun);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		   
//		  }
//		}
	
	
	 private void Selectpopwindow(Context context,String str){
		 TextView tv ;
		 wManager = (WindowManager) context.getApplicationContext().getSystemService(
			        Context.WINDOW_SERVICE);
	     mParams	= new WindowManager.LayoutParams();
			    mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
			    mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
			    //mParams.format = PixelFormat.RGBA_8888;
			    mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
			    mParams.width = 400;//窗口的宽和高
			    mParams.height = 100;
			    mParams.x = 630;//窗口位置的偏移量
			    mParams.y = 630;
			     myview = View.inflate(context, R.layout.popwindow,
						null);
			   tv = (TextView) myview.findViewById(R.id.tv);
			   tv.setText("当前可用内存为："+str+"%");
			   hand.sendEmptyMessage(0);
	 }
	 Handler hand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					wManager.addView(myview, mParams);
					
					break;
				case 1:
		            if (null!=myview.getParent()) {
						wManager.removeView(myview);
					}
					break;
				default:
					break;
				}
			}
	 };
	
	 /**
	  * 像手机助手发送所有应用
	  * */
	 public void sendappupdate(){
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("安装完成刷新一下手机助手中的APP");
				String startApp = "error";
				if (AllMaincontext != null) {
					startApp = preferencesUtils.getData(AllMaincontext,
							"SetAppStart");
					if (startApp == null) {
						startApp = "error";

					}
				}
				GetAppInfo getappinfo = new GetAppInfo(AllMaincontext);
				String jsonResult = gson.toJson(getappinfo
						.getAllAPK_add());
				jsonResult = jsonResult + "getappstart:" + startApp
						+ ":getappstart";
				// Log.e(null, jsonResult);
				byte[] dataByte;
				try {
					dataByte = jsonResult.getBytes("GBK");
					os.write(dataByte, 0, dataByte.length);
					os.flush();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}).start();
	 }
	 
}



//loadMusic();
		
