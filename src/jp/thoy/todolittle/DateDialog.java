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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DateDialog extends DialogFragment {
	OnDateChangedListener mListener;
	
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;

	int iYear;
	int iMonth;
	int iDay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		iYear = args.getInt("YEAR");
		iMonth = args.getInt("MONTH");
		iDay = args.getInt("DAY");
		if(isDebug) Log.w(CNAME,"y="+iYear+"m="+iMonth+"d"+iDay);
	}
	
	public void setCallBack(OnDateChangedListener listener){
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		Activity mActivity = getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

		DatePicker mDatePicker = new DatePicker(mActivity);
		mDatePicker.init(iYear, iMonth, iDay, mListener);
		mDatePicker.setSpinnersShown(false);
		builder.setView(mDatePicker);
		builder.setTitle(getString(R.string.ReqDate));
		builder.setPositiveButton(getString(R.string.btnOK),
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditActivity activity = (EditActivity) getActivity();
				Log.w(CNAME,"w=" + which);
				activity.cmdDateDialog(which);
			}
		});
		builder.setNegativeButton(getString(R.string.btnCancel),
			new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditActivity activity = (EditActivity) getActivity();
				Log.w(CNAME,"w=" + which);
				activity.cmdDateDialog(which);
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
