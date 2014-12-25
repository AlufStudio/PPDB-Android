package app.rumus.ppdb.lib;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.rumus.ppdb.model.CodeModel;

public class DatabaseHandler extends SQLiteOpenHelper{
	private static final int DB_VERSION = 1;

	private static final String DB_NAME = "pp_db";
	
	private static final String TBL_CODE = "code";
	
	private static final String KEY_ID = "id";
	private static final String KEY_CODE = "code";
	private static final String KEY_TANGGAL = "tanggal";
	
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE_CODE = "CREATE TABLE " + TBL_CODE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_CODE + " TEXT,"
				+ KEY_TANGGAL + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_CODE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TBL_CODE);
		
		onCreate(db);
		
	}
	
	//Code DAO
	public void addCode(CodeModel cm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(KEY_CODE, cm.getCode());
		cv.put(KEY_TANGGAL, cm.getTanggal());

		db.insert(TBL_CODE, null, cv);
		db.close();
	}
	
	public List<CodeModel> getAllCode() {
		ArrayList<CodeModel> codeList = new ArrayList<CodeModel>();
		String selectQuery = "SELECT  * FROM " + TBL_CODE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CodeModel cm = new CodeModel();
				cm.setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
				cm.setCode(cursor.getString(cursor.getColumnIndex(KEY_CODE)));
				cm.setTanggal(cursor.getString(cursor.getColumnIndex(KEY_TANGGAL)));

				codeList.add(cm);
			} while (cursor.moveToNext());
		}

		// return contact list
		return codeList;
	}

}
