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

		RegistService rService = new RegistService(context);
		List<DoList> nList = new ArrayList<DoList>();
		DataObject mDataObject = new DataObject(context);
		PendingIntent pIntent;
		Calendar calendar = Calendar.getInstance();
		TDShared tShared = new TDShared(context);
		String check = CommTools.replaceSymbol(CommTools.CalendarToString(Calendar.getInstance(),CommTools.DATETIMESHORT) + "00");
		SharedPreferences mPrefer = context.getSharedPreferences(TDValue.PREFILENAME,Activity.MODE_PRIVATE);
		int before = tShared.getBefore();
		if(isDebug) Log.w(CNAME,"onReceive1");
		if(intent.getAction().equals(TDValue.ACTION)){
			String stop = mPrefer.getString(TDValue.KEY_STOP, "");
			
			try {
				mDataObject = new DataObject(context);
				nList = mDataObject.getNotify(before + 10,TDValue.MAX,1);
			} catch (SQLException ex){
				TraceLog saveTrace = new TraceLog(context);
				try {
					saveTrace.saveLog(ex, CommTools.getLastPart(CNAME,".") + " at 1st");
				} catch (IOException e) {
					ex.printStackTrace();
				}
			}
			
			if(nList != null){
				calendar.setTimeInMillis(CommTools.strToMillis(nList.get(0).datetime + ":00") - before * 60 * 1000);
				String timing = CommTools.replaceSymbol(CommTools.CalendarToString(calendar,CommTools.DATETIMESHORT)) + "00";
				//まだ通知時刻前ならreturn
				if(Long.parseLong(timing) > Long.parseLong(check)){
					return;
				}
				
				nList = mDataObject.getNotify(before,TDValue.MAX,1);
				
				if(nList == null){
					rService.stop(context);
					return;
				}
				
				NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				for(int i = 0;i < nList.size();i++){
					try{
						Notification.Builder builder = new Notification.Builder(context);
						Intent cIntent = new Intent(context,MainActivity.class);
						pIntent = PendingIntent.getActivity(context, 0, cIntent, 0);
						Notification notify = builder
							.setContentTitle(nList.get(i).event)
							.setContentText(nList.get(i).datetime)
							.setSmallIcon(R.drawable.ic_notify)
							.setTicker("ToDoLittle Alert")
							.setContentIntent(pIntent)
							.setAutoCancel(true)
							.setDefaults(Notification.DEFAULT_ALL)
							.getNotification();
						nManager.notify(i, notify);
						String sql = "update " +  TDValue.TableName + " set notify = 0 where id = " + nList.get(i).id;
						if(isDebug){
							Log.w(CNAME,sql);
						}
						mDataObject.execSQL(sql);
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
			}//通知するものがすでにない場合の終わり
			if(Long.parseLong(stop) < Long.parseLong(check)){
				Intent sIntent = new Intent(context,UpdateWidget.class);
				context.startService(sIntent);
		    	rService.register(before);
			}
			
		} else {
			TraceLog saveLog = new TraceLog(context);
			try {
				saveLog.saveDebug(CommTools.getLastPart(CNAME, ".") + ":" + CommTools.getLastPart(intent.getAction(),"."));
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			rService.stop(context);
	    	rService.register(before);
		}
		if(isDebug) Log.w(CNAME,"onReceive10");
	}

}
