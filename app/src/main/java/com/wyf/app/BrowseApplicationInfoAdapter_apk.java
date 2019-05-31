package com.wyf.app;

import java.util.List;

import com.zhongqin.tianlai.R;
import com.wyf.soushuapk.MyFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;




public class BrowseApplicationInfoAdapter_apk extends BaseAdapter 
{
	
	private List<MyFile> mlistAppInfo = null;
	
	LayoutInflater infater = null;
    
	public BrowseApplicationInfoAdapter_apk(Context context,  List<MyFile> apps) 
	{
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistAppInfo = apps ;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		System.out.println("size" + mlistAppInfo.size());
		return mlistAppInfo.size();
	}
	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return mlistAppInfo.get(position);
	}
	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) 
	{
		System.out.println("getView at " + position);
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) 
		{
			view = infater.inflate(R.layout.browse_app_item_add, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else
		{
			view = convertview ;
			holder = (ViewHolder) convertview.getTag() ;
		}
		MyFile appInfo = (MyFile) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getApk_icon());
		holder.tvAppLabel.setText(appInfo.getLabel());
		//holder.tvPkgName.setText(appInfo.getPkgName());
		return view;
	}

	class ViewHolder 
	{
		ImageView appIcon;
		TextView tvAppLabel;
		TextView tvPkgName;

		public ViewHolder(View view) 
		{
			this.appIcon = (ImageView) view.findViewById(R.id.imgApp_add);
			this.tvAppLabel = (TextView) view.findViewById(R.id.tvAppLabel_add);
			//this.tvPkgName = (TextView) view.findViewById(R.id.tvPkgName);
		}
	}
}