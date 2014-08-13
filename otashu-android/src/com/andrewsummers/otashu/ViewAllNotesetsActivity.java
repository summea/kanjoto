package com.andrewsummers.otashu;

import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all notesets as a list.
 */
public class ViewAllNotesetsActivity extends ListActivity {
	/**
	 * onCreate override used to gather and display a list of all notesets
	 * saved in database.
	 * 
	 * @param savedInstanceState	Current application state data.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<String> allNotesetsData = new LinkedList<String>();
		NotesetCollectionOpenHelper db = new NotesetCollectionOpenHelper(this);
		
		// get string version of returned noteset list
		allNotesetsData = db.getAllNotesetListPreviews();
		
		// prevent crashes due to lack of database data
		if (allNotesetsData.isEmpty())
			allNotesetsData.add("empty");
		
		String[] allNotesets = allNotesetsData.toArray(new String[allNotesetsData.size()]);
		
		// pass list data to adapter
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_noteset, allNotesets));
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		// get individual noteset details
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// launch details activity
				Intent intent = new Intent(view.getContext(), ViewNotesetDetailActivity.class);
				startActivity(intent);
			}
		});
	}
}