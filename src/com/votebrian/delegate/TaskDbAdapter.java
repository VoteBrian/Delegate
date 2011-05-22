package com.votebrian.delegate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDbAdapter {
	public final static String TAG = "TaskDbAdapter";
	
	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	/**
	 * Database Stuff
	 */
	public final static String DB_NAME = "data";
	public final static int DB_VERSION = 2;
	
	public final static String DB_TASKS_TABLE = "tasks";
	public final static String DB_TASKS_ROWID = "_id";
	public final static String DB_TASKS_TITLE = "title";
	public final static String DB_TASKS_BODY = "body";
	public final static String DB_TASKS_STARRED = "starred";
	public final static String DB_TASKS_DATECREATED = "dateCreated";
	public final static String DB_TASKS_DATEDUE = "dateDue";
	public final static String DB_TASKS_COMPLETE = "complete";
	public final static String DB_TASKS_DATECOMPLETE = "dateComplete";
	
	public final static String DB_CREATE_TASKS = 
        "create table " + DB_TASKS_TABLE + " (" 
        + DB_TASKS_ROWID + " integer primary key autoincrement, "
        + DB_TASKS_TITLE + " text not null, "
        + DB_TASKS_BODY + " text not null, "
        + DB_TASKS_STARRED + " boolean not null, "
        + DB_TASKS_DATECREATED + " integer not null, "
        + DB_TASKS_DATEDUE + " integer not null, "
        + DB_TASKS_COMPLETE + " boolean not null, "
        + DB_TASKS_DATECOMPLETE + " integer not null );";
	
	public final static String DB_PROJECTS_TABLE = "projects";
	public final static String DB_PROJECTS_ROWID = "_id";
	public final static String DB_PROJECTS_TITLE = "title";
	public final static String DB_PROJECTS_STARRED = "priority";
	
	public final static String DB_CREATE_PROJECTS = 
		"create table " + DB_PROJECTS_TABLE + " ("
		+ DB_PROJECTS_ROWID + " integer primary key autoincrement, "
		+ DB_PROJECTS_TITLE + " text not null, "
		+ DB_PROJECTS_STARRED + " boolean not null );";
	
	public final static String DB_USERS_TABLE = "users";
	public final static String DB_USERS_ROWID = "_id";
	public final static String DB_USERS_NAME = "name";
	
	public final static String DB_CREATE_USERS =
		"create table " + DB_USERS_TABLE + " ("
		+ DB_USERS_ROWID + " integer primary key autoincrement, "
		+ DB_USERS_NAME + " text not null );";
	
	public final static String DB_LABELS_TABLE = "labels";
	public final static String DB_LABELS_ROWID = "_id";
	public final static String DB_LABELS_NAME = "name";
	public final static String DB_LABELS_COLOR = "color";
	
	public final static String DB_CREATE_LABELS = 
		"create table " + DB_LABELS_TABLE + " ("
		+ DB_LABELS_ROWID + " integer primary key autoincrement, "
		+ DB_LABELS_NAME + " text not null, "
		+ DB_LABELS_COLOR + " text not null);";
	
	
	// Constructor
	public TaskDbAdapter (Context ctx) {
		this.mCtx = ctx;
	}
	
	// Open Database Adapter
	public TaskDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
	}
	
	// Close Database Adapter
	public void close() {
		mDbHelper.close();
	}
	
	// Create Note
	public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DB_TASKS_TITLE, title);
        initialValues.put(DB_TASKS_BODY, body);

        return mDb.insert(DB_TASKS_TABLE, null, initialValues);
	}
	
	// Delete Note
	public boolean deleteNote(long taskID) {
		return mDb.delete(DB_TASKS_TABLE, DB_TASKS_ROWID + "=" + taskID, null) > 0;
	}
	
	// Fetch All Notes
    public Cursor fetchAllNotes() {
        return mDb.query(DB_TASKS_TABLE, new String[] {DB_TASKS_ROWID, DB_TASKS_TITLE,
                DB_TASKS_BODY}, null, null, null, null, null);
    }
    
    // Fetch Note
    public Cursor fetchNote(long rowID) throws SQLException {
    	Cursor mCursor =

            mDb.query(true, DB_TASKS_TABLE, new String[] {DB_TASKS_ROWID,
                    DB_TASKS_TABLE, DB_TASKS_BODY}, DB_TASKS_ROWID + "=" + rowID, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
    	return mCursor;
    }
    
    // Update Task
    public boolean updateTask(long rowId, String title, String body) {
    	ContentValues args = new ContentValues();
    	args.put(DB_TASKS_TITLE, title);
    	args.put(DB_TASKS_BODY, body);
    	
    	return mDb.update(DB_TASKS_TABLE, args, DB_TASKS_ROWID + "=" + rowId, null) > 0;
    }
    
	
    // Database Helper Class
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_TASKS);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
	}
}
