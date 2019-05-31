package com.weather.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.csw.csw_desktop_yahao.MainActivity;
import com.zhongqin.tianlai.R;
import com.wyf.util.Constant;
import com.wyf.util.ImageManager;
import com.wyf.util.NetCheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * 
 * @author i-zqluo 一个设置城市的Activity
 */
public class TianqiActivity extends Activity {
	// 定义的一个自动定位的列表
	private ListView gpsView;
	// 定义的一个省份可伸缩性的列表
	private ListView provinceList;

	private ListView tianqilistview;
	// 定义的用于过滤的文本输入框
	private TextView filterText;

	// 定义的一个记录城市码的SharedPreferences文件名
	public static final String CITY_CODE_FILE = "city_code";

	public static String StacityName;
	// 城市的编码
	private String[][] cityCodes;
	// 省份
	private String[] groups;
	// 对应的城市
	private String[][] childs;

	// 自定义的伸缩列表适配器
	private MyListAdapter adapter;

	// 记录应用程序widget的ID
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private static String cityName = null;
	private static String cityCode = null;
	public WeatherManager mainActivity;
	private TextView tianqiweather, tianqicity, tianqimax, tianqisymbol;
	private ImageView tianqiimage1, pmimv;
	private static int count = 0;
	private static String pm = "优";
	private RelativeLayout weather;

	public static SimpleAdapter ProvinceAdapter;
	public static SimpleAdapter AreaAdapter;
	public static List<Map<String, Object>> provincedata = new ArrayList<Map<String, Object>>();
	public static List<Map<String, Object>> areadata = new ArrayList<Map<String, Object>>();
	private static ImageManager imageManager = new ImageManager();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.tianqi);
		weather = (RelativeLayout) findViewById(R.id.weather);
		tianqiweather = (TextView) findViewById(R.id.tianqiweather);
		tianqicity = (TextView) findViewById(R.id.tianqicity);
		tianqimax = (TextView) findViewById(R.id.tianqimax);
		tianqisymbol = (TextView) findViewById(R.id.tianqisymbol);
		tianqiimage1 = (ImageView) findViewById(R.id.tianqiimage1);
		pmimv = (ImageView) findViewById(R.id.pmimv);

		provinceList = (ListView) findViewById(R.id.provinceList);
		tianqilistview = (ListView) findViewById(R.id.tianqilistview);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		//tianqiweather.setText(bundle.getString("weather"));
		tianqicity.setText(bundle.getString("cityname"));
		//tianqimax.setText(bundle.getString("max"));
		//tianqisymbol.setText(bundle.getString("min") + " ~");
		//pm = bundle.getString("pm");
		//getbg();
		//getpm();
		//getcurrentimage();
		// 导入城市编码数据库
		importInitDatabase();

		// 增强用户体验，在加载城市列表时显示进度对话框
		final ProgressDialog dialog = getProgressDialog("", "正在加载城市列表...");
		dialog.show();
		// 伸缩性列表的加载处理类
		final MyHandler mHandler = new MyHandler();
		new Thread(new Runnable() {
			public void run() {
				// 查询处理数据库,装载伸展列表
				DBHelper dbHelper = new DBHelper(TianqiActivity.this,
						"db_weather.db");
				groups = dbHelper.getAllProvinces();
				/*for (int i = 0; i < groups.length; i++) {
				 	System.out.println(groups[i] + "--------->城市名");
				}*/
				List<String[][]> result = dbHelper.getAllCityAndCode(groups);
				childs = result.get(0);
				/*for (int i = 0; i < groups.length; i++) {
					for (int x = 0; x < childs[i].length; x++)
					 	System.out.println(childs[i][x] + "--------->城市名");
				}*/
				cityCodes = result.get(1);
				// 交给Handler对象加载列表
				Message msg = new Message();
				mHandler.sendMessage(msg);
				dialog.cancel();
				dialog.dismiss();
			}
		}).start();
	}

	// 将res/raw中的城市数据库导入到安装的程序中的database目录下
	@SuppressLint("SdCardPath")
	public void importInitDatabase() {
		// 数据库的目录
		String dirPath = "/data/data/com.csw.csw_desktop/databases";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 数据库文件
		File dbfile = new File(dir, "db_weather.db");
		try {
			if (!dbfile.exists()) {
				dbfile.createNewFile();
			}
			// 加载欲导入的数据库
			InputStream is = this.getApplicationContext().getResources()
					.openRawResource(R.raw.db_weather);
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffere = new byte[is.available()];
			is.read(buffere);
			fos.write(buffere);
			is.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 得到一个进度对话框
	public ProgressDialog getProgressDialog(String title, String content) {
		// 实例化进度条对话框ProgressDialog
		ProgressDialog dialog = new ProgressDialog(this);

		// 可以不显示标题
		dialog.setTitle(title);
		dialog.setIndeterminate(true);
		dialog.setMessage(content);
		dialog.setCancelable(true);
		return dialog;
	}

	/*// 利用GPS功能得到当前位置的城市名
	public synchronized Map<Integer, String> getLocationCityInfo() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 设置一个Criteria标准用于过滤LocationProvider
		Criteria criteria = new Criteria();
		// 设置不需要高度信息
		criteria.setAltitudeRequired(false);
		// 设置不需要方位信息
		criteria.setBearingRequired(false);
		// 设置得到的为免费
		// criteria.setCostAllowed(false);

		// 得到最好的可用的Provider
		String provider = locationManager.getBestProvider(criteria, true);
		// 得到当前的位置对象
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			double latitude = location.getLatitude(); // 得到经度
			double longitude = location.getLongitude(); // 得到纬度
			// 根据经纬度得到详细的地址信息
			// 定义的一个网络访问工具类
			WebAccessTools webTools = new WebAccessTools(this);
			String addressContext = webTools
					.getWebContent("http://maps.google.cn/maps/geo?output=xml&q="
							+ latitude + "," + longitude);
			// 解析地址信息
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
				SAXParser parser = spf.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				LocationXMLParser handler = new LocationXMLParser();
				reader.setContentHandler(handler);

				StringReader read = new StringReader(addressContext);
				// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
				InputSource source = new InputSource(read);

				// 开始解析
				reader.parse(source);
				// 判断是否存在地址
				if (handler.hasAddress())
					return handler.getDetailAddress();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}*/

	// 用于处理装载伸缩性列表的处理类
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// 在伸缩性的列表中显示数据库中的省份与城市
			// adapter=new MyListAdapter(TianqiActivity.this, provinceList,
			// groups, childs);
			provincedata.clear();
			for (int i = 0; i < groups.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String x = groups[i];
				map.put("provinceName", x);
				provincedata.add(map);
			}

			ProvinceAdapter = new SimpleAdapter(TianqiActivity.this,
					provincedata, R.layout.listview,
					new String[] { "provinceName" }, new int[] { R.id.title });

			provinceList.setAdapter(ProvinceAdapter);

			provinceList.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if (!arg1) {
						provinceList.setSelector(R.drawable.xuankuang);
					} else {
						provinceList.setSelector(R.drawable.xuankuang1);
					}
				}
			});
			provinceList
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							// provinceList.setBackgroundResource(R.drawable.xuankuang1);
							provinceList.setSelector(R.drawable.xuankuang1);
							count = arg2;
							areadata.clear();
							String cityn;
							for (int x = 0; x < childs[arg2].length; x++) {
								// System.out.println(childs[arg2][x] +
								// "--------->城市名");
								cityn = childs[arg2][x];
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("areaName", cityn);
								areadata.add(map);
							}
							AreaAdapter = new SimpleAdapter(
									TianqiActivity.this, areadata,
									R.layout.listview,
									new String[] { "areaName" },
									new int[] { R.id.title });
							tianqilistview.setAdapter(AreaAdapter);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			provinceList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					provinceList.setSelector(R.drawable.xuankuang1);
					count = arg2;
					areadata.clear();
					String cityn;
					for (int x = 0; x < childs[arg2].length; x++) {
						// System.out.println(childs[arg2][x] +
						// "--------->城市名");
						cityn = childs[arg2][x];
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("areaName", cityn);
						areadata.add(map);
					}
					AreaAdapter = new SimpleAdapter(
							TianqiActivity.this, areadata,
							R.layout.listview,
							new String[] { "areaName" },
							new int[] { R.id.title });
					tianqilistview.setAdapter(AreaAdapter);
		
				}
				
			});
			
			
			
			
			tianqilistview
					.setOnFocusChangeListener(new OnFocusChangeListener() {

						@Override
						public void onFocusChange(View arg0, boolean arg1) {
							// TODO Auto-generated method stub
							if (arg1) {
								tianqilistview
										.setSelector(R.drawable.xuankuang1);
							} else {
								tianqilistview
										.setSelector(R.drawable.xuankuang);
							}
						}
					});
			tianqilistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					StacityName = childs[count][arg2];
					System.out.println(StacityName + "52525252525252");
					Intent resultValue = new Intent();
					resultValue.putExtra("city", StacityName);
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
							mAppWidgetId);
					setResult(RESULT_OK, resultValue);
					finish();
					System.gc();
				}
			});

		}
	}

	/*// 用于处理用户的定位信息
	private class LocateHandler extends Handler {
		// 记录定位的文本视图组件
		private TextView textView;

		public LocateHandler(TextView textView) {
			this.textView = textView;
		}

		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			int provinceIndex = data.getInt("provinceIndex");
			int cityIndex = data.getInt("cityIndex");
			// 判断定位匹配是否成功
			if (provinceIndex >= 0 && provinceIndex < groups.length
					&& cityIndex >= 0
					&& cityIndex < childs[provinceIndex].length) {

			} else {
				mainActivity = new WeatherManager();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							if (NetCheck
									.checkNetWorkStatus(TianqiActivity.this)) {
								String cityip = mainActivity.GetNetIp();
								if (!(cityip == null)) {
									String city1 = mainActivity
											.getCityByIp(cityip);
									cityName = mainActivity.getAdd(city1);
									DBHelper dbHelper = new DBHelper(
											TianqiActivity.this,
											"db_weather.db");
									cityCode = dbHelper
											.getCityCodeByName(cityName);

									GoToMainActivity thread = new GoToMainActivity(
											cityCode);
									thread.start();
									StacityName = cityName;
									Intent resultValue = new Intent();
									resultValue.putExtra("city", StacityName);
									resultValue
											.putExtra(
													AppWidgetManager.EXTRA_APPWIDGET_ID,
													mAppWidgetId);
									setResult(RESULT_OK, resultValue);
									finish();
								} else {
									Toast.makeText(getApplicationContext(),
											"网络异常！请检查网络", Toast.LENGTH_LONG)
											.show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"网络异常！请检查网络", Toast.LENGTH_LONG).show();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				}).start();
			}
		}
	}
*/
	// 处理用户选择好城市后的跳转到MainActivity
	private class GoToMainActivity extends Thread {

		// 保证跳转的城市码
		private String cityCode;
		// 跳转后显示的进度对话框
		private Dialog dialog;

		public GoToMainActivity(String cityCode) {
			this.cityCode = cityCode;
			// this.dialog = dialog;
		}

		public GoToMainActivity(String cityCode, Dialog dialog) {
			this.cityCode = cityCode;
			this.dialog = dialog;
		}

		public void run() {
			// 得到一个私有的SharedPreferences文件编辑对象
			SharedPreferences.Editor edit = getSharedPreferences(
					CITY_CODE_FILE, MODE_PRIVATE).edit();
			// 将城市码保存
			edit.putString("code", cityCode);
			edit.putString("city", StacityName);
			edit.commit();

			// 通过判断得到的widgetId是否有效来判断是跳转到MainActivity或Widget
			if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
				// 设置成功回退到天气情况显示Activity
				Intent intent = getIntent();
				// 当用户单击了城市返回，传入一个变量用于区分，是读存储文件天气，还是更新
				intent.putExtra("updateWeather", true);
				TianqiActivity.this.setResult(0, intent);
			} else {
				// 当有效则跳至widget
				AppWidgetManager appWidgetManager = AppWidgetManager
						.getInstance(TianqiActivity.this);
				RemoteViews views = new RemoteViews(
						TianqiActivity.this.getPackageName(),
						R.layout.widget_layout);
				// 更新widget
				Log.i("widget",
						"===================update  weather===========================");
				// 更新widget

				appWidgetManager.updateAppWidget(mAppWidgetId, views);
				// 设置成功，返回
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						mAppWidgetId);
				setResult(RESULT_OK, resultValue);
			}
			TianqiActivity.this.finish();

		}
	}

	public void getcurrentimage() {
		if (tianqiweather.getText().toString().contains("晴")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_01);
		} else if (tianqiweather.getText().toString().contains("多云")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_02);
		} else if (tianqiweather.getText().toString().contains("阴")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_04);
		} else if (tianqiweather.getText().toString().contains("雾")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_05);
		} else if (tianqiweather.getText().toString().contains("沙尘暴")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_06);
		} else if (tianqiweather.getText().toString().contains("阵雨")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_07);
		} else if (tianqiweather.getText().toString().contains("小雨")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_08);
		} else if (tianqiweather.getText().toString().contains("中雨")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_14);
		} else if (tianqiweather.getText().toString().contains("大雨")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_09);
		} else if (tianqiweather.getText().toString().contains("雷阵雨")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_10);
		} else if (tianqiweather.getText().toString().contains("小雪")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_11);
		} else if (tianqiweather.getText().toString().contains("大雪")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_12);
		} else if (tianqiweather.getText().toString().contains("雨夹雪")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_13);
		} else if (tianqiweather.getText().toString().contains("霾")) {
			tianqiimage1.setBackgroundResource(R.drawable.weather_15);
		} else {
			tianqiimage1.setBackgroundResource(R.drawable.weather_18);
		}
	}
 
	public void getbg() {
		if (tianqiweather.getText().toString().contains("晴")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_tianqing1));
		} else if (tianqiweather.getText().toString().contains("多云")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_duoyun1));
		} else if (tianqiweather.getText().toString().contains("阴")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_yin1));
		} else if (tianqiweather.getText().toString().contains("雪")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_xue1));
		} else if (tianqiweather.getText().toString().contains("雷")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_leiyu));
		} else if (tianqiweather.getText().toString().contains("沙")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_sha));
		} else if (tianqiweather.getText().toString().contains("雨")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_yu1));
		} else if (tianqiweather.getText().toString().contains("雾")
				|| tianqiweather.getText().toString().contains("霾")) {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_wu));
		} else {
			weather.setBackgroundDrawable(imageManager.getBitmapFromResources(this,R.drawable.weather_big));
		}
	}

	/*public void getpm() {
		if (pm.equals("优")) {
			pmimv.setBackgroundResource(R.drawable.pm_you);
		} else if (pm.equals("良")) {
			pmimv.setBackgroundResource(R.drawable.pm_liang);
		} else if (pm.equals("差")) {
			pmimv.setBackgroundResource(R.drawable.pm_cha);
		} else {
			pmimv.setBackgroundResource(R.drawable.pm_you);
		}
	}*/
	
	private static final String TAG="TianqiActivity";
	public void getpm() {
		
		try {
			int pmint=Integer.parseInt(pm);
			
			Log.d(TAG, "Current PM2.5 is "+pmint);
			if(pmint<50){
				Log.d(TAG, "Current air quality is best");
				pmimv.setBackgroundResource(R.drawable.pm_you);
			}else if(50<=pmint&&pmint<=100){
				Log.d(TAG, "Current air quality is good");
				pmimv.setBackgroundResource(R.drawable.pm_liang);
			}else if(100<pmint){
				Log.d(TAG, "Current air quality is bad");
				pmimv.setBackgroundResource(R.drawable.pm_cha);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
