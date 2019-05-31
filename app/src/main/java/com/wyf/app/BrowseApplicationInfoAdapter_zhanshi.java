package com.wyf.app;

import java.util.List;

import com.zhongqin.tianlai.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BrowseApplicationInfoAdapter_zhanshi extends BaseAdapter implements
		OnFocusChangeListener {

	private List<AppInfo> mlistAppInfo = null;

	LayoutInflater infater = null;

	public BrowseApplicationInfoAdapter_zhanshi(Context context,
			List<AppInfo> apps) {
		infater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistAppInfo = apps;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("size" + mlistAppInfo.size());
		return mlistAppInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistAppInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		System.out.println("getView at " + position);
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.browse_app_item_zhanshi, null);
			//view.setOnFocusChangeListener(this);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertview;
			holder = (ViewHolder) convertview.getTag();
		}
		AppInfo appInfo = (AppInfo) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.getAppIcon());
		holder.tvAppLabel.setText(appInfo.getAppLabel());
		holder.r_layout.setBackgroundDrawable(appInfo.getR_backgrond());
	//	holder.r_layout.setOnFocusChangeListener(this);
		// holder.tvPkgName.setText(appInfo.getPkgName());
		// view.setOnFocusChangeListener(this);
		return view;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView tvAppLabel;
		TextView tvPkgName;
		RelativeLayout r_layout;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.zhanshi_imgApp);
			this.tvAppLabel = (TextView) view
					.findViewById(R.id.zhanshi_tvAppLabel);
			this.r_layout = (RelativeLayout) view.findViewById(R.id.zhanshi_r);
			// this.tvPkgName = (TextView) view.findViewById(R.id.tvPkgName);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			scaleButton(v);
		} else {
			v.clearAnimation();
			v.invalidate();
		}
	}

	private void scaleButton(View v) {
		AnimationSet animationSet = new AnimationSet(true);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f,
				1.1f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animationSet.addAnimation(scaleAnimation);
		// animationSet.setStartOffset(1000);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		animationSet.setDuration(100);
		v.startAnimation(animationSet);
	}
}