package com.andrewsummers.otashu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewNotesetSequencePageFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_noteset_sequence_page, container, false);
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_noteset_sequence_page, container, false);
		
		String receivedText = getArguments().getString("textforfragment");
		TextView textView = (TextView)rootView.findViewById(R.id.fragment_text_view);		
		textView.setText(receivedText);
		
		return rootView;
	}
}
