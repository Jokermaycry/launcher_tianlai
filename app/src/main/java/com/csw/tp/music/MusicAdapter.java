package com.csw.tp.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.tp.babei.wyf.model.Music;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
	private List<Music> listMusic;
	private Context context;

	public MusicAdapter(Context context, List<Music> listMusic) {
		this.context = context;
		this.listMusic = listMusic;
	}

	public void setListItem(List<Music> listMusic) {
		this.listMusic = listMusic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.music_list_item_layout, null);
		}
		Music m = listMusic.get(position);

		TextView text = (TextView) convertView.findViewById(R.id.txt_ItemTitle);
		text.setText(position + 1 + ".");
		TextView textMusicName = (TextView) convertView
				.findViewById(R.id.music_item_name);
		textMusicName.setText(m.getName());
		return convertView;
	}

	/**
	 * ʱ���ʽת��
	 * 
	 * @param time
	 * @return
	 */
	public String toTime(int time) {
		time /= 1000;
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
}