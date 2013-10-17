package jp.thoy.todolittle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LongTermService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自動生成されたメソッド・スタブ
		RegistService rService = new RegistService(getApplicationContext());
		rService.stop(getApplicationContext());
		rService.register(RegistService.SHORT);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}
