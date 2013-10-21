package jp.thoy.todolittle;

import jp.thoy.todolittle.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimeDialog extends DialogFragment {
	OnTimeChangedListener mListener;
	
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;
	int iHour;
	int iMin;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		iHour = args.getInt("HOUR");
		iMin = args.getInt("MIN");
		if(isDebug) Log.w(CNAME,"h="+iHour+"m="+iMin);
	}
	
	public void setCallBack(OnTimeChangedListener listener){
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		Activity mActivity = getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

		TimePicker mTimePicker = new TimePicker(mActivity);
		mTimePicker.setIs24HourView(true);
		mTimePicker.setOnTimeChangedListener(mListener);
		mTimePicker.setCurrentHour(iHour);
		mTimePicker.setCurrentMinute(iMin);
		builder.setView(mTimePicker);
		builder.setTitle(getString(R.string.ReqTime));
		builder.setPositiveButton(getString(R.string.btnOK),
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditActivity activity = (EditActivity) getActivity();
				Log.w(CNAME,"w=" + which);
				activity.cmdTimeDialog(which);
			}
		});
		builder.setNegativeButton(getString(R.string.btnCancel),
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditActivity activity = (EditActivity) getActivity();
				Log.w(CNAME,"w=" + which);
				activity.cmdTimeDialog(which);
			}
		});
        return builder.create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void show(android.app.FragmentManager manager, String tag) {
		// TODO 自動生成されたメソッド・スタブ
		super.show(manager, tag);
	}

	
	
}
