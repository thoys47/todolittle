package jp.thoy.todolittle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Calendar;

import jp.thoy.todolittle.R;

import android.content.Context;
import android.util.Log;

public class TraceLog implements UncaughtExceptionHandler {
	Context mContext;
	static final String TRACEFILE =  "todolittle.trc";
	static final String DEBUGFILE =  "todolittle.debug";
	private final String CNAME = CommTools.getLastPart(this.getClass().getName(),".");
	private final static boolean isDebug = false;

	final Calendar calendar = Calendar.getInstance();

	public TraceLog(Context context){
		mContext = context;
	}
	
	public void saveLog(Exception ex,String msg) throws IOException {
		File file = null;
		file = new File(mContext.getExternalFilesDir(null), TRACEFILE);
		file.createNewFile();

        if (file != null && file.exists()) {
        	FileWriter fw = new FileWriter(file, false);
        	PrintWriter pw = new PrintWriter(fw,true);
    		pw.println(CommTools.CalendarToString(calendar,CommTools.DATETIMELONG).substring(5,19));
        	pw.println(msg);
        	ex.printStackTrace(pw);
        	pw.close();
        }
        if(isDebug) Log.e(CNAME,"Trace=" + ex.getMessage());
	}
	
	public void saveDebug(String msg) throws IOException{

		File file = null;
		file = new File(mContext.getExternalFilesDir(null), DEBUGFILE);
		file.createNewFile();

        if (file != null && file.exists()) {
        	FileReader fr = new FileReader(file);
        	BufferedReader br = new BufferedReader(fr);
    		ArrayList<String> debug = new ArrayList<String>();
        	String ln;
        	int line = 0;
        	while((ln = br.readLine()) != null){
        		debug.add(ln);
        		line++;
        	}
        	br.close();
        	fr.close();
        	
        	FileWriter fw = new FileWriter(file, false);
        	PrintWriter pw = new PrintWriter(fw,true);
        	
        	int i = 0;
        	if(line > 99){
        		i = line - 99;
        	}
        	for(;i < line;i ++){
        		pw.println(debug.get(i));
        	}
    		pw.println(CommTools.CalendarToString(calendar,CommTools.DATETIMELONG).substring(5,19) + ":" + msg);
        	pw.close();
        }

	}
	
	public ArrayList<String> readFile(int radio) throws IOException {
		
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<String> ar = new ArrayList<String>();
		File file = null;

    	switch(radio){
			case R.id.debugTrace:
				file = new File(mContext.getExternalFilesDir(null), TRACEFILE);
				break;
			case R.id.debugLog:
				file = new File(mContext.getExternalFilesDir(null), DEBUGFILE);
				break;
		}
        if (file.exists()) {
        	FileReader fr = new FileReader(file);
        	BufferedReader br = new BufferedReader(fr);
        	String ln;
        	int line = 0;
        	while((ln = br.readLine()) != null){
        		ar.add(ln);
        		line++;
        	}
        	br.close();
        	fr.close();
        	
        	switch(radio){
    			case R.id.debugTrace:
    				for(int i = 0;i < line;i++){
    					ret.add(ar.get(i));
    				}
    				break;
    			case R.id.debugLog:
    				for(int i = line - 1;i >= 0;i--){
    					ret.add(ar.get(i));
    				}
    				break;
    		}
        	return(ret);
        }
        return null;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO 自動生成されたメソッド・スタブ
		try{
			saveLog((Exception)ex,thread.getName());
		} catch (IOException e){
			ex.printStackTrace();
		}
		
	}

}
