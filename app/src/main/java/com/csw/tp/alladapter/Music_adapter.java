package com.csw.tp.alladapter;

import java.util.List;

import com.zhongqin.tianlai.R;
import com.csw.tp.babei.wyf.model.Gequinfo;



import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Music_adapter extends BaseAdapter {

	List<Gequinfo> listdata =null;
	Context context = null;
	LayoutInflater infater = null;
	public Music_adapter(List<Gequinfo> listdata,Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listdata = listdata;
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listdata.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;
		viewholder holder = null;
		if (arg1 == null || arg1.getTag() == null) 
		{
			view = infater.inflate(R.layout.musicitem, null);
			holder = new viewholder();
			holder.nametv = (TextView) view.findViewById(R.id.tv);
			holder.imv = (ImageView) view.findViewById(R.id.tishiimvh);
			view.setTag(holder);
		} 
		else
		{
			view = arg1 ;
			holder = (viewholder) arg1.getTag() ;
		}
		Gequinfo str1 = (Gequinfo) getItem(arg0);
		String  str = str1.getName(); 
		holder.nametv.setText(arg0+1+". "+str);
		if (str1.isIscheck()) {
			holder.imv.setVisibility(view.VISIBLE);
		}else if(!str1.isIscheck()){
			holder.imv.setVisibility(view.INVISIBLE);
		}
		return view;
	}
	class viewholder{
		 TextView nametv;
		 ImageView imv;
		
	 }
}
