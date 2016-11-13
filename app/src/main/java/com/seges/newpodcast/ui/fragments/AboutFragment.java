package com.seges.newpodcast.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seges.newpodcast.R;
import com.seges.newpodcast.ui.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_about, container, false);


        v.findViewById(R.id.btn_ideladen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ideladen.seges.dk/home/podcast-afsnit"));
                startActivity(browserIntent);
            }
        });


        ((MainActivity)getActivity()).tv_fragment_title.setText("Om SEGES");
        ((MainActivity)getActivity()).tv_fragment_title.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).img_white_logo.setVisibility(View.VISIBLE);
        return v;
    }

}
