package jp.thoy.todolittle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LongTermService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		RegistService rService = new RegistService(getApplicationContext());
		rService.stop(getApplicationContext());
		rService.register(RegistService.SHORT);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}
