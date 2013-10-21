package jp.thoy.todolittle;

import java.util.ArrayList;
import java.util.List;

import jp.thoy.todolittle.R;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;
	
	
	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
		Context context = getApplicationContext();

		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(context));

		TDShared tShared = new TDShared(context);
		int before = tShared.getBefore();
		
		RegistService rService = new RegistService(context);
		rService.register(before);
		RegistReceiver.regist(context);

		Intent sIntent = new Intent(context,UpdateWidget.class);
		context.startService(sIntent);
		if(isDebug) Log.w(CNAME,"OnStart");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PagerAdapter pagerAdapter;
		ViewPager viewPager;
		Context context = getApplicationContext();

		pagerAdapter = new PagerAdapter(context,getSupportFragmentManager());
		TDShared tShared = new TDShared(context);

		int limit = tShared.getLimit();

		pagerAdapter.setLimit(limit);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(pagerAdapter);
		if(isDebug) Log.w(CNAME,"OnCreate");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		Intent mIntent;
		switch(item.getItemId()){
		case R.id.action_add:
			mIntent = new Intent(getApplicationContext(),EditActivity.class);
			mIntent.putExtra(TDValue.KEY_ID, 0);
			mIntent.putExtra("CODE", 101);
			startActivityForResult(mIntent,101);
			break;
		case R.id.action_settings:
			mIntent = new Intent(this, EventPreference.class);
	    	startActivityForResult(mIntent,101);
			break;
		case R.id.action_debug:
			mIntent = new Intent(this, DebugActivity.class);
	    	startActivity(mIntent);
			break;
		default:
				;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, intent);
		int ret = requestCode;
		if(intent == null){
			return;
		}
		if(isDebug){
			Log.w(CNAME,"onActivityResult " + requestCode + " " + resultCode);
			Log.w(CNAME, "Request code ret " + ret);
		}
		if(resultCode == RESULT_OK) {

			Context context = this.getApplicationContext();
			TDShared tShared = new TDShared(context);
			int limit = tShared.getLimit();
			
			DataObject dObject = new DataObject(this);
			ListView recentView = (ListView)findViewById(R.id.listRecent);
			ListView pastView = (ListView)findViewById(R.id.listPast);

			List<DoList>rlist = null;
			rlist = dObject.reQuery(TDValue.RECENT,limit,0,0);
			ListAdapter adapter;
			if(rlist != null){
				adapter = new ListAdapter(this.getApplicationContext(),rlist);
				recentView.setAdapter(adapter);
			} else {
				rlist = new ArrayList<DoList>();
				rlist.add(InitDoList.initList(context));
				adapter = new ListAdapter(this.getApplicationContext(),rlist);
				recentView.setAdapter(adapter);
			}
			
			List<DoList>plist = null;
			plist = dObject.reQuery(TDValue.PAST,limit,0,0);
			if(plist != null){
				adapter = new ListAdapter(this.getApplicationContext(),plist);
				pastView.setAdapter(adapter);
			} else {
				plist = new ArrayList<DoList>();
				plist.add(InitDoList.initList(context));
				adapter = new ListAdapter(this.getApplicationContext(),plist);
				pastView.setAdapter(adapter);
			}
			Intent sIntent = new Intent(context,UpdateWidget.class);
			startService(sIntent);
			
			RegistService rService = new RegistService(this);
			rService.register(tShared.getBefore());

		}
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		if(isDebug){
			Context context = getApplicationContext();
	        Intent intent = new Intent(context, EventNotify.class);
	        PendingIntent pIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	        alarmManager.cancel(pIntent);

		}
	}

	public boolean isService(String className){
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningService = am.getRunningServices(Integer.MAX_VALUE);


		for (ActivityManager.RunningServiceInfo i : runningService) {
			if(i.service.getClassName().equals(className)){
				Log.w(CNAME,"service=" + i.service.getClassName());
				return true;
			}
		}
		return false;
	}

}
