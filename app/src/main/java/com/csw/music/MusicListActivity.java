package com.csw.music;

import java.util.ArrayList;
import java.util.List;


import com.zhongqin.tianlai.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MusicListActivity extends Activity implements OnItemClickListener{

	private ListView music_listview;
	private static String whichPathOwn = "internal_sd";// 在哪个目录下，U盘或者SD卡
	public static List<Music> mMusiclist = new ArrayList<Music>();
	private MusicAdapter musicAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musiclist);
		music_listview=(ListView) findViewById(R.id.music_listview);
		mMusiclist = MusicList.getMusicData(this, whichPathOwn);
		musicAdapter = new MusicAdapter(this, mMusiclist);
		music_listview.setAdapter(musicAdapter);
		music_listview.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		  Intent intent = new Intent();
		  Bundle bundle = new Bundle();
		  bundle.putString("music_url",mMusiclist.get(arg2).getUrl());
		  bundle.putString("music_title",mMusiclist.get(arg2).getTitle());
		  bundle.putInt("arg", arg2);
		  intent.putExtras(bundle);
		  this.setResult(1000, intent);
		  finish();
		  
	}
	
	
	
}
