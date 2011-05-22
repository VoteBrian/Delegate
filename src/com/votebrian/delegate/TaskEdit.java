package com.votebrian.delegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskEdit extends Activity {
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_edit);
		setTitle(R.string.edit_task);
		
		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		mRowId = null;
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			String title = extras.getString(TaskDbAdapter.DB_TASKS_TITLE);
			String body = extras.getString(TaskDbAdapter.DB_TASKS_BODY);
			mRowId = extras.getLong(TaskDbAdapter.DB_TASKS_ROWID);
			
			if(title != null) {
				mTitleText.setText(title);
			}
			if(body != null) {
				mBodyText.setText(body);
			}
		}
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				
				bundle.putString(TaskDbAdapter.DB_TASKS_TITLE, mTitleText.getText().toString());
				bundle.putString(TaskDbAdapter.DB_TASKS_BODY, mBodyText.getText().toString());
				if(mRowId != null) {
					bundle.putLong(TaskDbAdapter.DB_TASKS_ROWID, mRowId);
				}
				
				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}
}
