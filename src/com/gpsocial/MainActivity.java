package com.gpsocial;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.gpsocial.adapter.TabsPagerAdapter;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	private String[] tabs = {"Map", "Home", "Profile"};
	
	private String mUserId;
	private double mLong = 0, mLat = -100;
	private RequestParams mRequestParams = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mUserId = getIntent().getStringExtra("userId");
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
	        .imageScaleType(ImageScaleType.EXACTLY)
	        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
    		.defaultDisplayImageOptions(defaultOptions)
    		.threadPoolSize(4)
        	.build();
        ImageLoader.getInstance().init(config);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		for (String tab_name : tabs){
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}


		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	
			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
	
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
	
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		viewPager.setCurrentItem(1);
	}
	
	public RequestParams getRequestParams() {
		if (mRequestParams == null) {
			mRequestParams = new RequestParams();
			mRequestParams.add("id", mUserId);
		}
		if (mLat > -100) {
			mRequestParams.put("lng", Double.toString(mLong));
			mRequestParams.put("lat", Double.toString(mLat));
		}
		return mRequestParams;
	}
	
	public void setLocation(double lng, double lat) {
		mLong = lng; mLat = lat;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	
}
