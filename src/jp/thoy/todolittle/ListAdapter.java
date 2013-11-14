package jp.thoy.todolittle;

import java.util.List;

import jp.thoy.todolittle.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<DoList>{
	private final static boolean isDebug = false;
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");

	private LayoutInflater mInflater;
	
	public ListAdapter(Context context, List<DoList> objects) {
		super(context, 0, objects);
		// TODO 自動生成されたコンストラクター・スタブ
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position,View convertView,ViewGroup parent){
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.rows,null);
		}
		DoList item = this.getItem(position);
		if(item.id != 0){
			TextView id = (TextView)convertView.findViewById(R.id.textID);
			TextView name = (TextView)convertView.findViewById(R.id.textEvent);
			TextView date = (TextView)convertView.findViewById(R.id.textDate);
			TextView time = (TextView)convertView.findViewById(R.id.textTime);
			TextView notify = (TextView)convertView.findViewById(R.id.textNotify);
			id.setText(String.valueOf(item.id));
			name.setText(item.event);
			date.setText(item.date);
			time.setText(item.time);
			if(item.notify == 1) {
				notify.setText("N");
			} else {
				notify.setText("");
			}
			if(isDebug) Log.w(CNAME,"getView9");
		} else {
			TextView id = (TextView)convertView.findViewById(R.id.textID);
			TextView name = (TextView)convertView.findViewById(R.id.textEvent);
			TextView date = (TextView)convertView.findViewById(R.id.textDate);
			TextView time = (TextView)convertView.findViewById(R.id.textTime);
			id.setText(String.valueOf(item.id));
			name.setText(item.event);
			date.setText("");
			time.setText("");
		}
		if(isDebug) Log.w(CNAME,"getView10");
		return convertView;
	}
}
