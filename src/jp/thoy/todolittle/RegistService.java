package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class RegistService {
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;
	Context mContext;
	
	public RegistService(Context context){
		mContext = context;
	}
	
	public void stop(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		Intent intent = new Intent(context,EventNotify.class);
        intent.setAction(TDValue.NOTIFY);
		PendingIntent sIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	alarmManager.cancel(sIntent);
	}

	public void register(int before){
		Intent intent;
        PendingIntent sIntent;
        PendingIntent lIntent;
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Activity.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
        
        if(isDebug) Log.w(CNAME,"register 1");
        
		if(before == TDValue.SHORT){
	        intent = new Intent(mContext,EventNotify.class);
	        intent.setAction(TDValue.NOTIFY);
			sIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			calendar.add(Calendar.SECOND,(-1) * calendar.get(Calendar.SECOND));
			alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 1000 * 60,  sIntent);
		} else {
	        DataObject dObject = new DataObject(mContext);

	        this.stop(mContext);
			
			intent = new Intent(mContext,LongTermService.class);
			lIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	         
	        List<DoList> dList = new ArrayList<DoList>();
	        dList = dObject.reQuery(TDValue.RECENT, 1, 0, 0);
	        if(dList == null){
	        	this.stop(mContext);
	        	return;
	        }
	        
        	DoList list = dList.get(0);
			long cMillis = CommTools.strToMillis(list.datetime + ":00") - 1000 * 60 * (before + 10);
	    	alarmManager.set(AlarmManager.RTC, cMillis,  lIntent);
	    	if(isDebug) Log.w(CNAME,CommTools.millisToStr(cMillis));
		}
        if(isDebug) Log.w(CNAME,"register 10");
	}
	
}
