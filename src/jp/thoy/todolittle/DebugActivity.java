package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;

import jp.thoy.todolittle.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

public class DebugActivity extends Activity {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug_main);
		final Context context = getApplicationContext();

		RadioGroup radioDebug = (RadioGroup)findViewById(R.id.radioDebug);

		int radio = R.id.debugTrace;
		if(isDebug) Log.w(CNAME,"radio=" + radio);
		TraceLog readTrace = new TraceLog(context);
		try{
			ArrayList<String> tLog = readTrace.readFile(radio);
			ListView listDebug = (ListView)findViewById(R.id.listDebug);
			if(tLog != null){
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_debug,R.id.textDebug, tLog);
				listDebug.setAdapter(adapter);
			} else {
				tLog = new ArrayList<String>();
				tLog.add(getString(R.string.strnone2));
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_debug,R.id.textDebug, tLog);
				listDebug.setAdapter(adapter);
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}

		
		radioDebug.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO 自動生成されたメソッド・スタブ
				int radio = checkedId;
				TraceLog readTrace = new TraceLog(context);
				try{
					ArrayList<String> tLog = readTrace.readFile(radio);
					ListView listDebug = (ListView)findViewById(R.id.listDebug);
					if(tLog != null){
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_debug,R.id.textDebug, tLog);
						listDebug.setAdapter(adapter);
					} else {
						tLog = new ArrayList<String>();
						tLog.add(getString(R.string.strnone2));
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_debug,R.id.textDebug, tLog);
						listDebug.setAdapter(adapter);
					}
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		});


	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}

}
