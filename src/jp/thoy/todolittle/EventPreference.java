package jp.thoy.todolittle;


import jp.thoy.todolittle.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class EventPreference extends Activity implements OnCheckedChangeListener{

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		Context context = getApplicationContext();
		
		TDShared tShared = new TDShared(context);
		int limit = tShared.getLimit();
		int before = tShared.getBefore();
		
		RadioGroup radioLimit = (RadioGroup)findViewById(R.id.groupLimit);
		RadioGroup radioBefore = (RadioGroup)findViewById(R.id.groupBefore);
		
		switch(limit){
			case 5:
				radioLimit.check(R.id.radio05);
				break;
			case 10:
				radioLimit.check(R.id.radio10);
				break;
			case 15:
				radioLimit.check(R.id.radio15);
				break;
			default:
				;
		}

		switch(before){
			case 10:
				radioBefore.check(R.id.before10);
				break;
			case 20:
				radioBefore.check(R.id.before20);
				break;
			case 30:
				radioBefore.check(R.id.before30);
				break;
			default:
				;
		}

		radioLimit.setOnCheckedChangeListener(this);
		radioBefore.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		// TODO 自動生成されたメソッド・スタブ
		Context context = getApplicationContext();
		TDShared tShared = new TDShared(context);
		switch(radioGroup.getId()){
			case R.id.groupLimit:
				int limit = TDValue.DEF_LIMIT;
				switch(checkedId){
					case R.id.radio05:
						limit = 5;
						break;
					case R.id.radio10:
						limit = 10;
						break;
					case R.id.radio15:
						limit = 15;
						break;
				}
				if(isDebug) Log.w(CNAME,"limit=" + limit);
				tShared.putLimit(limit);
		        break;
			case R.id.groupBefore:
				int before = TDValue.DEF_DEFORE;
				switch(checkedId){
					case R.id.before10:
						before = 10;
						break;
					case R.id.before20:
						before = 20;
						break;
					case R.id.before30:
						before = 30;
						break;
				}
				tShared.putBefore(before);
		}
		
	}

	
}
