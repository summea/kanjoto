package com.andrewsummers.otashu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAllNotesetsActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] allNotesets = new String[] { "example one", "example two", "example three" };
		
		NotesetCollectionOpenHelper db = new NotesetCollectionOpenHelper(this);

		db.getAllNotesets();
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_noteset, allNotesets));
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// launch details activity
				Intent intent = new Intent(view.getContext(), ViewNotesetDetailActivity.class);
				startActivity(intent);
			}
		});
	}
}