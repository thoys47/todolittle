package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.thoy.todolittle.R;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidget extends Service {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;
	public final static String ACTION = "EDIT";
	final static int WR_NUM = 3;
	final static int WC_NUM = 3;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自動生成されたメソッド・スタブ
		if(isDebug) Log.w(CNAME,"onStart");
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(getApplicationContext()));
		widgetUpdate();
		if(isDebug) Log.w(CNAME,"onStart");
		return super.onStartCommand(intent, flags, startId);
	}

	public void widgetUpdate(){
		
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(getApplicationContext()));
		Context context = getApplicationContext();
        ComponentName mComponent = new ComponentName(context, EventWidget.class);
    	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

		RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(),R.layout.eventwidget);
		
		int[][] ids= new int[WR_NUM][WC_NUM];
		if(isDebug) Log.w(CNAME,"update 1");
		ids[0][0] = R.id.textEvent1;
		ids[0][1] = R.id.textDate1;
		ids[0][2] = R.id.textTime1;
		ids[1][0] = R.id.textEvent2;
		ids[1][1] = R.id.textDate2;
		ids[1][2] = R.id.textTime2;
		ids[2][0] = R.id.textEvent3;
		ids[2][1] = R.id.textDate3;
		ids[2][2] = R.id.textTime3;
		
		DataObject mDataObject = new DataObject(context);
		List<DoList> rList = new ArrayList<DoList>();
		try{
			rList = mDataObject.reQuery(0,"3",0);
		} catch (Exception ex) {
			TraceLog traceLog = new TraceLog(context);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CNAME + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		if(isDebug) Log.w(CNAME,"update 2");
		if(rList != null){
			if(isDebug) Log.w(CNAME,"update 2.1");
			for(int i = 0;i < rList.size();i++){
				mRemoteViews.setTextViewText(ids[i][0], rList.get(i).event);
				mRemoteViews.setTextViewText(ids[i][1], rList.get(i).date.toString().substring(5));
				mRemoteViews.setTextViewText(ids[i][2], rList.get(i).time);
			}
			for(int i = rList.size();i < WR_NUM;i++){
				for(int j = 0;j < WC_NUM;j++){
					mRemoteViews.setTextViewText(ids[i][j], "");
				}
			}
			if(isDebug) Log.w(CNAME,"cnt=" + rList.size());
		} else {
			if(isDebug) Log.w(CNAME,"update 2.2");
			for(int j = 0;j < WC_NUM;j++){
				//mRemoteViews.setTextViewText(ids[0][j], context.getString(R.string.strnone));
			}
			
		}
		
		if(isDebug) Log.w(CNAME,"update 3");
		Intent cIntent = new Intent(context,MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, cIntent, 0);
		mRemoteViews.setOnClickPendingIntent(R.id.widgetLayout, pIntent);
		
		appWidgetManager.updateAppWidget(mComponent, mRemoteViews);
		if(isDebug) Log.w(CNAME,"update 4");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
