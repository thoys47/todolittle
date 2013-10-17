package jp.thoy.todolittle;

import java.io.IOException;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class EventWidget extends AppWidgetProvider {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;

	long preMillis = 0;
	int[] mIds;
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO 自動生成されたメソッド・スタブ
		super.onDeleted(context, appWidgetIds);
		
		try{
			Intent sIntent = new Intent(context,UpdateWidget.class);
			context.stopService(sIntent);
		} catch(Exception ex) {
			TraceLog traceLog = new TraceLog(context);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CNAME + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		super.onReceive(context, intent);
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(context));
		if(isDebug){
			Log.w(CNAME,intent.getAction());
		}
		try{
			Intent sIntent = new Intent(context,UpdateWidget.class);
			context.startService(sIntent);
			if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
				SharedPreferences mPreference = context.getSharedPreferences(TDValue.PREFILENAME,Activity.MODE_PRIVATE);
				int before = mPreference.getInt(TDValue.KEY_BEFORE, 20);
				RegistService rService = new RegistService(context);
				rService.register(before);
				//RegistReceiver.regist(context);
			}
		} catch (Exception ex){
			TraceLog traceLog = new TraceLog(context);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CNAME + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		if(isDebug){
			try{
				TraceLog traceLog = new TraceLog(context);
				traceLog.saveDebug(CNAME + ":" + CommTools.getLastPart(intent.getAction(),"."));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO 自動生成されたメソッド・スタブ
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent sIntent = new Intent(context,UpdateWidget.class);
		context.startService(sIntent);
	}
	
}
