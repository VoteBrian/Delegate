package com.votebrian.delegate;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Delegate extends ListActivity {
	private TaskDbAdapter mDbHelper;
	
	private static final int ACTIVITY_CREATE	= 0;
	private static final int ACTIVITY_EDIT		= 1;
	private static final int ACTIVITY_DELETE	= 2;
	
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Create and open database helper
        mDbHelper = new TaskDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }
    
    // Fill Data
    private void fillData() {
    	// Get all rows from database
    	Cursor tasksCursor = mDbHelper.fetchAllNotes();
    	startManagingCursor(tasksCursor);
    	
    	String[] from = new String[]{TaskDbAdapter.DB_TASKS_TITLE};
    	int[] to = new int[]{R.id.row};
    	
    	SimpleCursorAdapter tasks =
    		new SimpleCursorAdapter(this, R.layout.task_row, tasksCursor, from, to);
    	setListAdapter(tasks);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, INSERT_ID, 0, R.string.menu_insert);
    	//menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureID, MenuItem item) {
    	switch(item.getItemId()) {
    	case INSERT_ID:
    		createTask();
    		return true;
    	}
    	
    	return super.onMenuItemSelected(featureID, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu,
    		View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    	//menu.add(0, INSERT_ID, 0, R.string.menu_insert);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case DELETE_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    		mDbHelper.deleteNote(info.id);
    		fillData();
    		return true;
    	}
    	return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	Intent i = new Intent(this, TaskEdit.class);
    	i.putExtra(TaskDbAdapter.DB_TASKS_ROWID, id);
    	startActivityForResult(i, ACTIVITY_EDIT);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	fillData();
    }
    
    private void createTask() {
    	Intent i = new Intent(this, TaskEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    private void deleteTask() {
    	Intent i = new Intent(this, TaskEdit.class);
    	startActivityForResult(i, ACTIVITY_DELETE);
    }
}