package jp.thoy.todolittle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataObject {

	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;
	
	Context mContext;
	
	public DataObject(Context context){
		mContext = context;
	}

	public static String makeBaseSQL(String option,String tablename) {
		String sql = null;
		if(option.equals(TDValue.DB_CREATE)){
			if(tablename.equals(TDValue.TableName)){
				sql = "CREATE TABLE IF NOT EXISTS " + tablename + " (";
				for(int i = 0;i < TDValue.todoColumn.length;i++){
					sql += TDValue.todoColumn[i] + " " + TDValue.todoType[i] + ",";
				}
			}
			sql = sql.substring(0, sql.length() - 1);
			sql += ")";
		} else if (option.equals(TDValue.DB_INSERT)){
			if(tablename.equals(TDValue.TableName)){
				sql = "INSERT INTO " + tablename + " (";
				for(int i = 0;i < TDValue.todoColumn.length;i++){
					sql += TDValue.todoColumn[i] + ","; 
				}
			}
			sql = sql.substring(0, sql.length() - 1);
			sql += ") values (";
		} else if(option.equals(TDValue.DB_SELECT)){
			if(tablename.equals(TDValue.TableName)){
				sql = "SELECT ";
				for(int i = 0;i < TDValue.todoColumn.length;i++){
					sql += TDValue.todoColumn[i] + ","; 
				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from " + tablename;
			}
		}
		return sql;
	}
	
	public SQLiteDatabase dbOpen(){
		SQLiteDatabase mdb = null;

		try{
			DBOpenHelper mHelper = new DBOpenHelper(mContext,TDValue.DatabaseName,null,TDValue.DatabaseVersion);
			mdb = mHelper.getWritableDatabase();
			return mdb;
		} catch (Exception ex) {
			if(mdb != null){
				dbClose(mdb);
			}
			TraceLog traceLog = new TraceLog(mContext);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CommTools.getLastPart(CNAME,".") + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public Cursor dbQuery(SQLiteDatabase db, String s){
		Cursor cursor = null;
		if(db != null) {
			try{
			cursor = db.rawQuery(s, null);
			cursor.moveToFirst();
			} catch (Exception ex) {
				if(db != null){
					dbClose(db);
				}
				TraceLog traceLog = new TraceLog(mContext);
				String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
				try {
					traceLog.saveLog(ex,CommTools.getLastPart(CNAME,".") + mname);
				} catch (IOException e) {
					ex.printStackTrace();
				}
				return null;
			}
		}
		if(isDebug){
			Log.w(CNAME, s);
		}
		
		return cursor;
	}
	
	public void dbClose(SQLiteDatabase db){
		try{
			db.close();
		} catch (Exception ex) {
			if(db != null){
				dbClose(db);
			}
			TraceLog traceLog = new TraceLog(mContext);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CommTools.getLastPart(CNAME,".") + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
	}
	

	public List<DoList> reQuery(int page,int limit,int notify,int id){
		List<DoList>rList = new ArrayList<DoList>();
		Cursor cursor = null;
		SQLiteDatabase db = dbOpen();
		String sql = makeBaseSQL(TDValue.DB_SELECT,TDValue.TableName);
		if(id > 0){
			sql += " WHERE ID = " + String.valueOf(id);
		} else {
			switch(page){
				case TDValue.RECENT:
					sql += " WHERE DATETIME >= '";
					sql += CommTools.CalendarToString(Calendar.getInstance(),CommTools.DATETIMELONG) + "'";
					sql += " AND DONE = 0";
					if(notify != 0){
						sql += " AND NOTIFY = 1";
					}
					sql += " ORDER BY DATETIME LIMIT " + String.valueOf(limit);
					break;
				case TDValue.PAST:
					sql += " WHERE (DATETIME <= '";
					sql += CommTools.CalendarToString(Calendar.getInstance(),CommTools.DATETIMELONG) + "'" ;
					sql += ") OR DONE = 1";
					sql += " ORDER BY DATETIME DESC LIMIT " + String.valueOf(limit);
					break;
			}
		}

		try{
			cursor = dbQuery(db,sql);
			
			if(cursor == null || cursor.getCount() == 0){
				dbClose(db);
				if(isDebug) Log.w(CNAME,"cursor.getcount=" + cursor.getCount());
				return null;
			}
			//public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
			//	"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};
			while(cursor.getPosition() < cursor.getCount()){
				rList.add(makeDoList(cursor));
				cursor.moveToNext();
			}
			dbClose(db);
		} catch (Exception ex) {
			if(db != null){
				dbClose(db);
			}
			TraceLog traceLog = new TraceLog(mContext);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CommTools.getLastPart(CNAME,".") + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		if(isDebug) Log.w(CNAME,sql);
		return rList;
	}

	public void execSQL(String sql){
		SQLiteDatabase db;
		try{
			db = dbOpen();
			db.execSQL(sql);
			dbClose(db);
		} catch (Exception ex){
			TraceLog traceLog = new TraceLog(mContext);
			String mname = ":" + Thread.currentThread().getStackTrace()[2].getMethodName();
			try {
				traceLog.saveLog(ex,CommTools.getLastPart(CNAME, ".") + mname);
			} catch (IOException e) {
				ex.printStackTrace();
			}
		}
		return;
	}
	
	public int insertList(List<DoList> dList){
		int ret = 0;
		//public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
		//	"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};
		
		for(ret = 0;ret < dList.size();ret++){
			String sql = makeBaseSQL("INSERT",TDValue.TableName);
			sql += "null,";
			sql += "'" + dList.get(ret).event + "',";
			sql += "'" + dList.get(ret).date + "',";
			sql += "'" + dList.get(ret).time + "',";
			sql += "'" + dList.get(ret).etime + "',";
			sql += String.valueOf(dList.get(ret).done) + ",";
			sql += String.valueOf(dList.get(ret).priority) + ",";
			sql += "'" + dList.get(ret).file + "',";
			sql += String.valueOf(dList.get(ret).alert) + ",";
			sql += "'" + dList.get(ret).memo + "',";
			sql += String.valueOf(dList.get(ret).notify) + ",";
			sql += "'" + dList.get(ret).datetime + "')";
			this.execSQL(sql);
		}
		
		return ret;
		
	}
	
	public int updateList(List<DoList> dList){
		int ret = 0;
		//public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
		//	"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};
		String sql = "update " + TDValue.TableName + " set ";
		for(ret = 0;ret < dList.size();ret++){
			sql = "update " + TDValue.TableName + " set ";
			sql += "event = '" + dList.get(ret).event + "',";
			sql += "date = '" + dList.get(ret).date + "',";
			sql += "time = '" + dList.get(ret).time + "',";
			sql += "etime = '" + dList.get(ret).etime + "',";
			sql += "done = " + String.valueOf(dList.get(ret).done) + ",";
			sql += "priority = " + String.valueOf(dList.get(ret).priority) + ",";
			sql += "file = '" + dList.get(ret).file + "',";
			sql += "alert = " + String.valueOf(dList.get(ret).alert) + ",";
			sql += "memo = '" + dList.get(ret).memo + "',";
			sql += "notify = " + String.valueOf(dList.get(ret).notify) + ",";
			sql += "datetime = '" + dList.get(ret).datetime + "'";
			sql += " where id = " + dList.get(ret).id;
			this.execSQL(sql);
		}
		if(isDebug) Log.w(CNAME,sql);
		return ret;
	}	
	public DoList makeDoList(Cursor cursor){
		//public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
		//	"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};
		
		DoList list = new DoList();
		list.id = cursor.getInt(0);
		list.event = cursor.getString(1);
		list.date = cursor.getString(2);
		list.time = cursor.getString(3);
		list.etime = cursor.getString(4);
		list.done = cursor.getInt(5);
		list.priority = cursor.getInt(6);
		list.file = cursor.getString(7);
		list.alert = cursor.getInt(8);
		list.memo = cursor.getString(9);
		list.notify = cursor.getInt(10);
		list.datetime = cursor.getString(11);
		
		return list;
	}
	
}
