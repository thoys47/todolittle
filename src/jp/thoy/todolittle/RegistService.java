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
	public final static int SHORT = 1;
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;
	Context mContext;
	
	public RegistService(Context context){
		mContext = context;
	}
	
	public void stop(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		Intent intent = new Intent(context,EventNotify.class);
        intent.setAction(TDValue.ACTION);
		PendingIntent sIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	alarmManager.cancel(sIntent);
	}

	public void register(int before){
		Intent intent;
        PendingIntent sIntent;
        PendingIntent lIntent;
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Activity.ALARM_SERVICE);

        if(isDebug) Log.w(CNAME,"register 1");
        
		if(before == SHORT){
	        intent = new Intent(mContext,EventNotify.class);
	        intent.setAction(TDValue.ACTION);
			sIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND,60 - calendar.get(Calendar.SECOND));
			alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), 1000 * 60,  sIntent);
		} else {
			this.stop(mContext);
			
			intent = new Intent(mContext,LongTermService.class);
			lIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        
	        DataObject mDataObject = new DataObject(mContext);
	        if(mDataObject.getNotify(before, 0, 0) == null){
	        	this.stop(mContext);
	        	return;
	        }
	        
	        List<DoList> dList = new ArrayList<DoList>();
	        dList = mDataObject.getNotify(0, 1, 0);
	        if(dList != null){
	        	String next;
	        	String stop;
	        	DoList list = dList.get(0);
				long cMillis = CommTools.strToMillis(list.datetime + ":00") - 1000 * 60 * (before + 10);
				if(cMillis != 0){
					if(cMillis < System.currentTimeMillis()) {
			    		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000,  lIntent);
			    		next = CommTools.millisToStr(System.currentTimeMillis() + 10000);
					} else {
			    		alarmManager.set(AlarmManager.RTC, cMillis,  lIntent);
			    		next = CommTools.millisToStr(cMillis);
					}
					stop = CommTools.replaceSymbol(list.datetime + ":00");
	                SharedPreferences mPreferences = mContext.getSharedPreferences(TDValue.PREFILENAME,Activity.MODE_PRIVATE);
	                Editor mEdit = mPreferences.edit();
	                mEdit.putString(TDValue.KEY_STOP,stop);
	                mEdit.commit();
	                if(isDebug){
		                TraceLog saveLog = new TraceLog(mContext);
		                try{
		                	saveLog.saveDebug("regist at:" + next.substring(5));
		                }catch (IOException ex){
		                	ex.printStackTrace();
		                }
		                	Log.w(CNAME,"next = " + next + " stop = " + stop);
	                }
		        } else {
		        	alarmManager.cancel(lIntent);
		        }
	        }
		}
        if(isDebug) Log.w(CNAME,"register 10");
	}
	
}
