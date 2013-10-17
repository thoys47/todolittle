package jp.thoy.todolittle;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class RegistReceiver {
	
	public static void regist(Context context){
		IntentFilter iFilter = new IntentFilter();
		
		iFilter.addAction(Intent.ACTION_DATE_CHANGED);
		iFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
		iFilter.addAction(Intent.ACTION_TIME_CHANGED);
		iFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

		EventNotify mReceiver = new EventNotify();
		try{
			context.unregisterReceiver(mReceiver);
		} catch (IllegalArgumentException ex) {
			;
		}
		try{
			context.registerReceiver(mReceiver, iFilter);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
