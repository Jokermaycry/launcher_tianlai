package com.wyf.xiezai;

import java.util.List;

import com.zhongqin.tianlai.R;
import com.wyf.app.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


//�Զ����������࣬�ṩ��listView���Զ���view
public class BrowseApplicationInfoAdapter_quanbuyingyong extends BaseAdapter 
{
	
	private List<AppInfo> mlistAppInfo = null;
	
	LayoutInflater infater = null;
    
	public BrowseApplicationInfoAdapter_quanbuyingyong(Context context,  List<AppInfo> apps) 
	{
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistAppInfo = apps ;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
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
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) 
		{
			view = infater.inflate(R.layout.browse_app_item_quanbuyingyong, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else
		{
			view = convertview ;
			holder = (ViewHolder) convertview.getTag() ;
		}
		AppInfo appInfo = (AppInfo) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getAppIcon());
		holder.tvAppLabel.setText(appInfo.getAppLabel());
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
			this.appIcon = (ImageView) view.findViewById(R.id.quanbuyingyong_imgApp);
			this.tvAppLabel = (TextView) view.findViewById(R.id.quanbuyingyong_tvAppLabel);
			//this.tvPkgName = (TextView) view.findViewById(R.id.tvPkgName);
		}
	}
}