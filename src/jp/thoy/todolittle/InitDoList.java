package jp.thoy.todolittle;

import jp.thoy.todolittle.R;
import android.content.Context;

public class InitDoList {
	
	public static DoList initList(Context context){
		DoList d = new DoList();
		d.id = 0;
		d.event = context.getString(R.string.strnone);
		d.date = context.getString(R.string.strnone);
		d.time = context.getString(R.string.strnone);
		d.etime = "";
		d.done = 0;
		d.priority = 0;
		d.file = "";
		d.alert = 0;
		d.memo = "";
		d.notify = 0;
		d.datetime = "";
		return d;
	}

}
