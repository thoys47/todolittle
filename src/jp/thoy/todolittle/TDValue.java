package jp.thoy.todolittle;

public class TDValue {
	public final static String PREFILENAME = "todolittle.Prefer";

	public final static String KEY_ID = "ID";
	public final static int RECENT = 0;
	public final static int PAST = 1;
	public final static int TAB_NUM = 2;
	public final static String KEY_PAGE = "PAGE";
	public final static int REQ_CODE = 101;
	
	public final static String KEY_LIMIT = "Limit";
	public final static String KEY_BEFORE = "Before";
	public final static String KEY_STOP = "Stop";
	public final static int DEF_DEFORE = 20;
	public final static int DEF_LIMIT = 15;

	public final static String NOTIFY = "ToDoLittle.Notify";
	public final static int NOTIFY_NUM = 3;
	public final static int SHORT = -1;
	public final static int WR_NUM = 3;
	public final static int WC_NUM = 3;

	public final static int CAPTURE_IMAGE = 100;
	public final static int SELECT_IMAGE = 200;
	public final static int VIEW_PICTURE = 300;
	public final static int MENU_PICTURE = 10;
	public final static int MENU_GALLERY = 20;
	public final static int MENU_REMOVE = 30;

	public final static String DB_CREATE = "CREATE";
	public final static String DB_INSERT = "INSERT";
	public final static String DB_UPDATE = "UPDATE";
	public final static String DB_SELECT = "SELECT";

	public final static String DatabaseName = "todolittle.db";
	public final static int DatabaseVersion = 1;
	public final static String TableName = "TODOLIST";
	
	public final static String[] todoColumn = {"ID","EVENT","DATE","TIME","ETIME","DONE",
											"PRIORITY","FILE","ALERT","MEMO","NOTIFY","DATETIME"};
	public final static String[] todoType = {"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","INTEGER",
											"INTEGER","TEXT","INTEGER","TEXT","INTEGER","TEXT"};

}
