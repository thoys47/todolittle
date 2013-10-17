package jp.thoy.todolittle;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	final static boolean isDebug = true;


	Context mContext;
	
	public DBOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		mContext = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) throws SQLException {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		db.execSQL(DataObject.makeBaseSQL("CREATE", TDValue.TableName));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		if(isDebug){
			Log.w(CNAME,"old=" + oldVersion + " new=" + newVersion);
		}
	}


}
