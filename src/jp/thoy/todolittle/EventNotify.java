package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.thoy.todolittle.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.util.Log;

public class EventNotify extends BroadcastReceiver {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(context));

		if(isDebug) Log.w(CNAME,"onReceive1");
		if(intent.getAction().equals(TDValue.NOTIFY)){
			RegistService rService = new RegistService(context);
			List<DoList> dList = new ArrayList<DoList>();
			List<DoList> nList = new ArrayList<DoList>();
			DataObject dObject = new DataObject(context);
			PendingIntent pIntent;
			Calendar calendar = Calendar.getInstance();
			TDShared tShared = new TDShared(context);
			int before = tShared.getBefore();
			int limit = tShared.getLimit();
			
			try {
				dObject = new DataObject(context);
				dList = dObject.reQuery(TDValue.RECENT,limit,0,0);
			} catch (SQLException ex){
				TraceLog saveTrace = new TraceLog(context);
				try {
					saveTrace.saveLog(ex, CommTools.getLastPart(CNAME,".") + " at 1st");
				} catch (IOException e) {
					ex.printStackTrace();
				}
			}
			
			if(dList != null){
				nList = dObject.reQuery(TDValue.RECENT,TDValue.NOTIFY_NUM,1,0);
				if(nList != null){
					NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					for(int i = 0;i < nList.size();i++){
						try{
							Long nMillis = CommTools.strToMillis(nList.get(i).datetime + ":10") - ((before + 1) * 60 * 1000);
							if(calendar.getTimeInMillis() >= nMillis){
						
								Notification.Builder builder = new Notification.Builder(context);
								Intent cIntent = new Intent(context,MainActivity.class);
								pIntent = PendingIntent.getActivity(context, 0, cIntent, 0);
								Notification notify = builder
									.setContentTitle(nList.get(i).event)
									.setContentText(nList.get(i).datetime)
									.setSmallIcon(R.drawable.ic_notify)
									.setTicker(context.getResources().getString(R.string.strNotify))
									.setContentIntent(pIntent)
									.setAutoCancel(true)
									.setDefaults(Notification.DEFAULT_ALL)
									.getNotification();
								nManager.notify(i, notify);
								String sql = "update " +  TDValue.TableName + " set notify = 0 where id = " + nList.get(i).id;
								if(isDebug){
									Log.w(CNAME,sql);
								}
								dObject.execSQL(sql);
							}
						} catch (Exception ex) {
							TraceLog saveTrace = new TraceLog(context);
							String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
							try {
								saveTrace.saveLog(ex,CommTools.getLastPart(CNAME, ".") + mname);
							} catch (IOException e) {
								ex.printStackTrace();
							}
						}
					}
				} else {
					//no data notify but is data recent
					Long nMillis = CommTools.strToMillis(dList.get(0).datetime + ":00");
					if(nMillis - calendar.getTimeInMillis() > ((before + 10) * 1000 * 60)){
						rService.stop(context);
						rService.register(before);
					}
				}
			} else {
				//no data recent
				rService.stop(context);
			}
		}
		Intent sIntent = new Intent(context,UpdateWidget.class);
		context.startService(sIntent);
		if(isDebug) Log.w(CNAME,"onReceive10");
	}

}
