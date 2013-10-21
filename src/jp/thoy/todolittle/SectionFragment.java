package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.thoy.todolittle.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SectionFragment extends Fragment  implements OnItemClickListener {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;
	int[] layouts;
	int[] ids;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		if(isDebug) Log.w(CNAME,"OnCreate1");

		layouts = new int[TDValue.TAB_NUM];
		ids = new int[TDValue.TAB_NUM];
		
		layouts[TDValue.RECENT] = R.layout.futurelist;
		layouts[TDValue.PAST] = R.layout.pastlist;
		ids[TDValue.RECENT] = R.id.listRecent;
		ids[TDValue.PAST] = R.id.listPast;

		if(isDebug) Log.w(CNAME,"OnCreate2");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ

		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(container.getContext()));

		int page = getArguments().getInt(TDValue.KEY_PAGE);
		int limit = getArguments().getInt(TDValue.KEY_LIMIT);
		
		View rootView = null;
		DataObject dObject;
		ListView listView = null; 

		rootView = inflater.inflate(this.layouts[page], container,false);
		listView = (ListView)rootView.findViewById(ids[page]);
		List<DoList> dList = null;
		Context context = rootView.getContext();
		
		try{
			dObject = new DataObject(context);
			dList = dObject.reQuery(page,limit,0,0);
		} catch (Exception ex) {
			TraceLog saveTrace = new TraceLog(context);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				saveTrace.saveLog(ex,CNAME + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		if(dList != null && dList.size() > 0){
			ListAdapter adapter = new ListAdapter(context,dList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		} else {
			dList = new ArrayList<DoList>();
			dList.add(InitDoList.initList(context));
			ListAdapter adapter = new ListAdapter(context,dList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tView = (TextView)view.findViewById(R.id.textID);
		if(isDebug) Log.w(CNAME,"OnClick text=" + tView.getText().toString());
		if(!tView.getText().toString().equals("0")) {
			Intent intent = new Intent(parent.getContext(),EditActivity.class);
			if(isDebug) Log.w(CNAME,"context" + view.getContext().toString());
			intent.putExtra(TDValue.KEY_ID,Integer.parseInt(tView.getText().toString()));
			if(isDebug) Log.w(CNAME,"put=" + Integer.parseInt(tView.getText().toString()));
			startActivityForResult(intent,TDValue.REQ_CODE);
		}
		if(isDebug) Log.w(CNAME,"OnClick");

	}

}
