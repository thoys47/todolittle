package jp.thoy.todolittle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.thoy.todolittle.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class EditActivity extends Activity implements OnClickListener {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = true;

	
	private IntegerCalendar intCalendar;
	private IntegerCalendar preCalendar;
	private Uri imageUri;
	private int timeIds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_main);
		//DatePickerDialog mDateDialog;
		DataObject dObject;
		intCalendar = new IntegerCalendar();
		preCalendar = new IntegerCalendar();
		
		Thread.setDefaultUncaughtExceptionHandler(new TraceLog(getApplicationContext()));
		Intent intent = getIntent();
		
		int ids = intent.getIntExtra(TDValue.KEY_ID,0);

		if(isDebug) Log.w(CNAME,"ids=" + ids);
		
		TextView lblID = (TextView)findViewById(R.id.lblID);
		EditText editEvent = (EditText)findViewById(R.id.editEvent);
		EditText editDate = (EditText)findViewById(R.id.editDate);
		EditText editTime = (EditText)findViewById(R.id.editTime);
		EditText editEndTime = (EditText)findViewById(R.id.editEndTime);
		EditText editAttach = (EditText)findViewById(R.id.editAttach);
		
		Button btnSave = (Button)findViewById(R.id.btnSave);
		Button btnDelete = (Button)findViewById(R.id.btnDelete);

		Switch notify = (Switch)findViewById(R.id.switchNotify);
		Switch done = (Switch)findViewById(R.id.switchDone);

		registerForContextMenu(editAttach);
		
		if(isDebug) Log.w(CNAME,"OnCreate2");
		List<DoList> aList = new ArrayList<DoList>(); 
		dObject = new DataObject(getApplicationContext());
		if(ids != 0){
			lblID.setText(String.valueOf(ids));
			aList = dObject.reQuery(0,"",ids);
			editEvent.setText(aList.get(0).event);
			editDate.setText(aList.get(0).date);
			editTime.setText(aList.get(0).time);
			editEndTime.setText(aList.get(0).etime);
			if(isDebug) Log.w(CNAME,"OnCreate3");
			if(aList.get(0).notify == 1){
				notify.setChecked(true);
			} else {
				notify.setChecked(false);
			}
			if(aList.get(0).done == 1){
				done.setChecked(true);
			} else {
				done.setChecked(false);
			}
			if(isDebug) Log.w(CNAME,"OnCreate4 + datetime=" + aList.get(0).datetime);
			editAttach.setText(aList.get(0).file);
			intCalendar.year = CommTools.DateStr2Int(aList.get(0).datetime + ":00", Calendar.YEAR);
			intCalendar.month = CommTools.DateStr2Int(aList.get(0).datetime + ":00", Calendar.MONTH) - 1;
		    intCalendar.day =  CommTools.DateStr2Int(aList.get(0).datetime + ":00", Calendar.DAY_OF_MONTH);
		    intCalendar.hour =  CommTools.DateStr2Int(aList.get(0).datetime + ":00", Calendar.HOUR_OF_DAY);
		    intCalendar.min =  CommTools.DateStr2Int(aList.get(0).datetime + ":00", Calendar.MINUTE);
			if(isDebug) Log.w(CNAME,"OnCreate5");
		} else {
			if(isDebug) Log.w(CNAME,"OnCreate6");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			calendar.add(Calendar.MINUTE, (-1) * calendar.get(Calendar.MINUTE));
			calendar.add(Calendar.SECOND, (-1) * calendar.get(Calendar.SECOND));
			intCalendar.year = calendar.get(Calendar.YEAR);
			intCalendar.month = calendar.get(Calendar.MONTH);
			intCalendar.day =  calendar.get(Calendar.DAY_OF_MONTH);
			intCalendar.hour =  calendar.get(Calendar.HOUR_OF_DAY);
			intCalendar.min =  calendar.get(Calendar.MINUTE);
			
			lblID.setText("");
			editDate.setText(CommTools.CalendarToString(calendar, CommTools.DATE));
			editTime.setText(CommTools.CalendarToString(calendar, CommTools.TIMESHORT));
			calendar.add(Calendar.HOUR_OF_DAY,1);
			editEndTime.setText(CommTools.CalendarToString(calendar, CommTools.TIMESHORT));
			notify.setChecked(false);
			btnDelete.setEnabled(false);
		}
		if(isDebug) Log.w(CNAME,"OnCreate9");

		editAttach.setOnClickListener(this);
		editDate.setOnClickListener(this);
		editTime.setOnClickListener(this);
		editEndTime.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
			
		if(isDebug) Log.w(CNAME,"OnCreate10");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		Intent intent = new Intent();
		switch(item.getItemId()){
		case TDValue.MENU_PICTURE:
			String filename = CommTools.replaceSymbol(CommTools.CalendarToString(Calendar.getInstance(),CommTools.DATETIMELONG)) + ".jpg";
			filename = getExternalFilesDir(null) + "/" + filename;
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE,filename);
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			imageUri = Uri.fromFile(new File(filename));
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TDValue.CAPTURE_IMAGE);
			break;
		case TDValue.MENU_GALLERY:
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, TDValue.SELECT_IMAGE);
			break;
		case TDValue.MENU_REMOVE:
			EditText editAttach = (EditText)findViewById(R.id.editAttach);
			editAttach.setText("");
			File targetFile = new File(editAttach.getText().toString());
			if(targetFile.exists()){
				targetFile.delete();
			}
			break;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		
		EditText editAttach = (EditText)findViewById(R.id.editAttach);

		if(requestCode == TDValue.CAPTURE_IMAGE && resultCode == RESULT_OK){
			if(new File(imageUri.getPath()).exists()){
				editAttach = (EditText)findViewById(R.id.editAttach);
				editAttach.setText(imageUri.getPath());
			}
		}
		
		if(requestCode == TDValue.SELECT_IMAGE && resultCode == RESULT_OK){
			Uri uri = data.getData();
			ContentResolver contentResolver = getContentResolver();
			String[] columns = { MediaStore.Images.Media.DATA };
			Cursor cursor = contentResolver.query(uri, columns, null, null, null);
			cursor.moveToFirst();
			String path = cursor.getString(0);
			cursor.close();
			File imgfile = new File(path);
			String tFile = getExternalFilesDir(null) + "/" + imgfile.getName();
			File targetFile = new File(tFile);
			
			FileChannel ic;
			FileChannel oc;
			try {
				ic = new FileInputStream(imgfile.getAbsolutePath()).getChannel();
				oc = new FileOutputStream(targetFile).getChannel();
				oc.transferFrom(ic, 0, ic.size());
				ic.close();
				oc.close();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(targetFile.exists()){
				editAttach = (EditText)findViewById(R.id.editAttach);
				editAttach.setText(tFile);
			}

		}
		
		if(requestCode == TDValue.VIEW_PICTURE && resultCode == RESULT_OK){
			if(!new File(imageUri.getPath()).exists()){
				editAttach = (EditText)findViewById(R.id.editAttach);
				editAttach.setText("");
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, TDValue.MENU_PICTURE, 0, getString(R.string.strMenuPicture));
		menu.add(0, TDValue.MENU_GALLERY, 1, getString(R.string.strMenuGallery));
		menu.add(0, TDValue.MENU_REMOVE, 2, getString(R.string.strMenuRemove));
	}

	public void cmdDateDialog(int which){
		if(which == -1){
			EditText mEditDate = (EditText)findViewById(R.id.editDate);
			mEditDate.setText(CommTools.intDate2String(
					intCalendar.year, intCalendar.month, intCalendar.day, 0, 0, 0, CommTools.DATE));
		} else {
			storeCalendar(preCalendar,intCalendar);
		}
	}
	
	public void cmdTimeDialog(int which){
		if(which == -1){
			EditText editTime = (EditText)findViewById(timeIds);
			editTime.setText(CommTools.intDate2String(
					0, 0, 0, intCalendar.hour, intCalendar.min, 0, CommTools.TIMESHORT));
	
		} else {
			storeCalendar(preCalendar,intCalendar);
		}
	}

	private void showDateDialog(){
		android.app.FragmentManager manager = getFragmentManager();  
		DateDialog dateDialog = new DateDialog();
		Bundle args = new Bundle();
		args.putInt("YEAR", intCalendar.year);
		args.putInt("MONTH", intCalendar.month);
		args.putInt("DAY", intCalendar.day);
		storeCalendar(intCalendar,preCalendar);
		dateDialog.setCallBack(DateChangeHandler);
		dateDialog.setArguments(args);
		dateDialog.show(manager, "Date");
	}

	private void showTimeDialog(){
		android.app.FragmentManager manager = getFragmentManager();  
		TimeDialog timeDialog = new TimeDialog();
		
		Bundle args = new Bundle();
		storeCalendar(intCalendar,preCalendar);
		args.putInt("HOUR", intCalendar.hour);
		args.putInt("MIN", intCalendar.min);
		timeDialog.setCallBack(TimeChangeHandler);
		timeDialog.setArguments(args);
		timeDialog.show(manager, "Time");
	}

	OnDateChangedListener DateChangeHandler = new OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker picker, int year, int month, int day) {
			// TODO 自動生成されたメソッド・スタブ
			intCalendar.year = year;
			intCalendar.month = month;
			intCalendar.day = day;
		}

	};

	OnTimeChangedListener TimeChangeHandler = new OnTimeChangedListener() {

		@Override
		public void onTimeChanged(TimePicker picker, int hour, int minute) {
			// TODO 自動生成されたメソッド・スタブ
			intCalendar.hour = hour;
			intCalendar.min = minute;
		}

	};


	@Override
	public void onClick(View view) {
		// TODO 自動生成されたメソッド・スタブ
		DataObject dObject = new DataObject(getApplicationContext());
		Context context = getApplicationContext();
		TextView lblID = (TextView)findViewById(R.id.lblID);
		Intent intent = new Intent();
		int ret = 0;
		int ids;
		if(!lblID.getText().equals("")){
			ids = Integer.parseInt(lblID.getText().toString());
		} else {
			ids = 0;
		}

		switch(view.getId()){
			case R.id.editDate:
				this.showDateDialog();
				break;
			case R.id.editTime:
				timeIds = R.id.editTime;
				this.showTimeDialog();
				break;
			case R.id.editEndTime:
				timeIds = R.id.editEndTime;
				this.showTimeDialog();
				break;
			case R.id.editAttach:
				EditText editAttach = (EditText)findViewById(R.id.editAttach);
				String filename = editAttach.getText().toString();
				if(!filename.equals("") && filename != null){
					try{
						intent = new Intent(Intent.ACTION_VIEW);
						String extention = MimeTypeMap.getFileExtensionFromUrl(filename);
						String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention);
						intent.setDataAndType(Uri.fromFile(new File(filename)), mimetype);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						startActivityForResult(intent,TDValue.VIEW_PICTURE);				
					} catch (Exception ex) {
						TraceLog traceLog = new TraceLog(getApplicationContext());
						try {
							String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
							traceLog.saveLog(ex,CommTools.getLastPart(CNAME, ".") + mname);
						} catch (Exception e){
							ex.printStackTrace();
						}
					}
				}
			case R.id.btnSave:
				List<DoList> dList = new ArrayList<DoList>();
				if(isDebug) Log.w(CNAME,"btnSave1");
				try{
					if(ids == 0) {
						if(isDebug) Log.w(CNAME,"btnSave2");
						dList.add(getDatas());
						if(dList.get(0).event.equals("")){
							Toast.makeText(context, R.string.ReqEvent, Toast.LENGTH_SHORT).show();
							return;
						}
						dObject.insertList(dList);
						if(isDebug) Log.w(CNAME,"btnSave4");
					} else {
						if(isDebug) Log.w(CNAME,"btnSave6");
						dList.add(getDatas());
						if(dList.get(0).event.equals("")){
							Toast.makeText(context, R.string.ReqEvent, Toast.LENGTH_SHORT).show();
							return;
						}
						dList.get(0).id = ids;
						dObject.updateList(dList);
						if(isDebug) Log.w(CNAME,"btnSave8");
					}
					ret = RESULT_OK;
				} catch (Exception ex) {
					Toast.makeText(context, R.string.SQLErr, Toast.LENGTH_SHORT).show();
					ret = RESULT_CANCELED;
					TraceLog saveTrace = new TraceLog(this);
					String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
					try {
						saveTrace.saveLog(ex,CommTools.getLastPart(CNAME, ".") + mname);
					} catch (IOException e) {
						ex.printStackTrace();
					}
				}
				intent = new Intent();
				setResult(ret,intent);
				finish();
				break;
			case R.id.btnDelete:
				if(ids != 0){
					try{
						dObject.execSQL("delete from " + TDValue.TableName + " where id=" + ids);
						ret = RESULT_OK;
					} catch (Exception ex) {
						Toast.makeText(context, R.string.SQLErr, Toast.LENGTH_SHORT).show();
						ret = RESULT_CANCELED;
						TraceLog saveTrace = new TraceLog(this);
						String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
						try {
							saveTrace.saveLog(ex,CommTools.getLastPart(CNAME, ".") + mname);
						} catch (IOException e) {
							ex.printStackTrace();
						}
					}
					intent = new Intent();
					setResult(ret,intent);
					finish();
					break;
				}
				
		}
	}
	
	private DoList getDatas(){
		DoList rList = new DoList();
		//public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
		//	"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};

		EditText editEvent = (EditText)findViewById(R.id.editEvent);
		EditText editDate = (EditText)findViewById(R.id.editDate);
		EditText editTime = (EditText)findViewById(R.id.editTime);
		EditText editEndTime = (EditText)findViewById(R.id.editEndTime);
		EditText editAttach = (EditText)findViewById(R.id.editAttach);
		Switch notify = (Switch)findViewById(R.id.switchNotify);
		Switch done = (Switch)findViewById(R.id.switchDone);
		
		rList.event = editEvent.getText().toString();
		rList.date = editDate.getText().toString();
		rList.time = editTime.getText().toString();
		rList.etime = editEndTime.getText().toString();
		rList.file = editAttach.getText().toString();
		if(notify.isChecked()){
			rList.notify = 1;
		}
		if(done.isChecked()){
			rList.done = 1;
		}
		rList.datetime = rList.date + " " + rList.time;
		return rList;
	}

	private void storeCalendar(IntegerCalendar from,IntegerCalendar to){
		to.year = from.year;
		to.month = from.month;
		to.day = from.day;
		to.hour = from.hour;
		to.min = from.min;
	}
}
