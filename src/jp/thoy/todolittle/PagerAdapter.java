package jp.thoy.todolittle;

import jp.thoy.todolittle.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentPagerAdapter {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;
	private Context mContext;
	private String mLimit;
	
	public PagerAdapter(Context context,FragmentManager fm) {
		super(fm);
		// TODO 自動生成されたコンストラクター・スタブ
		mContext = context;
	}

	public void setLimit(String limit){
		mLimit = limit;
	}
	
	@Override
	public Fragment getItem(int item) {
		// TODO 自動生成されたメソッド・スタブ
		Fragment fragment = new SectionFragment();
		
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(mContext));
		if(isDebug) Log.w(CNAME,"getItem1");
		Bundle args = new Bundle();
		args.putInt(TDValue.KEY_PAGE, item);
		args.putString(TDValue.KEY_LIMIT, mLimit);
		fragment.setArguments(args);
		if(isDebug) Log.w(CNAME,"getItem2");
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO 自動生成されたメソッド・スタブ
		return TDValue.TAB_NUM;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO 自動生成されたメソッド・スタブ
		switch(position){
		case TDValue.RECENT:
			return mContext.getString(R.string.title_section1);
		case TDValue.PAST:
			return mContext.getString(R.string.title_section2);
		}
		if(isDebug) Log.w(CNAME,"position=" + position);
		return null;
	}

	
}
