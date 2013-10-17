package jp.thoy.todolittle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TDShared {

	Context mContext;
	
	public TDShared(Context context){
		mContext = context;
	}
	
	private SharedPreferences getSharedPrefs(){
		return mContext.getSharedPreferences(TDValue.PREFILENAME,Activity.MODE_PRIVATE);
	}
	
	public int getBefore(){
		SharedPreferences sPrefs = getSharedPrefs();
		return sPrefs.getInt(TDValue.KEY_BEFORE, TDValue.DEF_DEFORE);
	}

	public void putBefore(int before){
		SharedPreferences sPrefs = getSharedPrefs();
		Editor edit = sPrefs.edit();
		edit.putInt(TDValue.KEY_BEFORE, before);
		edit.commit();
	}

	public int getLimit(){
		SharedPreferences sPrefs = getSharedPrefs();
		return sPrefs.getInt(TDValue.KEY_LIMIT, TDValue.DEF_LIMIT);
	}

	public void putLimit(int limit){
		SharedPreferences sPrefs = getSharedPrefs();
		Editor edit = sPrefs.edit();
		edit.putInt(TDValue.KEY_LIMIT, limit);
		edit.commit();
	}

}
