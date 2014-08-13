package com.andrewsummers.otashu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Page fragment used to display individual noteset note value data.
 */
public class ViewNotesetSequencePageFragment extends Fragment {
    /**
     * onCreateView override used to gather data (that was sent along in a
     * bundle) and then display the given data on current page.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // inflate view
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_view_noteset_sequence_page, container, false);

        // display received data in view
        String receivedText = getArguments().getString("textforfragment");
        TextView textView = (TextView) rootView
                .findViewById(R.id.fragment_text_view);
        textView.setText(receivedText);

        return rootView;
    }
}